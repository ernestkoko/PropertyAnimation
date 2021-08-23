/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }
        })

    }

    private fun rotater() {
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(
            star, View.ROTATION,
            -360f, 0f
        )
        animator.duration = 1000
        //disable the view while animating
        animator.disableViewDuringAnimation(rotateButton)
        //start the animation
        animator.start()


    }

    private fun translater() {
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)

        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        //disable the view while animating
        animator.disableViewDuringAnimation(translateButton)
        animator.start()
    }

    private fun scaler() {
        //define the properties
        val scaleX: PropertyValuesHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        //define the object
        val animator: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)

        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()

    }

    private fun fader() {
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(fadeButton)
        animator.start()
    }

    private fun colorizer() {
        val animator = ObjectAnimator.ofArgb(
            star.parent,
            "backgroundColor", Color.BLACK, Color.RED
        )
        animator.setDuration(500)
        animator.repeatCount = 1

        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(colorizeButton)
        animator.start()
    }

    private fun shower() {
        //create a container
        val container: ViewGroup = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var startW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()
        //create new view
        val newStar = AppCompatImageView(this)
        //set the properties of the new view
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        //add it to the container view
        container.addView(newStar)
        //position the star
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        startW *= newStar.scaleX
        starH *= newStar.scaleY

        newStar.translationX = Math.random().toFloat() * containerW - startW / 2
        //create animation
        val mover = ObjectAnimator.ofFloat(
            newStar,
            View.TRANSLATION_Y, -starH, containerH + starH
        )
        mover.interpolator = AccelerateInterpolator(1f)
        //create roate animation
        val rotate =
            ObjectAnimator.ofFloat(newStar, View.ROTATION, (Math.random() * 1080).toFloat())
        rotate.interpolator =LinearInterpolator()
        //create animator set
        val set = AnimatorSet()
        set.playTogether(mover,rotate)
        set.duration= (Math.random() * 1500 +500).toLong()
        set.addListener(object:Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

        })
        set.start()
    }

}

private val TAG = "MainActivity"
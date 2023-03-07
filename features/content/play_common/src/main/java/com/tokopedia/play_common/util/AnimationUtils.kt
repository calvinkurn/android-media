package com.tokopedia.play_common.util

import android.view.View
import androidx.annotation.FloatRange
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

/**
 * @author by astidhiyaa on 01/07/22
 */
object AnimationUtils {
    private const val DEFAULT_VELOCIY = 14f

    fun addSpringAnim(
        view: View,
        property: DynamicAnimation.ViewProperty,
        startPosition: Float =  0f,
        finalPosition: Float,
        @FloatRange(from = 0.1) stiffness: Float,
        @FloatRange(from = 0.0) dampingRatio: Float,
        @FloatRange(from = 0.0) velocity: Float = DEFAULT_VELOCIY,
    ): SpringAnimation {
        val spring = SpringForce(finalPosition)
        spring.apply {
            this.stiffness = stiffness
            this.dampingRatio = dampingRatio
        }
        val animation = SpringAnimation(view, property)
        animation.spring = spring
        animation.setStartVelocity(velocity)
        animation.setStartValue(startPosition)
        return animation
    }
}
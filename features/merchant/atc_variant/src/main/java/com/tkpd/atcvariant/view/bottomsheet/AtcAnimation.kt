package com.tkpd.atcvariant.view.bottomsheet

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.tkpd.atcvariant.databinding.AtcAnimationLayoutBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getLocationOnScreen
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.setLayoutWidth
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.toPx
import com.tkpd.atcvariant.R as atcvariantR


/**
 * Created by yovi.putra on 3/4/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */


class AtcAnimation(context: Context) {
    companion object {
        // refer to [com.tokopedia.searchbar.navigation_component.R.layout.nav_main_toolbar]
        private val ACTION_ICON_SIZE = 54.toPx()
        private val HALF_ACTION_ICON_SIZE = ACTION_ICON_SIZE / 2
        private val MARGIN_END_CONTAINER_TOOLBAR_ACTION = 2.toPx()
        private const val CART_POS_FROM_LEFT_IN_PDP_TOOLBAR = 2
        private val PDP_CART_LOCATION_X =
            ACTION_ICON_SIZE * CART_POS_FROM_LEFT_IN_PDP_TOOLBAR - HALF_ACTION_ICON_SIZE + MARGIN_END_CONTAINER_TOOLBAR_ACTION
        private val TOOLBAR_HEIGHT = 56.toPx()
        private val TARGET_IMAGE_SIZE_AFTER_ANIMATE = 24.toPx()
        private val STROKE_WIDTH = 4.toPx()

        // animation's attribute
        private const val DELAY_BETWEEN_SHOW_AND_FLY_ANIMATE = 750L
        private const val ANIMATE_DURATION = 750L
    }

    private var mSourceImageView: ImageView? = null
    private val targetLocationAbsolute = Point(
        getScreenWidth() - PDP_CART_LOCATION_X,
        TOOLBAR_HEIGHT.div(2)
    )
    private var mTargetLocation: Point = Point(-1, -1)
        get() = if (field.x == -1) {
            targetLocationAbsolute
        } else {
            field
        }


    private val binding by lazyThreadSafetyNone {
        AtcAnimationLayoutBinding.inflate(LayoutInflater.from(context))
    }
    private val popupWindow by lazyThreadSafetyNone {
        PopupWindow(
            binding.root, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        ).apply {
            animationStyle = atcvariantR.style.popup_window_animation
        }
    }

    private var currentX = 0
    private var currentY = 0
    private var currentWidth = 0
    private var currentHeight = 0
    private var currentAlpha = 1f

    fun setSourceView(view: ImageView): AtcAnimation {
        mSourceImageView = view
        return this
    }

    fun setTargetLocation(point: Point?) {
        mTargetLocation = point ?: Point(-1, -1)
    }

    private fun prepare(): Boolean {
        val source = mSourceImageView ?: return false

        // set resource as drawable from source
        binding.productImage.setImageDrawable(source.drawable)

        // set current size from source size
        currentWidth = source.width
        currentHeight = source.height

        // set current position from source location
        val location = source.getLocationOnScreen()
        setInitialLocationX(sourceX = location.x)
        setInitialLocationY(sourceY = location.y)

        currentAlpha = Float.ONE

        return true
    }

    private fun setInitialLocationX(sourceX: Int) {
        currentX = (sourceX - STROKE_WIDTH - binding.cardImage.marginStart)
            .coerceAtLeast(Int.ZERO)
    }

    private fun setInitialLocationY(sourceY: Int) {
        currentY = (sourceY - STROKE_WIDTH - binding.cardImage.marginTop)
            .coerceAtLeast(Int.ZERO)
    }

    fun show() {
        if (prepare()) {
            resetState()
            showPopUpWindow()
        }
    }

    private fun resetState() {
        transformSize()
        transformPosition()
        transformAlpha()
    }

    private fun showPopUpWindow() {
        val target = mSourceImageView ?: return
        popupWindow.showAtLocation(target, Gravity.NO_GRAVITY, currentX, currentY)

        binding.root.postDelayed({
            flyingAnimation()
        }, DELAY_BETWEEN_SHOW_AND_FLY_ANIMATE)
    }

    private val animatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(p0: Animator) {

        }

        override fun onAnimationEnd(p0: Animator) {
            popupWindow.dismiss()
        }

        override fun onAnimationCancel(p0: Animator) {
        }

        override fun onAnimationRepeat(p0: Animator) {
        }
    }

    private fun flyingAnimation() {
        val animators = prepareAnimator()
        val animatorSet = AnimatorSet().apply {
            playTogether(animators)
            duration = ANIMATE_DURATION
            addListener(animatorListener)
        }
        animatorSet.start()
    }

    private fun prepareAnimator(): List<Animator> {
        val targetSize = TARGET_IMAGE_SIZE_AFTER_ANIMATE

        val translateX = currentX.animatePositionTo(to = mTargetLocation.x) { _, value ->
            currentX = value
            transformPosition()
        }

        val translateY = currentY.animatePositionTo(to = mTargetLocation.y) { _, value ->
            currentY = value
            transformPosition()
        }

        val translateWidth = currentWidth.animatePositionTo(to = targetSize) { _, value ->
            currentWidth = value
            transformSize()
        }

        val translateHeight = currentHeight.animatePositionTo(to = targetSize) { _, value ->
            currentHeight = value
            transformSize()
        }

        val alpha = currentAlpha.animatePositionTo(to = Float.ZERO) {
            currentAlpha = it
            transformAlpha()
        }

        return listOf(translateX, translateY, translateWidth, translateHeight, alpha)
    }

    private fun Int.animatePositionTo(to: Int, onValueChanged: (ValueAnimator, Int) -> Unit) =
        ValueAnimator.ofInt(this, to).apply {
            addUpdateListener {
                val value = it.animatedValue as Int
                onValueChanged(this, value)
            }
        }

    private fun Float.animatePositionTo(to: Float, onValueChanged: (Float) -> Unit) =
        ValueAnimator.ofFloat(this, to).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                onValueChanged(value)
            }
        }

    // update popup-window content size
    private fun transformSize() {
        binding.productImage.apply {
            post {
                setLayoutWidth(currentWidth)
                setLayoutHeight(currentHeight)
            }
        }
    }

    // update popup-window location
    private fun transformPosition() {
        popupWindow.update(
            currentX,
            currentY,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    // update popup-window alpha
    private fun transformAlpha() {
        if (binding.root.alpha == currentAlpha) return
        binding.root.alpha = currentAlpha
    }
}

fun Fragment.atcAnimator() = lazy { AtcAnimation(context = requireContext()) }

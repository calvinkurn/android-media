package com.tkpd.atcvariant.view.bottomsheet

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.tkpd.atcvariant.databinding.AtcAnimationLayoutBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
import com.tokopedia.unifycomponents.toPx
import com.tkpd.atcvariant.R as atcvariantR


/**
 * Created by yovi.putra on 3/4/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */


class AtcAnimation(private val activity: Activity) {
    companion object {
        /**
         * refer to [R.layout.toolbar_viewholder_icon]
         */
        private val OPT_ICON_SIZE = 54.toPx()
        private val HALF_ICON_SIZE = OPT_ICON_SIZE / 2

        /**
         * refer to [com.tokopedia.searchbar.navigation_component.R.layout.nav_main_toolbar]
         */
        private val CONTAINER_OPT_ICON_MARGIN_END = 2.toPx()

        //Cart icon position from the left within PDP navToolbar
        private const val CART_POS_FROM_LEFT_IN_PDP_TOOLBAR = 2
        private val PDP_CART_LOCATION_X =
            (OPT_ICON_SIZE * CART_POS_FROM_LEFT_IN_PDP_TOOLBAR - HALF_ICON_SIZE) + CONTAINER_OPT_ICON_MARGIN_END
        private val TOOLBAR_HEIGHT = 54.toPx()
        private val TARGET_IMAGE_SIZE_AFTER_ANIMATE = 24.toPx()
        private const val DELAY_BETWEEN_SHOW_AND_FLY_ANIMATE = 750L
        private const val ANIMATE_DURATION = 250L
    }

    private var mSourceImageView: ImageView? = null
    private val binding = AtcAnimationLayoutBinding.inflate(LayoutInflater.from(activity))
    private val popupWindow = PopupWindow(activity).apply {
        animationStyle = atcvariantR.style.popup_window_animation
        contentView = binding.root
    }

    private val locationOnScreen = IntArray(2)
    private var currentX = 0
    private var currentY = 0
    private var currentWidth = 0
    private var currentHeight = 0
    private var currentAlpha = 1f

    fun setView(view: ImageView): AtcAnimation {
        mSourceImageView = view
        binding.productImage.setImageDrawable(view.drawable)
        currentWidth = view.width
        currentHeight = view.height
        return this
    }

    fun show() {
        initialBehaviour()
        showPopUpWindow()
    }

    /**
     * get current location of source image view
     * set to current position for popUpWindow
     */
    private fun initialBehaviour() {
        val target = mSourceImageView ?: return
        target.getLocationOnScreen(locationOnScreen)
        currentX = locationOnScreen[0]
        currentY = locationOnScreen[1]
        currentWidth = target.width
        currentHeight = target.width
        currentAlpha = Float.ONE

        updatePosition()
        updateAlpha()
    }

    private fun showPopUpWindow() {
        val target = mSourceImageView ?: return
        popupWindow.apply {
            showAtLocation(target, Gravity.NO_GRAVITY, currentX, currentY)
            contentView.postDelayed({
                flyingAnimation()
            }, DELAY_BETWEEN_SHOW_AND_FLY_ANIMATE)
        }
    }

    private val animatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(p0: Animator) {

        }

        override fun onAnimationEnd(p0: Animator) {
            // popupWindow.dismiss()
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
        val targetX = getScreenWidth() - PDP_CART_LOCATION_X
        val targetY = TOOLBAR_HEIGHT
        val targetSize = TARGET_IMAGE_SIZE_AFTER_ANIMATE

        val translateX = currentX.animatePositionTo(to = targetX) {
            currentX = it
            updatePosition()
        }

        val translateY = currentY.animatePositionTo(to = targetY) {
            currentY = it
            updatePosition()
        }

        val translateWidth = currentWidth.animatePositionTo(to = targetSize) {
            currentWidth = it
            updatePosition()
        }

        val translateHeight = currentHeight.animatePositionTo(to = targetSize) {
            currentHeight = it
            updatePosition()
        }

        val alpha = currentAlpha.animatePositionTo(to = Float.ZERO) {
            currentAlpha = it
            updateAlpha()
        }

        return listOf(translateX, translateY, translateWidth, translateHeight, alpha)
    }

    private fun Float.animatePositionTo(to: Float, onValueChanged: (Float) -> Unit) =
        ValueAnimator.ofFloat(this, to).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                onValueChanged(value)
            }
        }

    private fun Int.animatePositionTo(to: Int, onValueChanged: (Int) -> Unit) =
        ValueAnimator.ofInt(this, to).apply {
            addUpdateListener {
                val value = it.animatedValue as Int
                onValueChanged(value)
            }
        }

    private fun updatePosition() {
        binding.productImage.updateLayoutParams<LayoutParams> {
            this?.height = currentWidth
            this?.width = currentHeight
        }
        popupWindow.update(
            currentX,
            currentY,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    private fun updateAlpha() {
        binding.productImage.alpha = currentAlpha
    }
}

fun Fragment.atcAnimator() = lazy { AtcAnimation(activity = requireActivity()) }

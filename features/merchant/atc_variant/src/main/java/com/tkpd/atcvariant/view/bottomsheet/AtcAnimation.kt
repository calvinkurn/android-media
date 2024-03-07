package com.tkpd.atcvariant.view.bottomsheet

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.animation.PathInterpolator
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.tkpd.atcvariant.databinding.AtcAnimationLayoutBinding
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getLocationOnScreen
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.half
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion


/**
 * Created by yovi.putra on 3/4/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */


class AtcAnimation(private val activity: Activity) {
    companion object {
        // refer to [com.tokopedia.searchbar.navigation_component.R.layout.nav_main_toolbar]
        private val ACTION_ICON_SIZE = 54.toPx()
        private val HALF_ACTION_ICON_SIZE = ACTION_ICON_SIZE / 2
        private val MARGIN_END_CONTAINER_TOOLBAR_ACTION = 2.toPx()
        private const val CART_POS_FROM_LEFT_IN_PDP_TOOLBAR = 2
        private val PDP_CART_LOCATION_X =
            ACTION_ICON_SIZE * CART_POS_FROM_LEFT_IN_PDP_TOOLBAR - HALF_ACTION_ICON_SIZE + MARGIN_END_CONTAINER_TOOLBAR_ACTION
        private val TOOLBAR_HEIGHT = 56.toPx()
        private val TARGET_IMAGE_SIZE_AFTER_ANIMATE = 12.toPx()
        private val STROKE_WIDTH = 4.toPx()

        private val NO_LOCATION = Point(-1, -1)
    }

    private var mSourceImageView: ImageView? = null
    private val targetLocationAbsolute by lazy {
        Point(
            getScreenWidth() - PDP_CART_LOCATION_X,
            TOOLBAR_HEIGHT.div(2)
        )
    }
    private var mTargetLocation: Point = NO_LOCATION
        get() = if (field.x == NO_LOCATION.x) {
            targetLocationAbsolute
        } else {
            field
        }

    private var mSourceLocation: Point = NO_LOCATION

    private val binding by lazyThreadSafetyNone {
        AtcAnimationLayoutBinding.inflate(activity.layoutInflater)
    }
    private val popupWindow by lazyThreadSafetyNone {
        val matchParent = LayoutParams.MATCH_PARENT
        PopupWindow(binding.root, matchParent, matchParent).apply {
            isTouchable = false
        }
    }

    private val bezier = PathInterpolator(0.63f, 0.01f, 0.29f, 1f)

    private val showAnimatorSet = AnimatorSet().apply {
        interpolator = bezier
        duration = UnifyMotion.T3
    }

    private val flyAnimatorSet = AnimatorSet().apply {
        interpolator = bezier
        duration = UnifyMotion.T3
    }

    fun setSourceView(view: ImageView): AtcAnimation {
        mSourceImageView = view
        setSourceLocation(view = view)
        return this
    }

    private fun setSourceLocation(view: View) {
        view.post {
            val location = view.getLocationOnScreen()
            mSourceLocation = Point(location.actualX, location.actualY)
        }
    }

    fun setTargetLocation(point: Point?) {
        mTargetLocation = point ?: NO_LOCATION
    }

    private fun Context.getStatusBarHeight(): Int {
        var height = 0
        val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = resources.getDimensionPixelSize(resId)
        }
        return height
    }

    fun show() {
        showAnimatorSet.removeAllListeners()
        flyAnimatorSet.removeAllListeners()
        showAnimatorSet.cancel()
        flyAnimatorSet.cancel()
        popupWindow.dismiss()

        prepare()
        resetState()
        showPopUpWindow()
    }

    private fun prepare() {
        val source = mSourceImageView ?: throw IllegalStateException("SourceView not defined")

        // set resource as drawable from source
        binding.productImage.setImageDrawable(source.drawable)
    }

    private fun resetState() {
        val width = mSourceImageView?.width ?: 0
        val height = mSourceImageView?.height ?: 0
        // set current position from source location
        with(binding.cardImage) {
            hide()
            x = mSourceLocation.x.toFloat()
            y = mSourceLocation.y.toFloat()
            translationX = x
            translationY = y
            scaleX = 1f
            scaleY = 1f
            pivotX = width.half.toFloat()
            pivotY = height.half.toFloat()
            radius = 24.toPx().toFloat()
            alpha = 1f
        }
    }

    private fun showPopUpWindow() {
        val target = mSourceImageView ?: return
        popupWindow.showAtLocation(target, Gravity.CENTER, Int.ZERO, Int.ZERO)
        binding.cardImage.show()
        animate()
    }

    private fun animate() {
        showAnimation(onEnded = {
            flyingAnimation(onEnded = {
                popupWindow.dismiss()
            })
        })
    }

    private fun showAnimation(onEnded: () -> Unit) = with(binding.cardImage) {
        val animations = prepareShowAnimation()

        showAnimatorSet.apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    showAnimatorSet.removeListener(this)
                    onEnded()
                }
            })
            playTogether(animations)
        }.start()
    }

    private fun prepareShowAnimation(): List<Animator> = with(binding.cardImage) {
        val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(this, "scaleY", 0.5f, 1f)
        val alpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        return listOf(scaleX, scaleY, alpha)
    }

    private fun flyingAnimation(onEnded: () -> Unit) {
        val animators = prepareFlyingAnimator()
        flyAnimatorSet.apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    showAnimatorSet.removeListener(this)
                    onEnded()
                }
            })
            duration = UnifyMotion.T5
            startDelay = UnifyMotion.T3
            playTogether(animators)
        }.start()
    }

    private fun prepareFlyingAnimator(): List<Animator> = with(binding.cardImage) {
        val targetX = mTargetLocation.x - mSourceImageView?.width.orZero().half
        val targetY =
            mTargetLocation.y - mSourceImageView?.height.orZero().half - activity.getStatusBarHeight()

        val translateX = ObjectAnimator.ofFloat(
            this,
            "translationX",
            translationX,
            targetX.toFloat()
        )
        val translateY = ObjectAnimator.ofFloat(
            this,
            "translationY",
            translationY,
            targetY.toFloat()
        )
        val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.24f)
        val scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.24f)
        val alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        val rounded = ObjectAnimator.ofFloat(
            this,
            "radius",
            radius,
            radius * 2
        )

        return listOf(translateX, translateY, scaleX, scaleY, rounded, alpha)
    }

    private val Point.actualX
        get() = x - STROKE_WIDTH - binding.cardImage.marginStart

    private val Point.actualY
        get() = y - STROKE_WIDTH - binding.cardImage.marginTop - activity.getStatusBarHeight()
}

fun Fragment.atcAnimator() = lazy { AtcAnimation(activity = requireActivity()) }

package com.tkpd.atcvariant.view.bottomsheet

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.view.animation.PathInterpolator
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.tkpd.atcvariant.databinding.AtcAnimationLayoutBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getLocationOnScreen
import com.tokopedia.kotlin.extensions.view.half
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion


/**
 * Created by yovi.putra on 3/4/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */


class AtcAnimation(private val activity: Activity) {

    companion object {
        private val STROKE_WIDTH = 4.toPx()
        private val RADIUS_DEFAULT = 24.toPx().toFloat()
        private val TARGET_RADIUS = RADIUS_DEFAULT * 4
        private const val START_SCALE_IN_PLAY = 0.5f
        private const val END_SCALE_IN_FLY = 0.05f

        private const val PROP_TRANSLATION_X = "translationX"
        private const val PROP_TRANSLATION_Y = "translationY"
        private const val PROP_SCALE_X = "scaleX"
        private const val PROP_SCALE_Y = "scaleY"
        private const val PROP_ALPHA = "alpha"
        private const val PROP_RADIUS = "radius"

        private val NO_LOCATION = Point(-Int.ZERO, -Int.ZERO)
    }

    private var mSourceImageView: ImageView? = null

    private var mTargetLocation: Point = NO_LOCATION

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

    private val bezier by lazy {
        PathInterpolator(0.63f, 0.01f, 0.29f, 1f)
    }

    private val showAnimatorSet = AnimatorSet().apply {
        interpolator = bezier
        duration = UnifyMotion.T3
    }

    private val flyAnimatorSet = AnimatorSet().apply {
        interpolator = bezier
        duration = UnifyMotion.T3
    }

    fun setSourceView(view: ImageView) {
        mSourceImageView = view
    }

    fun setTargetLocation(point: Point?) {
        mTargetLocation = point ?: NO_LOCATION
    }

    private fun Context.getStatusBarHeight(): Int {
        var height = Int.ZERO
        val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > Int.ZERO) {
            height = resources.getDimensionPixelSize(resId)
        }
        return height
    }

    fun show(onAnimateEnded: () -> Unit = {}) {
        cancelPopUpWindow() // cancel popup window if it's showing
        showPopUpWindow(onAnimateEnded)
    }

    // cancel all animate and dismiss the popup window
    private fun cancelPopUpWindow() {
        showAnimatorSet.removeAllListeners()
        flyAnimatorSet.removeAllListeners()
        showAnimatorSet.cancel()
        flyAnimatorSet.cancel()
        popupWindow.dismiss()
    }

    private fun showPopUpWindow(onAnimateEnded: () -> Unit) {
        val target = mSourceImageView ?: return
        popupWindow.showAtLocation(target, Gravity.CENTER, Int.ZERO, Int.ZERO)
        popupWindow.contentView.addOneTimeGlobalLayoutListener {
            prepare()
            resetState()
            animate(onAnimateEnded)
        }
    }

    private fun prepare() {
        val source = mSourceImageView ?: throw IllegalStateException("SourceView not defined")

        // set resource as drawable from source
        binding.productImage.setImageDrawable(source.drawable)

        // get source location on screen by actual within popup window
        val location = source.getLocationOnScreen()
        mSourceLocation = Point(location.sourceActualX, location.sourceActualY)
    }

    private fun resetState() {
        val source = mSourceImageView ?: return
        with(binding.cardImage) {
            x = mSourceLocation.x.toFloat()
            y = mSourceLocation.y.toFloat()
            translationX = x
            translationY = y
            scaleX = Float.ONE
            scaleY = Float.ONE
            pivotX = source.width.half.toFloat()
            pivotY = source.height.half.toFloat()
            radius = RADIUS_DEFAULT
            alpha = Float.ZERO
        }
    }

    private fun animate(onAnimateEnded: () -> Unit) {
        showAnimation(onEnded = {
            flyingAnimation(onEnded = {
                popupWindow.dismiss()
                onAnimateEnded()
            })
        })
    }

    private fun showAnimation(onEnded: () -> Unit) = binding.cardImage.postOnAnimation {
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
        val scaleX = ObjectAnimator.ofFloat(this, PROP_SCALE_X, START_SCALE_IN_PLAY, Float.ONE)
        val scaleY = ObjectAnimator.ofFloat(this, PROP_SCALE_Y, START_SCALE_IN_PLAY, Float.ONE)
        val alpha = ObjectAnimator.ofFloat(this, PROP_ALPHA, Float.ZERO, Float.ONE)
        return listOf(scaleX, scaleY, alpha)
    }

    private fun flyingAnimation(onEnded: () -> Unit) = binding.cardImage.postOnAnimation {
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
        val targetX = mTargetLocation.targetActualX
        val targetY = mTargetLocation.targetActualY
        val translateX =
            ObjectAnimator.ofFloat(this, PROP_TRANSLATION_X, translationX, targetX.toFloat())
        val translateY =
            ObjectAnimator.ofFloat(this, PROP_TRANSLATION_Y, translationY, targetY.toFloat())
        val scaleX = ObjectAnimator.ofFloat(this, PROP_SCALE_X, Float.ONE, END_SCALE_IN_FLY)
        val scaleY = ObjectAnimator.ofFloat(this, PROP_SCALE_Y, Float.ONE, END_SCALE_IN_FLY)
        val alpha = ObjectAnimator.ofFloat(this, PROP_ALPHA, Float.ONE, Float.ZERO)
        val rounded = ObjectAnimator.ofFloat(this, PROP_RADIUS, radius, TARGET_RADIUS)

        return listOf(translateX, translateY, scaleX, scaleY, rounded, alpha)
    }

    private val Point.sourceActualX
        get() = x - STROKE_WIDTH - binding.cardImage.marginStart

    private val Point.sourceActualY
        get() = y - STROKE_WIDTH - binding.cardImage.marginTop - activity.getStatusBarHeight()

    private val Point.targetActualX
        get() = x - mSourceImageView?.width.orZero().half

    private val Point.targetActualY
        get() = y - mSourceImageView?.height.orZero().half - activity.getStatusBarHeight()
}

fun Fragment.atcAnimator() = lazy { AtcAnimation(activity = requireActivity()) }

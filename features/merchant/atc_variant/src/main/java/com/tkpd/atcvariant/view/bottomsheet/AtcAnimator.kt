package com.tkpd.atcvariant.view.bottomsheet

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
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
import com.tokopedia.kotlin.extensions.view.getLocationOnScreen
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.getStatusBarHeight
import com.tokopedia.kotlin.extensions.view.half
import com.tokopedia.kotlin.extensions.view.third
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by yovi.putra on 3/4/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */
class AtcAnimator(private val context: Context) {

    companion object {
        private val STROKE_WIDTH = 2f.dpToPx()
        private val CARD_ELEVATION = 8f.dpToPx()
        private val RADIUS_DEFAULT = 24f.dpToPx()
        private val TARGET_RADIUS = RADIUS_DEFAULT * 4
        private const val SCALE_AFTER_IN_CART = 0.05f
        private val IMAGE_SIZE_AFTER_IN_CENTER = 218f.dpToPx()

        private const val PROP_TRANSLATION_X = "translationX"
        private const val PROP_TRANSLATION_Y = "translationY"
        private const val PROP_SCALE_X = "scaleX"
        private const val PROP_SCALE_Y = "scaleY"
        private const val PROP_ALPHA = "alpha"
        private const val PROP_RADIUS = "radius"

        private val NO_LOCATION = Point(-Int.ZERO, -Int.ZERO)
    }

    private val mBinding by lazyThreadSafetyNone {
        AtcAnimationLayoutBinding.inflate(LayoutInflater.from(context)).apply {
            with(cardImage) {
                borderWidth = STROKE_WIDTH
                cardBorderColor = context.getColorChecker(unifyprinciplesR.color.Unify_NN50)
                cardType = CardUnify2.TYPE_BORDER
                cardElevation = CARD_ELEVATION
            }
        }
    }

    private val mPopupWindow by lazyThreadSafetyNone {
        val matchParent = LayoutParams.MATCH_PARENT
        PopupWindow(mBinding.root, matchParent, matchParent).apply {
            isTouchable = false
        }
    }

    private val mBezierPath by lazyThreadSafetyNone {
        PathInterpolator(0.63f, 0.01f, 0.29f, 1f)
    }

    private val mFirstAnimatorSet by lazyThreadSafetyNone {
        AnimatorSet().apply {
            interpolator = mBezierPath
            duration = UnifyMotion.T5
        }
    }

    private val mSecondAnimatorSet by lazyThreadSafetyNone {
        AnimatorSet().apply {
            interpolator = mBezierPath
            duration = UnifyMotion.T5
            startDelay = ProductDetailCommonConstant.START_DELAY_SECOND_ANIMATION
        }
    }

    private var mSourceImageView: ImageView? = null

    private var mSourceLocation: Point = NO_LOCATION

    private var mTargetLocation: Point = NO_LOCATION

    fun setSourceView(view: ImageView) {
        mSourceImageView = view
    }

    fun setTargetLocation(point: Point?) {
        mTargetLocation = point ?: NO_LOCATION
    }

    fun show(onAnimateEnded: () -> Unit = {}) {
        cancelPopUpWindow() // cancel popup window if it's showing
        showPopUpWindow(onAnimateEnded)
    }

    // cancel all animate and dismiss the popup window
    private fun cancelPopUpWindow() {
        mFirstAnimatorSet.removeAllListeners()
        mFirstAnimatorSet.cancel()

        mSecondAnimatorSet.removeAllListeners()
        mSecondAnimatorSet.cancel()

        mPopupWindow.dismiss()
    }

    private fun showPopUpWindow(onAnimateEnded: () -> Unit) {
        val target = mSourceImageView ?: return
        mPopupWindow.showAtLocation(target, Gravity.CENTER, Int.ZERO, Int.ZERO)
        prepare()
        resetState()
        animate(onAnimateEnded)
    }

    private fun prepare() {
        val source = mSourceImageView ?: throw IllegalStateException("SourceView not defined")

        // set resource as drawable from source
        mBinding.productImage.setImageDrawable(source.drawable)

        // calculate location from source view
        setSourceLocation(source = source)
    }

    private fun setSourceLocation(source: ImageView) {
        // get source location on screen by actual within popup window
        mSourceLocation = source.getLocationOnScreen().run {
            val cardImage = mBinding.cardImage
            val actualX = x - STROKE_WIDTH - cardImage.marginStart
            val actualY = y - STROKE_WIDTH - cardImage.marginTop - context.getStatusBarHeight()
            Point(actualX.toInt(), actualY.toInt())
        }
    }

    private fun resetState() {
        val source = mSourceImageView ?: return
        with(mBinding.cardImage) {
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
        playFirstAnimation(onEnded = {
            playSecondAnimation(onEnded = {
                mPopupWindow.dismiss()
                onAnimateEnded()
            })
        })
    }

    /***
     * ----------------------------
     * First Animation
     * ----------------------------
     * Animation specs as below.
     * - translate position from [mSourceImageView] position to center of screen
     * - scale from [SCALE_AFTER_IN_CART] to [IMAGE_SIZE_AFTER_IN_CENTER] div current width
     * - alpha 0 to 1
     */
    // region of first animation
    private fun playFirstAnimation(onEnded: () -> Unit) = mBinding.cardImage.postOnAnimation {
        val animations = prepareFirstAnimation()

        mFirstAnimatorSet.apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mFirstAnimatorSet.removeListener(this)
                    onEnded()
                }
            })
            playTogether(animations)
        }.start()
    }

    private val translateXtoCenterOfScreen
        get() = with(mBinding.cardImage) {
            getScreenWidth().toFloat().half - width.toFloat().half - marginStart
        }

    private val translateYtoThirdOfScreen
        get() = with(mBinding.cardImage) {
            getScreenHeight().toFloat().third - height.toFloat().half - marginTop
        }

    private fun prepareFirstAnimation(): List<Animator> = with(mBinding.cardImage) {
        val targetScaleX = IMAGE_SIZE_AFTER_IN_CENTER / width
        val targetScaleY = IMAGE_SIZE_AFTER_IN_CENTER / height
        val scaleX = animateBy(
            property = PROP_SCALE_X,
            to = targetScaleX
        )
        val scaleY = animateBy(
            property = PROP_SCALE_Y,
            to = targetScaleY
        )
        val alpha = animateBy(
            property = PROP_ALPHA,
            from = Float.ONE.half,
            to = Float.ONE
        )
        val translateX = animateBy(
            property = PROP_TRANSLATION_X,
            to = translateXtoCenterOfScreen
        )
        val translateY = animateBy(
            property = PROP_TRANSLATION_Y,
            to = translateYtoThirdOfScreen
        )
        return listOf(scaleX, scaleY, alpha, translateX, translateY)
    }
    // endregion

    /***
     * ----------------------------
     * Second Animation
     * ----------------------------
     * Animation specs as below.
     * - translate position from center of screen to [mTargetLocation]
     * - scale from current scale(after first animation) to [SCALE_AFTER_IN_CART]
     * - alpha 0 to 1
     */
    // region of second animation
    private fun playSecondAnimation(onEnded: () -> Unit) = mBinding.cardImage.postOnAnimation {
        val animators = prepareSecondAnimator()
        mSecondAnimatorSet.apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mFirstAnimatorSet.removeListener(this)
                    onEnded()
                }
            })
            playTogether(animators)
        }.start()
    }

    private fun prepareSecondAnimator(): List<Animator> = with(mBinding.cardImage) {
        val targetTranslation = getTargetTranslation()
        val translateX = animateBy(property = PROP_TRANSLATION_X, to = targetTranslation.first)
        val translateY = animateBy(property = PROP_TRANSLATION_Y, to = targetTranslation.second)
        val scaleX = animateBy(property = PROP_SCALE_X, to = SCALE_AFTER_IN_CART)
        val scaleY = animateBy(property = PROP_SCALE_Y, to = SCALE_AFTER_IN_CART)
        val alpha = animateBy(property = PROP_ALPHA, to = Float.ZERO)
        val rounded = animateBy(property = PROP_RADIUS, to = TARGET_RADIUS)

        return listOf(translateX, translateY, scaleX, scaleY, rounded, alpha)
    }

    private fun getTargetTranslation() = mTargetLocation.run {
        val source = mSourceImageView ?: return Float.ZERO to Float.ZERO
        val actualX = x - source.width.toFloat().half
        val actualY = y - source.height.toFloat().half - context.getStatusBarHeight()
        actualX to actualY
    }

    private fun View.animateBy(property: String, from: Float, to: Float): Animator {
        return ObjectAnimator.ofFloat(this, property, from, to)
    }

    private fun View.animateBy(property: String, to: Float): Animator {
        return ObjectAnimator.ofFloat(this, property, to)
    }
}

fun Fragment.atcAnimator() = lazy { AtcAnimator(context = requireContext()) }

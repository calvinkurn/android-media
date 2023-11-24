package com.tokopedia.shop.common.view.customview.bannerhotspot

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.view.model.ImageHotspotData
import com.tokopedia.shop.databinding.HotspotBubbleViewBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion

class HotspotBubbleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    companion object {
        private const val THRESHOLD_POS_Y_TO_INFLATE_TAGGING_BUBBLE_DOWNWARD = 0.60
        private const val POSITION_TOP = 1
        private const val POSITION_BOTTOM = 2
        private const val INT_TWO = 2
        private const val ALPHA_HIDE = 0f
        private const val SCALE_X_HIDE = 0.75f
        private const val SCALE_Y_HIDE = 0.75f
        private const val ALPHA_SHOW = 1f
        private const val SCALE_X_SHOW = 1f
        private const val SCALE_Y_SHOW = 1f
        private val MINIMUM_HORIZONTAL_MARGIN = 8f.dpToPx()
    }

    private var viewBinding: HotspotBubbleViewBinding
    private var position: Int = 0
    private val productTagPointerTop: View
        get() = viewBinding.productTagTopPointer
    private val productTagPointerBottom: View
        get() = viewBinding.productTagBottomPointer
    private var finalPointerView: View? = null
    private val productImage: ImageUnify
        get() = viewBinding.imageProduct
    private val productName: Typography
        get() = viewBinding.productNameText
    private val productPrice: Typography
        get() = viewBinding.productPriceText

    init {
        viewBinding = HotspotBubbleViewBinding.inflate(LayoutInflater.from(context), this)
        setDefaultAnimationState()
    }

    private fun setDefaultAnimationState() {
        alpha = ALPHA_HIDE
        scaleX = SCALE_X_HIDE
        scaleY = SCALE_Y_HIDE
        gone()
    }

    fun bindData(
        hotspotData: ImageHotspotData.HotspotData,
        bannerImageWidth: Int,
        bannerImageHeight: Int,
        hotspotTagView: View,
        listener: ImageHotspotView.Listener,
        index: Int
    ) {
        setBubbleProductData(hotspotData)
        determineBubbleVerticalPosition(hotspotData.y)
        setBubblePosition(hotspotData, bannerImageWidth, bannerImageHeight, hotspotTagView)
        setPivotXYForAnimation()
        setOnClickListener {
            listener.onBubbleViewClicked(hotspotData, it, index)
        }
    }

    private fun setBubbleProductData(hotspotData: ImageHotspotData.HotspotData) {
        productImage.loadImage(hotspotData.productImage)
        productName.text = hotspotData.productName
        productPrice.text = hotspotData.productPrice
    }

    private fun setBubblePosition(
        hotspotData: ImageHotspotData.HotspotData,
        bannerImageWidth: Int,
        bannerImageHeight: Int,
        hotspotTagView: View
    ) {
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        hotspotTagView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        finalPointerView?.apply {
            measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            show()
        }
        determineXPosition(bannerImageWidth, hotspotData.x, hotspotTagView.measuredWidth)
        determineYPosition(bannerImageHeight, hotspotData.y, hotspotTagView.measuredHeight)
    }

    private fun determineYPosition(bannerImageHeight: Int, taggingYPos: Float, hotspotTagViewMeasuredHeight: Int) {
        y = getYPosition(bannerImageHeight, taggingYPos, hotspotTagViewMeasuredHeight)
    }

    private fun determineXPosition(bannerImageWidth: Int, taggingXPos: Float, hotspotTagViewMeasuredWidth: Int) {
        val xPosRaw = getXPosition(bannerImageWidth, taggingXPos, hotspotTagViewMeasuredWidth)
        val xPosActual = adjustXPos(xPosRaw, bannerImageWidth)
        x = xPosActual
        finalPointerView?.x = getPointerXPos(xPosRaw, bannerImageWidth)
        finalPointerView?.show()
    }

    /***
    *   Set bubble view pointer x position by finding the middle of bubble view width
    *   Then adjust the pointer position if bubble position is out of bound
    */
    private fun getPointerXPos(xPosRaw: Float, bannerImageWidth: Int): Float {
        val middleBubbleView = (measuredWidth.toFloat() / INT_TWO)
        val middlePointerView = finalPointerView?.measuredWidth?.toFloat()?.div(INT_TWO).orZero()
        var pointerXPos = middleBubbleView - middlePointerView
        if (xPosRaw <= Int.ZERO) {
            pointerXPos-=MINIMUM_HORIZONTAL_MARGIN - xPosRaw
        } else if (xPosRaw + measuredWidth.toFloat() >= bannerImageWidth.toFloat()) {
            val excessBubbleWidth = (xPosRaw + measuredWidth.toFloat() - bannerImageWidth.toFloat())
            pointerXPos+=excessBubbleWidth + MINIMUM_HORIZONTAL_MARGIN
        }
        return pointerXPos
    }

    /***
     *  Adjust raw x value by checking if it will cause out of bounds
     */
    private fun adjustXPos(xPosRaw: Float, bannerImageWidth: Int): Float {
        return if (xPosRaw <= Float.ZERO) {
            Float.ZERO + MINIMUM_HORIZONTAL_MARGIN
        } else if (xPosRaw + measuredWidth.toFloat() >= bannerImageWidth.toFloat()) {
            val excessBubbleWidth = (xPosRaw + measuredWidth.toFloat() - bannerImageWidth.toFloat())
            (xPosRaw - excessBubbleWidth - MINIMUM_HORIZONTAL_MARGIN)
        } else {
            xPosRaw
        }
    }

    /***
     *  Get x position for bubble view, but the value is still raw
     *  meaning that it's possible this x value will cause out of bounds
     */
    private fun getXPosition(bannerImageWidth: Int, x: Float, hotspotTagViewMeasuredWidth: Int): Float {
        return (bannerImageWidth.toFloat() * x) - (measuredWidth.toFloat() / INT_TWO) + (hotspotTagViewMeasuredWidth.toFloat() / INT_TWO)
    }

    /***
     *  Get y position for bubble view, while also validate the position of the bubble
     *  so that it won't cause out of bound on vertical position
     */
    private fun getYPosition(bannerImageHeight: Int, y: Float, hotspotTagViewMeasuredHeight: Int): Float {
        return if (position == POSITION_TOP) {
            (bannerImageHeight.toFloat() * y) - measuredHeight - finalPointerView?.measuredHeight.orZero()
        } else {
            (bannerImageHeight.toFloat() * y) + hotspotTagViewMeasuredHeight
        }
    }

    private fun setPivotXYForAnimation() {
        val middlePointerView = finalPointerView?.measuredWidth?.toFloat()?.div(INT_TWO).orZero()
        pivotX = finalPointerView?.x.orZero() + middlePointerView
        pivotY = if (position == POSITION_TOP) {
            measuredHeight.toFloat()
        } else {
            Int.ZERO.toFloat()
        }
    }

    /***
     *  Determine bubble vertical position and which pointer should be taken based on defined threshold
     */
    private fun determineBubbleVerticalPosition(taggingYPos: Float) {
        position = if (taggingYPos > THRESHOLD_POS_Y_TO_INFLATE_TAGGING_BUBBLE_DOWNWARD) {
            finalPointerView = productTagPointerBottom
            POSITION_TOP
        } else {
            finalPointerView = productTagPointerTop
            POSITION_BOTTOM
        }
    }

    fun showWithAnimation() {
        animate().scaleX(SCALE_X_SHOW).scaleY(SCALE_Y_SHOW)
            .alpha(ALPHA_SHOW)
            .setInterpolator(UnifyMotion.EASE_OUT)
            .setDuration(UnifyMotion.T3)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                    show()
                }

                override fun onAnimationEnd(p0: Animator) {
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })
    }

    fun hideWithAnimation() {
        animate().scaleX(SCALE_X_HIDE).scaleY(SCALE_Y_HIDE)
            .alpha(ALPHA_HIDE)
            .setInterpolator(UnifyMotion.EASE_IN_OUT)
            .setDuration(UnifyMotion.T3)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    gone()
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })
    }

    fun isVisibleWithAlpha(): Boolean {
        return alpha != Int.ZERO.toFloat()
    }
}

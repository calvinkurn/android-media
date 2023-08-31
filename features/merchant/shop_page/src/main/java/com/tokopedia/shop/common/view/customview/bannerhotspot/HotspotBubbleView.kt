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
        determineBubbleVerticalPosition(hotspotData)
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
        finalPointerView?.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        finalPointerView?.show()
        x =
            (bannerImageWidth.toFloat() * (hotspotData.x)) - (measuredWidth.toFloat() / INT_TWO) + (hotspotTagView.measuredWidth.toFloat() / INT_TWO)
        finalPointerView?.x =
            (measuredWidth.toFloat() / INT_TWO) - finalPointerView?.measuredWidth?.toFloat()
                ?.div(INT_TWO)
                .orZero()
        y = if (position == POSITION_TOP) {
            (bannerImageHeight.toFloat() * (hotspotData.y)) - measuredHeight - finalPointerView?.measuredHeight.orZero()
        } else {
            (bannerImageHeight.toFloat() * (hotspotData.y)) + hotspotTagView.measuredHeight
        }
    }

    private fun setPivotXYForAnimation() {
        pivotX = (measuredWidth.toFloat() / INT_TWO)
        pivotY = if (position == POSITION_TOP) {
            Int.ZERO.toFloat()
        } else {
            measuredHeight.toFloat()
        }
    }

    private fun determineBubbleVerticalPosition(hotspotData: ImageHotspotData.HotspotData) {
        position = if (hotspotData.y > THRESHOLD_POS_Y_TO_INFLATE_TAGGING_BUBBLE_DOWNWARD) {
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

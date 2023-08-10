package com.tokopedia.product.detail.view.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaRecomData
import com.tokopedia.product.detail.databinding.AutoCollapseLabelBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.toPx

class AutoCollapseLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val MIN_ALPHA = 0f
        private const val MAX_ALPHA = 1f
        private const val ANIMATION_DURATION = 400L
        private const val IDLE_DURATION = 3000L
        private const val PADDING_HORIZONTAL_TEXT_WITH_ICON = 8
        private const val PADDING_HORIZONTAL_ICON_ONLY = 8
        private const val PADDING_VERTICAL_TEXT_WITH_ICON = 4
        private const val PADDING_VERTICAL_ICON_ONLY = 7
        private const val MARGIN_BOTTOM_TEXT_WITH_ICON = 8
        private const val MARGIN_BOTTOM_ICON_ONLY = 4
        private const val BACKGROUND_DRAWABLE_RADIUS_TEXT_WITH_ICON = 6
        private const val BACKGROUND_DRAWABLE_RADIUS_ICON_ONLY = 16
        private const val BACKGROUND_DRAWABLE_STROKE_WIDTH = 1
        private const val PROPERTY_NAME_ALPHA = "alpha"
        private const val PROPERTY_NAME_MAX_WIDTH = "maxWidth"
    }

    private val binding = AutoCollapseLabelBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    ).apply {
        root.background = createBackgroundDrawable()
    }
    private val animator = ViewAnimator()
    private var canShow: Boolean = false

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.pauseAnimators()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.resumeAnimators()
    }

    fun setup(recomData: ProductMediaRecomData) {
        canShow = recomData.shouldShow()
        if (canShow) bindData(recomData)
    }

    fun showView() {
        if (canShow) {
            if (collapsed()) {
                animator.animateShowCollapsed(0L)
            } else {
                animator.animateShowExpanded()
            }
        } else {
            hideView()
        }
    }

    fun hideView() {
        animator.animateHide()
    }

    private fun bindData(recomData: ProductMediaRecomData) {
        binding.tvAutoCollapseLabel.text = recomData.iconText
        binding.ivAutoCollapseLabel.loadImage(recomData.getIconUrl(context))
    }

    private fun collapsed(): Boolean {
        return binding.tvAutoCollapseLabel.ellipsize == null
    }

    private fun createBackgroundDrawable() = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(context.getColorChecker(com.tokopedia.unifyprinciples.R.color.Unify_NN0))
        cornerRadius = BACKGROUND_DRAWABLE_RADIUS_TEXT_WITH_ICON.toPx().toFloat()
        setStroke(
            BACKGROUND_DRAWABLE_STROKE_WIDTH.toPx(),
            context.getColorChecker(com.tokopedia.unifyprinciples.R.color.Unify_NN300)
        )
    }

    inner class ViewAnimator {
        private var showAnimator: Animator? = null
        private var showAnimatorListener: Animator.AnimatorListener? = null
        private var hideAnimator: Animator? = null
        private var hideAnimatorListener: Animator.AnimatorListener? = null
        private var collapseAnimator: AnimatorSet? = null
        private var collapseAnimatorListener: Animator.AnimatorListener? = null

        fun animateShowExpanded() {
            cancelAnimators()

            showAnimator = createShowAnimator()
            showAnimator?.start()
        }

        fun animateHide() {
            cancelAnimators()

            hideAnimator = createHideAnimator()
            hideAnimator?.start()
        }

        fun animateShowCollapsed(delay: Long) {
            cancelAnimators()

            collapseAnimator = createCollapseAnimator(delay)
            collapseAnimator?.start()
        }

        fun pauseAnimators() {
            showAnimator?.pause()
            collapseAnimator?.pause()
            hideAnimator?.pause()
        }

        fun resumeAnimators() {
            showAnimator?.resume()
            collapseAnimator?.resume()
            hideAnimator?.resume()
        }

        private fun createShowAnimator() = binding.root.createAlphaAnimator(
            binding.root.alpha,
            MAX_ALPHA
        ).apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            showAnimatorListener = addListener(
                onStart = ::onStartShowAnimator,
                onEnd = ::onEndShowAnimator
            )
        }

        private fun createHideAnimator() = binding.root.createAlphaAnimator(
            binding.root.alpha,
            MIN_ALPHA
        ).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
            hideAnimatorListener = addListener(onEnd = ::onEndHideAnimator)
        }

        private fun createCollapseAnimator(delay: Long) = AnimatorSet().apply {
            duration = ANIMATION_DURATION
            startDelay = delay
            playTogether(
                binding.tvAutoCollapseLabel.createMaxWidthAnimator(
                    binding.tvAutoCollapseLabel.width,
                    Int.ZERO
                ),
                binding.root.animateBackgroundDrawableRadius(
                    binding.root.getBackgroundDrawableCornerRadius(
                        BACKGROUND_DRAWABLE_RADIUS_TEXT_WITH_ICON.toPx().toFloat()
                    ),
                    BACKGROUND_DRAWABLE_RADIUS_ICON_ONLY.toPx().toFloat()
                ),
                binding.root.createAlphaAnimator(binding.root.alpha, MAX_ALPHA),
                binding.root.createHorizontalPaddingAnimator(
                    binding.root.paddingStart,
                    PADDING_HORIZONTAL_ICON_ONLY.toPx()
                ),
                binding.root.createVerticalPaddingAnimator(
                    binding.root.paddingTop,
                    PADDING_VERTICAL_ICON_ONLY.toPx()
                ),
                binding.root.createMarginBottomAnimator(
                    binding.root.marginBottom,
                    MARGIN_BOTTOM_ICON_ONLY.toPx()
                )
            )
            collapseAnimatorListener = addListener(onStart = ::onStartCollapseAnimator)
        }

        private fun cancelAnimators() {
            cancelShowAnimator()
            cancelHideAnimator()
            cancelCollapseAnimator()
        }

        private fun cancelShowAnimator() {
            showAnimator?.removeListener(showAnimatorListener)
            showAnimator?.cancel()
        }

        private fun cancelHideAnimator() {
            hideAnimator?.removeListener(hideAnimatorListener)
            hideAnimator?.cancel()
        }

        private fun cancelCollapseAnimator() {
            collapseAnimator?.removeListener(collapseAnimatorListener)
            collapseAnimator?.cancel()
        }

        @Suppress("UNUSED_PARAMETER")
        private fun onStartShowAnimator(animator: Animator) {
            show()
            binding.tvAutoCollapseLabel.maxWidth = Int.MAX_VALUE
            binding.tvAutoCollapseLabel.ellipsize = TextUtils.TruncateAt.END
            binding.root.setBackgroundDrawableCornerRadius(
                BACKGROUND_DRAWABLE_RADIUS_TEXT_WITH_ICON.toPx().toFloat()
            )
            binding.root.updatePadding(
                left = PADDING_HORIZONTAL_TEXT_WITH_ICON.toPx(),
                top = PADDING_VERTICAL_TEXT_WITH_ICON.toPx(),
                right = PADDING_HORIZONTAL_TEXT_WITH_ICON.toPx(),
                bottom = PADDING_VERTICAL_TEXT_WITH_ICON.toPx()
            )
            binding.root.updateMargin(bottom = MARGIN_BOTTOM_TEXT_WITH_ICON.toPx())
        }

        @Suppress("UNUSED_PARAMETER")
        private fun onEndShowAnimator(animator: Animator) {
            animateShowCollapsed(IDLE_DURATION)
        }

        @Suppress("UNUSED_PARAMETER")
        private fun onEndHideAnimator(animator: Animator) {
            gone()
        }

        @Suppress("UNUSED_PARAMETER")
        private fun onStartCollapseAnimator(animator: Animator) {
            show()
            binding.tvAutoCollapseLabel.ellipsize = null
        }

        private fun View.updatePadding(
            left: Int = paddingLeft,
            top: Int = paddingTop,
            right: Int = paddingRight,
            bottom: Int = paddingBottom
        ) {
            setPadding(left, top, right, bottom)
        }

        private fun View.updateMargin(
            left: Int = marginLeft,
            top: Int = marginTop,
            right: Int = marginRight,
            bottom: Int = marginBottom
        ) {
            setMargin(left, top, right, bottom)
        }

        private fun View.createAlphaAnimator(start: Float, end: Float): Animator {
            return ObjectAnimator.ofFloat(this, PROPERTY_NAME_ALPHA, start, end)
        }

        private fun TextView.createMaxWidthAnimator(start: Int, end: Int): Animator {
            return ObjectAnimator.ofInt(this, PROPERTY_NAME_MAX_WIDTH, start, end)
        }

        private fun View.createHorizontalPaddingAnimator(start: Int, end: Int): Animator {
            return ValueAnimator.ofInt(start, end).apply {
                addUpdateListener {
                    val padding = it.animatedValue as Int
                    updatePadding(left = padding, right = padding)
                }
            }
        }

        private fun View.createVerticalPaddingAnimator(start: Int, end: Int): Animator {
            return ValueAnimator.ofInt(start, end).apply {
                addUpdateListener {
                    val padding = it.animatedValue as Int
                    updatePadding(top = padding, bottom = padding)
                }
            }
        }

        private fun View.createMarginBottomAnimator(start: Int, end: Int): Animator {
            return ValueAnimator.ofInt(start, end).apply {
                addUpdateListener { updateMargin(bottom = it.animatedValue as Int) }
            }
        }

        private fun View.animateBackgroundDrawableRadius(start: Float, end: Float): Animator {
            return ValueAnimator.ofFloat(start, end).apply {
                addUpdateListener { newValue ->
                    setBackgroundDrawableCornerRadius(newValue.animatedValue as Float)
                }
            }
        }

        /*
            We save the background drawable corner radius on the tag attribute, this is because we
            can't access background drawable radius value prior to android 24
         */
        private fun View.setBackgroundDrawableCornerRadius(value: Float) {
            val drawable = background as? GradientDrawable
            drawable?.cornerRadius = value
            background = drawable
            tag = value
        }

        /*
            We save the background drawable corner radius on the tag attribute, this is because we
            can't access background drawable radius value prior to android 24
         */
        private fun View.getBackgroundDrawableCornerRadius(defaultValue: Float): Float {
            return (tag as? Float) ?: defaultValue
        }
    }
}

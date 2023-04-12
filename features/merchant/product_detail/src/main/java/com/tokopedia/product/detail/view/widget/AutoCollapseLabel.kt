package com.tokopedia.product.detail.view.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.animation.addListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaRecomData
import com.tokopedia.product.detail.databinding.AutoCollapseLabelBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.toPx
import java.lang.Float.min

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
        private const val PADDING_HORIZONTAL_ICON_ONLY = 5
        private const val PROPERTY_NAME_ALPHA = "alpha"
        private const val PROPERTY_NAME_MAX_WIDTH = "maxWidth"
    }

    private val binding = AutoCollapseLabelBinding.inflate(LayoutInflater.from(context), this, true)
    private val animator = ViewAnimator()
    private var canShow: Boolean = false

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
        }
    }

    fun hideView() {
        if (canShow) animator.animateHide()
    }

    private fun bindData(recomData: ProductMediaRecomData) {
        binding.tvAutoCollapseLabel.text = recomData.iconText
        binding.ivAutoCollapseLabel.loadImage(recomData.getIconUrl(context))
    }

    private fun collapsed(): Boolean {
        return binding.tvAutoCollapseLabel.ellipsize == null &&
            binding.tvAutoCollapseLabel.maxWidth == Int.ZERO
    }

    inner class ViewAnimator {
        private var showAnimator: AnimatorSet? = null
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

        private fun createShowAnimator() = AnimatorSet().apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            playTogether(
                binding.bgAutoCollapseLabelTextWithIcon.createAlphaAnimator(
                    binding.bgAutoCollapseLabelTextWithIcon.alpha,
                    MAX_ALPHA
                ),
                binding.containerAutoCollapse.createAlphaAnimator(
                    binding.containerAutoCollapse.alpha,
                    MAX_ALPHA
                )
            )
            showAnimatorListener = addListener(
                onStart = ::onStartShowAnimator,
                onEnd = ::onEndShowAnimator
            )
        }

        private fun createHideAnimator() = ValueAnimator.ofFloat(
            MAX_ALPHA,
            MIN_ALPHA
        ).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
            addUpdateListener { update -> applyHideAnimatorUpdate(update.animatedValue as Float) }
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
                binding.bgAutoCollapseLabelTextWithIcon.createAlphaAnimator(
                    binding.bgAutoCollapseLabelTextWithIcon.alpha,
                    MIN_ALPHA
                ),
                binding.bgAutoCollapseLabelIconOnly.createAlphaAnimator(
                    binding.bgAutoCollapseLabelIconOnly.alpha,
                    MAX_ALPHA
                ),
                binding.containerAutoCollapse.createAlphaAnimator(
                    binding.containerAutoCollapse.alpha,
                    MAX_ALPHA
                ),
                binding.containerAutoCollapse.createHorizontalPaddingAnimator(
                    binding.containerAutoCollapse.paddingStart,
                    PADDING_HORIZONTAL_ICON_ONLY.toPx()
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
            binding.bgAutoCollapseLabelIconOnly.alpha = MIN_ALPHA
            binding.containerAutoCollapse.updateContainerHorizontalPadding(
                PADDING_HORIZONTAL_TEXT_WITH_ICON.toPx()
            )
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

        private fun View.updateContainerHorizontalPadding(padding: Int) {
            setPadding(
                padding,
                binding.containerAutoCollapse.paddingTop,
                padding,
                binding.containerAutoCollapse.paddingBottom
            )
        }

        private fun applyHideAnimatorUpdate(alpha: Float) {
            binding.bgAutoCollapseLabelTextWithIcon.alpha = min(
                binding.bgAutoCollapseLabelTextWithIcon.alpha,
                alpha
            )
            binding.bgAutoCollapseLabelIconOnly.alpha = min(
                binding.bgAutoCollapseLabelIconOnly.alpha,
                alpha
            )
            binding.containerAutoCollapse.alpha = min(
                binding.containerAutoCollapse.alpha,
                alpha
            )
        }

        private fun View.createAlphaAnimator(start: Float, end: Float): Animator {
            return ObjectAnimator.ofFloat(this, PROPERTY_NAME_ALPHA, start, end)
        }

        private fun TextView.createMaxWidthAnimator(start: Int, end: Int): Animator {
            return ObjectAnimator.ofInt(this, PROPERTY_NAME_MAX_WIDTH, start, end)
        }

        private fun View.createHorizontalPaddingAnimator(start: Int, end: Int): Animator {
            return ValueAnimator.ofInt(start, end).apply {
                addUpdateListener { updateContainerHorizontalPadding(it.animatedValue as Int) }
            }
        }
    }
}

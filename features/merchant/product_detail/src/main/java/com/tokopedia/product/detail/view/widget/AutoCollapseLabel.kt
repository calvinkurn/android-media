package com.tokopedia.product.detail.view.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
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
    }

    private val binding = AutoCollapseLabelBinding.inflate(LayoutInflater.from(context), this, true)
    private val animator = ViewAnimator()
    private var canShow: Boolean = false

    fun setup(recommendation: ProductMediaRecomData) {
        canShow = recommendation.shouldShow()
        if (canShow) {
            binding.tvAutoCollapseLabel.text = recommendation.iconText
            binding.ivAutoCollapseLabel.loadImage(recommendation.getIconUrl(context))
        }
    }

    fun showView() {
        if (canShow) animator.animateShow()
    }

    fun hideView() {
        if (canShow) animator.animateHide()
    }

    inner class ViewAnimator {
        private var showAnimator: Animator? = null
        private var showAnimatorListener: Animator.AnimatorListener? = null
        private var hideAnimator: Animator? = null
        private var hideAnimatorListener: Animator.AnimatorListener? = null
        private var collapseAnimator: Animator? = null
        private var collapseAnimatorListener: Animator.AnimatorListener? = null

        fun animateShow() {
            cancelAnimators()

            showAnimator = createShowAnimator()
            showAnimator?.start()
        }

        fun animateHide() {
            cancelAnimators()

            hideAnimator = createHideAnimator()
            hideAnimator?.start()
        }

        private fun animateCollapse() {
            cancelAnimators()

            collapseAnimator = createCollapseAnimator()
            collapseAnimator?.start()
        }

        private fun createShowAnimator() = ValueAnimator.ofFloat(
            MIN_ALPHA,
            MAX_ALPHA
        ).apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            addUpdateListener { update ->
                binding.bgAutoCollapseLabelTextWithIcon.alpha = update.animatedValue as Float
                binding.containerAutoCollapse.alpha = update.animatedValue as Float
            }
            showAnimatorListener = addListener(
                onStart = {
                    show()
                    binding.tvAutoCollapseLabel.maxWidth = Int.MAX_VALUE
                    binding.tvAutoCollapseLabel.ellipsize = TextUtils.TruncateAt.END
                    binding.bgAutoCollapseLabelIconOnly.alpha = MIN_ALPHA
                    binding.containerAutoCollapse.setPadding(
                        PADDING_HORIZONTAL_TEXT_WITH_ICON.toPx(),
                        binding.containerAutoCollapse.paddingTop,
                        PADDING_HORIZONTAL_TEXT_WITH_ICON.toPx(),
                        binding.containerAutoCollapse.paddingBottom
                    )
                },
                onEnd = { animateCollapse() }
            )
        }

        private fun createHideAnimator() = ValueAnimator.ofFloat(
            MAX_ALPHA,
            MIN_ALPHA
        ).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
            addUpdateListener { update ->
                binding.bgAutoCollapseLabelTextWithIcon.alpha = min(
                    binding.bgAutoCollapseLabelTextWithIcon.alpha,
                    update.animatedValue as Float
                )
                binding.bgAutoCollapseLabelIconOnly.alpha = min(
                    binding.bgAutoCollapseLabelIconOnly.alpha,
                    update.animatedValue as Float
                )
                binding.containerAutoCollapse.alpha = min(
                    binding.containerAutoCollapse.alpha,
                    update.animatedValue as Float
                )
            }
            hideAnimatorListener = addListener(onEnd = { gone() })
        }

        private fun createCollapseAnimator() = ValueAnimator.ofInt(
            binding.tvAutoCollapseLabel.width,
            Int.ZERO
        ).apply {
            duration = ANIMATION_DURATION
            startDelay = IDLE_DURATION
            addUpdateListener { update ->
                binding.tvAutoCollapseLabel.maxWidth = update.animatedValue as Int
            }
            collapseAnimatorListener = addListener(
                onStart = {
                    binding.tvAutoCollapseLabel.ellipsize = null
                },
                onEnd = {
                    binding.bgAutoCollapseLabelTextWithIcon.alpha = MIN_ALPHA
                    binding.bgAutoCollapseLabelIconOnly.alpha = MAX_ALPHA
                    binding.containerAutoCollapse.setPadding(
                        PADDING_HORIZONTAL_ICON_ONLY.toPx(),
                        binding.containerAutoCollapse.paddingTop,
                        PADDING_HORIZONTAL_ICON_ONLY.toPx(),
                        binding.containerAutoCollapse.paddingBottom
                    )
                }
            )
        }

        private fun cancelAnimators() {
            showAnimator?.run {
                removeListener(showAnimatorListener)
                cancel()
            }
            hideAnimator?.cancel()
            collapseAnimator?.run {
                removeListener(collapseAnimatorListener)
                cancel()
            }
        }
    }
}

package com.tokopedia.review.feature.credibility.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetReviewCredibilityStatisticBoxBinding
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityStatisticBoxUiModel
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityStatisticBoxUiState
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ReviewCredibilityStatisticBoxWidget @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseReviewCustomView<WidgetReviewCredibilityStatisticBoxBinding>(
    context, attributeSet, defStyleAttr
) {

    companion object {
        private const val FIRST_STATISTIC_INDEX = 0
        private const val SECOND_STATISTIC_INDEX = 1
        private const val THIRD_STATISTIC_INDEX = 2
        private const val MAX_STATISTIC_COUNT = 3
    }

    override val binding: WidgetReviewCredibilityStatisticBoxBinding =
        WidgetReviewCredibilityStatisticBoxBinding.inflate(
            LayoutInflater.from(context), this, true
        ).apply {
            setupView()
        }

    private val partialWidgetStatistic1 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityStatistic(binding.reviewCredibilityStatistics1)
    }
    private val partialWidgetStatisticLoading1 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityStatisticLoading(binding.reviewCredibilityStatistics1Loading)
    }
    private val partialWidgetStatistic2 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityStatistic(binding.reviewCredibilityStatistics2)
    }
    private val partialWidgetStatisticLoading2 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityStatisticLoading(binding.reviewCredibilityStatistics2Loading)
    }
    private val partialWidgetStatistic3 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityStatistic(binding.reviewCredibilityStatistics3)
    }
    private val partialWidgetStatisticLoading3 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityStatisticLoading(binding.reviewCredibilityStatistics3Loading)
    }

    private var listener: Listener? = null

    private fun WidgetReviewCredibilityStatisticBoxBinding.setupView() {
        root.setBackgroundResource(R.drawable.bg_review_credibility_statistics_box)
    }

    private fun hideWidget() {
        animateHide(onAnimationEnd = {
            gone()
            listener?.onStatisticBoxTransitionEnd()
        })
    }

    private fun showLoading() {
        binding.reviewCredibilityStatisticBoxTitle.gone()
        partialWidgetStatistic1.hide()
        partialWidgetStatistic2.hide()
        partialWidgetStatistic3.hide()
        binding.reviewCredibilityStatisticsLoadingTitle.show()
        binding.reviewCredibilityStatisticsLoadingSubtitle.show()
        partialWidgetStatisticLoading1.show()
        partialWidgetStatisticLoading2.show()
        partialWidgetStatisticLoading3.show()
        updateStatisticItemsConstraint(MAX_STATISTIC_COUNT, true)
        animateShow(onAnimationStart = {
            show()
        }, onAnimationEnd = {
            listener?.onStatisticBoxTransitionEnd()
        })
    }

    private fun showData(data: ReviewCredibilityStatisticBoxUiModel) {
        runTransitions(createShowDataTransition())
        showUIData(data)
        updateStatisticItemsConstraint(data.statistics.size, false)
        animateShow(onAnimationStart = {
            show()
        }, onAnimationEnd = {
            listener?.onStatisticBoxTransitionEnd()
        })
    }

    private fun showUIData(data: ReviewCredibilityStatisticBoxUiModel) {
        binding.reviewCredibilityStatisticsLoadingTitle.gone()
        binding.reviewCredibilityStatisticsLoadingSubtitle.gone()
        partialWidgetStatisticLoading1.hide()
        partialWidgetStatisticLoading2.hide()
        partialWidgetStatisticLoading3.hide()
        partialWidgetStatistic1.showData(
            data.statistics.getOrNull(FIRST_STATISTIC_INDEX)
        )
        partialWidgetStatistic2.showData(
            data.statistics.getOrNull(SECOND_STATISTIC_INDEX)
        )
        partialWidgetStatistic3.showData(
            data.statistics.getOrNull(THIRD_STATISTIC_INDEX)
        )
        binding.reviewCredibilityStatisticBoxTitle.text = context?.let {
            HtmlLinkHelper(it, data.title).spannedString
        } ?: data.title
        binding.reviewCredibilityStatisticBoxTitle.show()
    }

    private fun updateStatisticItemsConstraint(statisticItemCount: Int, isLoading: Boolean) {
        val constraintSet = ConstraintSet()
        val topView = if (isLoading) {
            binding.reviewCredibilityStatisticsLoadingSubtitle.id
        } else {
            binding.reviewCredibilityStatisticBoxTitle.id
        }
        constraintSet.clone(binding.containerReviewCredibilityStatisticBox)
        constraintSet.connect(
            binding.reviewCredibilityStatistics1.root.id,
            ConstraintSet.START,
            binding.containerReviewCredibilityStatisticBox.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            binding.reviewCredibilityStatistics1Loading.root.id,
            ConstraintSet.START,
            binding.containerReviewCredibilityStatisticBox.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            binding.reviewCredibilityStatistics1.root.id,
            ConstraintSet.TOP,
            topView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.reviewCredibilityStatistics1Loading.root.id,
            ConstraintSet.TOP,
            topView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.reviewCredibilityStatistics2.root.id,
            ConstraintSet.TOP,
            topView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.reviewCredibilityStatistics2Loading.root.id,
            ConstraintSet.TOP,
            topView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.reviewCredibilityStatistics3.root.id,
            ConstraintSet.TOP,
            topView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.reviewCredibilityStatistics3Loading.root.id,
            ConstraintSet.TOP,
            topView,
            ConstraintSet.BOTTOM
        )
        when (statisticItemCount) {
            1 -> {
                constraintSet.connect(
                    binding.reviewCredibilityStatistics1.root.id,
                    ConstraintSet.END,
                    binding.containerReviewCredibilityStatisticBox.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics1Loading.root.id,
                    ConstraintSet.END,
                    binding.containerReviewCredibilityStatisticBox.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics1.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics1Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.END,
                    binding.reviewCredibilityStatistics3.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.END,
                    binding.reviewCredibilityStatistics3Loading.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics3.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics3Loading.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.clear(
                    binding.reviewCredibilityStatistics3.root.id, ConstraintSet.END
                )
                constraintSet.clear(
                    binding.reviewCredibilityStatistics3Loading.root.id, ConstraintSet.END
                )
            }
            2 -> {
                constraintSet.connect(
                    binding.reviewCredibilityStatistics1.root.id,
                    ConstraintSet.END,
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics1Loading.root.id,
                    ConstraintSet.END,
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics1.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics1Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.END,
                    binding.containerReviewCredibilityStatisticBox.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.END,
                    binding.containerReviewCredibilityStatisticBox.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics3.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics3Loading.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.clear(
                    binding.reviewCredibilityStatistics3.root.id, ConstraintSet.END
                )
                constraintSet.clear(
                    binding.reviewCredibilityStatistics3Loading.root.id, ConstraintSet.END
                )
            }
            else -> {
                constraintSet.connect(
                    binding.reviewCredibilityStatistics1.root.id,
                    ConstraintSet.END,
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics1Loading.root.id,
                    ConstraintSet.END,
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics1.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics1Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.END,
                    binding.reviewCredibilityStatistics3.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.END,
                    binding.reviewCredibilityStatistics3Loading.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics3.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics2.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics3Loading.root.id,
                    ConstraintSet.START,
                    binding.reviewCredibilityStatistics2Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics3.root.id,
                    ConstraintSet.END,
                    binding.containerReviewCredibilityStatisticBox.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.reviewCredibilityStatistics3Loading.root.id,
                    ConstraintSet.END,
                    binding.containerReviewCredibilityStatisticBox.id,
                    ConstraintSet.END
                )
            }
        }
        constraintSet.applyTo(binding.containerReviewCredibilityStatisticBox)
    }

    private fun createShowDataTransition(): Transition {
        return TransitionSet().addTransition(ChangeBounds()).addTransition(Fade()).setInterpolator(
            PathInterpolatorCompat.create(
                CUBIC_BEZIER_X1, CUBIC_BEZIER_Y1, CUBIC_BEZIER_X2, CUBIC_BEZIER_Y2
            )
        ).setDuration(ANIMATION_DURATION)
    }

    private fun runTransitions(transition: Transition) {
        TransitionManager.beginDelayedTransition(
            binding.containerReviewCredibilityStatisticBox, transition
        )
    }

    fun updateUiState(uiState: ReviewCredibilityStatisticBoxUiState) {
        when (uiState) {
            is ReviewCredibilityStatisticBoxUiState.Hidden -> hideWidget()
            is ReviewCredibilityStatisticBoxUiState.Loading -> showLoading()
            is ReviewCredibilityStatisticBoxUiState.Showed -> showData(uiState.data)
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    interface Listener {
        fun onStatisticBoxTransitionEnd()
    }
}
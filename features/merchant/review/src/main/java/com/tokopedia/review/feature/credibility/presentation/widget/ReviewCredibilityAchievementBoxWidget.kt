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
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetReviewCredibilityAchievementBoxBinding
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityAchievementBoxUiModel
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityAchievementBoxUiState

class ReviewCredibilityAchievementBoxWidget @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseReviewCustomView<WidgetReviewCredibilityAchievementBoxBinding>(
    context, attributeSet, defStyleAttr
) {

    companion object {
        private const val FIRST_ACHIEVEMENT_INDEX = 0
        private const val SECOND_ACHIEVEMENT_INDEX = 1
        private const val THIRD_ACHIEVEMENT_INDEX = 2
        private const val MAX_ACHIEVEMENT_COUNT = 3
    }

    override val binding: WidgetReviewCredibilityAchievementBoxBinding =
        WidgetReviewCredibilityAchievementBoxBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    private val partialWidgetAchievement1 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityAchievement(binding.widgetReviewCredibilityAchievement1)
    }
    private val partialWidgetAchievementLoading1 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityAchievementLoading(binding.widgetReviewCredibilityAchievement1Loading)
    }
    private val partialWidgetAchievement2 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityAchievement(binding.widgetReviewCredibilityAchievement2)
    }
    private val partialWidgetAchievementLoading2 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityAchievementLoading(binding.widgetReviewCredibilityAchievement2Loading)
    }
    private val partialWidgetAchievement3 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityAchievement(binding.widgetReviewCredibilityAchievement3)
    }
    private val partialWidgetAchievementLoading3 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityAchievementLoading(binding.widgetReviewCredibilityAchievement3Loading)
    }

    private var listener: Listener? = null

    private fun hideWidget() {
        animateHide {
            listener?.onAchievementBoxTransitionEnd()
        }
    }

    private fun showLoading() {
        binding.tvReviewCredibilityAchievementTitle.gone()
        binding.tvReviewCredibilityAchievementLabel.gone()
        binding.btnReviewCredibilityAchievementSeeMore.invisible()
        partialWidgetAchievement1.hide()
        partialWidgetAchievement2.hide()
        partialWidgetAchievement3.hide()
        binding.loaderReviewCredibilityAchievementTitle.show()
        binding.loaderReviewCredibilityAchievementLabel1.show()
        binding.loaderReviewCredibilityAchievementLabel2.show()
        binding.loaderReviewCredibilityAchievementSeeMore.show()
        partialWidgetAchievementLoading1.show()
        partialWidgetAchievementLoading2.show()
        partialWidgetAchievementLoading2.show()
        updateAchievementItemsConstraint(
            achievementItemCount = MAX_ACHIEVEMENT_COUNT, showCTA = true, isLoading = true
        )
        animateShow {
            listener?.onAchievementBoxTransitionEnd()
        }
    }

    private fun showData(data: ReviewCredibilityAchievementBoxUiModel) {
        runTransitions(createShowDataTransition())
        showUIData(data)
        updateAchievementItemsConstraint(
            achievementItemCount = data.achievements.size,
            showCTA = data.cta.text.isNotBlank() && data.cta.appLink.isNotBlank(),
            isLoading = false
        )
        animateShow {
            listener?.onAchievementBoxTransitionEnd()
        }
    }

    private fun showUIData(data: ReviewCredibilityAchievementBoxUiModel) {
        val showCTA = data.cta.text.isNotBlank() && data.cta.appLink.isNotBlank()
        binding.loaderReviewCredibilityAchievementTitle.gone()
        binding.loaderReviewCredibilityAchievementLabel1.gone()
        binding.loaderReviewCredibilityAchievementLabel2.gone()
        binding.loaderReviewCredibilityAchievementSeeMore.invisible()
        partialWidgetAchievementLoading1.hide()
        partialWidgetAchievementLoading2.hide()
        partialWidgetAchievementLoading3.hide()
        binding.tvReviewCredibilityAchievementTitle.show()
        binding.tvReviewCredibilityAchievementLabel.show()
        partialWidgetAchievement1.showData(data.achievements.getOrNull(FIRST_ACHIEVEMENT_INDEX))
        partialWidgetAchievement2.showData(data.achievements.getOrNull(SECOND_ACHIEVEMENT_INDEX))
        partialWidgetAchievement3.showData(data.achievements.getOrNull(THIRD_ACHIEVEMENT_INDEX))
        if (showCTA) {
            binding.btnReviewCredibilityAchievementSeeMore.show()
        } else {
            binding.btnReviewCredibilityAchievementSeeMore.invisible()
        }
        binding.tvReviewCredibilityAchievementTitle.text = data.title
        binding.tvReviewCredibilityAchievementLabel.text = data.label
        binding.btnReviewCredibilityAchievementSeeMore.text = data.cta.text
        binding.btnReviewCredibilityAchievementSeeMore.setOnClickListener {
            RouteManager.route(context, data.cta.appLink)
        }
    }

    private fun updateAchievementItemsConstraint(
        achievementItemCount: Int, showCTA: Boolean, isLoading: Boolean
    ) {
        val constraintSet = ConstraintSet()
        val reviewCredibilityAchievementTopView = if (isLoading) {
            binding.loaderReviewCredibilityAchievementLabel2.id
        } else {
            binding.tvReviewCredibilityAchievementLabel.id
        }
        val reviewCredibilitySeeMoreAchievementTopView = if (isLoading) {
            binding.widgetReviewCredibilityAchievement1Loading.root.id
        } else {
            if (showCTA) {
                binding.widgetReviewCredibilityAchievement1.root.id
            } else {
                binding.layoutReviewCredibilityAchievementBoxContent.id
            }
        }
        constraintSet.clone(binding.layoutReviewCredibilityAchievementBoxContent)
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement1.root.id,
            ConstraintSet.START,
            binding.layoutReviewCredibilityAchievementBoxContent.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement1Loading.root.id,
            ConstraintSet.START,
            binding.layoutReviewCredibilityAchievementBoxContent.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement1.root.id,
            ConstraintSet.TOP,
            reviewCredibilityAchievementTopView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement1Loading.root.id,
            ConstraintSet.TOP,
            reviewCredibilityAchievementTopView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement2.root.id,
            ConstraintSet.TOP,
            reviewCredibilityAchievementTopView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement2Loading.root.id,
            ConstraintSet.TOP,
            reviewCredibilityAchievementTopView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement3.root.id,
            ConstraintSet.TOP,
            reviewCredibilityAchievementTopView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement3Loading.root.id,
            ConstraintSet.TOP,
            reviewCredibilityAchievementTopView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.btnReviewCredibilityAchievementSeeMore.id,
            ConstraintSet.TOP,
            reviewCredibilitySeeMoreAchievementTopView,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.loaderReviewCredibilityAchievementSeeMore.id,
            ConstraintSet.TOP,
            reviewCredibilitySeeMoreAchievementTopView,
            ConstraintSet.BOTTOM
        )
        when (achievementItemCount) {
            1 -> {
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement1.root.id,
                    ConstraintSet.END,
                    binding.layoutReviewCredibilityAchievementBoxContent.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement1Loading.root.id,
                    ConstraintSet.END,
                    binding.layoutReviewCredibilityAchievementBoxContent.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement1.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement1Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.END,
                    binding.widgetReviewCredibilityAchievement3.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.END,
                    binding.widgetReviewCredibilityAchievement3Loading.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3Loading.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.clear(
                    binding.widgetReviewCredibilityAchievement3.root.id, ConstraintSet.END
                )
                constraintSet.clear(
                    binding.widgetReviewCredibilityAchievement3Loading.root.id, ConstraintSet.END
                )
            }
            2 -> {
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement1.root.id,
                    ConstraintSet.END,
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement1Loading.root.id,
                    ConstraintSet.END,
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement1.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement1Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.END,
                    binding.layoutReviewCredibilityAchievementBoxContent.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.END,
                    binding.layoutReviewCredibilityAchievementBoxContent.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3Loading.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.clear(
                    binding.widgetReviewCredibilityAchievement3.root.id, ConstraintSet.END
                )
                constraintSet.clear(
                    binding.widgetReviewCredibilityAchievement3Loading.root.id, ConstraintSet.END
                )
            }
            else -> {
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement1.root.id,
                    ConstraintSet.END,
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement1Loading.root.id,
                    ConstraintSet.END,
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement1.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement1Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.END,
                    binding.widgetReviewCredibilityAchievement3.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.END,
                    binding.widgetReviewCredibilityAchievement3Loading.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3Loading.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement2Loading.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3.root.id,
                    ConstraintSet.END,
                    binding.layoutReviewCredibilityAchievementBoxContent.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3Loading.root.id,
                    ConstraintSet.END,
                    binding.layoutReviewCredibilityAchievementBoxContent.id,
                    ConstraintSet.END
                )
            }
        }
        constraintSet.applyTo(binding.layoutReviewCredibilityAchievementBoxContent)
    }

    private fun createShowDataTransition(): Transition {
        return TransitionSet().addTransition(ChangeBounds()).addTransition(Fade()).setInterpolator(
                PathInterpolatorCompat.create(
                    CUBIC_BEZIER_X1, CUBIC_BEZIER_Y1, CUBIC_BEZIER_X2, CUBIC_BEZIER_Y2
                )
            ).setDuration(ANIMATION_DURATION)
    }

    private fun runTransitions(transition: Transition) {
        TransitionManager.beginDelayedTransition(binding.root, transition)
    }

    fun updateUiState(uiState: ReviewCredibilityAchievementBoxUiState) {
        when (uiState) {
            is ReviewCredibilityAchievementBoxUiState.Hidden -> hideWidget()
            is ReviewCredibilityAchievementBoxUiState.Loading -> showLoading()
            is ReviewCredibilityAchievementBoxUiState.Showed -> showData(uiState.data)
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    interface Listener {
        fun onAchievementBoxTransitionEnd()
    }
}
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
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetReviewCredibilityAchievementBoxBinding
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityAchievementBoxUiModel
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityAchievementBoxUiState
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ReviewCredibilityAchievementBoxWidget @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseReviewCustomView<WidgetReviewCredibilityAchievementBoxBinding>(
    context, attributeSet, defStyleAttr
) {

    companion object {
        private const val FIRST_ACHIEVEMENT_INDEX = 0
        private const val SECOND_ACHIEVEMENT_INDEX = 1
        private const val THIRD_ACHIEVEMENT_INDEX = 2
    }

    override val binding: WidgetReviewCredibilityAchievementBoxBinding =
        WidgetReviewCredibilityAchievementBoxBinding.inflate(
            LayoutInflater.from(context), this, true
        ).apply {
            setupView()
        }

    private val partialWidgetAchievement1 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityAchievement(binding.widgetReviewCredibilityAchievement1, listener)
    }
    private val partialWidgetAchievement2 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityAchievement(binding.widgetReviewCredibilityAchievement2, listener)
    }
    private val partialWidgetAchievement3 by lazy(LazyThreadSafetyMode.NONE) {
        PartialReviewCredibilityAchievement(binding.widgetReviewCredibilityAchievement3, listener)
    }

    private var listener: Listener? = null

    private fun WidgetReviewCredibilityAchievementBoxBinding.setupView() {
        layoutReviewCredibilityAchievementBoxContent.setBackgroundResource(R.drawable.bg_review_credibility_statistics_box)
    }

    private fun hideWidget() {
        animateHide(onAnimationEnd = {
            listener?.onAchievementBoxTransitionEnd()
        })
    }

    private fun showData(data: ReviewCredibilityAchievementBoxUiModel) {
        runTransitions(createShowDataTransition())
        showUIData(data)
        updateAchievementItemsConstraint(
            achievementItemCount = data.achievements.size,
            showCTA = data.cta.text.isNotBlank() && data.cta.appLink.isNotBlank()
        )
        animateShow(onAnimationEnd = {
            listener?.onAchievementBoxTransitionEnd()
        })
    }

    private fun showUIData(data: ReviewCredibilityAchievementBoxUiModel) {
        with(binding) {
            setupTitle(data.title)
            setupLabel(data.label)
            setupCta(data.cta)
        }
        partialWidgetAchievement1.showData(data.achievements.getOrNull(FIRST_ACHIEVEMENT_INDEX))
        partialWidgetAchievement2.showData(data.achievements.getOrNull(SECOND_ACHIEVEMENT_INDEX))
        partialWidgetAchievement3.showData(data.achievements.getOrNull(THIRD_ACHIEVEMENT_INDEX))
        listener?.onImpressAchievementStickers(data.achievements)
    }

    private fun updateAchievementItemsConstraint(achievementItemCount: Int, showCTA: Boolean) {
        val constraintSet = ConstraintSet()
        val reviewCredibilitySeeMoreAchievementTopView = if (showCTA) {
            binding.widgetReviewCredibilityAchievement1.root.id
        } else {
            binding.layoutReviewCredibilityAchievementBoxContent.id
        }
        constraintSet.clone(binding.layoutReviewCredibilityAchievementBoxContent)
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement1.root.id,
            ConstraintSet.START,
            binding.layoutReviewCredibilityAchievementBoxContent.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement1.root.id,
            ConstraintSet.TOP,
            binding.tvReviewCredibilityAchievementLabel.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement2.root.id,
            ConstraintSet.TOP,
            binding.tvReviewCredibilityAchievementLabel.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.widgetReviewCredibilityAchievement3.root.id,
            ConstraintSet.TOP,
            binding.tvReviewCredibilityAchievementLabel.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.tvReviewCredibilityAchievementSeeMore.id,
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
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement1.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.END,
                    binding.widgetReviewCredibilityAchievement3.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.END
                )
                constraintSet.clear(
                    binding.widgetReviewCredibilityAchievement3.root.id, ConstraintSet.END
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
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement1.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2.root.id,
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
                constraintSet.clear(
                    binding.widgetReviewCredibilityAchievement3.root.id, ConstraintSet.END
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
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement1.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.END,
                    binding.widgetReviewCredibilityAchievement3.root.id,
                    ConstraintSet.START
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3.root.id,
                    ConstraintSet.START,
                    binding.widgetReviewCredibilityAchievement2.root.id,
                    ConstraintSet.END
                )
                constraintSet.connect(
                    binding.widgetReviewCredibilityAchievement3.root.id,
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

    private fun WidgetReviewCredibilityAchievementBoxBinding.setupTitle(title: String) {
        tvReviewCredibilityAchievementTitle.show()
        tvReviewCredibilityAchievementTitle.text = HtmlLinkHelper(
            root.context, title
        ).spannedString ?: ""
    }

    private fun WidgetReviewCredibilityAchievementBoxBinding.setupLabel(label: String) {
        tvReviewCredibilityAchievementLabel.show()
        tvReviewCredibilityAchievementLabel.text = HtmlLinkHelper(
            root.context, label
        ).spannedString ?: ""
    }

    private fun WidgetReviewCredibilityAchievementBoxBinding.setupCta(
        cta: ReviewCredibilityAchievementBoxUiModel.Button
    ) {
        val showCTA = cta.text.isNotBlank() && cta.appLink.isNotBlank()
        val text = HtmlLinkHelper(root.context, cta.text).spannedString ?: ""
        if (showCTA) {
            tvReviewCredibilityAchievementSeeMore.show()
        } else {
            tvReviewCredibilityAchievementSeeMore.invisible()
        }
        tvReviewCredibilityAchievementSeeMore.text = text
        tvReviewCredibilityAchievementSeeMore.setOnClickListener {
            listener?.onClickSeeMoreAchievement(cta.appLink, text.toString())
        }
    }

    fun updateUiState(uiState: ReviewCredibilityAchievementBoxUiState) {
        when (uiState) {
            is ReviewCredibilityAchievementBoxUiState.Hidden -> hideWidget()
            is ReviewCredibilityAchievementBoxUiState.Showed -> showData(uiState.data)
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
        partialWidgetAchievement1.setListener(newListener)
        partialWidgetAchievement2.setListener(newListener)
        partialWidgetAchievement3.setListener(newListener)
    }

    interface Listener {
        fun onAchievementBoxTransitionEnd()
        fun onClickAchievementSticker(appLink: String, name: String)
        fun onClickSeeMoreAchievement(appLink: String, buttonText: String)
        fun onImpressAchievementStickers(achievements: List<ReviewCredibilityAchievementBoxUiModel.ReviewCredibilityAchievementUiModel>)
    }
}

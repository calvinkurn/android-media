package com.tokopedia.review.feature.credibility.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.databinding.WidgetReviewCredibilityAchievementBoxBinding
import com.tokopedia.review.feature.credibility.data.Achievement
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityLabel
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewCredibilityAchievementBoxWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attributeSet, defStyleAttr) {

    private val binding: WidgetReviewCredibilityAchievementBoxBinding =
        WidgetReviewCredibilityAchievementBoxBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    private fun setupTitle(name: String) {
        binding.tvCredibilityAchievementTitle.text = name
    }

    private fun setupLabel(subLabel: String) {
        binding.tvCredibilityAchievementLabel.text = subLabel
    }

    private fun setupFirstAchievement(achievement: Achievement?) {
        binding.widgetReviewCredibilityAchievementOne.setAchievement(achievement)
    }

    private fun setupSecondAchievement(achievement: Achievement?) {
        binding.widgetReviewCredibilityAchievementTwo.setAchievement(achievement)
    }

    private fun setupThirdAchievement(achievement: Achievement?) {
        binding.widgetReviewCredibilityAchievementThree.setAchievement(achievement)
    }

    private fun setupCta(totalAchievementFmt: String?, achievementListLink: String?) {
        binding.btnReviewCredibilityAchievementSeeMore.apply {
            text = totalAchievementFmt
            setOnClickListener { RouteManager.route(context, achievementListLink) }
        }
    }

    private fun setupVisibility(label: ReviewerCredibilityLabel) {
        showWithCondition(!label.achievements.isNullOrEmpty())
    }

    fun setAchievements(label: ReviewerCredibilityLabel) {
        setupTitle(label.name)
        setupLabel(label.subLabel)
        setupFirstAchievement(label.achievements?.getOrNull(Int.ZERO))
        setupSecondAchievement(label.achievements?.getOrNull(1))
        setupThirdAchievement(label.achievements?.getOrNull(2))
        setupCta(label.totalAchievementFmt, label.achievementListLink)
        setupVisibility(label)
    }
}

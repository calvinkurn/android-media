package com.tokopedia.review.feature.credibility.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.databinding.WidgetReviewCredibilityAchievementBinding
import com.tokopedia.review.feature.credibility.data.Achievement
import com.tokopedia.reviewcommon.extension.toColorInt
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewCredibilityAchievementWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attributeSet, defStyleAttr) {
    private val binding: WidgetReviewCredibilityAchievementBinding =
        WidgetReviewCredibilityAchievementBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    private fun setupAvatar(image: String?) {
        binding.ivReviewCredibilityAchievementAvatar.urlSrc = image.orEmpty()
    }

    private fun setupName(name: String?) {
        binding.tvReviewCredibilityAchievementName.text = name
    }

    private fun setupCounter(total: String?) {
        binding.tvReviewCredibilityAchievementCounter.text = total
    }

    private fun setupColor(color: String?) {
        binding.containerReviewCredibilityAchievementDetail.setBackgroundColor(
            color.toColorInt(
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
        )
    }

    private fun setupVisibility(achievement: Achievement?) {
        val hasAvatar = !achievement?.image.isNullOrBlank()
        val hasName = !achievement?.name.isNullOrBlank()
        val hasCounter = !achievement?.total.isNullOrBlank()
        val hasColor = !achievement?.color.isNullOrBlank()
        showWithCondition(hasAvatar && hasName && hasCounter && hasColor)
    }

    fun setAchievement(achievement: Achievement?) {
        setupAvatar(achievement?.image)
        setupName(achievement?.name)
        setupCounter(achievement?.total)
        setupColor(achievement?.color)
        setupVisibility(achievement)
    }
}
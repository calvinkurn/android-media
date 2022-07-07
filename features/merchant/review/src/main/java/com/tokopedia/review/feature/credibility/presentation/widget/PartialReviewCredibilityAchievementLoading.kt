package com.tokopedia.review.feature.credibility.presentation.widget

import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.review.databinding.PartialReviewCredibilityAchievementLoadingBinding

class PartialReviewCredibilityAchievementLoading(
    private val binding: PartialReviewCredibilityAchievementLoadingBinding
) {
    fun hide() {
        binding.root.invisible()
    }

    fun show() {
        binding.root.visible()
    }
}
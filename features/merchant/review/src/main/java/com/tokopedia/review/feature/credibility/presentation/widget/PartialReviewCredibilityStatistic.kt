package com.tokopedia.review.feature.credibility.presentation.widget

import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.review.databinding.PartialReviewCredibilityStatisticBinding
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityStatisticBoxUiModel

class PartialReviewCredibilityStatistic(
    private val binding: PartialReviewCredibilityStatisticBinding
) {
    fun hide() {
        binding.root.invisible()
    }

    fun showData(data: ReviewCredibilityStatisticBoxUiModel.ReviewCredibilityStatisticUiModel?) {
        if (data == null) {
            hide()
        } else {
            binding.reviewCredibilityStatisticIcon.urlSrc = data.icon
            binding.reviewCredibilityStatisticTitle.text = data.title
            binding.reviewCredibilityStatisticCount.text = data.count
            binding.root.visible()
        }
    }
}
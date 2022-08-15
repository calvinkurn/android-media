package com.tokopedia.review.feature.credibility.presentation.widget

import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.review.databinding.PartialReviewCredibilityStatisticLoadingBinding

class PartialReviewCredibilityStatisticLoading(
    private val binding: PartialReviewCredibilityStatisticLoadingBinding
) {
    fun hide() {
        binding.root.invisible()
    }

    fun show() {
        binding.root.visible()
    }
}
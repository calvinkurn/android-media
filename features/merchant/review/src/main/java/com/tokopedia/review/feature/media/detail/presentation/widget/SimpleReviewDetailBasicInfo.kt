package com.tokopedia.review.feature.media.detail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.review.databinding.PartialWidgetReviewDetailBasicInfoBinding
import com.tokopedia.review.feature.media.detail.presentation.uimodel.ReviewDetailBasicInfoUiModel

class SimpleReviewDetailBasicInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ReviewDetailBasicInfo(context, attrs, defStyleAttr) {
    override fun setData(data: ReviewDetailBasicInfoUiModel, source: Source) {
        super.setData(data, source)
        binding.layoutReviewDetailBasicInfo.overlayCredibilityClickArea.gone()
    }

    override fun PartialWidgetReviewDetailBasicInfoBinding.setupReviewerProfilePicture(
        profilePicture: String
    ) {
        ivReviewDetailReviewerProfilePicture.gone()
    }

    override fun PartialWidgetReviewDetailBasicInfoBinding.setupReviewerName(
        reviewerName: String,
        source: Source
    ) {
        tvReviewDetailReviewerName.gone()
    }

    override fun PartialWidgetReviewDetailBasicInfoBinding.setupReviewerStatsSummary(
        reviewerStatsSummary: String,
        source: Source
    ) {
        tvReviewDetailReviewerStatsSummary.gone()
    }
}
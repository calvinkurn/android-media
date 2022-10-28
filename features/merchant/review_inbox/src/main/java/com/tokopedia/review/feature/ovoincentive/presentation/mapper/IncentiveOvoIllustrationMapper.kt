package com.tokopedia.review.feature.ovoincentive.presentation.mapper

import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.presentation.model.IncentiveOvoIllustrationUiModel

class IncentiveOvoIllustrationMapper {
    fun mapResponseToUiModel(response: ProductRevIncentiveOvoDomain): List<IncentiveOvoIllustrationUiModel> {
        return response.productrevIncentiveOvo?.illustrations?.mapNotNull {
            if (!it.imageUrl.isNullOrBlank() && !it.text.isNullOrBlank()) {
                IncentiveOvoIllustrationUiModel(it.imageUrl, it.text)
            } else null
        }.orEmpty()
    }
}
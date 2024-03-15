package com.tokopedia.thankyou_native.domain.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

data class TopAdsUIModel (
        val topAdsImageUiModel: TopAdsImageUiModel,
        val impressHolder : ImpressHolder = ImpressHolder()
)

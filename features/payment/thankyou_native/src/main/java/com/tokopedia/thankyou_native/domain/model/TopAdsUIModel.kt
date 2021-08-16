package com.tokopedia.thankyou_native.domain.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class TopAdsUIModel (
    val topAdsImageViewModel: TopAdsImageViewModel,
    val impressHolder : ImpressHolder = ImpressHolder()
)
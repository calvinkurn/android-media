package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tdnbanner

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class TdnBannerUiModel(
    val id: String = String.EMPTY,
    val tdnBanners: List<List<TopAdsImageViewModel>> = emptyList()
)

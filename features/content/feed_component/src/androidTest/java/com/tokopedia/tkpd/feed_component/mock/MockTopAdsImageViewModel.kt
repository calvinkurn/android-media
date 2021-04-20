package com.tokopedia.tkpd.feed_component.mock

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

object MockTopAdsImageViewModel {
    fun get() = TopAdsImageViewModel(
            bannerId = "1"
    )
}
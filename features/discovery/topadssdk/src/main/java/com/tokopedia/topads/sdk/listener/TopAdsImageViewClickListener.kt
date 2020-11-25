package com.tokopedia.topads.sdk.listener

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

interface TopAdsImageViewClickListener {
    fun onTopAdsImageViewClicked(topAdsModel: TopAdsImageViewModel)
}

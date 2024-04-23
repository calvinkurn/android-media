package com.tokopedia.topads.sdk.v2.tdnbanner.listener

import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

interface TdnBannerResponseListener {

    fun onTdnBannerResponse(categoriesList: MutableList<List<TopAdsImageUiModel>>)
    fun onError(t: Throwable)

}

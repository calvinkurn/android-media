package com.tokopedia.topads.sdk.old.listener

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

interface TdnBannerResponseListener {

    fun onTdnBannerResponse(categoriesList: MutableList<List<TopAdsImageViewModel>>)
    fun onError(t: Throwable)

}

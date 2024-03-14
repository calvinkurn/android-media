package com.tokopedia.topads.sdk.old.listener

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

interface TdnVerticalBannerResponseListener {

    fun onTdnVerticalBannerResponse(data: ArrayList<TopAdsImageViewModel>)

    fun onError(t: Throwable)

}

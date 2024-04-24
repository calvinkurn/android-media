package com.tokopedia.topads.sdk.v2.tdnvertical.listener

import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

interface TdnVerticalBannerResponseListener {

    fun onTdnVerticalBannerResponse(data: ArrayList<TopAdsImageUiModel>)

    fun onError(t: Throwable)

}

package com.tokopedia.topads.sdk.listener

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

interface TopAdsImageVieWApiResponseListener {

    fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>)

    fun onError(t:Throwable)

}
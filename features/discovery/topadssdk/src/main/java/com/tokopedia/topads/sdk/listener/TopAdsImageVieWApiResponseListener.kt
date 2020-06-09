package com.tokopedia.topads.sdk.listener

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewResponse

interface TopAdsImageVieWApiResponseListener {

    fun onImageViewResponse(imageList: List<TopAdsImageViewResponse.Data.Banner.Image?>?, pageToken :String)

    fun onError(t:Throwable)

}
package com.tokopedia.topads.sdk.old.listener

import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

interface TopAdsImageVieWApiResponseListener {

    fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageUiModel>)

    fun onError(t:Throwable)

}

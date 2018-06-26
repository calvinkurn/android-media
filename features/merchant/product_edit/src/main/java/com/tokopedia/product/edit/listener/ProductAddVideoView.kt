package com.tokopedia.product.edit.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.edit.model.VideoRecommendationData

interface ProductAddVideoView : CustomerView {

    val contextView : Context

    fun onSuccessGetVideoRecommendation(videoRecommendationDataList: List<VideoRecommendationData>)

    fun onErrorGetVideoRecommendation(e: Throwable)
}
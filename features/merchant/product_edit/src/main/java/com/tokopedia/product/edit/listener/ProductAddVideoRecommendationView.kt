package com.tokopedia.product.edit.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.edit.model.VideoRecommendationData

interface ProductAddVideoRecommendationView : CustomerView {

    val contextView : Context

    fun onSuccessGetVideoRecommendation(videoRecommendationDataList: List<VideoRecommendationData>)

    fun onErrorGetVideoRecommendation(e: Throwable)
}
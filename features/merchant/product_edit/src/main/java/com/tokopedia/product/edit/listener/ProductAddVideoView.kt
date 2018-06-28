package com.tokopedia.product.edit.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.edit.model.videorecommendation.VideoRecommendationData
import com.tokopedia.product.edit.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.viewmodel.ProductAddVideoBaseViewModel
import java.util.ArrayList

interface ProductAddVideoView : CustomerView {

    val contextView : Context

    fun onSuccessGetYoutubeDataVideoRecommendationFeatured(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>)

    fun onSuccessGetYoutubeDataVideoChoosen(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>)

    fun onErrorGetVideoData(e: Throwable)

    fun renderListData(productAddVideoBaseViewModelList : List<ProductAddVideoBaseViewModel>)
}
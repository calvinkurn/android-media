package com.tokopedia.product.manage.item.video.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import java.util.ArrayList

interface ProductAddVideoView : CustomerView {

    val contextView : Context

    fun addVideoIDfromURL(videoID: String)

    fun onSuccessGetYoutubeDataVideoRecommendation(youtubeVideoModelArrayList: ArrayList<YoutubeVideoDetailModel>)

    fun onSuccessGetYoutubeDataVideoChosen(youtubeVideoModelArrayList: ArrayList<YoutubeVideoDetailModel>)

    fun onSuccessGetYoutubeDataVideoUrl(youtubeVideoModel: YoutubeVideoDetailModel)

    fun onErrorGetVideoData(e: Throwable)

    fun onEmptyGetVideoRecommendation()

    fun showSnackbarGreen(message: String)

    fun showSnackbarRed(message: String)
}
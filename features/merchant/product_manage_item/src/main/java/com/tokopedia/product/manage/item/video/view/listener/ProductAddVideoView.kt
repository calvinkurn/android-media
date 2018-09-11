package com.tokopedia.product.manage.item.video.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.manage.item.video.domain.model.youtube.YoutubeVideoModel
import java.util.ArrayList

interface ProductAddVideoView : CustomerView {

    val contextView : Context

    fun addVideoIDfromURL(videoID: String)

    fun onSuccessGetYoutubeDataVideoRecommendation(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>)

    fun onSuccessGetYoutubeDataVideoChosen(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>)

    fun onSuccessGetYoutubeDataVideoUrl(youtubeVideoModel: YoutubeVideoModel)

    fun onErrorGetVideoData(e: Throwable)

    fun onEmptyGetVideoRecommendation()

    fun showSnackbarGreen(message: String)

    fun showSnackbarRed(message: String)
}
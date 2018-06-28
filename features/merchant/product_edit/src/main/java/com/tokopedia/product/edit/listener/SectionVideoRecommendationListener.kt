package com.tokopedia.product.edit.listener

import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel


interface SectionVideoRecommendationListener {

    fun onShowMoreClicked()

    fun onVideoRecommendationFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel)

    fun setGetVideoRecommendationListener(listener: GetVideoRecommendationListener)

    val getVideoIDs: ArrayList<String>
}
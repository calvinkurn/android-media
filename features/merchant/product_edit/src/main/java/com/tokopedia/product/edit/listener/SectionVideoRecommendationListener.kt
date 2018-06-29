package com.tokopedia.product.edit.listener

import com.tokopedia.product.edit.fragment.ProductAddVideoFragment
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel

interface SectionVideoRecommendationListener {

    fun onShowMoreClicked()

    fun onVideoRecommendationFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel)

    fun setGetVideoRecommendationListener(listener: ProductAddVideoFragment.GetVideoRecommendationListener)

    fun setVideoChoosenDeletedListener(listener: ProductAddVideoFragment.VideoChoosenDeletedListener)

    val getVideoIDs: ArrayList<String>
}
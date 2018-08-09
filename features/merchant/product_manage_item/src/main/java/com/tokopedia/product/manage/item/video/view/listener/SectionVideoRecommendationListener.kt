package com.tokopedia.product.manage.item.video.view.listener

import com.tokopedia.product.manage.item.video.view.fragment.ProductAddVideoFragment
import com.tokopedia.product.manage.item.video.view.model.VideoRecommendationViewModel

interface SectionVideoRecommendationListener {

    fun onShowMoreClicked()

    fun onVideoRecommendationFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel)

    fun onVideoRecommendationPlusClicked(videoRecommendationViewModel : VideoRecommendationViewModel)

    fun setProductAddVideoFragmentListener(listener: ProductAddVideoFragment.Listener)

}
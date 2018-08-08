package com.tokopedia.product.manage.item.view.listener

import com.tokopedia.product.manage.item.view.fragment.ProductAddVideoFragment
import com.tokopedia.product.manage.item.view.viewmodel.VideoRecommendationViewModel

interface SectionVideoRecommendationListener {

    fun onShowMoreClicked()

    fun onVideoRecommendationFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel)

    fun onVideoRecommendationPlusClicked(videoRecommendationViewModel : VideoRecommendationViewModel)

    fun setProductAddVideoFragmentListener(listener: ProductAddVideoFragment.Listener)

}
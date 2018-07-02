package com.tokopedia.product.edit.view.listener

import com.tokopedia.product.edit.view.fragment.ProductAddVideoFragment
import com.tokopedia.product.edit.view.viewmodel.VideoRecommendationViewModel

interface SectionVideoRecommendationListener {

    fun onShowMoreClicked()

    fun onVideoRecommendationFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel)

    fun setProductAddVideoFragmentListener(listener: ProductAddVideoFragment.Listener)

}
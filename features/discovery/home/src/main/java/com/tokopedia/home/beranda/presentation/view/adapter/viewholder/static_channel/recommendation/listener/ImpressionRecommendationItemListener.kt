package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.listener

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel

// temporary to avoid performance issue
// this listener will be removed once the global component 100% rollout
interface ImpressionRecommendationItemListener {
    fun onProductCardImpressed(model: HomeRecommendationItemDataModel, position: Int)
}

package com.tokopedia.recommendation_widget_common.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by Lukas on 2019-07-22
 */

interface TrackingListener {
    fun onProductClick(item: RecommendationItem, layoutType: String? = null, vararg position: Int)
    fun onProductImpression(item: RecommendationItem)
    fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: ((Boolean, Throwable?) -> Unit))
}
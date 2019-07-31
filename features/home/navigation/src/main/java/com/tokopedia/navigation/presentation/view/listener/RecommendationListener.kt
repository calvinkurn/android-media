package com.tokopedia.navigation.presentation.view.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by Lukas on 2019-07-31
 *
 * An Interface for handling Recommendation Widget Listener
 */
interface RecommendationListener {
    fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: ((Boolean, Throwable?) -> Unit))
}
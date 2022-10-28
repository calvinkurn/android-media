package com.tokopedia.recommendation_widget_common.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener

/**
 * Created by Lukas on 2019-08-01
 */
interface RecommendationListener {
    fun onProductClick(item: RecommendationItem, layoutType: String? = null, vararg position: Int)
    fun onProductImpression(item: RecommendationItem)
    fun onWishlistV2Click(item: RecommendationItem, isAddWishlist: Boolean)
    fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {

    }
}
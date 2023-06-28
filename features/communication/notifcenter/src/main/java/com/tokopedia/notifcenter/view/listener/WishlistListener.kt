package com.tokopedia.notifcenter.view.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener

interface WishlistListener {
    fun addWishlistV2(model: RecommendationItem, listener: WishlistV2ActionListener)
    fun removeWishlistV2(model: RecommendationItem, listener: WishlistV2ActionListener)
}

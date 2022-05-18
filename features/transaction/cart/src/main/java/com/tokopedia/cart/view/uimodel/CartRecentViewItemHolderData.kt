package com.tokopedia.cart.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

data class CartRecentViewItemHolderData(
        var id: String = "",
        var name: String = "",
        var price: String = "",
        var imageUrl: String = "",
        var isWishlist: Boolean = false,
        var rating: Int = 0,
        var reviewCount: Int = 0,
        var badgesUrl: List<String?> = listOf(),
        var shopLocation: String = "",
        var shopId: String = "",
        var shopName: String = "",
        var shopType: String = "",
        var minOrder: Int = 1,
        var slashedPrice: String = "",
        var discountPercentage: String = "",
        var isTopAds: Boolean = false,
        var isFreeOngkirActive: Boolean = false,
        var freeOngkirImageUrl: String = "",
        var clickUrl: String = "",
        var trackerImageUrl: String = "",
        var labelGroupList: List<RecommendationLabel> = listOf() ) : ImpressHolder(){}
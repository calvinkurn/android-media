package com.tokopedia.recommendation_widget_common.presentation.model

import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.kotlin.model.ImpressHolder

data class RecommendationItem(val productId: Int = 0,
                              val name: String = "",
                              val categoryBreadcrumbs: String = "",
                              val url: String = "",
                              val appUrl: String = "",
                              val clickUrl: String = "",
                              val wishlistUrl: String = "",
                              val trackerImageUrl: String = "",
                              val imageUrl: String = "",
                              val price: String = "",
                              val priceInt: Int = 0,
                              val departmentId: Int = 0,
                              val rating: Int = 0,
                              val countReview: Int = 0,
                              val stock: Int = 0,
                              val recommendationType: String = "",
                              val isTopAds: Boolean = false,
                              var isWishlist: Boolean = false,
                              val slashedPrice: String = "",
                              val slashedPriceInt: Int = 0,
                              val discountPercentageInt: Int = 0,
                              val discountPercentage: String = "",
                              val position: Int = 0,
                              val shopId: Int = 0,
                              val shopType: String = "",
                              val shopName: String = "",
                              var cartId: Int = 0,
                              val quantity: Int = 0,
                              val header: String = "",
                              val pageName: String = "",
                              val minOrder: Int = 0,
                              val location: String = "",
                              val badgesUrl: List<String?> = listOf(),
                              val type: String = "",
                              val isFreeOngkirActive: Boolean = false,
                              val freeOngkirImageUrl: String = "",
                              val labelPromo: RecommendationLabel = RecommendationLabel(),
                              val labelOffers: RecommendationLabel = RecommendationLabel(),
                              val labelCredibility: RecommendationLabel = RecommendationLabel(),
                              val isGold: Boolean = false): ImpressHolder(){

    fun getPriceIntFromString() = CurrencyFormatHelper.convertRupiahToInt(price)
}

data class RecommendationLabel(var title: String = "", val type: String = "")
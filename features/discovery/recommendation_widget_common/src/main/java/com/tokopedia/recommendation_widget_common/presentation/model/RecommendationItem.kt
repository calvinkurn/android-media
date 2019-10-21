package com.tokopedia.recommendation_widget_common.presentation.model

import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.kotlin.model.ImpressHolder

class RecommendationItem(val productId: Int,
                         val name: String,
                         val categoryBreadcrumbs: String,
                         val url: String,
                         val appUrl: String,
                         val clickUrl: String,
                         val wishlistUrl: String,
                         val trackerImageUrl: String,
                         val imageUrl: String,
                         val price: String,
                         val priceInt: Int,
                         val departmentId: Int,
                         val rating: Int,
                         val countReview: Int,
                         val stock: Int,
                         val recommendationType: String,
                         val isTopAds: Boolean,
                         var isWishlist: Boolean,
                         val slashedPrice: String,
                         val slashedPriceInt: Int,
                         val discountPercentageInt: Int,
                         val discountPercentage: String,
                         val position: Int,
                         val shopId: Int,
                         val shopType: String,
                         val shopName: String,
                         var cartId: Int,
                         val quantity: Int,
                         val header: String,
                         val pageName: String,
                         val minOrder: Int,
                         val location: String,
                         val badgesUrl: List<String?>,
                         val type: String,
                         val isFreeOngkirActive: Boolean,
                         val freeOngkirImageUrl: String,
                         val labelPromo: RecommendationLabel,
                         val labelOffers: RecommendationLabel,
                         val labelCredibility: RecommendationLabel): ImpressHolder(){

    fun getPriceIntFromString() = CurrencyFormatHelper.convertRupiahToInt(price)
}

data class RecommendationLabel(var title: String = "", val type: String = "")
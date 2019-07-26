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
                         val isWishlist: Boolean,
                         val slashedPrice: String,
                         val slashedPriceInt: Int,
                         val discountPercentage: Int,
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
                         val type: String): ImpressHolder(){

    fun getPriceIntFromString() = CurrencyFormatHelper.convertRupiahToInt(price)
}
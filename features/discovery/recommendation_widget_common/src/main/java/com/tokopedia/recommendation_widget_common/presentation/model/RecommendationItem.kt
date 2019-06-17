package com.tokopedia.recommendation_widget_common.presentation.model

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
                         val slashedPrice: String,
                         val slashedPriceInt: Int,
                         val discountPercentage: Int) : ImpressHolder() {
}
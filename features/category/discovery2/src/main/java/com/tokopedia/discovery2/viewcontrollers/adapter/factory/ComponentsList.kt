package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import com.tokopedia.discovery2.R


enum class ComponentsList(val id: Int, val componentName: String) {
    Video(R.layout.multi_banner_layout, "video"),
    SingleBanner(R.layout.multi_banner_layout, "banner_image"),
    DoubleBanner(R.layout.multi_banner_layout, "banner_image_double"),
    TripleBanner(R.layout.multi_banner_layout, "banner_image_triple"),
    QuadrupleBanner(R.layout.multi_banner_layout, "banner_image_quadruple"),
    Share(R.layout.multi_banner_layout, "share"),
    BrandRecommendation(R.layout.brand_recommendation_layout, "brand_recommendation"),
    BrandRecommendationItem(R.layout.brand_recommendation_layout_item, "brand_recommendation_item"),
    CarouselBanner(R.layout.carousel_banner_layout, "carousel_banner"),
    CarouselBannerItemView(R.layout.carousel_banner_item_layout, "carousel_banner_item")
}

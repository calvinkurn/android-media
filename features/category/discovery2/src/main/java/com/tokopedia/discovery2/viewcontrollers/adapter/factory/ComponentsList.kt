package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R


enum class ComponentsList(val id: Int, val componentName: String) {
    Default(R.layout.coming_soon_view, "default"),
    SingleBanner(R.layout.multi_banner_layout, "banner_image"),
    DoubleBanner(R.layout.multi_banner_layout, "banner_image_double"),
    TripleBanner(R.layout.multi_banner_layout, "banner_image_triple"),
    QuadrupleBanner(R.layout.multi_banner_layout, "banner_image_quadruple"),
    Share(R.layout.multi_banner_layout, "share"),
    CategoryNavigation(R.layout.discovery_horizontal_recycler_view_with_title, ComponentNames.HorizontalCategoryNavigation.componentName),
    CategoryNavigationIem(R.layout.item_category_navigation, "horizontal_category_navigation_item"),
    BrandRecommendation(R.layout.brand_recommendation_layout, ComponentNames.BrandRecommendation.componentName),
    BrandRecommendationItem(R.layout.brand_recommendation_layout_item, "brand_recommendation_item"),
    CarouselBannerItemView(R.layout.carousel_banner_item_layout, "carousel_banner_item"),
    SliderBanner(R.layout.slider_banner_layout, ComponentNames.SliderBanner.componentName),
    CarouselBanner(R.layout.carousel_banner_layout, ComponentNames.CarouselBanner.componentName),
    BannerTimer(R.layout.banner_timer_layout, "timer_with_banner"),
    Tokopoints(R.layout.tokopoints_layout, "tokopoints"),
    TokopointsItem(R.layout.tokopoints_item_layout, "tokopoints_item"),
    LihatSemua(R.layout.lihat_semua, ComponentNames.LihatSemua.componentName),
    CpmTopAds(R.layout.cpm_topads_layout, "topads"),
    CpmTopAdsItem(R.layout.item_cpm_topads_shop_layout, ComponentNames.CpmTopAdsShopItem.componentName),
    CpmTopAdsProductItem(R.layout.item_cpm_topads_product_layout, ComponentNames.CpmTopAdsProductItem.componentName),
    YouTubeView(R.layout.coming_soon_view, ComponentNames.Video.componentName),
    ChipsFilterView(R.layout.coming_soon_view, ComponentNames.ChipsFilter.componentName),
    HeaderDesktopView(R.layout.coming_soon_view, ComponentNames.HeaderDekstop.componentName),
    ShareEmpty(R.layout.coming_soon_view, ComponentNames.Share.componentName),
    Notifier(R.layout.coming_soon_view, ComponentNames.Notifier.componentName),
    TitleImage(R.layout.coming_soon_view, ComponentNames.TitleImage.componentName),
    TextComponent(R.layout.coming_soon_view, ComponentNames.TextComponent.componentName),
    ClaimCoupon(R.layout.component_claim_coupon_layout, ComponentNames.ClaimCoupon.componentName),
    ClaimCouponItem(R.layout.component_claim_coupon_item, ComponentNames.ClaimCouponItem.componentName),
    ProductCardCarousel(R.layout.coming_soon_view, ComponentNames.ProductCardCarousel.componentName),
    Margin(R.layout.coming_soon_view, ComponentNames.Margin.componentName),
    CustomTopChat(R.layout.coming_soon_view, ComponentNames.CustomTopchat.componentName),
    Tabs(R.layout.coming_soon_view, ComponentNames.Tabs.componentName),
    ProductCardRevamp(R.layout.coming_soon_view, ComponentNames.PrductCardRevamp.componentName),
    BreadCrumbs(R.layout.coming_soon_view, ComponentNames.Breadcrumbs.componentName),
    Spacing(R.layout.spacing_layout, "margin"),

}

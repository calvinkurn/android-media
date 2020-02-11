package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.ComponentNames


enum class ComponentsList(val id: Int, val componentName: String) {
    Default(R.layout.coming_soon_view, "default"),
    SingleBanner(R.layout.multi_banner_layout, "banner_image"),
    DoubleBanner(R.layout.multi_banner_layout, "banner_image_double"),
    TripleBanner(R.layout.multi_banner_layout, "banner_image_triple"),
    QuadrupleBanner(R.layout.multi_banner_layout, "banner_image_quadruple"),
    Share(R.layout.multi_banner_layout, "share"),
    BannerTimer(R.layout.banner_timer_layout, "timer_with_banner"),
    CpmTopAds(R.layout.cpm_topads_layout, "topads"),
    CpmTopAdsItem(R.layout.item_cpm_topads_shop_layout, ComponentNames.CpmTopAdsShopItem.componentName),
    CpmTopAdsProductItem(R.layout.item_cpm_topads_product_layout, ComponentNames.CpmTopAdsProductItem.componentName),
    YouTubeView(R.layout.coming_soon_view, ComponentNames.Video.componentName),
    ChipsFilterView(R.layout.coming_soon_view, ComponentNames.ChipsFilter.componentName),
    HeaderDesktopView(R.layout.coming_soon_view, ComponentNames.HeaderDekstop.componentName),
    ShareEmpty(R.layout.coming_soon_view, ComponentNames.Share.componentName),
    LihatSemuaView(R.layout.coming_soon_view, ComponentNames.LihatSemua.componentName),
    SliderBanner(R.layout.coming_soon_view, ComponentNames.SliderBanner.componentName),
    Notifier(R.layout.coming_soon_view, ComponentNames.Notifier.componentName),
    CarouselBanner(R.layout.coming_soon_view, ComponentNames.CarouselBanner.componentName),
    TitleImage(R.layout.coming_soon_view, ComponentNames.TitleImage.componentName),
    TextComponent(R.layout.coming_soon_view, ComponentNames.TextComponent.componentName),
    ClaimCoupon(R.layout.coming_soon_view, ComponentNames.ClaimCoupon.componentName),
    ProductCardCarousel(R.layout.coming_soon_view, ComponentNames.ProductCardCarousel.componentName),
    BrandRecommendation(R.layout.coming_soon_view, ComponentNames.BrandRecommendation.componentName),
    Margin(R.layout.coming_soon_view, ComponentNames.Margin.componentName),
    HorizontalCaregoryNavigation(R.layout.coming_soon_view, ComponentNames.HorizontalCategoryNavigation.componentName),
    CustomTopChat(R.layout.coming_soon_view, ComponentNames.CustomTopchat.componentName),
    Tabs(R.layout.coming_soon_view, ComponentNames.Tabs.componentName),
    ProductCardRevamp(R.layout.coming_soon_view, ComponentNames.PrductCardRevamp.componentName),
    BreadCrumbs(R.layout.coming_soon_view, ComponentNames.Breadcrumbs.componentName),

}

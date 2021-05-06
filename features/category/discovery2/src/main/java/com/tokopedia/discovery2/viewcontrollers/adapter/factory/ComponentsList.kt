package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.play.widget.PlayWidgetViewHolder


enum class ComponentsList(val id: Int, val componentName: String) {
    Default(R.layout.coming_soon_view, ComponentNames.Default.componentName),   // ***** Don't move this line ******
    SingleBanner(R.layout.multi_banner_layout, ComponentNames.SingleBanner.componentName),
    DoubleBanner(R.layout.multi_banner_layout, ComponentNames.DoubleBanner.componentName),
    TripleBanner(R.layout.multi_banner_layout, ComponentNames.TripleBanner.componentName),
    QuadrupleBanner(R.layout.multi_banner_layout, ComponentNames.QuadrupleBanner.componentName),
    Share(R.layout.multi_banner_layout, ComponentNames.Share.componentName),
    CategoryNavigation(R.layout.discovery_horizontal_recycler_view_with_title, ComponentNames.HorizontalCategoryNavigation.componentName),
    CategoryNavigationIem(R.layout.item_category_navigation, ComponentNames.HorizontalCategoryNavigationIem.componentName),
    BrandRecommendation(R.layout.brand_recommendation_layout, ComponentNames.BrandRecommendation.componentName),
    BrandRecommendationItem(R.layout.brand_recommendation_layout_item, ComponentNames.BrandRecommendationItem.componentName),
    CarouselBanner(R.layout.carousel_banner_layout, ComponentNames.CarouselBanner.componentName),
    CarouselBannerItemView(R.layout.carousel_banner_item_layout, ComponentNames.CarouselBannerItemView.componentName),
    SliderBanner(R.layout.circular_slider_banner_layout, ComponentNames.SliderBanner.componentName),
    BannerTimer(R.layout.banner_timer_layout, ComponentNames.BannerTimer.componentName),
    Tokopoints(R.layout.horizontal_recycler_view_layout, ComponentNames.Tokopoints.componentName),
    TokopointsItem(R.layout.tokopoints_item_layout, ComponentNames.TokopointsItem.componentName),
    LihatSemua(R.layout.lihat_semua, ComponentNames.LihatSemua.componentName),
    CpmTopAds(R.layout.cpm_topads_layout, ComponentNames.CpmTopAds.componentName),
    YouTubeView(R.layout.youtube_player_view, ComponentNames.Video.componentName),
    ChipsFilterView(R.layout.coming_soon_view, ComponentNames.ChipsFilter.componentName),
    Notifier(R.layout.coming_soon_view, ComponentNames.Notifier.componentName),
    TitleImage(R.layout.coming_soon_view, ComponentNames.TitleImage.componentName),
    TextComponent(R.layout.discovery_text_component_layout, ComponentNames.TextComponent.componentName),
    ClaimCoupon(R.layout.component_claim_coupon_layout, ComponentNames.ClaimCoupon.componentName),
    ClaimCouponItem(R.layout.component_claim_coupon_item, ComponentNames.ClaimCouponItem.componentName),
    Spacing(R.layout.spacing_layout, ComponentNames.Margin.componentName),
    ChipsFilter(R.layout.widget_recycler_view, ComponentNames.ChipsFilter.componentName),
    ChipsFilterItem(R.layout.chips_filter_item_layout, ComponentNames.ChipsFilterItem.componentName),
    Tabs(R.layout.tabs_layout, ComponentNames.Tabs.componentName),
    TabsItem(R.layout.tabs_item_layout, ComponentNames.TabsItem.componentName),
    DynamicCategory(R.layout.dynamic_category_layout, ComponentNames.DynamicCategory.componentName),
    DynamicCategoryItem(R.layout.dynamic_category_single_item_layout, ComponentNames.DynamicCategoryItem.componentName),
    LihatFlashSaleTimer(R.layout.lihat_flash_sale_timer_widget, ComponentNames.FlashSaleTimer.componentName),
    TimerSprintSale(R.layout.discovery_timer_sprint_sale_layout, ComponentNames.TimerSprintSale.componentName),
    ShimmerProductCard(R.layout.discovery_shimmer_product_card_layout, ComponentNames.ShimmerProductCard.componentName),
    Shimmer(R.layout.discovery_shimmer_layout, ComponentNames.Shimmer.componentName),
    LoadMore(R.layout.discovery_progress_bar_layout, ComponentNames.LoadMore.componentName),
    QuickCoupon(R.layout.quick_coupon_layout, ComponentNames.QuickCoupon.componentName),
    BannerCarousel(R.layout.horizontal_recycler_view_lihat, ComponentNames.BannerCarousel.componentName),
    BannerCarouselItemView(R.layout.banner_carousel_item_layout, ComponentNames.BannerCarouselItemView.componentName),

    // CustomTopChat(R.layout.coming_soon_view, ComponentNames.CustomTopchat.componentName),
    // Product Card Revamp
    ProductCardRevamp(R.layout.product_card_revamp_layout, ComponentNames.ProductCardRevamp.componentName),
    ProductCardRevampItem(R.layout.master_product_card_item_layout, ComponentNames.ProductCardRevampItem.componentName),
    MasterProductCardItemList(R.layout.master_product_card_item_list_layout, ComponentNames.MasterProductCardItemList.componentName),

    // Product Card Revamp Carousel
    ProductCardCarousel(R.layout.carousel_product_card_recycler_view, ComponentNames.ProductCardCarousel.componentName),
    ProductCardCarouselItem(R.layout.master_product_card_item_layout, ComponentNames.ProductCardCarouselItem.componentName),

    // Product Card Sprint Sale
    ProductCardSprintSale(R.layout.product_card_revamp_layout, ComponentNames.ProductCardSprintSale.componentName),
    ProductCardSprintSaleItem(R.layout.master_product_card_item_layout, ComponentNames.ProductCardSprintSaleItem.componentName),

    // Product Card Sprint Sale Carousel
    ProductCardSprintSaleCarousel(R.layout.carousel_product_card_recycler_view, ComponentNames.ProductCardSprintSaleCarousel.componentName),
    ProductCardSprintSaleCarouselItem(R.layout.master_product_card_item_layout, ComponentNames.ProductCardSprintSaleCarouselItem.componentName),

    ProductListEmptyState(R.layout.product_list_state, ComponentNames.ProductListEmptyState.componentName),
    SaleEndState(R.layout.sale_end_state, ComponentNames.SaleEndState.componentName),

    //Quick Filter
    QuickFilter(R.layout.discovery_quick_filter_layout, ComponentNames.QuickFilter.componentName),

    //Navigation Chips
    NavigationChips(R.layout.navigation_chips_view, ComponentNames.NavigationChips.componentName),
    NavigationCHipsItem(R.layout.navigation_chips_item, ComponentNames.NavigationChipsItem.componentName),

    //Banned View
    BannedView(R.layout.banned_view, ComponentNames.BannedView.componentName),

    //Discovery Play Widget
    DiscoPlayWidgetView(PlayWidgetViewHolder.layout, ComponentNames.DiscoPlayWidgetView.componentName),


}

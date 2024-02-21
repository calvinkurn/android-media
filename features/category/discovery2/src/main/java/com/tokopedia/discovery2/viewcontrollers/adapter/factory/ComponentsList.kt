package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.play.widget.PlayWidgetViewHolder

enum class ComponentsList(val id: Int, val componentName: String) {
    Default(R.layout.coming_soon_view, ComponentNames.Default.componentName), // ***** Don't move this line ******
    SingleBanner(R.layout.multi_banner_layout, ComponentNames.SingleBanner.componentName),
    DoubleBanner(R.layout.multi_banner_layout, ComponentNames.DoubleBanner.componentName),
    TripleBanner(R.layout.multi_banner_layout, ComponentNames.TripleBanner.componentName),
    QuadrupleBanner(R.layout.multi_banner_layout, ComponentNames.QuadrupleBanner.componentName),
    BrandRecommendation(R.layout.brand_recommendation_layout, ComponentNames.BrandRecommendation.componentName),
    BrandRecommendationItem(R.layout.brand_recommendation_layout_item, ComponentNames.BrandRecommendationItem.componentName),
    SliderBanner(R.layout.circular_slider_banner_layout, ComponentNames.SliderBanner.componentName),
    BannerTimer(R.layout.banner_timer_layout, ComponentNames.BannerTimer.componentName),
    LihatSemua(R.layout.lihat_semua, ComponentNames.LihatSemua.componentName),
    YouTubeView(R.layout.youtube_player_view, ComponentNames.Video.componentName),
    TitleImage(R.layout.coming_soon_view, ComponentNames.TitleImage.componentName),
    TextComponent(R.layout.discovery_text_component_layout, ComponentNames.TextComponent.componentName),
    ClaimCoupon(R.layout.component_claim_coupon_layout, ComponentNames.ClaimCoupon.componentName),
    ClaimCouponItem(R.layout.component_claim_coupon_item, ComponentNames.ClaimCouponItem.componentName),
    Spacing(R.layout.spacing_layout, ComponentNames.Margin.componentName),
    Tabs(R.layout.tabs_layout, ComponentNames.Tabs.componentName),
    TabsItem(R.layout.tabs_item_layout, ComponentNames.TabsItem.componentName),
    TabsIcon(R.layout.tabs_layout, ComponentNames.TabsIcon.componentName),
    TabsIconItem(R.layout.tabs_icon_item_layout, ComponentNames.TabsIconItem.componentName),
    TimerSprintSale(R.layout.discovery_timer_sprint_sale_layout, ComponentNames.TimerSprintSale.componentName),
    ShimmerProductCard(R.layout.discovery_shimmer_product_card_layout, ComponentNames.ShimmerProductCard.componentName),
    Shimmer(R.layout.discovery_shimmer_layout, ComponentNames.Shimmer.componentName),
    LoadMore(R.layout.discovery_product_list_states_layout, ComponentNames.LoadMore.componentName),
    QuickCoupon(R.layout.quick_coupon_layout, ComponentNames.QuickCoupon.componentName),
    BannerCarousel(R.layout.horizontal_recycler_view_lihat, ComponentNames.BannerCarousel.componentName),
    BannerCarouselItemView(R.layout.banner_carousel_item_layout, ComponentNames.BannerCarouselItemView.componentName),
    BannerCarouselShimmer(R.layout.disco_shimmer_carousel_banner_layout, ComponentNames.BannerCarouselShimmer.componentName),

    // Product Card Revamp
    ProductCardRevamp(R.layout.product_card_revamp_layout, ComponentNames.ProductCardRevamp.componentName),
    ProductCardRevampItem(R.layout.master_product_card_item_layout, ComponentNames.ProductCardRevampItem.componentName),
    MasterProductCardItemList(R.layout.master_product_card_item_list_layout, ComponentNames.MasterProductCardItemList.componentName),

    // Reimagine
    MasterProductCardReimagine(R.layout.master_product_card_item_layout_reimagine, ComponentNames.MasterProductCardItemReimagine.componentName),
    MasterProductCardListReimagine(R.layout.master_product_card_item_list_layout_reimagine, ComponentNames.MasterProductCardItemListReimagine.componentName),

    // Product Card Revamp Carousel
    ProductCardCarousel(R.layout.carousel_product_card_recycler_view, ComponentNames.ProductCardCarousel.componentName),
    ProductCardCarouselItem(R.layout.master_product_card_item_layout, ComponentNames.ProductCardCarouselItem.componentName),
    ProductCardCarouselItemList(R.layout.master_product_card_item_list_layout, ComponentNames.ProductCardCarouselItemList.componentName),

    // Reimagine
    ProductCardCarouselItemReimagine(R.layout.master_product_card_carousel_item_layout_reimagine, ComponentNames.ProductCardCarouselItemReimagine.componentName),
    ProductCardCarouselItemListReimagine(R.layout.master_product_card_carousel_item_list_layout_reimagine, ComponentNames.ProductCardCarouselItemListReimagine.componentName),

    // Product Card Sprint Sale
    ProductCardSprintSale(R.layout.product_card_revamp_layout, ComponentNames.ProductCardSprintSale.componentName),
    ProductCardSprintSaleItem(R.layout.master_product_card_item_layout, ComponentNames.ProductCardSprintSaleItem.componentName),

    // Reimagine
    ProductCardSprintSaleItemReimagine(R.layout.master_product_card_item_layout_reimagine, ComponentNames.ProductCardSprintSaleItemReimagine.componentName),

    // Product Card Sprint Sale Carousel
    ProductCardSprintSaleCarousel(R.layout.carousel_product_card_recycler_view, ComponentNames.ProductCardSprintSaleCarousel.componentName),
    ProductCardSprintSaleCarouselItem(R.layout.master_product_card_item_layout, ComponentNames.ProductCardSprintSaleCarouselItem.componentName),

    // Reimagine
    ProductCardSprintSaleCarouselItemReimagine(R.layout.master_product_card_carousel_item_layout_reimagine, ComponentNames.ProductCardSprintSaleCarouselItemReimagine.componentName),

    ProductListEmptyState(R.layout.product_list_state, ComponentNames.ProductListEmptyState.componentName),
    ContentCardEmptyState(R.layout.disco_content_card_empty_state, ComponentNames.ContentCardEmptyState.componentName),
    SaleEndState(R.layout.sale_end_state, ComponentNames.SaleEndState.componentName),

    MixLeftEmptyItem(R.layout.disco_mix_left_empty_item, ComponentNames.MixLeftEmptyItem.componentName),

    // Quick Filter
    QuickFilter(R.layout.discovery_quick_filter_layout, ComponentNames.QuickFilter.componentName),

    // Product Card Single
    ProductCardSingle(R.layout.disco_product_card_single, ComponentNames.ProductCardSingle.componentName),
    ProductCardSingleItem(R.layout.master_product_card_item_list_layout, ComponentNames.ProductCardSingleItem.componentName),

    // Product Card Single Reimagine
    ProductCardSingleReimagine(R.layout.disco_product_card_single, ComponentNames.ProductCardSingleReimagine.componentName),
    ProductCardSingleItemReimagine(R.layout.master_product_card_item_list_layout_reimagine, ComponentNames.ProductCardSingleItemReimagine.componentName),

    // Navigation Chips
    NavigationChips(R.layout.navigation_chips_view, ComponentNames.NavigationChips.componentName),
    NavigationCHipsItem(R.layout.navigation_chips_item, ComponentNames.NavigationChipsItem.componentName),

    // Banned View
    BannedView(R.layout.banned_view, ComponentNames.BannedView.componentName),

    // Discovery Play Widget
    DiscoPlayWidgetView(PlayWidgetViewHolder.layout, ComponentNames.DiscoPlayWidgetView.componentName),

    DiscoTDNBanner(R.layout.discovery_tdn_banner_view, ComponentNames.DiscoTDNBanner.componentName),
    MerchantVoucher(R.layout.discovery_mv_view, ComponentNames.MerchantVoucher.componentName),
    MerchantVoucherCarousel(R.layout.disco_merchant_voucher_carousel, ComponentNames.MerchantVoucherCarousel.componentName),
    MerchantVoucherList(R.layout.disco_merchant_voucher_list, ComponentNames.MerchantVoucherList.componentName),
    MerchantVoucherListItem(R.layout.disco_merchant_voucher_carousel_item, ComponentNames.MerchantVoucherListItem.componentName),
    MerchantVoucherCarouselItem(R.layout.disco_merchant_voucher_carousel_item, ComponentNames.MerchantVoucherCarouselItem.componentName),

    CarouselErrorLoad(R.layout.discovery_carousel_error_load, ComponentNames.CarouselErrorLoad.componentName),
    ProductListErrorLoad(R.layout.discovery_product_list_error_load, ComponentNames.ProductListErrorLoad.componentName),
    ProductListNetworkErrorLoad(R.layout.discovery_list_2_buttons_empty_state, ComponentNames.ProductListNetworkErrorLoad.componentName),

    // Category Best Seller
    CategoryBestSeller(R.layout.carousel_best_seller_recycler_view, ComponentNames.CategoryBestSeller.componentName),

    // CLP Featured Products
    CLPFeatureProducts(R.layout.carousel_best_seller_recycler_view, ComponentNames.CLPFeaturedProducts.componentName),

    // Topads Headline View
    TopadsHeadlineView(R.layout.topads_headline_layout, ComponentNames.TopadsHeadlineView.componentName),

    Section(R.layout.disco_section_component, ComponentNames.Section.componentName),
    AnchorTabs(R.layout.disco_anchor_tabs, ComponentNames.AnchorTabs.componentName),
    AnchorTabsItem(R.layout.disco_anchor_tabs_item, ComponentNames.AnchorTabsItem.componentName),

    // Rilisan Spesial View
    ShopCardView(R.layout.horizontal_rv_shop_card_layout, ComponentNames.ShopCardView.componentName),

    // Rilisan Spesial ItemView
    ShopCardItemView(R.layout.shop_card_item_layout, ComponentNames.ShopCardItemView.componentName),

    FlashSaleTokoTab(R.layout.discovery_flash_sale_toko_tabs, ComponentNames.FlashSaleTokoTab.componentName),

    ViewAllCarouselCard(R.layout.discovery_carousel_view_all_card, ComponentNames.ViewAllCardCarousel.componentName),

    // Calendar Widget
    CalendarWidgetCarousel(R.layout.discovery_calendar_widget_carousel_layout, ComponentNames.CalendarWidgetCarousel.componentName),
    CalendarWidgetGrid(R.layout.discovery_calendar_widget_grid_layout, ComponentNames.CalendarWidgetGrid.componentName),
    CalendarWidgetItem(R.layout.discovery_calendar_widget_item_layout, ComponentNames.CalendarWidgetItem.componentName),
    ShimmerCalendarWidget(R.layout.discovery_shimmer_calendar_layout, ComponentNames.ShimmerCalendarWidget.componentName),
    TopQuestWidget(R.layout.disco_top_quest, ComponentNames.TopQuestWidget.componentName),

    MyCoupon(R.layout.component_my_coupon_layout, ComponentNames.MyCoupon.componentName),
    MyCouponItem(R.layout.item_disco_my_coupon, ComponentNames.MyCouponItem.componentName),
    BannerInfinite(R.layout.disco_merchant_voucher_list, ComponentNames.BannerInfinite.componentName),
    BannerInfiniteItem(R.layout.disco_infinite_shop_banner_item, ComponentNames.BannerInfiniteItem.componentName),
    ShopCardInfinite(R.layout.disco_merchant_voucher_list, ComponentNames.ShopCardInfinite.componentName),
    ProductBundling(R.layout.disco_product_bundling_layout, ComponentNames.ProductBundling.componentName),
    ContentCard(R.layout.disco_content_card, ComponentNames.ContentCard.componentName),
    ContentCardItem(R.layout.disco_content_card_item, ComponentNames.ContentCardItem.componentName),
    ProductHighlight(R.layout.multi_banner_layout, ComponentNames.ProductHighlight.componentName),
    ThematicHeader(R.layout.disco_thematic_header_layout, ComponentNames.ThematicHeader.componentName),
    ExplicitWidget(R.layout.explicit_widget_layout, ComponentNames.ExplicitWidget.componentName),

    // Product Card Column List
    ProductCardColumnList(
        id = R.layout.item_discovery_product_card_column_list_layout,
        componentName = ComponentNames.ProductCardColumnList.componentName
    ),
    MerchantVoucherGrid(R.layout.merchant_voucher_grid_layout, ComponentNames.MerchantVoucherGrid.componentName),
    MerchantVoucherGridItem(R.layout.merchant_voucher_grid_item_layout, ComponentNames.MerchantVoucherGridItem.componentName),

    ShopOfferHeroBrand(
        id = R.layout.item_discovery_shop_offer_hero_brand_layout,
        componentName = ComponentNames.ShopOfferHeroBrand.componentName
    ),
    ShopOfferHeroBrandProductItem(
        id = R.layout.master_product_card_item_layout,
        componentName = ComponentNames.ShopOfferHeroBrandProductItem.componentName
    ),
    ShopOfferSupportingBrand(
        id = R.layout.discovery_supporting_brand_layout,
        componentName = ComponentNames.ShopOfferSupportingBrand.componentName
    ),
    ShopOfferSupportingBrandItem(
        id = R.layout.item_discovery_shop_offer_supporting_brand_layout,
        componentName = ComponentNames.ShopOfferSupportingBrandItem.componentName
    ),

    TabsImage(R.layout.tabs_layout, ComponentNames.TabsImage.componentName),
    TabsImageItem(R.layout.tabs_image_item_layout, ComponentNames.TabsImageItem.componentName),

    // Automate Coupon
    SingleAutomateCoupon(
        id = R.layout.single_automate_coupon_layout,
        componentName = ComponentNames.SingleAutomateCoupon.componentName
    ),

    GridAutomateCoupon(
        id = R.layout.grid_automate_coupon_layout,
        componentName = ComponentNames.GridAutomateCoupon.componentName
    ),

    GridAutomateCouponItem(
        id = R.layout.grid_automate_coupon_layout,
        componentName = ComponentNames.GridAutomateCouponItem.componentName
    ),

//    CarouselAutomateCoupon(
//        id = R.layout.carousel_automate_coupon_layout,
//        componentName = ComponentNames.CarouselAutomateCoupon.componentName
//    )
}

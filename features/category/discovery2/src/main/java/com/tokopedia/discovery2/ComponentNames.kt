package com.tokopedia.discovery2

enum class ComponentNames(val componentName: String) {
    Default("default"),
    SingleBanner("banner_image"),
    DoubleBanner("banner_image_double"),
    TripleBanner("banner_image_triple"),
    QuadrupleBanner("banner_image_quadruple"),
    BrandRecommendation("brand_recommendation"),
    BrandRecommendationItem("brand_recommendation_item"),
    SliderBanner("slider_banner"),
    BannerTimer("timer_with_banner"),
    LihatSemua("lihat_semua"),
    Video("video"),
    TitleImage("title_image"),
    TextComponent("text_component"),
    ClaimCoupon("claim_coupon"),
    ClaimCouponItem("claim_coupon_item"),
    Margin("margin"),

    // Tabs
    Tabs("tabs"),
    TabsItem("tabs_item"),

    // Tabs With Icon
    TabsIcon("tabs_icon"),
    TabsIconItem("tabs_icon_item"),

    // Tabs With Image or Text
    PlainTab("plain_tab"),
    PlainTabItem("tabs_image_item"),

    TimerSprintSale("timer_sprint_sale"),
    ShimmerProductCard("shimmer_product_card"),
    Shimmer("shimmer"),
    LoadMore("load_more"),
    QuickCoupon("quick_coupon"),
    BannerCarousel("banner_carousel"),
    BannerCarouselItemView("banner_carousel_item"),
    BannerCarouselShimmer("banner_carousel_shimmer"),

    // Product Card Revamp
    ProductCardRevamp("product_card_revamp"),
    ProductCardRevampItem("product_card_revamp_item"),
    MasterProductCardItemList("master_product_card_item_list"),

    // Reimagine
    MasterProductCardItemReimagine("master_product_card_item_reimagine"),
    MasterProductCardItemListReimagine("master_product_card_item_list_reimagine"),

    // Product Card Horizontal Carousel
    ProductCardCarousel("product_card_carousel"),
    ProductCardCarouselItem("product_card_carousel_item"),
    ProductCardCarouselItemList("product_card_carousel_item_list"),

    // Reimagine
    ProductCardCarouselItemReimagine("product_card_carousel_item_reimagine"),
    ProductCardCarouselItemListReimagine("product_card_carousel_item_list_reimagine"),

    // Product Card Sprint Sale
    @Deprecated(message = "According to the BE team, this component is no longer used")
    ProductCardSprintSale("product_card_sprint_sale"),
    @Deprecated(message = "According to the BE team, this component is no longer used")
    ProductCardSprintSaleItem("product_card_sprint_sale_item"),

    // Reimagine
    @Deprecated(message = "According to the BE team, this component is no longer used")
    ProductCardSprintSaleItemReimagine("product_card_sprint_sale_item_reimagine"),

    // Product Card Horizontal Sprint Sale
    @Deprecated(message = "According to the BE team, this component is no longer used")
    ProductCardSprintSaleCarousel("product_card_sprint_sale_carousel"),
    @Deprecated(message = "According to the BE team, this component is no longer used")
    ProductCardSprintSaleCarouselItem("product_card_sprint_sale_carousel_item"),

    // Reimagine
    @Deprecated(message = "According to the BE team, this component is no longer used")
    ProductCardSprintSaleCarouselItemReimagine("product_card_sprint_sale_carousel_item_reimagine"),

    ProductListEmptyState("product_list_empty_state"),
    ContentCardEmptyState("content_card_empty_state"),
    SaleEndState("sale_end_state"),

//    Product Card Single
    ProductCardSingle("product_card_single"),
    ProductCardSingleItem("product_card_single_item"),

    // Reimagine
    ProductCardSingleReimagine("product_card_single_reimagine"),
    ProductCardSingleItemReimagine("product_card_single_item_reimagine"),

//    Empty Item for MixLeft
    MixLeftEmptyItem("mix_left_empty_item"),

    // Quick Filter
    QuickFilter("chips_filter_v2"),

    // Navigation Chips
    NavigationChips("navigation_chips"),
    NavigationChipsItem("navigation_chips_item"),

    // Banned View
    BannedView("banned_view"),

    // Play Widget View
    DiscoPlayWidgetView("play_widget"),

    DiscoTDNBanner("tdn_banner"),

    MerchantVoucher("merchant_voucher"),

    CarouselErrorLoad("carousel_error_load"),
    ProductListErrorLoad("product_list_error_load"),
    ProductListNetworkErrorLoad("product_list_network_error_load"),

    // Category Best Seller
    CategoryBestSeller("product-card-horizontal-scroll"),

    // Promo Featured Products - CLP
    CLPFeaturedProducts("featured-product"),

    // Topads Headline View
    TopadsHeadlineView("topads"),

    // Rilisan Spesial View
    ShopCardView("shop_card"),

    // Rilisan Spesial ItemView
    ShopCardItemView("shop_card_item"),

    MerchantVoucherCarousel("merchant_voucher_carousel"),
    MerchantVoucherList("merchant_voucher_list"),
    MerchantVoucherListItem("merchant_voucher_list_item"),
    MerchantVoucherCarouselItem("merchant_voucher_carousel_item"),

    Section("section"),
    AnchorTabs("anchor_tabs"),
    AnchorTabsItem("anchor_tabs_item"),

    // Calendar Widget
    CalendarWidgetCarousel("calendar"),
    CalendarWidgetGrid("calendar_grid"),
    CalendarWidgetItem("calendar_item"),
    ShimmerCalendarWidget("shimmer_calendar_widget"),

    TopQuestWidget("top_quest"),

    // my coupon
    MyCoupon("my_coupon"),
    MyCouponItem("my_coupon_item"),

    BannerInfinite("banner_infinite"),
    BannerInfiniteItem("banner_infinite_item"),

    ShopCardInfinite("shop_card_infinite"),
    ProductBundling("product_bundling"),

    ThematicHeader("thematic_header"),

    ExplicitWidget("explicit_widget"),

    ContentCard("content_card"),
    ContentCardItem("content_card_item"),
    ProductHighlight("product_highlight"),

    // Product Card Column List
    ProductCardColumnList(
        componentName = "product_card_column_list"
    ),

    FlashSaleTokoTab("tabs_flash_sale_toko"),

    ViewAllCardCarousel("view_all_card_carousel"),

    MerchantVoucherGrid("merchant_voucher_grid_infinite"),
    MerchantVoucherGridItem("merchant_voucher_grid_infinite_item"),

    // BMSM Hero Brand
    ShopOfferHeroBrand("shop_offer_hero_brand"),
    ShopOfferHeroBrandProductItem("shop_offer_hero_brand_product_item"),
    ShopOfferHeroBrandProductItemReimagine("shop_offer_hero_brand_product_item_reimagine"),

    // BMSM Supporting Brand
    ShopOfferSupportingBrand("shop_offer_supporting_brand"),
    ShopOfferSupportingBrandItem("shop_offer_supporting_brand_item"),

    AutomateCoupon("automate_coupon"),
    SingleAutomateCoupon("single_automate_coupon"),
    GridAutomateCoupon("grid_automate_coupon"),
    GridAutomateCouponItem("grid_automate_coupon_item"),
    CarouselAutomateCoupon("carousel_automate_coupon"),
    CarouselAutomateCouponItem("carousel_automate_coupon_item")
}

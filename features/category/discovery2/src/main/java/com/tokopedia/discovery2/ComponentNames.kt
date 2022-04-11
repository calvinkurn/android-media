package com.tokopedia.discovery2


enum class ComponentNames(val componentName: String) {
    Default("default"),
    SingleBanner("banner_image"),
    DoubleBanner("banner_image_double"),
    TripleBanner("banner_image_triple"),
    QuadrupleBanner("banner_image_quadruple"),
    Share("share"),
    HorizontalCategoryNavigation("horizontal_category_navigation"),
    HorizontalCategoryNavigationIem("horizontal_category_navigation_item"),
    BrandRecommendation("brand_recommendation"),
    BrandRecommendationItem("brand_recommendation_item"),
    CarouselBanner("carousel_banner"),
    CarouselBannerItemView("carousel_banner_item"),
    SliderBanner("slider_banner"),
    BannerTimer("timer_with_banner"),
    Tokopoints("tokopoints"),
    TokopointsItem("tokopoints_item"),
    LihatSemua("lihat_semua"),
    Video("video"),
    Notifier("notifier"),
    TitleImage("title_image"),
    TextComponent("text_component"),
    ClaimCoupon("claim_coupon"),
    ClaimCouponItem("claim_coupon_item"),
    Margin("margin"),
    CustomTopchat("custom_topchat"),
    ChipsFilter("chips_filter"),
    ChipsFilterItem("chips_filter_item"),
    Tabs("tabs"),
    TabsItem("tabs_item"),
    DynamicCategory("dynamic_category"),
    DynamicCategoryItem("dynamic_category_item"),
    FlashSaleTimer("flash_sale_timer"),
    TimerSprintSale("timer_sprint_sale"),
    ShimmerProductCard("shimmer_product_card"),
    Shimmer("shimmer"),
    LoadMore("load_more"),
    QuickCoupon("quick_coupon"),
    BannerCarousel("banner_carousel"),
    BannerCarouselItemView("banner_carousel_item"),
    BannerCarouselShimmer("banner_carousel_shimmer"),
    BottomNavigation("bottom_navigation"),

    // Product Card Revamp
    ProductCardRevamp("product_card_revamp"),
    ProductCardRevampItem("product_card_revamp_item"),
    MasterProductCardItemList("master_product_card_item_list"),

    // Product Card Horizontal Carousel
    ProductCardCarousel("product_card_carousel"),
    ProductCardCarouselItem("product_card_carousel_item"),

    // Product Card Sprint Sale
    ProductCardSprintSale("product_card_sprint_sale"),
    ProductCardSprintSaleItem("product_card_sprint_sale_item"),

    // Product Card Horizontal Sprint Sale
    ProductCardSprintSaleCarousel("product_card_sprint_sale_carousel"),
    ProductCardSprintSaleCarouselItem("product_card_sprint_sale_carousel_item"),
    ProductListEmptyState("product_list_empty_state"),
    SaleEndState("sale_end_state"),

//    Product Card Single
    ProductCardSingle("product_card_single"),
    ProductCardSingleItem("product_card_single_item"),

//    Empty Item for MixLeft
    MixLeftEmptyItem("mix_left_empty_item"),

    //Quick Filter
    QuickFilter("chips_filter_v2"),

    //Navigation Chips
    NavigationChips("navigation_chips"),
    NavigationChipsItem("navigation_chips_item"),

    //Banned View
    BannedView("banned_view"),

    //Play Widget View
    DiscoPlayWidgetView("play_widget"),

    DiscoTDNBanner("tdn_banner"),

    MerchantVoucher("merchant_voucher"),

    CarouselErrorLoad("carousel_error_load"),
    ProductListErrorLoad("product_list_error_load"),

    //Category Best Seller
    CategoryBestSeller("product-card-horizontal-scroll"),

    //Topads Headline View
    TopadsHeadlineView("topads"),

    //Rilisan Spesial View
    ShopCardView("shop_card"),
    //Rilisan Spesial ItemView
    ShopCardItemView("shop_card_item"),

    MerchantVoucherCarousel("merchant_voucher_carousel"),
    MerchantVoucherList("merchant_voucher_list"),
    MerchantVoucherListItem("merchant_voucher_list_item"),
    MerchantVoucherCarouselItem("merchant_voucher_carousel_item"),

    Section("section"),
    AnchorTabs("anchor_tabs"),
    AnchorTabsItem("anchor_tabs_item"),

    //Calendar Widget
    CalendarWidgetCarousel("calendar"),
    CalendarWidgetGrid("calendar_grid"),
    CalendarWidgetItem("calendar_item"),
    ShimmerCalendarWidget("shimmer_calendar_widget"),

    TopQuestWidget("top_quest"),

    //my coupon
    MyCoupon("my_coupon"),
    MyCouponItem("my_coupon_item")

}

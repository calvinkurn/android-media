package com.tokopedia.discovery2

import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapperTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.ContentCard.ContentCardItemModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.ContentCard.ContentCardModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.DefaultComponentViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs.AnchorTabsItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs.AnchorTabsViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.textcomponent.TextComponentViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.MultiBannerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.BannerTimerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations.BrandRecommendationItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations.BrandRecommendationViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget.CalendarWidgetCarouselViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget.CalendarWidgetGridViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget.CalendarWidgetItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner.CarouselBannerItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner.CarouselBannerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload.CarouselErrorLoadViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller.CategoryBestSellerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation.CategoryNavigationItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation.CategoryNavigationViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips.ChipsFilterItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips.ChipsFilterViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner.CircularSliderBannerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview.ComingSoonViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory.DynamicCategoryItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory.DynamicCategoryViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.explicitwidget.ExplicitWidgetViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatflashsaletimerwidget.LihatFlashSaleTimerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua.LihatSemuaViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore.LoadMoreViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher.DiscoMerchantVoucherViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel.MerchantVoucherCarouselItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel.MerchantVoucherCarouselViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel.MerchantVoucherListViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon.MyCouponItemModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon.MyCouponModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips.NavigationChipsViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget.DiscoveryPlayWidgetViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productbundling.ProductBundlingViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.MixLeftEmptyViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate.EmptyStateViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp.ProductCardRevampViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsingle.ProductCardSingleViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsprintsalecarousel.ProductCardSprintSaleCarouselViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickcoupon.QuickCouponViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter.QuickFilterViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section.SectionViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer.ShimmerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite.ShopBannerInfiniteItemModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite.ShopBannerInfiniteModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard.ShopCardItemModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard.ShopCardModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcardinfinite.ShopCardInfiniteModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.sliderbanner.SliderBannerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.spacing.SpacingViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader.ThematicHeaderViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale.TimerSprintSaleItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tndbanner.DiscoveryTNDBannerViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints.TokopointsItemViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints.TokopointsViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topadsheadline.TopAdsHeadlineViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topquest.TopQuestViewModelTest
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.YouTubeViewViewModelTest
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MultiBannerViewModelTest::class,
    TimerSprintSaleItemViewModelTest::class,
    UtilsTest::class,
    DiscoveryDataMapperTest::class,
    BannerCarouselItemViewModelTest::class,
    BannerCarouselViewModelTest::class,
    BrandRecommendationViewModelTest::class,
    BrandRecommendationItemViewModelTest::class,
    CarouselBannerViewModelTest::class,
    CarouselBannerItemViewModelTest::class,
    CategoryNavigationItemViewModelTest::class,
    CategoryNavigationViewModelTest::class,
    ChipsFilterItemViewModelTest::class,
    CircularSliderBannerViewModelTest::class,
    SliderBannerViewModelTest::class,
    NavigationChipsViewModelTest::class,
    DynamicCategoryItemViewModelTest::class,
    DynamicCategoryViewModelTest::class,
    LihatSemuaViewModelTest::class,
    YouTubeViewViewModelTest::class,
    ShimmerViewModelTest::class,
    ProductCardCarouselViewModelTest::class,
    ProductCardSingleViewModelTest::class,
    MixLeftEmptyViewModelTest::class,
    TextComponentViewModelTest::class,
    ComingSoonViewModelTest::class,
    CalendarWidgetItemViewModelTest::class,
    CalendarWidgetGridViewModelTest::class,
    CalendarWidgetCarouselViewModelTest::class,
    AnchorTabsViewModelTest::class,
    AnchorTabsItemViewModelTest::class,
    ShopCardModelTest::class,
    ShopCardItemModelTest::class,
    MyCouponItemModelTest::class,
    MyCouponModelTest::class,
    MasterProductCardItemViewModelTest::class,
    ProductCardCarouselViewModelTest::class,
    MerchantVoucherCarouselItemViewModelTest::class,
    MerchantVoucherCarouselViewModelTest::class,
    MerchantVoucherListViewModelTest::class,
    DiscoMerchantVoucherViewModelTest::class,
    TextComponentViewModelTest::class,
    DiscoveryTNDBannerViewModelTest::class,
    TokopointsViewModelTest::class,
    TokopointsItemViewModelTest::class,
    SpacingViewModelTest::class,
    TabsViewModelTest::class,
    TabsItemViewModelTest::class,
    CarouselErrorLoadViewModelTest::class,
    LoadMoreViewModelTest::class,
    QuickCouponViewModelTest::class,
    TopQuestViewModelTest::class,
    BannerTimerViewModelTest::class,
    TopAdsHeadlineViewModelTest::class,
    SectionViewModelTest::class,
    ProductCardRevampViewModelTest::class,
    EmptyStateViewModelTest::class,
    MixLeftEmptyViewModelTest::class,
    LihatFlashSaleTimerViewModelTest::class,
    ClaimCouponViewModelTest::class,
    ClaimCouponItemViewModelTest::class,
    ChipsFilterViewModelTest::class,
    ShopBannerInfiniteModelTest::class,
    ShopBannerInfiniteItemModelTest::class,
    CategoryBestSellerViewModelTest::class,
    ShopCardInfiniteModelTest::class,
    DiscoveryPlayWidgetViewModelTest::class,
    QuickFilterViewModelTest::class,
    ExplicitWidgetViewModelTest::class,
    DiscoveryViewModelTest::class,
    ProductBundlingViewModelTest::class,
    DefaultComponentViewModelTest::class,
    ProductCardSprintSaleCarouselViewModelTest::class,
    ThematicHeaderViewModelTest::class,
    ContentCardModelTest::class,
    ContentCardItemModelTest::class,
)


class DiscoveryTestSuite {
}

package com.tokopedia.discovery2.di

import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs.AnchorTabsItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs.AnchorTabsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon.ListAutomateCouponItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon.ListAutomateCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon.SingleAutomateCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget.CalendarWidgetCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget.CalendarWidgetGridViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget.CalendarWidgetItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload.CarouselErrorLoadViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller.CategoryBestSellerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard.ContentCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard.ContentCardViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.explicitwidget.ExplicitWidgetViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.flashsaletoko.FlashSaleTokoTabViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore.LoadMoreViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher.DiscoMerchantVoucherViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel.MerchantVoucherCarouselItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel.MerchantVoucherCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel.MerchantVoucherListViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon.MyCouponItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon.MyCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips.NavigationChipsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget.DiscoveryPlayWidgetViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productbundling.ProductBundlingViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist.ProductCardColumnListViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate.EmptyStateViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate.ErrorLoadViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp.ProductCardRevampViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsingle.ProductCardSingleViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight.ProductHighlightViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickcoupon.QuickCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter.QuickFilterViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section.SectionViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite.ShopBannerInfiniteViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard.ShopCardViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcardinfinite.ShopCardInfiniteViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.ShopOfferHeroBrandViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand.ShopOfferSupportingBrandItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand.ShopOfferSupportingBrandViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topadsheadline.TopAdsHeadlineViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topquest.TopQuestViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.viewallcard.ViewAllCarouselViewModel
import dagger.Subcomponent

@Subcomponent
interface UIWidgetComponent {
    fun inject(mutliViewModel: MultiBannerViewModel)
    fun inject(productCardCarouselViewModel: ProductCardCarouselViewModel)
    fun inject(productCardRevampViewModel: ProductCardRevampViewModel)
    fun inject(tabsViewModel: TabsViewModel)
    fun inject(claimCouponViewModel: ClaimCouponViewModel)
    fun inject(claimCouponItemViewModel: ClaimCouponItemViewModel)
    fun inject(masterProductCardItemViewModel: MasterProductCardItemViewModel)
    fun inject(loadMoreViewModel: LoadMoreViewModel)
    fun inject(quickCouponViewModel: QuickCouponViewModel)
    fun inject(quickFilterViewModel: QuickFilterViewModel)
    fun inject(navigationChipsViewModel: NavigationChipsViewModel)
    fun inject(emptyStateViewModel: EmptyStateViewModel)
    fun inject(discoveryPlayWidgetViewModel: DiscoveryPlayWidgetViewModel)
    fun inject(carouselErrorLoadViewModel: CarouselErrorLoadViewModel)
    fun inject(errorLoadViewModel: ErrorLoadViewModel)
    fun inject(categoryBestSellerViewModel: CategoryBestSellerViewModel)
    fun inject(topAdsHeadlineViewModel: TopAdsHeadlineViewModel)
    fun inject(discoMerchantVoucherViewModel: DiscoMerchantVoucherViewModel)
    fun inject(merchantVoucherCarouselViewModel: MerchantVoucherCarouselViewModel)
    fun inject(merchantVoucherListViewModel: MerchantVoucherListViewModel)
    fun inject(calendarWidgetGridViewModel: CalendarWidgetGridViewModel)
    fun inject(calendarWidgetItemViewModel: CalendarWidgetItemViewModel)
    fun inject(calendarWidgetCarouselViewModel: CalendarWidgetCarouselViewModel)
    fun inject(rilisanSpesialViewModel: ShopCardViewModel)
    fun inject(sectionViewModel: SectionViewModel)
    fun inject(viewModel: AnchorTabsItemViewModel)
    fun inject(viewModel: AnchorTabsViewModel)
    fun inject(bannerCarouselViewModel: BannerCarouselViewModel)
    fun inject(myCouponViewModel: MyCouponViewModel)
    fun inject(myCouponItemViewModel: MyCouponItemViewModel)
    fun inject(viewModel: ProductCardSingleViewModel)
    fun inject(shopBannerInfiniteViewModel: ShopBannerInfiniteViewModel)
    fun inject(viewModel: TopQuestViewModel)
    fun inject(shopCardInfiniteViewModel: ShopCardInfiniteViewModel)
    fun inject(explicitWidgetViewModel: ExplicitWidgetViewModel)
    fun inject(productBundlingViewModel: ProductBundlingViewModel)
    fun inject(contentCardViewModel: ContentCardViewModel)
    fun inject(contentCardItemViewModel: ContentCardItemViewModel)
    fun inject(productHighlightViewModel: ProductHighlightViewModel)
    fun inject(productCardColumnListViewModel: ProductCardColumnListViewModel)
    fun inject(flashSaleTokoTabViewModel: FlashSaleTokoTabViewModel)
    fun inject(viewAllCarouselViewModel: ViewAllCarouselViewModel)
    fun inject(merchantVoucherGridViewModel: MerchantVoucherGridViewModel)
    fun inject(merchantVoucherGridItemViewModel: MerchantVoucherGridItemViewModel)
    fun inject(shopOfferHeroBrandViewModel: ShopOfferHeroBrandViewModel)
    fun inject(shopOfferSupportingBrandViewModel: ShopOfferSupportingBrandViewModel)
    fun inject(shopOfferSupportingBrandItemViewModel: ShopOfferSupportingBrandItemViewModel)
    fun inject(singleAutomateCouponViewModel: SingleAutomateCouponViewModel)
    fun inject(listAutomateCouponViewModel: ListAutomateCouponViewModel)
    fun inject(listAutomateCouponItemViewModel: ListAutomateCouponItemViewModel)
    fun inject(merchantVoucherCarouselItemViewModel: MerchantVoucherCarouselItemViewModel)
}

package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.app.Application
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.DefaultComponentViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs.AnchorTabsItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs.AnchorTabsItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannedview.BannedViewViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.BannerTimerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.BannerTimerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations.BrandRecommendationItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations.BrandRecommendationItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations.BrandRecommendationViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations.BrandRecommendationViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget.*
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload.CarouselErrorLoadViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload.CarouselErrorLoadViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller.CategoryBestSellerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller.CategoryBestSellerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner.CircularSliderBannerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner.CircularSliderBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview.ComingSoonViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview.ComingSoonViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard.ContentCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard.ContentCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard.ContentCardViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard.ContentCardViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentcardemptystate.ContentCardEmptyStateViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentcardemptystate.ContentCardEmptyStateViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.explicitwidget.ExplicitWidgetViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.explicitwidget.ExplicitWidgetViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua.LihatSemuaViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua.LihatSemuaViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore.LoadMoreViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore.LoadMoreViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher.DiscoMerchantVoucherViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher.DiscoMerchantVoucherViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel.*
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon.MyCouponItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon.MyCouponItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon.MyCouponViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon.MyCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips.NavigationChipsItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips.NavigationChipsViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips.NavigationChipsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget.DiscoveryPlayWidgetViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget.DiscoveryPlayWidgetViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productbundling.ProductBundlingViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productbundling.ProductBundlingViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.MixLeftEmptyViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.MixLeftEmptyViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate.EmptyStateViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate.EmptyStateViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate.ErrorLoadViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate.ErrorLoadViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp.ProductCardRevampViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp.ProductCardRevampViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsingle.ProductCardSingleViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsingle.ProductCardSingleViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight.ProductHighlightViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight.ProductHighlightViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickcoupon.QuickCouponViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickcoupon.QuickCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter.QuickFilterViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter.QuickFilterViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.saleendstates.SaleEndStateViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.saleendstates.SaleEndStateViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section.SectionViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section.SectionViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer.*
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite.ShopBannerInfiniteItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite.ShopBannerInfiniteItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite.ShopBannerInfiniteViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite.ShopBannerInfiniteViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard.ShopCardViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard.ShopCardViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcardinfinite.ShopCardInfiniteViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcardinfinite.ShopCardInfiniteViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcarditem.ShopCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcarditem.ShopCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.spacing.SpacingViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.spacing.SpacingViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsItemIconViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsItemIconViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tdnbanner.DiscoveryTDNBannerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tdnbanner.DiscoveryTDNBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.textcomponent.TextComponentViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.textcomponent.TextComponentViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader.ThematicHeaderViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader.ThematicHeaderViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale.TimerSprintSaleItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale.TimerSprintSaleItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topadsheadline.TopAdsHeadlineViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topadsheadline.TopAdsHeadlineViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topquest.TopQuestViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topquest.TopQuestViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.YouTubeViewViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.YoutubeViewViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class DiscoveryHomeFactory {

    companion object {
        private val componentIdMap = mutableMapOf<String, Int>()
        private val componentMapper =
            mutableMapOf<Int, ComponentHelpersHolder<out AbstractViewHolder, out DiscoveryBaseViewModel>>()

        init {
            initializeComponent(
                ComponentsList.SingleBanner,
                ::MultiBannerViewHolder,
                ::MultiBannerViewModel
            )
            initializeComponent(
                ComponentsList.DoubleBanner,
                ::MultiBannerViewHolder,
                ::MultiBannerViewModel
            )
            initializeComponent(
                ComponentsList.TripleBanner,
                ::MultiBannerViewHolder,
                ::MultiBannerViewModel
            )
            initializeComponent(
                ComponentsList.QuadrupleBanner,
                ::MultiBannerViewHolder,
                ::MultiBannerViewModel
            )
            initializeComponent(
                ComponentsList.YouTubeView,
                ::YoutubeViewViewHolder,
                ::YouTubeViewViewModel
            )
            initializeComponent(
                ComponentsList.BannerTimer,
                ::BannerTimerViewHolder,
                ::BannerTimerViewModel
            )
            initializeComponent(
                ComponentsList.LihatSemua,
                ::LihatSemuaViewHolder,
                ::LihatSemuaViewModel
            )
//            initializeComponent(ComponentsList.SliderBanner, ::SliderBannerViewHolder, ::SliderBannerViewModel)
            initializeComponent(
                ComponentsList.SliderBanner,
                ::CircularSliderBannerViewHolder,
                ::CircularSliderBannerViewModel
            )
            initializeComponent(
                ComponentsList.TitleImage,
                ::ComingSoonViewHolder,
                ::ComingSoonViewModel
            )
            initializeComponent(
                ComponentsList.TextComponent,
                ::TextComponentViewHolder,
                ::TextComponentViewModel
            )
            initializeComponent(
                ComponentsList.ClaimCoupon,
                ::ClaimCouponViewHolder,
                ::ClaimCouponViewModel
            )
            initializeComponent(
                ComponentsList.ClaimCouponItem,
                ::ClaimCouponItemViewHolder,
                ::ClaimCouponItemViewModel
            )
            initializeComponent(
                ComponentsList.BrandRecommendation,
                ::BrandRecommendationViewHolder,
                ::BrandRecommendationViewModel
            )
            initializeComponent(
                ComponentsList.BrandRecommendationItem,
                ::BrandRecommendationItemViewHolder,
                ::BrandRecommendationItemViewModel
            )
            initializeComponent(
                ComponentsList.Default,
                ::ComingSoonViewHolder,
                ::ComingSoonViewModel
            )
            initializeComponent(ComponentsList.Spacing, ::SpacingViewHolder, ::SpacingViewModel)
            initializeComponent(ComponentsList.Tabs, ::TabsViewHolder, ::TabsViewModel)
            initializeComponent(ComponentsList.TabsItem, ::TabsItemViewHolder, ::TabsItemViewModel)
            initializeComponent(ComponentsList.TabsIcon, ::TabsViewHolder, ::TabsViewModel)
            initializeComponent(
                ComponentsList.TabsIconItem,
                ::TabsItemIconViewHolder,
                ::TabsItemIconViewModel
            )
            initializeComponent(
                ComponentsList.TimerSprintSale,
                ::TimerSprintSaleItemViewHolder,
                ::TimerSprintSaleItemViewModel
            )
            initializeComponent(ComponentsList.Shimmer, ::ShimmerViewHolder, ::ShimmerViewModel)
            initializeComponent(
                ComponentsList.ShimmerProductCard,
                ::ShimmerProductCardViewHolder,
                ::ShimmerViewModel
            )
            initializeComponent(ComponentsList.LoadMore, ::LoadMoreViewHolder, ::LoadMoreViewModel)
            initializeComponent(
                ComponentsList.QuickCoupon,
                ::QuickCouponViewHolder,
                ::QuickCouponViewModel
            )
            initializeComponent(
                ComponentsList.BannerCarousel,
                ::BannerCarouselViewHolder,
                ::BannerCarouselViewModel
            )
            initializeComponent(
                ComponentsList.BannerCarouselItemView,
                ::BannerCarouselItemViewHolder,
                ::BannerCarouselItemViewModel
            )
            initializeComponent(
                ComponentsList.BannerCarouselShimmer,
                ::ShimmerBannerCarouselViewHolder,
                ::ShimmerViewModel
            )

            // Product Card Revamp
            initializeComponent(
                ComponentsList.ProductCardRevamp,
                ::ProductCardRevampViewHolder,
                ::ProductCardRevampViewModel
            )
            initializeComponent(
                ComponentsList.ProductCardRevampItem,
                ::MasterProductCardItemViewHolder,
                ::MasterProductCardItemViewModel
            )
            initializeComponent(
                ComponentsList.MasterProductCardItemList,
                ::MasterProductCardItemViewHolder,
                ::MasterProductCardItemViewModel
            )

            // Product Card Horizontal Carousel
            initializeComponent(
                ComponentsList.ProductCardCarousel,
                ::ProductCardCarouselViewHolder,
                ::ProductCardCarouselViewModel
            )
            initializeComponent(
                ComponentsList.ProductCardCarouselItem,
                ::MasterProductCardItemViewHolder,
                ::MasterProductCardItemViewModel
            )
            initializeComponent(
                ComponentsList.ProductCardCarouselItemList,
                ::MasterProductCardItemViewHolder,
                ::MasterProductCardItemViewModel
            )

            // Product Card Sprint Sale
            initializeComponent(
                ComponentsList.ProductCardSprintSale,
                ::ProductCardRevampViewHolder,
                ::ProductCardRevampViewModel
            )
            initializeComponent(
                ComponentsList.ProductCardSprintSaleItem,
                ::MasterProductCardItemViewHolder,
                ::MasterProductCardItemViewModel
            )

            // Product Card Horizontal Sprint Sale
            initializeComponent(
                ComponentsList.ProductCardSprintSaleCarousel,
                ::ProductCardCarouselViewHolder,
                ::ProductCardCarouselViewModel
            )
            initializeComponent(
                ComponentsList.ProductCardSprintSaleCarouselItem,
                ::MasterProductCardItemViewHolder,
                ::MasterProductCardItemViewModel
            )

            initializeComponent(
                ComponentsList.ProductListEmptyState,
                ::EmptyStateViewHolder,
                ::EmptyStateViewModel
            )
            initializeComponent(
                ComponentsList.ContentCardEmptyState,
                ::ContentCardEmptyStateViewHolder,
                ::ContentCardEmptyStateViewModel
            )
            initializeComponent(
                ComponentsList.SaleEndState,
                ::SaleEndStateViewHolder,
                ::SaleEndStateViewModel
            )

            initializeComponent(
                ComponentsList.MixLeftEmptyItem,
                ::MixLeftEmptyViewHolder,
                ::MixLeftEmptyViewModel
            )

            // Quick Filter
            initializeComponent(
                ComponentsList.QuickFilter,
                ::QuickFilterViewHolder,
                ::QuickFilterViewModel
            )

            // Product Card Single
            initializeComponent(
                ComponentsList.ProductCardSingle,
                ::ProductCardSingleViewHolder,
                ::ProductCardSingleViewModel
            )
            initializeComponent(
                ComponentsList.ProductCardSingleItem,
                ::MasterProductCardItemViewHolder,
                ::MasterProductCardItemViewModel
            )

            // Navigation Chips
            initializeComponent(
                ComponentsList.NavigationChips,
                ::NavigationChipsViewHolder,
                ::NavigationChipsViewModel
            )
            initializeComponent(
                ComponentsList.NavigationCHipsItem,
                ::NavigationChipsItemViewHolder,
                ::DefaultComponentViewModel
            )

            // Banned View
            initializeComponent(
                ComponentsList.BannedView,
                ::BannedViewViewHolder,
                ::DefaultComponentViewModel
            )

            // Play Widget
            initializeComponent(
                ComponentsList.DiscoPlayWidgetView,
                ::DiscoveryPlayWidgetViewHolder,
                ::DiscoveryPlayWidgetViewModel
            )

            initializeComponent(
                ComponentsList.DiscoTDNBanner,
                ::DiscoveryTDNBannerViewHolder,
                ::DiscoveryTDNBannerViewModel
            )
            initializeComponent(
                ComponentsList.MerchantVoucher,
                ::DiscoMerchantVoucherViewHolder,
                ::DiscoMerchantVoucherViewModel
            )
            initializeComponent(
                ComponentsList.MerchantVoucherCarousel,
                ::MerchantVoucherCarouselViewHolder,
                ::MerchantVoucherCarouselViewModel
            )
            initializeComponent(
                ComponentsList.MerchantVoucherList,
                ::MerchantVoucherListViewHolder,
                ::MerchantVoucherListViewModel
            )
            initializeComponent(
                ComponentsList.MerchantVoucherListItem,
                ::MerchantVoucherCarouselItemViewHolder,
                ::MerchantVoucherCarouselItemViewModel
            )
            initializeComponent(
                ComponentsList.MerchantVoucherCarouselItem,
                ::MerchantVoucherCarouselItemViewHolder,
                ::MerchantVoucherCarouselItemViewModel
            )

            initializeComponent(
                ComponentsList.CarouselErrorLoad,
                ::CarouselErrorLoadViewHolder,
                ::CarouselErrorLoadViewModel
            )
            initializeComponent(
                ComponentsList.ProductListErrorLoad,
                ::ErrorLoadViewHolder,
                ::ErrorLoadViewModel
            )
            initializeComponent(
                ComponentsList.ProductListNetworkErrorLoad,
                ::ErrorLoadViewHolder,
                ::ErrorLoadViewModel
            )

            // Category Best Seller
            initializeComponent(
                ComponentsList.CategoryBestSeller,
                ::CategoryBestSellerViewHolder,
                ::CategoryBestSellerViewModel
            )

            // Category Best Seller
            initializeComponent(
                ComponentsList.CLPFeatureProducts,
                ::CategoryBestSellerViewHolder,
                ::CategoryBestSellerViewModel
            )

            // Topads Headline View
            initializeComponent(
                ComponentsList.TopadsHeadlineView,
                ::TopAdsHeadlineViewHolder,
                ::TopAdsHeadlineViewModel
            )

            // Rilisan Spesial View
            initializeComponent(
                ComponentsList.ShopCardView,
                ::ShopCardViewHolder,
                ::ShopCardViewModel
            )
            // Rilisan Spesial ItemView
            initializeComponent(
                ComponentsList.ShopCardItemView,
                ::ShopCardItemViewHolder,
                ::ShopCardItemViewModel
            )

            initializeComponent(
                ComponentsList.Section,
                ::SectionViewHolder,
                ::SectionViewModel
            )

//            initializeComponent(ComponentsList.AnchorTabs,::AnchorTabsViewHolder,::AnchorTabsViewModel)

            initializeComponent(
                ComponentsList.AnchorTabsItem,
                ::AnchorTabsItemViewHolder,
                ::AnchorTabsItemViewModel
            )
            // Calendar View
            initializeComponent(
                ComponentsList.CalendarWidgetCarousel,
                ::CalendarWidgetCarouselViewHolder,
                ::CalendarWidgetCarouselViewModel
            )
            initializeComponent(
                ComponentsList.CalendarWidgetGrid,
                ::CalendarWidgetGridViewHolder,
                ::CalendarWidgetGridViewModel
            )
            initializeComponent(
                ComponentsList.CalendarWidgetItem,
                ::CalendarWidgetItemViewHolder,
                ::CalendarWidgetItemViewModel
            )
            initializeComponent(
                ComponentsList.ShimmerCalendarWidget,
                ::ShimmerCalendarViewHolder,
                ::ShimmerViewModel
            )
            initializeComponent(
                ComponentsList.TopQuestWidget,
                ::TopQuestViewHolder,
                ::TopQuestViewModel
            )
            initializeComponent(ComponentsList.MyCoupon, ::MyCouponViewHolder, ::MyCouponViewModel)
            initializeComponent(
                ComponentsList.MyCouponItem,
                ::MyCouponItemViewHolder,
                ::MyCouponItemViewModel
            )
            initializeComponent(
                ComponentsList.BannerInfinite,
                ::ShopBannerInfiniteViewHolder,
                ::ShopBannerInfiniteViewModel
            )
            initializeComponent(
                ComponentsList.BannerInfiniteItem,
                ::ShopBannerInfiniteItemViewHolder,
                ::ShopBannerInfiniteItemViewModel
            )
            initializeComponent(
                ComponentsList.ShopCardInfinite,
                ::ShopCardInfiniteViewHolder,
                ::ShopCardInfiniteViewModel
            )
            initializeComponent(
                ComponentsList.ExplicitWidget,
                ::ExplicitWidgetViewHolder,
                ::ExplicitWidgetViewModel
            )
            initializeComponent(
                ComponentsList.ProductBundling,
                ::ProductBundlingViewHolder,
                ::ProductBundlingViewModel
            )
            initializeComponent(
                ComponentsList.ContentCard,
                ::ContentCardViewHolder,
                ::ContentCardViewModel
            )
            initializeComponent(
                ComponentsList.ContentCardItem,
                ::ContentCardItemViewHolder,
                ::ContentCardItemViewModel
            )
            initializeComponent(
                ComponentsList.ProductHighlight,
                ::ProductHighlightViewHolder,
                ::ProductHighlightViewModel
            )

            initializeComponent(
                ComponentsList.ThematicHeader,
                ::ThematicHeaderViewHolder,
                ::ThematicHeaderViewModel
            )
        }

        private fun <E : AbstractViewHolder, T : DiscoveryBaseViewModel> initializeComponent(
            component: ComponentsList,
            componentViewHolder: (v: View, fragment: Fragment) -> E,
            componentViewModel: (application: Application, components: ComponentsItem, position: Int) -> T
        ) {
            componentIdMap[component.componentName] = component.ordinal
            componentMapper[component.ordinal] =
                ComponentHelpersHolder(componentViewHolder, componentViewModel)
        }

        fun getComponentId(viewType: String?): Int? {
            return componentIdMap[viewType]
        }

        fun createViewHolder(
            itemView: View,
            viewType: Int,
            fragment: Fragment
        ): AbstractViewHolder? {
            return componentMapper[viewType]?.getViewHolder(itemView, fragment)?.apply {
                uiWidgetComponent = (fragment as DiscoveryFragment).provideSubComponent()
            }
        }

        fun createViewModel(viewType: Int): (application: Application, components: ComponentsItem, position: Int) -> DiscoveryBaseViewModel {
            if (componentMapper[viewType] != null) {
                return componentMapper[viewType]!!.getComponentModels()
            }
            return ::ComingSoonViewModel
        }

        fun isStickyHeader(viewType: Int): Boolean {
            return viewType == ComponentsList.Tabs.ordinal || viewType == ComponentsList.AnchorTabs.ordinal
        }
    }
}

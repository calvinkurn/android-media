package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.app.Application
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
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
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner.CarouselBannerItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner.CarouselBannerItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner.CarouselBannerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner.CarouselBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation.CategoryNavigationItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation.CategoryNavigationItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation.CategoryNavigationViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation.CategoryNavigationViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips.NavigationChipsItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.DefaultComponentViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannedview.BannedViewViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips.NavigationChipsViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips.NavigationChipsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter.QuickFilterViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips.ChipsFilterItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips.ChipsFilterItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips.ChipsFilterViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips.ChipsFilterViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner.CircularSliderBannerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner.CircularSliderBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview.ComingSoonViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview.ComingSoonViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads.*
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory.DynamicCategoryItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory.DynamicCategoryItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory.DynamicCategoryViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory.DynamicCategoryViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatflashsaletimerwidget.LihatFlashSaleTimerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatflashsaletimerwidget.LihatFlashSaleTimerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua.LihatSemuaViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua.LihatSemuaViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore.LoadMoreViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore.LoadMoreViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget.DiscoveryPlayWidgetViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget.DiscoveryPlayWidgetViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate.EmptyStateViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate.EmptyStateViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp.ProductCardRevampViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp.ProductCardRevampViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickcoupon.QuickCouponViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickcoupon.QuickCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter.QuickFilterViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.saleendstates.SaleEndStateViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.saleendstates.SaleEndStateViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer.ShimmerProductCardViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer.ShimmerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer.ShimmerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.spacing.SpacingViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.spacing.SpacingViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.textcomponent.TextComponentViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.textcomponent.TextComponentViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale.TimerSprintSaleItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale.TimerSprintSaleItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints.TokopointsItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints.TokopointsItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints.TokopointsViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints.TokopointsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.YouTubeViewViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.YoutubeViewViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class DiscoveryHomeFactory {

    companion object {
        private val componentIdMap = mutableMapOf<String, Int>()
        private val componentMapper = mutableMapOf<Int, ComponentHelpersHolder<out AbstractViewHolder, out DiscoveryBaseViewModel>>()

        init {
            initializeComponent(ComponentsList.SingleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            initializeComponent(ComponentsList.DoubleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            initializeComponent(ComponentsList.TripleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            initializeComponent(ComponentsList.QuadrupleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            initializeComponent(ComponentsList.YouTubeView, ::YoutubeViewViewHolder, ::YouTubeViewViewModel)
            initializeComponent(ComponentsList.CategoryNavigation, ::CategoryNavigationViewHolder, ::CategoryNavigationViewModel)
            initializeComponent(ComponentsList.CategoryNavigationIem, ::CategoryNavigationItemViewHolder, ::CategoryNavigationItemViewModel)
            initializeComponent(ComponentsList.BannerTimer, ::BannerTimerViewHolder, ::BannerTimerViewModel)
            initializeComponent(ComponentsList.LihatSemua, ::LihatSemuaViewHolder, ::LihatSemuaViewModel)
            initializeComponent(ComponentsList.CpmTopAds, ::CpmTopAdsViewHolder, ::CpmTopAdsViewModel)
            initializeComponent(ComponentsList.ChipsFilterView, ::ComingSoonViewHolder, ::ComingSoonViewModel)
//            initializeComponent(ComponentsList.SliderBanner, ::SliderBannerViewHolder, ::SliderBannerViewModel)
            initializeComponent(ComponentsList.SliderBanner, ::CircularSliderBannerViewHolder, ::CircularSliderBannerViewModel)
            initializeComponent(ComponentsList.Notifier, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.CarouselBanner, ::CarouselBannerViewHolder, ::CarouselBannerViewModel)
            initializeComponent(ComponentsList.CarouselBannerItemView, ::CarouselBannerItemViewHolder, ::CarouselBannerItemViewModel)
            initializeComponent(ComponentsList.TitleImage, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.TextComponent, ::TextComponentViewHolder, ::TextComponentViewModel)
            initializeComponent(ComponentsList.ClaimCoupon, ::ClaimCouponViewHolder, ::ClaimCouponViewModel)
            initializeComponent(ComponentsList.ClaimCouponItem, ::ClaimCouponItemViewHolder, ::ClaimCouponItemViewModel)
            initializeComponent(ComponentsList.BrandRecommendation, ::BrandRecommendationViewHolder, ::BrandRecommendationViewModel)
            initializeComponent(ComponentsList.BrandRecommendationItem, ::BrandRecommendationItemViewHolder, ::BrandRecommendationItemViewModel)
            initializeComponent(ComponentsList.Default, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.Tokopoints, ::TokopointsViewHolder, ::TokopointsViewModel)
            initializeComponent(ComponentsList.TokopointsItem, ::TokopointsItemViewHolder, ::TokopointsItemViewModel)
            initializeComponent(ComponentsList.Spacing, ::SpacingViewHolder, ::SpacingViewModel)
            initializeComponent(ComponentsList.Tabs, ::TabsViewHolder, ::TabsViewModel)
            initializeComponent(ComponentsList.TabsItem, ::TabsItemViewHolder, ::TabsItemViewModel)
            initializeComponent(ComponentsList.ChipsFilter, ::ChipsFilterViewHolder, ::ChipsFilterViewModel)
            initializeComponent(ComponentsList.ChipsFilterItem, ::ChipsFilterItemViewHolder, ::ChipsFilterItemViewModel)
            initializeComponent(ComponentsList.DynamicCategory, ::DynamicCategoryViewHolder, ::DynamicCategoryViewModel)
            initializeComponent(ComponentsList.DynamicCategoryItem, ::DynamicCategoryItemViewHolder, ::DynamicCategoryItemViewModel)
            initializeComponent(ComponentsList.LihatFlashSaleTimer, ::LihatFlashSaleTimerViewHolder, ::LihatFlashSaleTimerViewModel)
            initializeComponent(ComponentsList.TimerSprintSale, ::TimerSprintSaleItemViewHolder, ::TimerSprintSaleItemViewModel)
            initializeComponent(ComponentsList.Shimmer, ::ShimmerViewHolder, ::ShimmerViewModel)
            initializeComponent(ComponentsList.ShimmerProductCard, ::ShimmerProductCardViewHolder, ::ShimmerViewModel)
            initializeComponent(ComponentsList.LoadMore, ::LoadMoreViewHolder, ::LoadMoreViewModel)
            initializeComponent(ComponentsList.QuickCoupon, ::QuickCouponViewHolder, ::QuickCouponViewModel)
            initializeComponent(ComponentsList.BannerCarousel, ::BannerCarouselViewHolder, ::BannerCarouselViewModel)
            initializeComponent(ComponentsList.BannerCarouselItemView, ::BannerCarouselItemViewHolder, ::BannerCarouselItemViewModel)

            // Product Card Revamp
            initializeComponent(ComponentsList.ProductCardRevamp, ::ProductCardRevampViewHolder, ::ProductCardRevampViewModel)
            initializeComponent(ComponentsList.ProductCardRevampItem, ::MasterProductCardItemViewHolder, ::MasterProductCardItemViewModel)
            initializeComponent(ComponentsList.MasterProductCardItemList, ::MasterProductCardItemViewHolder, ::MasterProductCardItemViewModel)

            // Product Card Horizontal Carousel
            initializeComponent(ComponentsList.ProductCardCarousel, ::ProductCardCarouselViewHolder, ::ProductCardCarouselViewModel)
            initializeComponent(ComponentsList.ProductCardCarouselItem, ::MasterProductCardItemViewHolder, ::MasterProductCardItemViewModel)

            // Product Card Sprint Sale
            initializeComponent(ComponentsList.ProductCardSprintSale, ::ProductCardRevampViewHolder, ::ProductCardRevampViewModel)
            initializeComponent(ComponentsList.ProductCardSprintSaleItem, ::MasterProductCardItemViewHolder, ::MasterProductCardItemViewModel)

            // Product Card Horizontal Sprint Sale
            initializeComponent(ComponentsList.ProductCardSprintSaleCarousel, ::ProductCardCarouselViewHolder, ::ProductCardCarouselViewModel)
            initializeComponent(ComponentsList.ProductCardSprintSaleCarouselItem, ::MasterProductCardItemViewHolder, ::MasterProductCardItemViewModel)

            initializeComponent(ComponentsList.ProductListEmptyState, ::EmptyStateViewHolder, ::EmptyStateViewModel)
            initializeComponent(ComponentsList.SaleEndState, ::SaleEndStateViewHolder, ::SaleEndStateViewModel)

            //Quick Filter
            initializeComponent(ComponentsList.QuickFilter, ::QuickFilterViewHolder, ::QuickFilterViewModel)

            //Navigation Chips
            initializeComponent(ComponentsList.NavigationChips, ::NavigationChipsViewHolder, ::NavigationChipsViewModel)
            initializeComponent(ComponentsList.NavigationCHipsItem, ::NavigationChipsItemViewHolder, ::DefaultComponentViewModel)

            //Banned View
            initializeComponent(ComponentsList.BannedView, ::BannedViewViewHolder, ::DefaultComponentViewModel)

            //Play Widget
            initializeComponent(ComponentsList.DiscoPlayWidgetView, ::DiscoveryPlayWidgetViewHolder, ::DiscoveryPlayWidgetViewModel)


        }

        private fun <E : AbstractViewHolder, T : DiscoveryBaseViewModel> initializeComponent(component: ComponentsList, componentViewHolder:(v: View, fragment: Fragment) -> E,
                                                                                             componentViewModel:(application: Application, components: ComponentsItem, position: Int)->T) {
            componentIdMap[component.componentName] = component.ordinal
            componentMapper[component.ordinal] = ComponentHelpersHolder(componentViewHolder, componentViewModel)
        }

        fun getComponentId(viewType: String?): Int? {
            return componentIdMap[viewType]
        }


        fun createViewHolder(itemView: View, viewType: Int, fragment: Fragment): AbstractViewHolder? {
            return componentMapper[viewType]?.getViewHolder(itemView, fragment)
        }

        fun createViewModel(viewType: Int): (application: Application,  components: ComponentsItem, position: Int)->DiscoveryBaseViewModel {
            if (componentMapper[viewType] != null) {
                return componentMapper[viewType]!!.getComponentModels()
            }
            return ::ComingSoonViewModel
        }

        fun isStickyHeader(viewType:Int):Boolean {
            return viewType == ComponentsList.Tabs.ordinal
        }
    }
}
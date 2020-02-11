package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.MultiBannerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.BannerTimerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.BannerTimerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview.ComingSoonViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview.ComingSoonViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads.*
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import kotlin.reflect.KFunction

class DiscoveryHomeFactory {

    companion object {
        private val componentIdMap = mutableMapOf<String, Int>()
        private val componentMapper = mutableMapOf<Int, ComponentHelpersHolder<out AbstractViewHolder, out DiscoveryBaseViewModel>>()

        init {
            initializeComponent(ComponentsList.SingleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            initializeComponent(ComponentsList.DoubleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            initializeComponent(ComponentsList.TripleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            initializeComponent(ComponentsList.QuadrupleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            initializeComponent(ComponentsList.YouTubeView, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.BannerTimer, ::BannerTimerViewHolder, ::BannerTimerViewModel)
            initializeComponent(ComponentsList.CpmTopAds, ::CpmTopAdsViewHolder, ::CpmTopAdsViewModel)
            initializeComponent(ComponentsList.CpmTopAdsItem, ::CpmTopadsShopItemViewHolder, ::CpmTopadsShopItemViewModel)
            initializeComponent(ComponentsList.CpmTopAdsProductItem, ::CpmTopadsProductItemViewHolder, ::CpmTopadsProductItemViewModel)
            initializeComponent(ComponentsList.ChipsFilterView, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.HeaderDesktopView, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.ShareEmpty, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.LihatSemuaView, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.SliderBanner, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.Notifier, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.CarouselBanner, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.TitleImage, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.TextComponent, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.ClaimCoupon, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.ProductCardCarousel, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.BrandRecommendation, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.BrandRecommendation, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.Margin, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.HorizontalCaregoryNavigation, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.CustomTopChat, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.Tabs, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.ProductCardRevamp, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.BreadCrumbs, ::ComingSoonViewHolder, ::ComingSoonViewModel)
            initializeComponent(ComponentsList.Default, ::ComingSoonViewHolder, ::ComingSoonViewModel)
        }

        private fun <E : AbstractViewHolder, T : DiscoveryBaseViewModel> initializeComponent(component: ComponentsList, viewModel: KFunction<E>, componentViewModel: KFunction<T>) {
            componentIdMap[component.componentName] = component.ordinal
            componentMapper[component.ordinal] = ComponentHelpersHolder(viewModel, componentViewModel);
        }

        fun getComponentId(viewType: String?): Int? {
            return componentIdMap[viewType]
        }


        fun createViewHolder(parent: ViewGroup, viewType: Int, fragment: Fragment): AbstractViewHolder? {
            val itemView: View =
                    LayoutInflater.from(parent.context).inflate(ComponentsList.values()[viewType].id, parent, false);
            return componentMapper[viewType]?.getViewHolder(itemView, fragment)
        }

        fun createViewModel(viewType: Int): KFunction<DiscoveryBaseViewModel> {
            if (componentMapper[viewType] != null) {
                return componentMapper[viewType]!!.getComponentModels()
            }
            return ::ComingSoonViewModel
        }
    }
}
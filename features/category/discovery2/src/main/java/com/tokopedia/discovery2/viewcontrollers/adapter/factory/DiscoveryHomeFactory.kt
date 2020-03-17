package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.MultiBannerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.YouTubeViewViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.YoutubeViewViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads.*
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.BannerTimerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.BannerTimerViewModel
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
            initializeComponent(ComponentsList.YouTubeView, ::YoutubeViewViewHolder, ::YouTubeViewViewModel)
            initializeComponent(ComponentsList.BannerTimer, ::BannerTimerViewHolder, ::BannerTimerViewModel)
            initializeComponent(ComponentsList.CpmTopAds, ::CpmTopAdsViewHolder, ::CpmTopAdsViewModel)
            initializeComponent(ComponentsList.CpmTopAdsItem, ::CpmTopadsShopItemViewHolder, ::CpmTopadsShopItemViewModel)
            initializeComponent(ComponentsList.CpmTopAdsProductItem, ::CpmTopadsProductItemViewHolder, ::CpmTopadsProductItemViewModel)
        }

        private fun <E : AbstractViewHolder, T : DiscoveryBaseViewModel> initializeComponent(component: ComponentsList, viewModel: KFunction<E>,componentViewModel: KFunction<T>) {
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
            if(componentMapper[viewType] != null){
                return componentMapper[viewType]!!.getComponentModels()
            }
            return ::MultiBannerViewModel
        }
    }
}
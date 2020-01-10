package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.MultiBannerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.MultiBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlin.reflect.KFunction

class DiscoveryHomeFactory {

    companion object {
        private val componentIdMap = mutableMapOf<String, Int>()
        private val componentMapper = mutableMapOf<Int, ComponentHelpersHolder<*, *>>()

        init {
            intializeComponent(ComponentsList.SingleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            intializeComponent(ComponentsList.DoubleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            intializeComponent(ComponentsList.TripleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
            intializeComponent(ComponentsList.QuadrupleBanner, ::MultiBannerViewHolder, ::MultiBannerViewModel)
        }

        private fun <E : AbstractViewHolder, T : DiscoveryBaseViewModel> intializeComponent(component: ComponentsList, viewModel: KFunction<E>,
                                                                                                            componentViewModel: KFunction<T>) {
            componentIdMap[component.componentName] = component.ordinal
            componentMapper[component.ordinal] = ComponentHelpersHolder(viewModel, componentViewModel);
        }

        fun getComponentId(viewType: String?): Int? {
            return componentIdMap[viewType]
        }


        fun createViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder? {
            val itemView: View =
                    LayoutInflater.from(parent.context).inflate(ComponentsList.values()[viewType].id, parent, false);
            return componentMapper[viewType]?.getViewHolder(itemView)
        }

        fun createViewModel(viewType: Int): KFunction<DiscoveryBaseViewModel>? {
            return componentMapper.get(viewType)?.getComponentModels()
        }
    }
}
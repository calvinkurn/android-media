package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.View
import com.tokopedia.discovery2.data.ComponentOneDataModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryVisitable
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.ComponentOneViewHolder
import kotlin.reflect.KFunction

class DiscoveryHomeFactory {

    companion object {
        private val componentIdMap = mutableMapOf<String, Int>()
        private val componentMapper = mutableMapOf<Int, ComponentHelpersHolder<*, *>>()


        init {
            intializeComponent(ComponentsList.Banner, ::ComponentOneDataModel, ::ComponentOneViewHolder)
        }


        private fun <T : DiscoveryVisitable, E : AbstractViewHolder<T>> intializeComponent(component: ComponentsList, dataModel: () -> T, viewModel: KFunction<E>) {
            componentIdMap[component.nam] = component.id;
            componentMapper[component.id] = ComponentHelpersHolder(dataModel, viewModel);
        }

        fun getComponentId(viewType: String): Int? {
            return componentIdMap.get(viewType)
        }

        fun createDataModel(viewType: Int): DiscoveryVisitable? {
            return componentMapper[viewType]?.getDataModel()
        }

        fun createViewHolder(viewType: Int, view: View): AbstractViewHolder<*>? {
            return componentMapper[viewType]?.getViewHolder(view)
        }

    }
}
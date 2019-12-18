package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.View
import com.tokopedia.discovery2.data.ComponentOneDataModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryVisitable
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

//class ComponentHelpersHolder<T : DiscoveryVisitable,E : AbstractViewHolder<T>>( f1 : ()-> T,f: () -> E)
class ComponentHelpersHolder<T : DiscoveryVisitable,E : AbstractViewHolder<T>> (val dataModel: ()->T,val viewModel:KFunction<E>) {

        fun getViewHolder(v: View) :E {
           return viewModel.call(v)
        }
    fun getDataModel() :T {
        return dataModel()
    }
}

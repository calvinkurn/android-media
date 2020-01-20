package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.View
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import kotlin.reflect.KFunction

class ComponentHelpersHolder<E : AbstractViewHolder, T : DiscoveryBaseViewModel>(val viewHolder: KFunction<E>,
                                                                                                 val componentModel: KFunction<T>) {

    fun getViewHolder(v: View): E {
        return viewHolder.call(v)
    }

    fun getComponentModels(): KFunction<T> {
        return componentModel
    }
}

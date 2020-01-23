package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import kotlin.reflect.KFunction

class ComponentHelpersHolder<E : AbstractViewHolder, T : DiscoveryBaseViewModel>(val viewHolder: KFunction<E>,
                                                                                                 val componentModel: KFunction<T>) {

    fun getViewHolder(v: View, fragment: Fragment): E {
        return viewHolder.call(v,fragment)
    }

    fun getComponentModels(): KFunction<T> {
        return componentModel
    }
}

package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.app.Application
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import kotlin.reflect.KFunction

class ComponentHelpersHolder<E : AbstractViewHolder, T : DiscoveryBaseViewModel>(val viewHolder:(v: View, fragment: Fragment) -> E,
                                                                                 val componentModel:(application: Application, components: ComponentsItem, position: Int)->T) {

    fun getViewHolder(v: View, fragment: Fragment): E {
        return viewHolder(v, fragment)
    }

    fun getComponentModels(): (application: Application,  components: ComponentsItem, position: Int)-> T {
        return componentModel
    }
}

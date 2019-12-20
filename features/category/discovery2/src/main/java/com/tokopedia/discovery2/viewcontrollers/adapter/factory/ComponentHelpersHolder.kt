package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.View
import com.tokopedia.discovery2.viewcontrollers.adapter.BaseDataModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import kotlin.reflect.KFunction

class ComponentHelpersHolder<T : BaseDataModel, E : AbstractViewHolder<T>>(val dataModel: () -> T, val viewModel: KFunction<E>) {

    fun getViewHolder(v: View): E {
        return viewModel.call(v)
    }

    fun getDataModel(): T {
        return dataModel()
    }
}

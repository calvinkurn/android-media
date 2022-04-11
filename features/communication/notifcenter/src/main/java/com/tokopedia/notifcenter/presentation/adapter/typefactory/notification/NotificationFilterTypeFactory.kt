package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.uimodel.filter.FilterLoadingUiModel
import com.tokopedia.notifcenter.data.uimodel.filter.FilterUiModel

interface NotificationFilterTypeFactory : AdapterTypeFactory {
    fun type(filterLoadingUiModel: FilterLoadingUiModel): Int
    fun type(filterUiModel: FilterUiModel): Int

    /**
     * use to pass interface implmented on adapter to viewholder
     * @param any can be used to pass several interfaces without the need
     * to call `this` multiple times
     */
    fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int, adapterListener: Any
    ): AbstractViewHolder<out Visitable<*>>
}
package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.uimodel.filter.FilterLoadingUiModel
import com.tokopedia.notifcenter.data.uimodel.filter.FilterUiModel
import com.tokopedia.notifcenter.presentation.adapter.viewholder.filter.FilterLoadingViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.filter.FilterViewHolder

class NotificationFilterTypeFactoryImpl(

) : BaseAdapterTypeFactory(), NotificationFilterTypeFactory {

    override fun type(filterLoadingUiModel: FilterLoadingUiModel): Int {
        return FilterLoadingViewHolder.LAYOUT
    }

    override fun type(filterUiModel: FilterUiModel): Int {
        return FilterViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
            adapterListener: Any
    ): AbstractViewHolder<out Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            FilterViewHolder.LAYOUT -> FilterViewHolder(
                    view, adapterListener as? FilterViewHolder.Listener
            )
            else -> createViewHolder(view, viewType)
        }
    }

    override fun createViewHolder(
            parent: View?, type: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FilterLoadingViewHolder.LAYOUT -> FilterLoadingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
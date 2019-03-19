package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemView

class BusinessWidgetTypeFactory(private val listener: BusinessUnitItemView) : BaseAdapterTypeFactory() {

    fun type(itemTab: HomeWidget.ContentItemTab): Int {
        return when (itemTab.templateId) {
            1 -> SizeSmallBusinessViewHolder.LAYOUT
            2 -> SizeMiddleBusinessViewHolder.LAYOUT
            3 -> SizeLargeBusinessViewHolder.LAYOUT
            else -> DefaultBusinessViewHolder.LAYOUT
        }
    }

    override fun type(viewModel: ErrorNetworkModel?): Int {
        return ErrorBusinessViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel?): Int {
        return DefaultBusinessViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SizeSmallBusinessViewHolder.LAYOUT -> SizeSmallBusinessViewHolder(parent)
            SizeMiddleBusinessViewHolder.LAYOUT -> SizeMiddleBusinessViewHolder(parent)
            SizeLargeBusinessViewHolder.LAYOUT -> SizeLargeBusinessViewHolder(parent)
            DefaultBusinessViewHolder.LAYOUT -> DefaultBusinessViewHolder(parent)
            ErrorBusinessViewHolder.LAYOUT -> ErrorBusinessViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}

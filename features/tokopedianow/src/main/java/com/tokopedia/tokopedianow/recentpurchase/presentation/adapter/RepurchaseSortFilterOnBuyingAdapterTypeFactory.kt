package com.tokopedia.tokopedianow.recentpurchase.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.*
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseSortFilterOnBuyingViewHolder

class RepurchaseSortFilterOnBuyingAdapterTypeFactory(val listener: RepurchaseSortFilterOnBuyingViewHolder.RepurchaseSortFilterOnBuyingViewHolderListener) : BaseAdapterTypeFactory(), RepurchaseSortFilterOnBuyingTypeFactory {

    override fun type(uiModel: RepurchaseSortFilterOnBuyingUiModel): Int = RepurchaseSortFilterOnBuyingViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            RepurchaseSortFilterOnBuyingViewHolder.LAYOUT -> RepurchaseSortFilterOnBuyingViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        }
    }
}
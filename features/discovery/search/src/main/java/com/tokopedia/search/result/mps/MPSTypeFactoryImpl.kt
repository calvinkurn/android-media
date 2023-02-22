package com.tokopedia.search.result.mps

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetViewHolder

class MPSTypeFactoryImpl: BaseAdapterTypeFactory(), MPSTypeFactory {

    override fun type(mpsShopWidgetDataView: MPSShopWidgetDataView): Int =
        MPSShopWidgetViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            MPSShopWidgetViewHolder.LAYOUT -> MPSShopWidgetViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}

package com.tokopedia.search.result.mps

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView

interface MPSTypeFactory {

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>

    fun type(mpsShopWidgetDataView: MPSShopWidgetDataView): Int
}

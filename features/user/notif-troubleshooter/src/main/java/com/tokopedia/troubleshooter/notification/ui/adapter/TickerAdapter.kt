package com.tokopedia.troubleshooter.notification.ui.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TickerItemFactory

internal open class TickerAdapter(
        factory: TickerItemFactory
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(factory) {

    fun addTickers(tickers: List<Visitable<*>>) {
        visitables.clear()
        visitables.addAll(tickers)
        notifyDataSetChanged()
    }

}
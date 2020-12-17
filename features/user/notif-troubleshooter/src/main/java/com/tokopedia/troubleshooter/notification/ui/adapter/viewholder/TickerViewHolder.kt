package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.adapter.TickerAdapter
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TickerItemFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView
import com.tokopedia.unifyprinciples.Typography

class TickerViewHolder(view: View): AbstractViewHolder<TickerUIView>(view) {

    private val txtTitle = view.findViewById<Typography>(R.id.txtTitle)
    private val lstTicker = view.findViewById<RecyclerView>(R.id.lstTicker)

    private val context by lazy { itemView.context }
    private var adapter: TickerAdapter? = null

    override fun bind(element: TickerUIView?) {
        if (element == null) return

        if (adapter == null) {
            adapter = TickerAdapter(TickerItemFactory())
            lstTicker?.layoutManager = LinearLayoutManager(context)
            lstTicker?.adapter = adapter
        }

        if (element.tickers.isNotEmpty()) {
            txtTitle?.show()
        }

        txtTitle?.text = context.getString(R.string.notif_ticker_title)
        adapter?.addTickers(element.tickers)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_layout_ticker
    }

}
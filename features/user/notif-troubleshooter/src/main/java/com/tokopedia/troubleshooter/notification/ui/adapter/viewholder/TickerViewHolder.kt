package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.databinding.ItemLayoutTickerBinding
import com.tokopedia.troubleshooter.notification.ui.adapter.TickerAdapter
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TickerItemFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView
import com.tokopedia.utils.view.binding.viewBinding

class TickerViewHolder(view: View): AbstractViewHolder<TickerUIView>(view) {

    private val binding: ItemLayoutTickerBinding? by viewBinding()
    private val context by lazy { itemView.context }
    private var adapter: TickerAdapter? = null

    override fun bind(element: TickerUIView?) {
        if (element == null) return

        if (adapter == null) {
            adapter = TickerAdapter(TickerItemFactory())
            binding?.lstTicker?.layoutManager = LinearLayoutManager(context)
            binding?.lstTicker?.adapter = adapter
        }

        if (element.tickers.isNotEmpty()) {
            binding?.txtTitle?.show()
        }

        binding?.txtTitle?.text = context.getString(R.string.notif_ticker_title)
        adapter?.addTickers(element.tickers)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_layout_ticker
    }

}
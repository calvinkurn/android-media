package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohTickerItemBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.util.UohConsts.TICKER_LABEL
import com.tokopedia.unifyorderhistory.util.UohConsts.TICKER_URL
import com.tokopedia.unifyorderhistory.util.UohUtils
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter
import java.net.URLDecoder

/**
 * Created by fwidjaja on 15/09/20.
 */
class UohTickerItemViewHolder(private val binding: UohTickerItemBinding, private val actionListener: UohItemAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UohTypeData) {
        if (item.dataObject is UohListOrder.UohOrders) {
            binding.tickerInfo.visible()

            if (item.dataObject.tickers.size > 1) {
                val listTickerData = arrayListOf<TickerData>()
                item.dataObject.tickers.forEach {
                    var desc = it.text
                    if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                        desc += " ${itemView.resources.getString(R.string.uoh_ticker_info_selengkapnya)
                            .replace(TICKER_URL, it.action.appUrl)
                            .replace(TICKER_LABEL, it.action.label)}"
                    }
                    listTickerData.add(TickerData(it.title, desc, UohUtils.getTickerType(it.text), true))
                }
                itemView.context?.let {
                    val adapter = TickerPagerAdapter(it, listTickerData)
                    adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                        override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                            actionListener?.onTickerDetailInfoClicked(linkUrl.toString())
                        }
                    })
                    binding.tickerInfo.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                        override fun onDismiss() {
                        }
                    })
                    binding.tickerInfo.addPagerView(adapter, listTickerData)
                }
            } else {
                item.dataObject.tickers.first().let { ticker ->
                    var desc = ticker.text
                    if (ticker.action.appUrl.isNotEmpty() && ticker.action.label.isNotEmpty()) {
                        desc += " ${itemView.resources.getString(R.string.uoh_ticker_info_selengkapnya)
                            .replace(TICKER_URL, URLDecoder.decode(ticker.action.appUrl, UohConsts.UTF_8))
                            .replace(TICKER_LABEL, ticker.action.label)}"
                    }

                    binding.tickerInfo.run {
                        tickerTitle = ticker.title
                        setHtmlDescription(desc)
                        tickerType = UohUtils.getTickerType(ticker.type)
                        setDescriptionClickEvent(object : TickerCallback {
                            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                actionListener?.onTickerDetailInfoClicked(linkUrl.toString())
                            }

                            override fun onDismiss() {
                            }
                        })
                    }
                }
            }
        }
    }
}

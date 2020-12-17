package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.BuyerConsts.TICKER_LABEL
import com.tokopedia.buyerorder.common.util.BuyerConsts.TICKER_URL
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohUtils
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import kotlinx.android.synthetic.main.uoh_ticker_item.view.*
import java.net.URLDecoder

/**
 * Created by fwidjaja on 15/09/20.
 */
class UohTickerItemViewHolder(itemView: View, private val actionListener: UohItemAdapter.ActionListener?) : UohItemAdapter.BaseViewHolder<UohTypeData>(itemView) {
    override fun bind(item: UohTypeData, position: Int) {
        if (item.dataObject is UohListOrder.Data.UohOrders) {
            itemView.ticker_info.visible()

            if (item.dataObject.tickers.size > 1) {
                val listTickerData = arrayListOf<TickerData>()
                item.dataObject.tickers.forEach {
                    var desc = it.text
                    if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                        desc += " ${itemView.resources.getString(R.string.buyer_ticker_info_selengkapnya)
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
                    itemView.ticker_info?.setDescriptionClickEvent(object: TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                        override fun onDismiss() {
                        }

                    })
                    itemView.ticker_info?.addPagerView(adapter, listTickerData)
                }
            } else {
                item.dataObject.tickers.first().let {
                    itemView.ticker_info?.tickerTitle = it.title
                    var desc = it.text
                    if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                        desc += " ${itemView.resources.getString(R.string.buyer_ticker_info_selengkapnya)
                                .replace(TICKER_URL, URLDecoder.decode(it.action.appUrl, UohConsts.UTF_8))
                                .replace(TICKER_LABEL, it.action.label)}"
                    }
                    itemView.ticker_info?.setHtmlDescription(desc)
                    itemView.ticker_info?.tickerType = UohUtils.getTickerType(it.type)
                    itemView.ticker_info?.setDescriptionClickEvent(object : TickerCallback {
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
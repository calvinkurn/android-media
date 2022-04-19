package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.databinding.WishlistV2TickerItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2TickerViewHolder(private val binding: WishlistV2TickerItemBinding, private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistV2TypeLayoutData) {
        binding.run {
            wishlistv2TickerMaxQty.apply {
                if (item.dataObject is WishlistV2Response.Data.WishlistV2.TickerState) {
                    visible()
                    when (item.dataObject.type) {
                        WishlistV2Consts.TICKER_TYPE_ANNOUNCEMENT -> {
                            tickerType = Ticker.TYPE_ANNOUNCEMENT
                            closeButtonVisibility = View.GONE
                        }
                        WishlistV2Consts.TICKER_TYPE_WARNING -> {
                            tickerType = Ticker.TYPE_WARNING
                            closeButtonVisibility = View.GONE
                        }
                        WishlistV2Consts.TICKER_TYPE_ERROR -> {
                            tickerType = Ticker.TYPE_ERROR
                            closeButtonVisibility = View.GONE
                        }
                    }

                    setHtmlDescription(item.dataObject.message + " <a href=\"cta_text\">" + item.dataObject.button.text + "</a>")
                    setDescriptionClickEvent(object: TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            when (item.dataObject.button.action) {
                                WishlistV2Consts.TICKER_CTA_OPEN_DELETE_BOTTOMSHEET -> {
                                    actionListener?.onTickerCTAShowBottomSheet()
                                }
                                WishlistV2Consts.TICKER_CTA_SORT_FROM_OLDEST -> {
                                    actionListener?.onTickerCTASortFromLatest()
                                }
                            }
                        }

                        override fun onDismiss() {}
                    })
                }
            }
        }
    }
}
package com.tokopedia.wishlist.detail.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.wishlist.detail.data.model.WishlistTickerCleanerData
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.util.WishlistConsts
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter
import com.tokopedia.wishlist.collection.analytics.WishlistCollectionAnalytics
import com.tokopedia.wishlist.databinding.WishlistTickerItemBinding

class WishlistTickerViewHolder(private val binding: WishlistTickerItemBinding, private val actionListener: WishlistAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistTypeLayoutData, hasClosed: Boolean, isShowCheckbox: Boolean) {
        if (isShowCheckbox || hasClosed) {
            binding.root.gone()
            val params = (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
                height = 0
                width = 0
            }
            binding.root.layoutParams = params
        } else {
            if (item.dataObject is WishlistTickerCleanerData) {
                binding.root.visible()
                val params = (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    isFullSpan = true
                }
                binding.root.layoutParams = params
                binding.wishlistv2TickerMaxQty.apply {
                    val tickerData = item.dataObject.tickerCleanerData
                    val bottomSheetCleanerData = item.dataObject.bottomSheetCleanerData
                    when (tickerData.type) {
                        WishlistConsts.TICKER_TYPE_ANNOUNCEMENT -> {
                            tickerType = Ticker.TYPE_ANNOUNCEMENT
                            closeButtonVisibility = View.VISIBLE
                        }
                        WishlistConsts.TICKER_TYPE_WARNING -> {
                            tickerType = Ticker.TYPE_WARNING
                            closeButtonVisibility = View.VISIBLE
                            WishlistCollectionAnalytics.sendViewMaxQtyTickerOnWishlistPageEvent(false)
                        }
                        WishlistConsts.TICKER_TYPE_ERROR -> {
                            tickerType = Ticker.TYPE_ERROR
                            closeButtonVisibility = View.GONE
                            WishlistCollectionAnalytics.sendViewMaxQtyTickerOnWishlistPageEvent(true)
                        }
                    }

                    setHtmlDescription(tickerData.message + " <a href=\"cta_text\">" + tickerData.button.text + "</a>")
                    setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            when (tickerData.button.action) {
                                WishlistConsts.TICKER_CTA_OPEN_DELETE_BOTTOMSHEET -> {
                                    actionListener?.onTickerCTAShowBottomSheet(bottomSheetCleanerData)
                                    WishlistCollectionAnalytics.sendClickLihatButtonOnMaxQtyTickerOnWishlistPageEvent(true)
                                }
                                WishlistConsts.TICKER_CTA_SORT_FROM_OLDEST -> {
                                    actionListener?.onTickerCTASortFromLatest()
                                    WishlistCollectionAnalytics.sendClickLihatButtonOnMaxQtyTickerOnWishlistPageEvent(false)
                                }
                            }
                        }

                        override fun onDismiss() {
                            actionListener?.onTickerCloseIconClicked()
                        }
                    })
                }
            }
        }
    }
}

package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.wishlist.data.model.WishlistV2TickerCleanerData
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2TickerItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics

class WishlistV2TickerViewHolder(private val binding: WishlistV2TickerItemBinding, private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistV2TypeLayoutData, hasClosed: Boolean, isShowCheckbox: Boolean) {
        if (isShowCheckbox || hasClosed) {
            binding.root.gone()
            val params = (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
                height = 0
                width = 0
            }
            binding.root.layoutParams = params
        } else {
            if (item.dataObject is WishlistV2TickerCleanerData) {
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
                        WishlistV2Consts.TICKER_TYPE_ANNOUNCEMENT -> {
                            tickerType = Ticker.TYPE_ANNOUNCEMENT
                            closeButtonVisibility = View.VISIBLE
                        }
                        WishlistV2Consts.TICKER_TYPE_WARNING -> {
                            tickerType = Ticker.TYPE_WARNING
                            closeButtonVisibility = View.VISIBLE
                            WishlistCollectionAnalytics.sendViewMaxQtyTickerOnWishlistPageEvent(false)
                        }
                        WishlistV2Consts.TICKER_TYPE_ERROR -> {
                            tickerType = Ticker.TYPE_ERROR
                            closeButtonVisibility = View.GONE
                            WishlistCollectionAnalytics.sendViewMaxQtyTickerOnWishlistPageEvent(true)
                        }
                    }

                    setHtmlDescription(tickerData.message + " <a href=\"cta_text\">" + tickerData.button.text + "</a>")
                    setDescriptionClickEvent(object: TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            when (tickerData.button.action) {
                                WishlistV2Consts.TICKER_CTA_OPEN_DELETE_BOTTOMSHEET -> {
                                    actionListener?.onTickerCTAShowBottomSheet(bottomSheetCleanerData)
                                    WishlistCollectionAnalytics.sendClickLihatButtonOnMaxQtyTickerOnWishlistPageEvent(true)
                                }
                                WishlistV2Consts.TICKER_CTA_SORT_FROM_OLDEST -> {
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
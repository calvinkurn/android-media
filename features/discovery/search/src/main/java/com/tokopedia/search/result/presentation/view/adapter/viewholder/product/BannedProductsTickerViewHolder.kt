package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BannedProductsTickerViewModel
import com.tokopedia.search.result.presentation.view.listener.BannedProductsRedirectToBrowserListener
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.search_result_banned_products_ticker_layout.view.*

class BannedProductsTickerViewHolder(
        itemView: View,
        private val bannedProductsRedirectToBrowserListener: BannedProductsRedirectToBrowserListener
): AbstractViewHolder<BannedProductsTickerViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_banned_products_ticker_layout
    }

    override fun bind(element: BannedProductsTickerViewModel?) {
        if (element == null) return

        itemView.searchResultBannedProductsTicker?.visibility = View.VISIBLE
        itemView.searchResultBannedProductsTicker?.setHtmlDescription(element.htmlErrorMessage)
        itemView.searchResultBannedProductsTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                bannedProductsRedirectToBrowserListener.onGoToBrowserClicked(false, linkUrl.toString())
            }

            override fun onDismiss() {}
        })
    }
}
package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BannedProductsTickerDataView
import kotlinx.android.synthetic.main.search_result_banned_products_ticker_layout.view.*

class BannedProductsTickerViewHolder(itemView: View): AbstractViewHolder<BannedProductsTickerDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_banned_products_ticker_layout
    }

    override fun bind(element: BannedProductsTickerDataView?) {
        if (element == null) return

        itemView.searchResultBannedProductsTicker?.visibility = View.VISIBLE
        itemView.searchResultBannedProductsTicker?.setHtmlDescription(element.htmlErrorMessage)
    }
}
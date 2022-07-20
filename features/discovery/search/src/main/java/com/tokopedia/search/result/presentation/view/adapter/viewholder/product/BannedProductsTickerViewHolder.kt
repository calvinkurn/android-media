package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultBannedProductsTickerLayoutBinding
import com.tokopedia.search.result.presentation.model.BannedProductsTickerDataView
import com.tokopedia.utils.view.binding.viewBinding

class BannedProductsTickerViewHolder(itemView: View): AbstractViewHolder<BannedProductsTickerDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_banned_products_ticker_layout
    }

    private var binding: SearchResultBannedProductsTickerLayoutBinding? by viewBinding()

    override fun bind(element: BannedProductsTickerDataView?) {
        if (element == null) return

        binding?.searchResultBannedProductsTicker?.let {
            it.visibility = View.VISIBLE
            it.setHtmlDescription(element.htmlErrorMessage)
        }
    }
}
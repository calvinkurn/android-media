package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.TobaccoTickerViewModel
import com.tokopedia.search.result.presentation.view.listener.TobaccoRedirectToBrowserListener
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.search_result_tobacco_ticker_layout.view.*

class TobaccoTickerViewHolder(
        itemView: View,
        private val tobaccoRedirectToBrowserListener: TobaccoRedirectToBrowserListener
): AbstractViewHolder<TobaccoTickerViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_tobacco_ticker_layout
    }

    override fun bind(element: TobaccoTickerViewModel?) {
        if (element == null) return

        itemView.searchResultTobaccoTicker?.visibility = View.VISIBLE
        itemView.searchResultTobaccoTicker?.setHtmlDescription(element.htmlErrorMessage)
        itemView.searchResultTobaccoTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                tobaccoRedirectToBrowserListener.onGoToBrowserClicked(linkUrl.toString())
            }

            override fun onDismiss() {}
        })
    }
}
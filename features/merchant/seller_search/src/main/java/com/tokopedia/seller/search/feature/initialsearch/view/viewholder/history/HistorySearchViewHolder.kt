package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import kotlinx.android.synthetic.main.initial_search_with_history.view.*

class HistorySearchViewHolder(itemView: View,
                              private val historySearchListener: HistorySearchListener): AbstractViewHolder<SellerSearchUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.initial_search_with_history
    }

    private val adapterHistory by lazy { ItemHistorySearchAdapter(historySearchListener) }

    override fun bind(element: SellerSearchUiModel) {
        with(itemView) {
            tvLatestSearch?.text = element.title
            rvInitialHistory?.apply {
                layoutManager = LinearLayoutManager(itemView.context)
                adapter = adapterHistory
            }
            tvClearAll.setOnClickListener {
                historySearchListener.onClearAllSearch()
            }
        }

        if(element.sellerSearchList.isNotEmpty()) {
            adapterHistory.submitList(element.sellerSearchList)
        }
    }
}
package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.InitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import kotlinx.android.synthetic.main.initial_search_with_history.view.*

class HistorySearchViewHolder(private val view: View,
                              private val historySearchListener: HistorySearchListener) :
        AbstractViewHolder<InitialSearchUiModel>(view) {

    companion object {
        val LAYOUT_RES = R.layout.initial_search_with_history
    }

    private val adapterHistory by lazy { ItemHistorySearchAdapter(historySearchListener) }

    override fun bind(element: InitialSearchUiModel) {
        with(view) {
            rvInitialHistory?.apply {
                layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                adapter = adapterHistory
            }
            tvClearAll?.setOnClickListener {
                historySearchListener.onClearAllSearch()
            }
        }

        if (element.sellerSearchList.isNotEmpty()) {
            adapterHistory.submitList(element.sellerSearchList)
        }
    }
}
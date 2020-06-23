package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import kotlinx.android.synthetic.main.item_initial_search_with_history.view.*

class ItemHistorySearchViewHolder(
        private val itemViewHistory: View,
        private val historySearchListener: HistorySearchListener
) : RecyclerView.ViewHolder(itemViewHistory) {

    fun bind(itemSellerSearchUiModel: ItemInitialSearchUiModel) {
        itemViewHistory.tvTitleHistory?.text = itemSellerSearchUiModel.title

        itemViewHistory.ivCloseHistory?.setOnClickListener {
            historySearchListener.onClearSearchItem(itemSellerSearchUiModel.title.orEmpty(), adapterPosition)
        }
        itemViewHistory.setOnClickListener {
            historySearchListener.onHistoryItemClicked(itemSellerSearchUiModel.appUrl.orEmpty())
        }
    }
}
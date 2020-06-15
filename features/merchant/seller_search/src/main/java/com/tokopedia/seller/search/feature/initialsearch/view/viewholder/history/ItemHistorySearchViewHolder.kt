package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import kotlinx.android.synthetic.main.item_initial_search_with_history.view.*

class ItemHistorySearchViewHolder(
        itemView: View,
        private val historySearchListener: HistorySearchListener?
): RecyclerView.ViewHolder(itemView) {

    fun bind(itemSellerSearchUiModel: ItemSellerSearchUiModel) {
        with(itemView) {
            tvTitleHistory?.text = itemSellerSearchUiModel.title.orEmpty()
            ivCloseHistory?.setOnClickListener {
                historySearchListener?.onClearSearchItem(itemSellerSearchUiModel.title.orEmpty())
            }
            setOnClickListener {
                historySearchListener?.onHistoryItemClicked(itemSellerSearchUiModel.appUrl.orEmpty())
            }
        }

    }
}
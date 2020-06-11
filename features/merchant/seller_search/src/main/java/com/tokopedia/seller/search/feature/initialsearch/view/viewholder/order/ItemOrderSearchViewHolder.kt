package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.order

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import kotlinx.android.synthetic.main.item_search_result_order.view.*

class ItemOrderSearchViewHolder(
        itemView: View
): RecyclerView.ViewHolder(itemView) {

    fun bind(itemSellerSearchUiModel: ItemSellerSearchUiModel) {
        with(itemView) {
            ivSearchResultOrder?.setImageUrl(itemSellerSearchUiModel.imageUrl.orEmpty())
            tvSearchResultOrderTitle?.text = itemSellerSearchUiModel.title
            tvSearchResultOrderDesc?.text = itemSellerSearchUiModel.desc
        }
    }
}
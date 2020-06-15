package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.navigation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.NavigationSearchListener
import kotlinx.android.synthetic.main.item_search_result_navigation.view.*

class ItemNavigationSearchViewHolder(
        itemView: View,
        private val navigationSearchListener: NavigationSearchListener?
) : RecyclerView.ViewHolder(itemView) {

    fun bind(itemSellerSearchUiModel: ItemSellerSearchUiModel) {
        with(itemView) {
            tvTitleSearchResultNav?.text = itemSellerSearchUiModel.title
            tvDescSearchResultNav?.text = itemSellerSearchUiModel.desc

            setOnClickListener {
                navigationSearchListener?.onNavigationItemClicked(itemSellerSearchUiModel, adapterPosition)
            }
        }
    }
}
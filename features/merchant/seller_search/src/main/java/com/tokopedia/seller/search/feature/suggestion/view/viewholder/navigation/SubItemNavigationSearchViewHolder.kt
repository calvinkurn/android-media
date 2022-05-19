package com.tokopedia.seller.search.feature.suggestion.view.viewholder.navigation

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.search.databinding.ItemSubSearchResultNavigationBinding
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchSubItemUiModel

/**
 * Created by @ilhamsuaib on 08/12/21.
 */

class SubItemNavigationSearchViewHolder(
    private val binding: ItemSubSearchResultNavigationBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: NavigationSellerSearchSubItemUiModel) {
        with(binding) {
            tvChipSearchSubItem.text = model.title
            chipSearchSubItemContainer.setOnClickListener {
                RouteManager.route(root.context, model.appLink)
            }
        }
    }
}
package com.tokopedia.seller.search.feature.suggestion.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.databinding.ItemSubSearchResultNavigationBinding
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchSubItemUiModel
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.navigation.SubItemNavigationSearchViewHolder

/**
 * Created by @ilhamsuaib on 08/12/21.
 */

class SubItemNavigationSearchAdapter(
    private val items: List<NavigationSellerSearchSubItemUiModel>
) : RecyclerView.Adapter<SubItemNavigationSearchViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubItemNavigationSearchViewHolder {
        val binding = ItemSubSearchResultNavigationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SubItemNavigationSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubItemNavigationSearchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
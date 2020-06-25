package com.tokopedia.seller.search.feature.suggestion.view.viewholder.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.NavigationSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ItemSellerSearchUiModel

class ItemNavigationSearchAdapter(private val navigationSearchListener: NavigationSearchListener):
        RecyclerView.Adapter<ItemNavigationSearchViewHolder>() {

    private var itemNavigationList: MutableList<ItemSellerSearchUiModel> = mutableListOf()

    fun setItemNavigationList(list: List<ItemSellerSearchUiModel>) {
        this.itemNavigationList = list.toMutableList()
        notifyDataSetChanged()
    }

    fun clearAllData() {
        this.itemNavigationList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNavigationSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result_navigation, parent, false)
        return ItemNavigationSearchViewHolder(view, navigationSearchListener)
    }

    override fun onBindViewHolder(holderSearch: ItemNavigationSearchViewHolder, position: Int) {
        holderSearch.bind(itemNavigationList[position])
    }

    override fun getItemCount(): Int {
        return itemNavigationList.size
    }
}
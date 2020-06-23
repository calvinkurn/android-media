package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.ProductSearchListener

class ItemProductSearchAdapter(private val productSearchListener: ProductSearchListener) :
        RecyclerView.Adapter<ItemProductSearchViewHolder>() {

    private var itemProductList: MutableList<ItemSellerSearchUiModel> = mutableListOf()

    fun setItemProductList(list: List<ItemSellerSearchUiModel>) {
        this.itemProductList = list.toMutableList()
        notifyDataSetChanged()
    }

    fun clearAllData() {
        this.itemProductList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result_product, parent, false)
        return ItemProductSearchViewHolder(view, productSearchListener)
    }

    override fun onBindViewHolder(holderSearch: ItemProductSearchViewHolder, position: Int) {
        holderSearch.bind(itemProductList[position])
    }

    override fun getItemCount(): Int {
        return itemProductList.size
    }
}
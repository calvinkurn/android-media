package com.tokopedia.search.result.presentation.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.shop.ShopProductItemViewHolder
import com.tokopedia.search.result.presentation.view.listener.ShopListener

class ShopProductItemAdapter(
        private val context: Context,
        private val shopProductItemList: List<ShopViewModel.ShopItem.ShopItemProduct>,
        private val shopListener: ShopListener
) : RecyclerView.Adapter<ShopProductItemViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ShopProductItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_result_shop_item_product_card, viewGroup, false)

        return ShopProductItemViewHolder(view, shopListener)
    }

    override fun getItemCount(): Int {
        return shopProductItemList.size
    }

    override fun onBindViewHolder(shopProductItemViewHolder: ShopProductItemViewHolder, position: Int) {
        shopProductItemViewHolder.bind(shopProductItemList[position])
    }
}
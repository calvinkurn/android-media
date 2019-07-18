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
        shopProductItemList: List<ShopViewModel.ShopItem.ShopItemProduct>,
        private val shopProductItemCount: Int,
        private val shopListener: ShopListener
) : RecyclerView.Adapter<ShopProductItemViewHolder>() {

    private val mutableShopProductItemList = shopProductItemList.toMutableList()

    init {
        addOrRemoveProductItemBasedOnCount()
    }

    private fun addOrRemoveProductItemBasedOnCount() {
        if(mutableShopProductItemList.size < shopProductItemCount) {
            addDummyProductItemToList()
        }
        else {
            removeProductItemFromList()
        }
    }

    private fun addDummyProductItemToList() {
        var shopProductItemListSize = mutableShopProductItemList.size

        while(shopProductItemListSize < shopProductItemCount) {
            mutableShopProductItemList.add(ShopViewModel.ShopItem.ShopItemProduct())
            shopProductItemListSize++
        }
    }

    private fun removeProductItemFromList() {
        val productListDropCount = mutableShopProductItemList.size - shopProductItemCount
        mutableShopProductItemList.dropLast(productListDropCount)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ShopProductItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_result_shop_item_product_card, viewGroup, false)

        return ShopProductItemViewHolder(view, shopListener)
    }

    override fun getItemCount(): Int {
        return shopProductItemCount
    }

    override fun onBindViewHolder(shopProductItemViewHolder: ShopProductItemViewHolder, position: Int) {
        shopProductItemViewHolder.bind(mutableShopProductItemList[position])
    }
}
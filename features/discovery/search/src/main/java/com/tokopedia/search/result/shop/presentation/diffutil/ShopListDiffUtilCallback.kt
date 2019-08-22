package com.tokopedia.search.result.shop.presentation.diffutil

import android.support.v7.util.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel

class ShopListDiffUtilCallback(
        private val oldList: List<Visitable<*>> = listOf(),
        private val newList: List<Visitable<*>> = listOf()
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition >= oldList.size) return false
        if (newPosition >= newList.size) return false

        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        return if (oldItem is ShopViewModel.ShopItem && newItem is ShopViewModel.ShopItem)
            areShopItemsTheSame(oldItem, newItem)
        else
            oldItem::class == newItem::class
    }

    private fun areShopItemsTheSame(oldShopItem: ShopViewModel.ShopItem, newShopItem: ShopViewModel.ShopItem): Boolean {
        return oldShopItem.id == newShopItem.id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return true
    }
}
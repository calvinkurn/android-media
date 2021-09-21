package com.tokopedia.search.result.shop.presentation.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchDataView
import com.tokopedia.search.result.shop.presentation.model.ShopDataView

internal class ShopListDiffUtilCallback(
        private val oldList: List<Visitable<*>> = listOf(),
        private val newList: List<Visitable<*>> = listOf()
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition >= oldList.size) return false
        if (newPosition >= newList.size) return false

        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        return if (oldItem is ShopDataView.ShopItem && newItem is ShopDataView.ShopItem)
            areShopItemsTheSame(oldItem, newItem)
        else
            oldItem::class == newItem::class
    }

    private fun areShopItemsTheSame(oldShopDataItem: ShopDataView.ShopItem, newShopDataItem: ShopDataView.ShopItem): Boolean {
        return oldShopDataItem.id == newShopDataItem.id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition >= oldList.size) return false
        if (newPosition >= newList.size) return false

        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        return if (oldItem is ShopEmptySearchDataView && newItem is ShopEmptySearchDataView) oldItem == newItem
        else true
    }
}
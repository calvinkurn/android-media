package com.tokopedia.home_wishlist.view.custom

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.home_wishlist.model.datamodel.WishlistDataModel
import com.tokopedia.smart_recycler_helper.SmartVisitable

class WishlistCallback(
        private val oldList: List<SmartVisitable<*>> = listOf(),
        private val newList: List<SmartVisitable<*>> = listOf()
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition >= oldList.size) return false
        if (newPosition >= newList.size) return false

        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        return if (oldItem is WishlistDataModel && newItem is WishlistDataModel)
            areShopItemsTheSame(oldItem, newItem)
        else
            oldItem::class == newItem::class
    }

    private fun areShopItemsTheSame(oldShopItem: WishlistDataModel, newShopItem: WishlistDataModel): Boolean {
        return oldShopItem.getUniqueIdentity() == newShopItem.getUniqueIdentity()
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

        return if (oldItem is WishlistDataModel && newItem is WishlistDataModel) oldItem.equalsDataModel(newItem)
        else true
    }
}
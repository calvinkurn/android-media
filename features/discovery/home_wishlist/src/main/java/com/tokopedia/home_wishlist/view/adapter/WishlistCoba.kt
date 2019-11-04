package com.tokopedia.home_wishlist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartVisitable
import com.tokopedia.home_wishlist.model.datamodel.WishlistDataModel
import com.tokopedia.home_wishlist.view.listener.WishlistListener

class WishlistCoba(
    private val shopListTypeFactory: WishlistTypeFactory,
    private val listener: WishlistListener
): RecyclerView.Adapter<SmartAbstractViewHolder<SmartVisitable<*>>>() {

    private var list = ArrayList<SmartVisitable<*>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartAbstractViewHolder<SmartVisitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)

        @Suppress("UNCHECKED_CAST")
        return shopListTypeFactory.createViewHolder(view, viewType) as SmartAbstractViewHolder<SmartVisitable<*>>
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        @Suppress("UNCHECKED_CAST")
        val data = list[position] as Visitable<WishlistTypeFactory>

        return data.type(shopListTypeFactory)
    }

    override fun onBindViewHolder(holder: SmartAbstractViewHolder<SmartVisitable<*>>, position: Int) {
        val data = list[position]
        holder.bind(data, listener)
    }

    fun updateList(newList: List<SmartVisitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(WishlistCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }
}

internal class WishlistCallback(
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
package com.tokopedia.feedcomponent.helper

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by jegul on 2019-10-01.
 */
abstract class BaseAdapter<T: Any> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val delegatesManager = AdapterDelegatesManager<T>()
    protected val itemList: MutableList<T> = mutableListOf()

    @Deprecated(
            message = "Use getItems() instead",
            replaceWith = ReplaceWith("getItems()"),
            level = DeprecationLevel.WARNING
    )
    val data: List<T>
        get() = itemList

    val firstIndex: Int
        get() = if (itemList.isNotEmpty()) 0 else -1

    val lastIndex: Int
        get() = itemList.size - 1

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(itemList, position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return delegatesManager.onBindViewHolder(itemList, position, holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is BaseViewHolder) { holder.onViewRecycled() }
    }

    fun getItems(): List<T> = itemList

    fun getItems(position: Int): T = itemList[position]

    fun setItems(itemList: List<T>) {
        this.itemList.clear()
        addItems(itemList)
    }

    fun addItems(itemList: List<T>) {
        this.itemList.addAll(itemList)
    }

    fun addItem(item: T) {
        this.itemList.add(item)
    }

    fun addItem(position: Int, item: T) {
        this.itemList.add(position, item)
    }

    fun removeItem(item: T) {
        this.itemList.remove(item)
    }

    fun removeItemAt(position: Int) {
        this.itemList.removeAt(position)
    }

    fun clearAllItems() {
        this.itemList.clear()
    }

    /**
     * Needed to support easy migration from old Adapter
     */
    @Deprecated(
            message = "Use addItems(items) instead, and call notify as you wish",
            replaceWith = ReplaceWith("addItems(item)"),
            level = DeprecationLevel.WARNING
    )
    fun addElement(item: List<T>) {
        addItems(item)
        notifyDataSetChanged()
    }

    @Deprecated(
            message = "Use addItem(item) instead, and call notify as you wish",
            replaceWith = ReplaceWith("addItem(item)"),
            level = DeprecationLevel.WARNING
    )
    fun addElement(item: T) {
        addItem(item)
        notifyDataSetChanged()
    }

    @Deprecated(
            message = "Use clearAllItems() instead, and call notify as you wish",
            replaceWith = ReplaceWith("clearAllItems()"),
            level = DeprecationLevel.WARNING
    )
    fun clearAllElements() {
        clearAllItems()
        notifyDataSetChanged()
    }

    @Deprecated(
            message = "Use removeItem(item) instead, and call notify as you wish",
            replaceWith = ReplaceWith("removeItem(item)"),
            level = DeprecationLevel.WARNING
    )
    fun removeElement(item: T) {
        removeItem(item)
        notifyDataSetChanged()
    }

    @Deprecated(
            message = "Use addItems(items) instead, and call notify as you wish",
            replaceWith = ReplaceWith("addItems(items)"),
            level = DeprecationLevel.WARNING
    )
    fun addMoreData(itemList: List<T>) {
        val positionStart = itemList.size
        addItems(itemList)
        if (positionStart == 0) notifyDataSetChanged()
        else notifyItemRangeInserted(positionStart, itemList.size)
    }
}
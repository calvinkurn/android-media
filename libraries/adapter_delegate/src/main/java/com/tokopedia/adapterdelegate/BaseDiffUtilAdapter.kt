package com.tokopedia.adapterdelegate

import androidx.recyclerview.widget.DiffUtil


/**
 * Created by jegul on 2019-10-02.
 */
abstract class BaseDiffUtilAdapter<T: Any> : BaseAdapter<T>() {

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    inner class BaseDiffUtilCallback(
            private val oldItemList: List<T>,
            private val newItemList: List<T>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            return areItemsTheSame(oldItemList[oldPos], newItemList[newPos])
        }

        override fun getOldListSize(): Int {
            return oldItemList.size
        }

        override fun getNewListSize(): Int {
            return newItemList.size
        }

        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            return areContentsTheSame(oldItemList[oldPos], newItemList[newPos])
        }
    }

    fun addItemsAndAnimateChanges(itemList: List<T>) {
        animateNotifyChanged(
                oldItemList = this.itemList,
                newItemList = this.itemList + itemList,
                changeList = {
                    addItems(itemList)
                }
        )
    }

    fun setItemsAndAnimateChanges(itemList: List<T>) {
        animateNotifyChanged(
                oldItemList = this.itemList,
                newItemList = itemList,
                changeList = {
                    setItems(itemList)
                }
        )
    }

    fun addItemAndAnimateChanges(item: T) {
        addItem(item)
        notifyItemInserted(lastIndex)
    }

    fun clearAllItemsAndAnimateChanges() {
        animateNotifyChanged(
                oldItemList = this.itemList,
                newItemList = emptyList(),
                changeList = {
                    clearAllItems()
                }
        )
    }

    private inline fun animateNotifyChanged(oldItemList: List<T>, newItemList: List<T>, changeList: () -> Unit) {
        val diffCallback = BaseDiffUtilCallback(oldItemList, newItemList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        changeList()
        diffResult.dispatchUpdatesTo(this)
    }
}
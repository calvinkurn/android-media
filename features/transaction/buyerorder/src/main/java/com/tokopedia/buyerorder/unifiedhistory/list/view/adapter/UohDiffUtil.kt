package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData

/**
 * Created by fwidjaja on 1/9/21.
 */
class UohDiffUtil(private val oldList: List<UohTypeData>, private val newList: List<UohTypeData>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}
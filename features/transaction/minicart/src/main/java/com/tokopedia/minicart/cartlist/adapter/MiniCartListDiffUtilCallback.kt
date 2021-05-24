package com.tokopedia.minicart.cartlist.adapter

import androidx.recyclerview.widget.DiffUtil

class MiniCartListDiffUtilCallback(private val oldList: List<Any>,
                                   private val newList: List<Any>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Todo : add logic
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Todo : add logic
        return false
    }

}

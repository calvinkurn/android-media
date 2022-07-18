package com.tokopedia.feedcomponent.view.widget.shoprecom.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem

class ShopRecomDiffUtil(
    private val oldList: List<ShopRecomUiModelItem>,
    private val newList: List<ShopRecomUiModelItem>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}
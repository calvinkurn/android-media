package com.tokopedia.product_ar.view.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product_ar.model.ModifaceUiModel

class VariantArDiffutilCallback(
        private val oldList: List<ModifaceUiModel>,
        private val newList: List<ModifaceUiModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.productId == newItem.productId || oldItem.productName == newItem.productName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.modifaceProductData == newItem.modifaceProductData &&
                oldItem.backgroundUrl == newItem.backgroundUrl &&
                oldItem.isSelected == newItem.isSelected
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        val bundle = Bundle()

        if (oldItem.isSelected != newItem.isSelected) {
            bundle.putInt("asdf", 12345)
        }

        return bundle
    }
}
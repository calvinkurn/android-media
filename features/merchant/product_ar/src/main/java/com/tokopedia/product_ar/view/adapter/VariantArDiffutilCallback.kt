package com.tokopedia.product_ar.view.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product_ar.model.ModifaceUiModel

class VariantArDiffutilCallback(
        private val oldList: List<ModifaceUiModel>,
        private val newList: List<ModifaceUiModel>
) : DiffUtil.Callback() {

    companion object {
        const val BUNDLE_PAYLOAD_UPDATE_BORDER_KEY = "selected_border"
        const val BUNDLE_PAYLOAD_UPDATE_BORDER_VALUE = 12345
    }

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
        return oldItem.modifaceProductData.hashCode() == newItem.modifaceProductData.hashCode() &&
                oldItem.backgroundUrl == newItem.backgroundUrl &&
                oldItem.isSelected == newItem.isSelected &&
                oldItem.counter == newItem.counter
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        val bundle = Bundle()

        if (oldItem.isSelected != newItem.isSelected || oldItem.counter != newItem.counter) {
            bundle.putInt(BUNDLE_PAYLOAD_UPDATE_BORDER_KEY, BUNDLE_PAYLOAD_UPDATE_BORDER_VALUE)
        }

        return bundle
    }
}
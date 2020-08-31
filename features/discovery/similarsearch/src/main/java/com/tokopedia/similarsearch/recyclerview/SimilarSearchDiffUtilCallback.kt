package com.tokopedia.similarsearch.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.similarsearch.getsimilarproducts.model.Product

internal class SimilarSearchDiffUtilCallback(
        private val oldList: List<Any>,
        private val newList: List<Any>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is Product && newItem is Product)
            areSimilarProductItemTheSame(oldItem, newItem)
        else
            oldItem::class == newItem::class
    }

    private fun areSimilarProductItemTheSame(oldSimilarProductItem: Product, newSimilarProductItem: Product): Boolean {
        return oldSimilarProductItem.id == newSimilarProductItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is Product && newItem is Product)
            oldItem == newItem
        else
            true
    }
}
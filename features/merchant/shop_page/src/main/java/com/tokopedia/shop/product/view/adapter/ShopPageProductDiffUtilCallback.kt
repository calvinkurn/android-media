package com.tokopedia.shop.product.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.product.view.datamodel.*

class ShopPageProductDiffUtilCallback(
    private val oldItems: List<Visitable<*>>,
    private val newItems: List<Visitable<*>>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        if (isItemMatchWithUiModel<ShopProductChangeGridSectionUiModel>(oldItem, newItem)) {
            return false
        }
        if (isItemMatchWithUiModel<ShopProductSortFilterUiModel>(oldItem, newItem)) {
            return false
        }
        return oldItem == newItem
    }

    private inline fun <reified T> isItemMatchWithUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is T && newItem is T
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        if (isItemMatchWithUiModel<ShopProductUiModel>(oldItem, newItem)) {
            val oldData = oldItem as? ShopProductUiModel
            val newData = newItem as? ShopProductUiModel
            return oldData?.isNewData == false && newData?.isNewData == false
        }
        if (isItemMatchWithUiModel<ShopProductFeaturedUiModel>(oldItem, newItem)) {
            val oldData = oldItem as? ShopProductFeaturedUiModel
            val newData = newItem as? ShopProductFeaturedUiModel
            return oldData?.isNewData == false && newData?.isNewData == false
        }
        if (isItemMatchWithUiModel<ShopProductEtalaseHighlightUiModel>(oldItem, newItem)) {
            val oldData = oldItem as? ShopProductEtalaseHighlightUiModel
            val newData = newItem as? ShopProductEtalaseHighlightUiModel
            return oldData?.isNewData == false && newData?.isNewData == false
        }
        return oldItem == newItem
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }
}

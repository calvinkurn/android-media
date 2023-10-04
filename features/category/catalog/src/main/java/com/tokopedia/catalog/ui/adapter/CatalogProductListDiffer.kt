package com.tokopedia.catalog.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard.ProductCardModel

class CatalogProductListDiffer : DiffUtil.Callback() {

    private var oldProductList: List<ProductCardModel> = emptyList()
    private var newProductList: List<ProductCardModel> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldProductList[oldItemPosition]
        val newItem = newProductList[newItemPosition]

        val isTheSameObject = when {
            oldItem is ProductCardModel && newItem is ProductCardModel -> {
                isTheSameProduct(oldItem, newItem)
            }
            else -> false
        }

        return isTheSameObject || isTheSameItem(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldProductList[oldItemPosition] == newProductList[newItemPosition]
    }

    override fun getOldListSize() = oldProductList.size

    override fun getNewListSize() = newProductList.size



    private fun isTheSameProduct(oldItem: ProductCardModel, newItem: ProductCardModel): Boolean {
        return oldItem.productName == newItem.productName
    }
    private fun isTheSameItem(oldItem: ProductCardModel, newItem: ProductCardModel): Boolean {
        return oldItem == newItem
    }
}

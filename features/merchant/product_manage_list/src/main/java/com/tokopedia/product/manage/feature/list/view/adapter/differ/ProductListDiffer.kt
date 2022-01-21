package com.tokopedia.product.manage.feature.list.view.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.view.adapter.differ.ProductManageDiffer
import com.tokopedia.product.manage.feature.list.view.model.ProductManageEmptyModel

class ProductListDiffer: ProductManageDiffer() {

    private var oldProductList: List<Visitable<*>> = emptyList()
    private var newProductList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldProductList[oldItemPosition]
        val newItem = newProductList[newItemPosition]

        val isTheSameObject = when {
            oldItem is ProductUiModel && newItem is ProductUiModel -> {
                isTheSameProduct(oldItem, newItem)
            }
            oldItem is ProductManageEmptyModel && newItem is ProductManageEmptyModel -> {
                isTheSameEmptyModel(oldItem, newItem)
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

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): ProductManageDiffer {
        oldProductList = oldList
        newProductList= newList
        return this
    }

    private fun isTheSameProduct(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    private fun isTheSameEmptyModel(oldItem: ProductManageEmptyModel,
                                    newItem: ProductManageEmptyModel): Boolean {
        return oldItem.id == newItem.id
    }

    private fun isTheSameItem(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem == newItem
    }
}
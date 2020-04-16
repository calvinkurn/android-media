package com.tokopedia.product.manage.feature.list.view.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel

class ProductListDiffer(
    private val oldProductList: List<Visitable<*>>,
    private val newProductList: List<Visitable<*>>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldProductList[oldItemPosition]
        val newItem = newProductList[newItemPosition]

        return if(oldItem is ProductViewModel && newItem is ProductViewModel) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldProductList[oldItemPosition] == newProductList[newItemPosition]
    }

    override fun getOldListSize() = oldProductList.size

    override fun getNewListSize() = newProductList.size
}
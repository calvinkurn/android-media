package com.tokopedia.tokopedianow.searchcategory.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView

open class SearchCategoryDiffUtil: BaseTokopediaNowDiffer() {

    private var oldList: List<Visitable<*>> = listOf()
    private var newList: List<Visitable<*>> = listOf()

    override fun create(
            oldList: List<Visitable<*>>,
            newList: List<Visitable<*>>
    ): BaseTokopediaNowDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition !in oldList.indices) return false
        if (newItemPosition !in newList.indices) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is ProductItemDataView && newItem is ProductItemDataView)
            areProductItemTheSame(oldItem, newItem)
        else
            oldItem::class == newItem::class
    }

    private fun areProductItemTheSame(oldItem: ProductItemDataView, newItem: ProductItemDataView) =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size
}
package com.tokopedia.catalogcommon.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalogcommon.uimodel.BaseCatalogUiModel

class CatalogDifferImpl: CatalogDiffer() {

    private var oldWidget: List<Visitable<*>> = emptyList()
    private var newWidget: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldWidget[oldItemPosition]
        val newItem = newWidget[newItemPosition]

        val isTheSameObject = when {
            oldItem is BaseCatalogUiModel && newItem is BaseCatalogUiModel -> {
                isTheSameWidget(oldItem, newItem)
            }
            else -> false
        }

        return isTheSameObject || isTheSameItem(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldWidget[oldItemPosition] == newWidget[newItemPosition]
    }

    override fun getOldListSize() = oldWidget.size

    override fun getNewListSize() = newWidget.size

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): CatalogDiffer {
        oldWidget = oldList
        newWidget= newList
        return this
    }

    private fun isTheSameWidget(oldItem: BaseCatalogUiModel, newItem: BaseCatalogUiModel): Boolean {
        return oldItem.idWidget == newItem.idWidget
    }

    private fun isTheSameItem(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem == newItem
    }
}

package com.tokopedia.productcard_compact.similarproduct.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.SimilarProductUiModel

class SimilarProductDiffer : BaseTokopediaNowDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is SimilarProductUiModel && newItem is SimilarProductUiModel) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is SimilarProductUiModel && newItem is SimilarProductUiModel) {
            oldItem != newItem
        } else {
            super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): BaseTokopediaNowDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}

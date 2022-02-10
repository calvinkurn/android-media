package com.tokopedia.shop_widget.common.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop_widget.common.uimodel.ProductCardSeeAllUiModel
import com.tokopedia.shop_widget.common.uimodel.ProductCardUiModel

class ProductCardDiffer: DiffUtil.Callback() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    /**
     * @see areItemsTheSame
     *
     * This differ based on keyword of items
     **/
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is ProductCardUiModel && newItem is ProductCardUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is ProductCardSeeAllUiModel && newItem is ProductCardSeeAllUiModel) {
            oldItem.appLink == newItem.appLink
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): ProductCardDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}
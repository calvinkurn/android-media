package com.tokopedia.shop_widget.common.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop_widget.common.uimodel.ProductCardUiModel

class ProductCardDiffer: DiffUtil.Callback() {
    private var oldList: List<ProductCardUiModel> = emptyList()
    private var newList: List<ProductCardUiModel> = emptyList()

    /**
     * @see areItemsTheSame
     *
     * This differ based on keyword of items
     **/
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
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
        this.oldList = oldList as List<ProductCardUiModel>
        this.newList = newList as List<ProductCardUiModel>
        return this
    }
}
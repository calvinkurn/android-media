package com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.thematicwidget.ProductCardSeeAllUiModel

class ShopHomeThematicWidgetDiffUtil: DiffUtil.Callback() {
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

        return if (oldItem is ShopHomeProductUiModel && newItem is ShopHomeProductUiModel) {
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
    ): ShopHomeThematicWidgetDiffUtil {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}

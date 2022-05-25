package com.tokopedia.tokopedianow.home.presentation.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel

class HomeLeftCarouselAtcProductCardDiffer: DiffUtil.Callback() {
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

        return if (oldItem is HomeLeftCarouselAtcProductCardUiModel && newItem is HomeLeftCarouselAtcProductCardUiModel) {
            oldItem.id == newItem.id
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
    ): HomeLeftCarouselAtcProductCardDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}
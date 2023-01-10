package com.tokopedia.tokopedianow.common.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel

class TokoNowCategoryGridDiffer : BaseTokopediaNowDiffer() {
    private var oldList: List<TokoNowCategoryMenuItemUiModel> = emptyList()
    private var newList: List<TokoNowCategoryMenuItemUiModel> = emptyList()

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

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): BaseTokopediaNowDiffer {
        this.oldList = oldList as List<TokoNowCategoryMenuItemUiModel>
        this.newList = newList as List<TokoNowCategoryMenuItemUiModel>
        return this
    }
}

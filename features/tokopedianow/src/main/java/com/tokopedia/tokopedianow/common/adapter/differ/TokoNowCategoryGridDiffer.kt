package com.tokopedia.tokopedianow.common.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel

class TokoNowCategoryGridDiffer : BaseTokopediaNowDiffer() {
    private var oldList: List<TokoNowCategoryItemUiModel> = emptyList()
    private var newList: List<TokoNowCategoryItemUiModel> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.visitableId == newItem.visitableId
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
        this.oldList = oldList as List<TokoNowCategoryItemUiModel>
        this.newList = newList as List<TokoNowCategoryItemUiModel>
        return this
    }
}
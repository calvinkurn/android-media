package com.tokopedia.tokopedianow.category.presentation.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel

class CategoryDiffer : BaseTokopediaNowDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is CategoryShowcaseUiModel && newItem is CategoryShowcaseUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is TokoNowAdsCarouselUiModel && newItem is TokoNowAdsCarouselUiModel) {
            oldItem.id == newItem.id && oldItem.state == newItem.state
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is CategoryShowcaseUiModel && newItem is CategoryShowcaseUiModel) {
            oldItem.getChangePayload(newItem)
        } else if (oldItem is TokoNowAdsCarouselUiModel && newItem is TokoNowAdsCarouselUiModel) {
            oldItem.getChangePayload(newItem)
        } else {
            super.getChangePayload(oldItemPosition, newItemPosition)
        }
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

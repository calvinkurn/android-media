package com.tokopedia.tokopedianow.category.presentation.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView

class CategoryL2TabDiffer : BaseTokopediaNowDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return checkItemNotNull(oldItemPosition, newItemPosition) { oldItem, newItem ->
            if (oldItem is TokoNowAdsCarouselUiModel && newItem is TokoNowAdsCarouselUiModel) {
                oldItem.id == newItem.id && oldItem.state == newItem.state
            } else if (oldItem is ProductItemDataView && newItem is ProductItemDataView) {
                oldItem.productCardModel.productId == newItem.productCardModel.productId
            } else {
                oldItem == newItem
            }
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return checkItemNotNull(oldItemPosition, newItemPosition) { oldItem, newItem ->
            oldItem == newItem
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)

        return if (oldItem is TokoNowAdsCarouselUiModel && newItem is TokoNowAdsCarouselUiModel) {
            oldItem.getChangePayload(newItem)
        } else {
            null
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

    private fun checkItemNotNull(
        oldItemPosition: Int,
        newItemPosition: Int,
        compare: (oldItem: Visitable<*>, newItem: Visitable<*>) -> Boolean
    ): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        if(oldItem == null || newItem == null) return false

        return compare(oldItem, newItem)
    }
}

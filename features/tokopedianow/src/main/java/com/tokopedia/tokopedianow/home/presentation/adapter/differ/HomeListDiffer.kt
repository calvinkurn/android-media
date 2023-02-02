package com.tokopedia.tokopedianow.home.presentation.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel

class HomeListDiffer : BaseTokopediaNowDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is HomeProductRecomUiModel && newItem is HomeProductRecomUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is HomeLeftCarouselAtcUiModel && newItem is HomeLeftCarouselAtcUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
            oldItem.visitableId() == newItem.visitableId()
        } else if (oldItem is TokoNowRepurchaseUiModel && newItem is TokoNowRepurchaseUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is TokoNowCategoryMenuUiModel && newItem is TokoNowCategoryMenuUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is HomeSharingReferralWidgetUiModel && newItem is HomeSharingReferralWidgetUiModel) {
            oldItem.id == newItem.id
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

        return if (oldItem is HomeLayoutUiModel && newItem is HomeLayoutUiModel) {
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

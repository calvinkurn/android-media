package com.tokopedia.dilayanitokopedia.home.presentation.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.common.base.adapter.BaseDtDiffer

class HomeListDiffer : BaseDtDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

//        return if (oldItem is HomeProductRecomUiModel && newItem is HomeProductRecomUiModel) {
//            oldItem.recomWidget == newItem.recomWidget
//        } else if (oldItem is HomeLayoutUiModel && newItem is HomeLayoutUiModel) {
//            oldItem.visitableId == newItem.visitableId
//        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
//            oldItem.visitableId() == newItem.visitableId()
//        } else if (oldItem is TokoNowRepurchaseUiModel && newItem is TokoNowRepurchaseUiModel) {
//            oldItem.id == newItem.id
//        } else if (oldItem is TokoNowCategoryGridUiModel && newItem is TokoNowCategoryGridUiModel) {
//            oldItem.id == newItem.id
//        } else if (oldItem is HomeSharingReferralWidgetUiModel && newItem is HomeSharingReferralWidgetUiModel) {
//            oldItem.id == newItem.id
//        } else {
//            oldItem == newItem
//        }

        //remove later
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): BaseDtDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}
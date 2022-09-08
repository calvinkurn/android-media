package com.tokopedia.tokofood.feature.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokofood.common.presentation.adapter.BaseTokoFoodDiffer
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeLayoutUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel

class TokoFoodListDiffer: BaseTokoFoodDiffer() {

    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is TokoFoodHomeLayoutUiModel && newItem is TokoFoodHomeLayoutUiModel) {
            oldItem.visitableId == newItem.visitableId
        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
            oldItem.visitableId() == newItem.visitableId()
        } else if (oldItem is TokoFoodMerchantListUiModel && newItem is TokoFoodMerchantListUiModel){
            oldItem.id == newItem.id
        } else if (oldItem is TokoFoodCategoryLoadingStateUiModel && newItem is TokoFoodCategoryLoadingStateUiModel){
            oldItem.id == newItem.id
        } else if (oldItem is TokoFoodProgressBarUiModel && newItem is TokoFoodProgressBarUiModel) {
            oldItem.id == newItem.id
        } else oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getNewListSize(): Int = newList.size

    override fun getOldListSize(): Int = oldList.size

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): BaseTokoFoodDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}
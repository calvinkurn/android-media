package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.presentation.adapter.BaseTokoFoodDiffer
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchResultUiModel

class TokofoodSearchResultDiffer: BaseTokoFoodDiffer() {

    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): BaseTokoFoodDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is MerchantSearchResultUiModel && newItem is MerchantSearchResultUiModel -> {
                oldItem.id == newItem.id
            }
            oldItem is TokoFoodProgressBarUiModel && newItem is TokoFoodProgressBarUiModel -> {
                oldItem.id == newItem.id
            }
            oldItem is TokoFoodCategoryLoadingStateUiModel && newItem is TokoFoodCategoryLoadingStateUiModel -> {
                oldItem.id == newItem.id
            }
            else -> {
                oldItem == newItem
            }
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
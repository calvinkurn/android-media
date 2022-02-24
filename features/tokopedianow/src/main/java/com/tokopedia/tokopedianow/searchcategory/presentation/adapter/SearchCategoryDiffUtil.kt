package com.tokopedia.tokopedianow.searchcategory.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView

open class SearchCategoryDiffUtil: BaseTokopediaNowDiffer() {

    private var oldList: List<Visitable<*>> = listOf()
    private var newList: List<Visitable<*>> = listOf()

    override fun create(
            oldList: List<Visitable<*>>,
            newList: List<Visitable<*>>
    ): BaseTokopediaNowDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            safeGuardPosition(oldItemPosition, newItemPosition) { oldItem, newItem ->
                if (oldItem is ProductItemDataView && newItem is ProductItemDataView)
                    areProductItemTheSame(oldItem, newItem)
                else
                    oldItem::class == newItem::class
            }

    private fun safeGuardPosition(
            oldItemPosition: Int,
            newItemPosition: Int,
            compare: (oldItem: Visitable<*>, newItem: Visitable<*>) -> Boolean
    ): Boolean {
        if (oldItemPosition !in oldList.indices) return false
        if (newItemPosition !in newList.indices) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return compare(oldItem, newItem)
    }

    private fun areProductItemTheSame(oldItem: ProductItemDataView, newItem: ProductItemDataView) =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            safeGuardPosition(oldItemPosition, newItemPosition) { oldItem, newItem ->
                if (oldItem is TokoNowCategoryGridUiModel && newItem is TokoNowCategoryGridUiModel) {
                    areGridContentTheSame(oldItem, newItem)
                } else if(oldItem is CategoryFilterDataView && newItem is CategoryFilterDataView) {
                    oldItem.categoryFilterItemList == newItem.categoryFilterItemList
                } else if (oldItem is QuickFilterDataView && newItem is QuickFilterDataView) {
                    oldItem.quickFilterItemList == newItem.quickFilterItemList
                } else if (oldItem is ProductCountDataView && newItem is ProductCountDataView) {
                    oldItem.totalDataText == newItem.totalDataText
                } else if (oldItem is ChooseAddressDataView && newItem is ChooseAddressDataView) {
                    oldItem.chooseAddressData == newItem.chooseAddressData
                } else if (oldItem is TitleDataView && newItem is TitleDataView) {
                    oldItem.serviceType == newItem.serviceType && oldItem.is15mAvailable == newItem.is15mAvailable
                } else {
                    true
                }
            }

    private fun areGridContentTheSame(
            oldItem: TokoNowCategoryGridUiModel,
            newItem: TokoNowCategoryGridUiModel,
    ): Boolean {
        return oldItem.state == newItem.state
                && oldItem.categoryList?.size == newItem.categoryList?.size
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size
}
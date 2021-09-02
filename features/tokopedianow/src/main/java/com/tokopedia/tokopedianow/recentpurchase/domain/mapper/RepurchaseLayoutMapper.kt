package com.tokopedia.tokopedianow.recentpurchase.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseProductMapper.mapToProductListUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.factory.RepurchaseSortFilterFactory.createSortFilterList
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseLoadingUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductGridUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel

object RepurchaseLayoutMapper {

    fun MutableList<Visitable<*>>.addLayoutList() {
        val sortFilterList = createSortFilterList()

        add(RepurchaseSortFilterUiModel(sortFilterList))
        add(RepurchaseProductGridUiModel(emptyList()))
    }

    fun MutableList<Visitable<*>>.addProductGrid(response: List<RepurchaseProduct>) {
        val productList = response.mapToProductListUiModel()
        add(RepurchaseProductGridUiModel(productList))
    }

    fun MutableList<Visitable<*>>.addLoading() {
        add(RepurchaseLoadingUiModel)
    }

    fun MutableList<Visitable<*>>.removeLoading() {
        firstOrNull { it is RepurchaseLoadingUiModel }?.let {
            val index = indexOf(it)
            removeAt(index)
        }
    }
}

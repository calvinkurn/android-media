package com.tokopedia.tokofood.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel

fun createLoadingCategoryState(): TokoFoodListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val loadingStateUiModel = TokoFoodCategoryLoadingStateUiModel(id = TokoFoodHomeStaticLayoutId.LOADING_STATE)
    mutableList.add(loadingStateUiModel)
    return TokoFoodListUiModel(
        items = mutableList,
        state = TokoFoodLayoutState.LOADING
    )
}
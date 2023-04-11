package com.tokopedia.tokofood.feature.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryEmptyStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel

object TokoFoodCategoryMapper {

    fun MutableList<Visitable<*>>.addLoadingCategoryIntoList() {
        val loadingLayout =
            TokoFoodCategoryLoadingStateUiModel(id = TokoFoodHomeStaticLayoutId.LOADING_STATE)
        add(loadingLayout)
    }

    fun MutableList<Visitable<*>>.addErrorState(throwable: Throwable) {
        val errorLayout =
            TokoFoodErrorStateUiModel(id = TokoFoodHomeStaticLayoutId.ERROR_STATE, throwable)
        add(errorLayout)
    }

    fun MutableList<Visitable<*>>.addProgressBar() {
        val progressBarLayout =
            TokoFoodProgressBarUiModel(id = TokoFoodHomeStaticLayoutId.PROGRESS_BAR)
        add(progressBarLayout)
    }

    fun MutableList<Visitable<*>>.removeProgressBar() {
        removeAll { it is TokoFoodProgressBarUiModel }
    }

    fun MutableList<Visitable<*>>.mapCategoryLayoutList(
        responses: List<Merchant>
    ) {
        responses.forEach {
            val merchant = TokoFoodMerchantListUiModel(it.id, it)
            add(merchant)
        }
    }

    fun MutableList<Visitable<*>>.mapCategoryEmptyLayout() {
        add(TokoFoodCategoryEmptyStateUiModel(id = TokoFoodHomeStaticLayoutId.EMPTY_STATE_CATEGORY_PAGE))
    }
}

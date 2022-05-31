package com.tokopedia.tokofood.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeStaticLayoutId
import com.tokopedia.tokofood.home.domain.data.Merchant
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodProgressBarUiModel

object TokoFoodCategoryMapper {

    fun MutableList<Visitable<*>>.addLoadingCategoryIntoList() {
        val loadingLayout = TokoFoodCategoryLoadingStateUiModel(id = TokoFoodHomeStaticLayoutId.LOADING_STATE)
        add(loadingLayout)
    }

    fun MutableList<Visitable<*>>.addErrorState(throwable: Throwable) {
        val errorLayout = TokoFoodErrorStateUiModel(id = TokoFoodHomeStaticLayoutId.ERROR_STATE, throwable)
        add(errorLayout)
    }

    fun MutableList<Visitable<*>>.addProgressBar() {
        val progressBarLayout = TokoFoodProgressBarUiModel(id = TokoFoodHomeStaticLayoutId.PROGRESS_BAR)
        add(progressBarLayout)
    }

    fun MutableList<Visitable<*>>.removeProgressBar() {
        removeAll { it is TokoFoodProgressBarUiModel}
    }

    fun MutableList<Visitable<*>>.mapCategoryLayoutList(
        responses: List<Merchant>
    ){
        responses.forEach {
            val merchant = TokoFoodMerchantListUiModel(it.id, it)
            add(merchant)
        }
    }
}
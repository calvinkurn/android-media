package com.tokopedia.tokofood.feature.search.searchresult.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodSearchMerchantResponse
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchResultUiModel
import javax.inject.Inject

class TokofoodMerchantSearchResultMapper @Inject constructor() {

    fun mapResponseToVisitables(response: TokofoodSearchMerchantResponse): List<Visitable<*>> {
        return response.tokofoodSearchMerchant.merchants.map {
            MerchantSearchResultUiModel(
                id = it.id,
                merchant = it
            )
        }
    }

    fun getLoadMoreVisitables(currentVisitables: List<Visitable<*>>?): List<Visitable<*>> {
        val loadMoreVisitables =
            if (currentVisitables?.lastOrNull() is TokoFoodProgressBarUiModel) {
                currentVisitables
            } else {
                currentVisitables?.toMutableList()?.apply {
                    add(TokoFoodProgressBarUiModel(""))
                }
            }
        return loadMoreVisitables.orEmpty()
    }

}
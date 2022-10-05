package com.tokopedia.tokofood.feature.search.searchresult.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.util.TokofoodExt.getGlobalErrorType
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.tokofood.feature.search.common.presentation.uimodel.TokofoodSearchErrorStateUiModel
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodSearchMerchantResponse
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithoutFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchOOCUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchResultUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSearchUiState
import javax.inject.Inject

class TokofoodMerchantSearchResultMapper @Inject constructor() {

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

    /**
     * Checks whether the visitable has other models other than merchant item
     *
     * @return  isVisitableContainsOtherStates
     */
    fun getIsVisitableContainOtherStates(currentVisitables: List<Visitable<*>>?): Boolean {
        val layoutList = currentVisitables.orEmpty()
        return layoutList.find {
            it is TokoFoodProgressBarUiModel || it is TokoFoodErrorStateUiModel || it is TokofoodSearchErrorStateUiModel ||
                    it is MerchantSearchEmptyWithFilterUiModel || it is MerchantSearchEmptyWithoutFilterUiModel
        } != null
    }

    fun getSuccessLoadSearchResultInitial(data: Any?): List<Visitable<*>> {
        return (data as? TokofoodSearchMerchantResponse)?.let { response ->
            mapResponseToVisitables(response)
        }.orEmpty()
    }

    fun getSuccessLoadSearchResultMore(data: Any?,
                                       currentVisitables: List<Visitable<*>>?): List<Visitable<*>> {
        return (data as? TokofoodSearchMerchantResponse)?.let { response ->
            currentVisitables.orEmpty()
                .filter { it !is TokoFoodProgressBarUiModel } + mapResponseToVisitables(
                response
            )
        }.orEmpty()
    }

    fun getOutOfCoverageUiModels(data: Any?): List<Visitable<*>> {
        return (data as? Int)?.let { oocState ->
            listOf(
                MerchantSearchOOCUiModel(oocState)
            )
        }.orEmpty()
    }

    fun getErrorSearchResultInitial(throwable: Throwable?): List<Visitable<*>> {
        return throwable?.let {
            val globalErrorType = it.getGlobalErrorType()
            listOf(TokofoodSearchErrorStateUiModel(globalErrorType))
        }.orEmpty()
    }

    private fun mapResponseToVisitables(response: TokofoodSearchMerchantResponse): List<Visitable<*>> {
        return response.tokofoodSearchMerchant.merchants.map {
            MerchantSearchResultUiModel(
                id = it.id,
                merchant = it
            )
        }
    }

}
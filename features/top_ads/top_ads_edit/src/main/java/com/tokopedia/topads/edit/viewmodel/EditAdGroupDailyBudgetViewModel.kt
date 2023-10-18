package com.tokopedia.topads.edit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionBrowseUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionSearchUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.async
import javax.inject.Inject

class EditAdGroupDailyBudgetViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val topAdsImpressionPredictionSearchUseCase: TopAdsImpressionPredictionSearchUseCase,
    private val topAdsImpressionPredictionBrowseUseCase: TopAdsImpressionPredictionBrowseUseCase,
) :
    BaseViewModel(dispatchers.main) {

    private val _performanceData = MutableLiveData<Result<MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>>>()
    val performanceData: LiveData<Result<MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>>>
        get() = _performanceData

    fun getPerformanceData(productIds: List<String>,
                           bids: MutableList<Float?>,
                           dailyBudget: Float) {
        val list: MutableList<Pair<Int, Int>> = mutableListOf()
        launchCatchError(block = {
            val searchDef = async {
                bids.firstOrNull()?.let { topAdsImpressionPredictionSearchUseCase.invoke("test", productIds, it, it, dailyBudget) }
            }
            val browseDef = async {
                bids.getOrNull(TopAdsCommonConstant.CONST_1)?.let { topAdsImpressionPredictionBrowseUseCase.invoke("test", productIds, it, it, dailyBudget) }
            }

            val searchData = searchDef.await()
            val browseData = browseDef.await()
            when (searchData) {
                is Success -> {
                    val data = searchData.data.umpGetImpressionPrediction.impressionPredictionData.impression
                    list.add(Pair(data.finalImpression, data.increment))
                }

                else -> {}
            }
            when (browseData) {
                is Success -> {
                    val data = browseData.data.umpGetImpressionPrediction.impressionPredictionData.impression
                    list.add(Pair(data.finalImpression, data.increment))
                }

                else -> {}
            }
            _performanceData.value = getPerformanceDataModel(list, getTotalIncrementPercentage(list))
        }) {}
    }

    private fun getPerformanceDataModel(list: MutableList<Pair<Int, Int>>, totalIncrementPercentage: Int): Result<MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>> {
        val modelList = mutableListOf(
            CreateEditAdGroupItemAdsPotentialWidgetUiModel("Di Pencarian", list.first().first.toString(), list.first().second.toString()),
            CreateEditAdGroupItemAdsPotentialWidgetUiModel("Di Rekomendasi", list[TopAdsCommonConstant.CONST_1].first.toString(), list[TopAdsCommonConstant.CONST_1].second.toString()),
            CreateEditAdGroupItemAdsPotentialWidgetUiModel("Total Tampil ", (list.first().first + list[TopAdsCommonConstant.CONST_1].first).toString(), totalIncrementPercentage.toString())
        )
        return Success(modelList)

    }

    private fun getTotalIncrementPercentage(list: MutableList<Pair<Int, Int>>): Int {
        val increment = 0
        list.forEach {
            if (it.second > 0) increment + it.second
        }
        return increment
    }
}

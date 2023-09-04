package com.tokopedia.topads.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE_VALUE
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.SuggestionKeywordUseCase
import com.tokopedia.topads.data.ImpressionPredictionResponse
import com.tokopedia.topads.data.TopAdsGetBidSuggestionResponse
import com.tokopedia.topads.domain.usecase.TopAdsGetBidSuggestionByProductIDsUseCase
import com.tokopedia.topads.domain.usecase.TopAdsImpressionPredictionBrowseUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,November,2019
 */
class BudgetingAdsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val bidInfoUseCase: BidInfoUseCase,
    private val bidInfoUseCaseDefault: BidInfoUseCase,
    private val topAdsGetBidSuggestionByProductIDsUseCase: TopAdsGetBidSuggestionByProductIDsUseCase,
    private val topAdsImpressionPredictionUseCase: TopAdsImpressionPredictionBrowseUseCase,
    private val suggestionKeywordUseCase: SuggestionKeywordUseCase) : BaseViewModel(dispatcher.main) {


    private val _bidProductData = MutableLiveData<Result<TopAdsGetBidSuggestionResponse>>()
    val bidProductData: LiveData<Result<TopAdsGetBidSuggestionResponse>>
        get() = _bidProductData

    private val _performanceData = MutableLiveData<Result<ImpressionPredictionResponse>>()
    val performanceData: LiveData<Result<ImpressionPredictionResponse>>
        get() = _performanceData

    fun getBidInfo(suggestions: List<DataSuggestions>, onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit, onEmpty: (() -> Unit)) {

        launch(block = {
            bidInfoUseCase.setParams(suggestions, KEYWORD, SOURCE_VALUE)
            bidInfoUseCase.executeQuerySafeMode({
                if (it.topadsBidInfo.data.isEmpty()) {
                    onEmpty()
                } else {
                    onSuccess(it.topadsBidInfo.data)
                }
            }, {
                it.printStackTrace()
            })
        })
    }

    fun getSuggestionKeyword(productIds: String, groupId: Int, onSuccess: ((List<KeywordData>) -> Unit), onEmpty: (() -> Unit)) {
        launch {
            suggestionKeywordUseCase.setParams(groupId, productIds)
            suggestionKeywordUseCase.executeQuerySafeMode(
                {
                    if (it.topAdsGetKeywordSuggestionV3.data.isEmpty()) {
                        onEmpty()
                    } else {
                        onSuccess(it.topAdsGetKeywordSuggestionV3.data)
                    }
                },
                {
                    it.printStackTrace()
                }
            )
        }
    }

    fun getBidInfoDefault(suggestions: List<DataSuggestions>, onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit) {
        launch(block = {
            bidInfoUseCaseDefault.setParams(suggestions, PRODUCT, SOURCE_VALUE)
            bidInfoUseCaseDefault.executeQuerySafeMode({
                onSuccess(it.topadsBidInfo.data)
            }, {
                it.printStackTrace()
            })
        })
    }

    fun getProductBid(productIds: List<String>) {
        launchCatchError(dispatcher.main, block = {
            val data = topAdsGetBidSuggestionByProductIDsUseCase.invoke("adbidinsight.test", productIds)
            _bidProductData.value = data
        }, {
            _bidProductData.value = Fail(it)
        })
    }

    fun getPerformanceData(productIds: List<String>,
                           finalBid: Float,
                           initialBid: Float,
                           dailyBudget: Float) {
        launchCatchError(dispatcher.main, {
            val data = topAdsImpressionPredictionUseCase.invoke("test", productIds, finalBid, initialBid, dailyBudget)
            _performanceData.value = data
        }, {
            _performanceData.value = Fail(it)
        })
    }
}

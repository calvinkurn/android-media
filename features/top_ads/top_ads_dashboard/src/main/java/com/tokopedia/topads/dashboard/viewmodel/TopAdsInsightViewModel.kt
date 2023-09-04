package com.tokopedia.topads.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.domain.model.createheadline.TopAdsManageHeadlineInput2
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.data.model.insightkey.TopAdsShopHeadlineKeyword
import com.tokopedia.topads.common.domain.usecase.CreateHeadlineAdsUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsShopKeywordSuggestionUseCase
import com.tokopedia.topads.common.domain.model.createheadline.TopadsManageHeadlineAdResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TopAdsInsightViewModel @Inject constructor(
    private val createHeadlineAdsUseCase: CreateHeadlineAdsUseCase,
    private val shopKeywordSuggestionUseCase: TopAdsShopKeywordSuggestionUseCase
) : BaseViewModel(Dispatchers.Main) {

    private val _recommendedKeyword = MutableLiveData<RecommendedKeywordData>()
    val recommendedKeyword: LiveData<RecommendedKeywordData> = _recommendedKeyword

    private val _applyKeyword = MutableLiveData<Int>()
    val applyKeyword: LiveData<Int> = _applyKeyword

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getShopKeywords(shopID: String, groupIds: Array<String>) {
        launchCatchError(block = {
            val gqlResponse = shopKeywordSuggestionUseCase
                .getKeywordRecommendation(shopKeywordSuggestionUseCase.getParams(shopID, groupIds))
            val keyword = gqlResponse.getData(TopAdsShopHeadlineKeyword::class.java) as? TopAdsShopHeadlineKeyword

            keyword?.suggestion?.recommendedKeywordData?.let {
                _recommendedKeyword.postValue(it)
            }
            keyword?.suggestion?.errors?.let {
                _error.postValue(it.getOrNull(0)?.detail)
            }
        }, onError = {
            _error.postValue(it.message)
            it.printStackTrace()
        })
    }

    fun applyRecommendedKeywords(input: TopAdsManageHeadlineInput2) {
        launchCatchError(
            block = {
                createHeadlineAdsUseCase.setParams(input)
                val response : TopadsManageHeadlineAdResponse.Data = createHeadlineAdsUseCase.executeOnBackground()
                if (response.topadsManageHeadlineAd.success.id.isNotEmpty()) {
                    _applyKeyword.postValue(input.operation.group.keywordOperations.size)
                }
                if (response.topadsManageHeadlineAd.errors.isNotEmpty()) {
                    _error.postValue(response.topadsManageHeadlineAd.errors.getOrNull(0)?.detail)
                }
            },
            onError = {
                _error.postValue(it.message)
                it.printStackTrace()
            }
        )
    }
}

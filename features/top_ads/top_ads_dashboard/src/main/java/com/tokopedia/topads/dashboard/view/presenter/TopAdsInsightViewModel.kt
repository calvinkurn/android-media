package com.tokopedia.topads.dashboard.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput2
import com.tokopedia.top_ads_headline.usecase.CreateHeadlineAdsUseCase
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.data.model.insightkey.TopAdsShopHeadlineKeyword
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsShopKeywordSuggestionUseCase
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

            _recommendedKeyword.postValue(getShopAdsKeywordRecommendation().suggestion!!.recommendedKeywordData)
            keyword?.suggestion?.recommendedKeywordData?.let {
                //_recommendedKeyword.postValue(it)
            }
            keyword?.suggestion?.errors?.let {
                _error.postValue(it[0]?.detail)
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
                val response = createHeadlineAdsUseCase.executeOnBackground()
                if (response.topadsManageHeadlineAd.success.id.isNotEmpty()) {
                    _applyKeyword.postValue(input.operation.group.keywordOperations.size)
                }
                if (response.topadsManageHeadlineAd.errors.isNotEmpty()) {
                    _error.postValue(response.topadsManageHeadlineAd.errors[0].detail)
                }
            },
            onError = {
                _error.postValue(it.message)
                it.printStackTrace()
            }
        )
    }

    companion object {
        fun getShopAdsKeywordRecommendation(): TopAdsShopHeadlineKeyword {
            return Gson().fromJson(getResp(), TopAdsShopHeadlineKeyword::class.java)
        }

        private fun getResp() = "{\n" +
                "    \"topadsHeadlineKeywordSuggestion\": {\n" +
                "      \"data\": {\n" +
                "        \"shopID\": \"479085\",\n" +
                "        \"recommendedKeywordCount\": 2,\n" +
                "        \"groupCount\": 1,\n" +
                "        \"totalImpressionCount\": \"243\",\n" +
                "        \"recommendedKeywordDetails\": [\n" +
                "          {\n" +
                "            \"keywordTag\": \"svj\",\n" +
                "            \"groupID\": \"9254\",\n" +
                "            \"groupName\": \"testing el\",\n" +
                "            \"totalHits\": \"222\",\n" +
                "            \"recommendedBid\": 12000,\n" +
                "            \"minBid\": 12000,\n" +
                "            \"maxBid\": 500000,\n" +
                "            \"impressionCount\": \"243\"\n" +
                "},\n" +
                "{\n" +
                "            \"keywordTag\": \"svj\",\n" +
                "            \"groupID\": \"9254\",\n" +
                "            \"groupName\": \"testing el\",\n" +
                "            \"totalHits\": \"222\",\n" +
                "            \"recommendedBid\": 12000,\n" +
                "            \"minBid\": 12000,\n" +
                "            \"maxBid\": 500000,\n" +
                "            \"impressionCount\": \"243\"\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      \"errors\": []\n" +
                "    }\n" +
                "}"
    }
}
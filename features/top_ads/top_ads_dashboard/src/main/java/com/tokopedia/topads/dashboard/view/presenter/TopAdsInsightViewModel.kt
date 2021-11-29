package com.tokopedia.topads.dashboard.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.data.model.insightkey.ErrorsItem
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.data.model.insightkey.TopAdsShopHeadlineKeyword
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsHeadlineShopInsightUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TopAdsInsightViewModel @Inject constructor() : BaseViewModel(Dispatchers.Main) {

    private val _recommendedKeyword = MutableLiveData<RecommendedKeywordData>()
    val recommendedKeyword : LiveData<RecommendedKeywordData> = _recommendedKeyword
    val error = MutableLiveData<ErrorsItem>()

    fun getShopKeywords(shopID: String, groupIds: Array<String>) {

        val useCase = TopAdsHeadlineShopInsightUseCase()

        launchCatchError(block = {
            val gqlResponse = useCase.getKeywordRecommendation(useCase.getParams(shopID, groupIds))
            val keyword = gqlResponse
                .getData(TopAdsShopHeadlineKeyword::class.java) as? TopAdsShopHeadlineKeyword

            keyword?.suggestion?.recommendedKeywordData?.let {
                _recommendedKeyword.postValue(it)
            }
            keyword?.suggestion?.errors?.let {
                //todo add error callback
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    companion object {
        fun getShopAdsKeywordRecommendation(): TopAdsShopHeadlineKeyword {
            return Gson().fromJson(getResp(), TopAdsShopHeadlineKeyword::class.java)
        }

        private fun getResp() = "{\n" +
                "    \"topadsHeadlineKeywordSuggestion\": {\n" +
                "      \"data\": {\n" +
                "        \"shopID\": \"479085\",\n" +
                "        \"recommendedKeywordCount\": 1,\n" +
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
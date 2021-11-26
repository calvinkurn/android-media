package com.tokopedia.topads.dashboard.view.presenter

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.data.model.insightkey.TopAdsShopHeadlineKeyword
import kotlinx.coroutines.Dispatchers


class TopAdsInsightViewModel : BaseViewModel(Dispatchers.Main) {

    val recommendedKeyword = MutableLiveData<RecommendedKeywordData>()

    fun getKeywords(shopID: String, groupIds: Array<String>) {
        val map = mutableMapOf<String, Any>(
            "shopID" to shopID
        )
        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(query, TopAdsShopHeadlineKeyword::class.java, map)
        gql.addRequest(request)
        launchCatchError(block = {
            val resp = gql.executeOnBackground()
                .getData(TopAdsShopHeadlineKeyword::class.java) as? TopAdsShopHeadlineKeyword
            recommendedKeyword.postValue(resp?.suggestion?.recommendedKeywordData)
        }, onError = {
            it.printStackTrace()
        })
    }

    val query = """
        query topadsHeadlineKeywordSuggestion(${'$'}shopID: String!) {
          topadsHeadlineKeywordSuggestion(shopID: ${'$'}shopID) {
            data {
              shopID
              recommendedKeywordCount
              groupCount
              totalImpressionCount
              recommendedKeywordDetails {
                keywordTag
                groupID
                groupName
                totalHits
                recommendedBid
                minBid
                maxBid
                impressionCount
              }
            }
            errors {
              code
              title
              detail
              object {
                type
                text
              }
            }
          }
        }
    """.trimIndent()

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
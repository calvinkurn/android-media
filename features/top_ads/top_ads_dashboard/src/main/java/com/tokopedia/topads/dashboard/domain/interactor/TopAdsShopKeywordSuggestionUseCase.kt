package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.TopAdsShopHeadlineKeyword
import javax.inject.Inject

class TopAdsShopKeywordSuggestionUseCase @Inject constructor() {

    suspend fun getKeywordRecommendation(map: Map<String,Any>): GraphqlResponse {

        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(query, TopAdsShopHeadlineKeyword::class.java, map)
        gql.addRequest(request)
        return gql.executeOnBackground()
    }

    fun getParams(shopId: String, groupIds : Array<String>) : Map<String,Any> {
        return mapOf(
            P_SHOP_ID to shopId,
            P_GROUP_IDS to groupIds
        )
    }

    private val query = """
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
        private const val P_SHOP_ID = "shopID"
        private const val P_GROUP_IDS = "groupIDs"
    }
}
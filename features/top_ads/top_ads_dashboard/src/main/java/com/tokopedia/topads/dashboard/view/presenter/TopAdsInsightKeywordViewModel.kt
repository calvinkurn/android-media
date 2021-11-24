package com.tokopedia.topads.dashboard.view.presenter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.data.model.insightkey.TopadsHeadlineKeyword
import kotlinx.coroutines.Dispatchers


class TopAdsInsightKeywordViewModel : BaseViewModel(Dispatchers.Main) {

    val recommendedKeyword = MutableLiveData<TopadsHeadlineKeyword>()

    fun getKeywords(shopID: String, groupIds: Array<String>) {
        val map = mutableMapOf<String, Any>(
            "shopID" to shopID
        )
        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(query, TopadsHeadlineKeyword::class.java, map)
        gql.addRequest(request)
        launchCatchError(block = {
            val resp = gql.executeOnBackground().getData(TopadsHeadlineKeyword::class.java) as? TopadsHeadlineKeyword
            Log.d("TAG", "getKeywords: ${resp?.suggestion?.recommendedKeyword?.shopID}")
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
}
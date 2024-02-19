package com.tokopedia.privacycenter.main.di

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.privacycenter.data.GetConsentGroupListDataModel
import com.tokopedia.privacycenter.data.SocialNetworkGetConsentResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import timber.log.Timber
import java.util.*

class FakeGraphqlRepository : GraphqlRepository {

    private val _recommendationRequest: Queue<RecommendationState> = LinkedList()
    private val _consentWithdrawalRequest: Queue<ConsentWithdrawalState> = LinkedList()

    fun setRecommendationResponse(vararg states: RecommendationState) {
        _recommendationRequest.addAll(states)
    }

    fun setConsentWithdrawalResponse(vararg states: ConsentWithdrawalState) {
        _consentWithdrawalRequest.addAll(states)
    }

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        Timber.d(GqlQueryParser.parse(requests).first())
        when (GqlQueryParser.parse(requests).first()) {
            "SocialNetworkGetConsent" -> {
                var optIn: Boolean? = null
                _recommendationRequest.poll()?.let { state ->
                    optIn = when (state) {
                        RecommendationState.RECOMMENDATION_FRIEND_OPT_IN -> {
                            true
                        }
                        RecommendationState.RECOMMENDATION_FRIEND_OPT_OUT -> {
                            false
                        }
                        RecommendationState.RECOMMENDATION_FRIEND_FAILED -> {
                            throw Exception("bad request")
                        }
                    }
                }

                val response = """
                    {"SocialNetworkGetConsent":{"data":{"opt_in":$optIn},"messages":[],"error_code":""}}
                """.trimIndent()
                return createResponseFromJson<SocialNetworkGetConsentResponse>(response)
            }
            "GetConsentGroupList" -> {
                val state = _consentWithdrawalRequest.poll()
                val isSuccess = state != ConsentWithdrawalState.CONSENT_WITHDRAWAL_FAILED
                val response = """
                    {
                      "GetConsentGroupList": {
                        "success": $isSuccess,
                        "refId": "e800ed0d-2721-45d5-af0d-348d62101fa8",
                        "errorMessages": ["error message"],
                        "groups": [
                          {
                            "id": 10265,
                            "groupTitle": "Testing DPPO Portal",
                            "groupSubtitle": "-",
                            "groupImg": "-",
                            "priority": 3
                          }
                        ],
                        "ticker": "Nanti akan ada fitur lain yang bisa kamu atur persetujuannya di sini."
                      }
                    }
                """.trimIndent()
                return createResponseFromJson<GetConsentGroupListDataModel>(response)
            }
            else -> throw Exception("bad request")
        }
    }
}

package com.tokopedia.privacycenter.main.di

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.privacycenter.data.AccountLinkingResponse
import com.tokopedia.privacycenter.data.SocialNetworkGetConsentResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import timber.log.Timber

class FakeGraphqlRepository : GraphqlRepository {
    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        Timber.d(GqlQueryParser.parse(requests).first())
        return when (GqlQueryParser.parse(requests).first()) {
            "accountsLinkerStatus" -> {
                val response = """
                    {"accountsLinkerStatus":{"link_status":[{"linking_type":"account_linking","status":"linked","partner_user_id":"549262345","linked_time":"2022-03-29T06:44:13.729308Z"}],"error":""}}
                """.trimIndent()
                val obj = Gson().fromJson(response, AccountLinkingResponse::class.java)
                GqlMockUtil.createSuccessResponse(obj)
            }
            "SocialNetworkGetConsent" -> {
                val response = """
                    {"SocialNetworkGetConsent":{"data":{"opt_in":false},"messages":[],"error_code":""}}
                """.trimIndent()
                val obj = Gson().fromJson(response, SocialNetworkGetConsentResponse::class.java)
                GqlMockUtil.createSuccessResponse(obj)
            }
            else -> throw Exception("bad request")
        }
    }
}

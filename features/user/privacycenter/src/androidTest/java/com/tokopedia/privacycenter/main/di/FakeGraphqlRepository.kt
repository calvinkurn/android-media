package com.tokopedia.privacycenter.main.di

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
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

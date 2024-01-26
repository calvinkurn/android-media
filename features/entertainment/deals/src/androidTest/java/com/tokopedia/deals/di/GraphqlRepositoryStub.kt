package com.tokopedia.deals.di

import com.tokopedia.deals.data.entity.DealsBrandDetail
import com.tokopedia.deals.test.R
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import timber.log.Timber

class GraphqlRepositoryStub : GraphqlRepository {
    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        Timber.d("Passed through FakeGraphql: ${GqlQueryParser.parse(requests).first()}")
        return when (GqlQueryParser.parse(requests).first()) {
            "event_brand_detail_v2" -> {
                GqlMockUtil.createSuccessResponse<DealsBrandDetail>(R.raw.mock_gql_deals_brand_detail)
            }

            else -> throw Exception("unresolved request")
        }
    }
}

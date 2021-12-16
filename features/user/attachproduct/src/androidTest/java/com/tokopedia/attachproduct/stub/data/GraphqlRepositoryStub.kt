package com.tokopedia.attachproduct.stub.data

import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.test.R
import com.tokopedia.attachproduct.utils.FileUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import javax.inject.Inject

class GraphqlRepositoryStub @Inject constructor() : GraphqlRepository {

    var state: TestState = TestState.DEFAULT

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        when (state) {
            TestState.DEFAULT -> {
                val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product, AceSearchProductResponse::class.java)
                return GqlMockUtil.createSuccessResponse(response)
            }
            TestState.EMPTY -> {
                val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product_empty, AceSearchProductResponse::class.java)
                return GqlMockUtil.createSuccessResponse(response)
            }

            TestState.FILTER -> {
                val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product_filter, AceSearchProductResponse::class.java)
                return GqlMockUtil.createSuccessResponse(response)
            }
            else -> {
                val response = AceSearchProductResponse()
                return GqlMockUtil.createSuccessResponse(response)
            }
        }

    }
}
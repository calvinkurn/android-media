package com.tokopedia.attachproduct.stub.data

import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.test.R
import com.tokopedia.attachproduct.utils.FileUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type
import javax.inject.Inject

class GraphqlRepositoryStub @Inject constructor() : GraphqlRepository {

    private var errorData: MutableMap<Type, List<GraphqlError>> = hashMapOf()

    var state: TestState = TestState.DEFAULT

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        when (state) {
            TestState.DEFAULT -> {
                val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product, AceSearchProductResponse::class.java)
                return GraphqlResponse(mapOf(AceSearchProductResponse::class.java to response), errorData, false)
            }
            TestState.EMPTY -> {
                val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product_empty, AceSearchProductResponse::class.java)
                return GraphqlResponse(mapOf(AceSearchProductResponse::class.java to response), errorData, false)
            }

            TestState.FILTER -> {
                val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product_filter, AceSearchProductResponse::class.java)
                return GraphqlResponse(mapOf(AceSearchProductResponse::class.java to response), errorData, false)
            }
            else -> {
                val response = AceSearchProductResponse()
                return GraphqlResponse(mapOf(AceSearchProductResponse::class.java to response), errorData, false)
            }
        }

    }
}
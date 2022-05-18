package com.tokopedia.manageaddress.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser

class FakeGraphqlRepository : GraphqlRepository {
    override suspend fun response(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponse {
        return when(GqlQueryParser.parse(requests).first()) {
            "keroAddrSetStateChosenAddress" -> GqlMockUtil.createSuccessResponse(SetStateChosenAddressQqlResponse())
            else -> throw Exception("bad request")
        }
    }
}
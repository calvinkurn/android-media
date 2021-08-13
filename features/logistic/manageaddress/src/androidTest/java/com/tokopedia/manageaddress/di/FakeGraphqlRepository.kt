package com.tokopedia.manageaddress.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse

class FakeGraphqlRepository : GraphqlRepository {
    override suspend fun getReseponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponse {
        requests.firstOrNull()?.query?.let {
            when {
                it.contains("keroAddrSetStateChosenAddress") ->
                    return GraphqlResponse(
                            mapOf(SetStateChosenAddressQqlResponse::class.java to SetStateChosenAddressQqlResponse()),
                            mapOf(), false)
                else -> throw Exception("bad request")
            }
        }
        throw Exception("unexpected condition")
    }
}
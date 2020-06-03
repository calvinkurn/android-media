package com.tokopedia.graphql.coroutines.domain.repository

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse

interface GraphqlRepository{

    suspend fun getReseponse(requests: List<GraphqlRequest>,
                             cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.NONE).build())
            : GraphqlResponse
}
package com.tokopedia.flashsale.management.coroutineGraphql

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse

interface GraphqlRepositoryKt{

    suspend fun getReseponse(requests: List<GraphqlRequest>,
                             cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.NONE).build())
            : GraphqlResponse
}
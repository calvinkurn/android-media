package com.tokopedia.mediauploader

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse

interface MediaRepository {
    suspend fun response(requests: List<GraphqlRequest>): GraphqlResponse
}
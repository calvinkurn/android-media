package com.tokopedia.mediauploader

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse

open class MediaRepositoryImpl(
        private val repository: GraphqlRepository
): MediaRepository {

    override suspend fun response(requests: List<GraphqlRequest>): GraphqlResponse {
        return repository.getReseponse(requests)
    }

}
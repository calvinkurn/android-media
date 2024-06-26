package com.tokopedia.editshipping.util

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest

internal suspend inline fun <reified T> GraphqlRepository.getResponse(request: GraphqlRequest): T {
    return response(listOf(request)).getSuccessData<T>()
        ?: throw NullPointerException("Data with your type might not exist")
}

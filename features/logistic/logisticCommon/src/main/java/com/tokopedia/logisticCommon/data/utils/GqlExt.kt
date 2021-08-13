package com.tokopedia.logisticCommon.data.utils

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest

internal suspend inline fun <reified T> GraphqlRepository.getResponse(request: GraphqlRequest): T {
    return getReseponse(listOf(request)).getSuccessData<T>()
            ?: throw NullPointerException("Data with your type might not exist")
}

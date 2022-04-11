package com.tokopedia.attachvoucher

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import io.mockk.MockKAdditionalAnswerScope
import io.mockk.coEvery
import java.lang.reflect.Type

inline fun <reified R: Throwable> GraphqlRepository.stubRepositoryAsThrow(
    throwable: R
): MockKAdditionalAnswerScope<GraphqlResponse, GraphqlResponse> {
    val it = this
    return coEvery {
        it.response(any(), any())
    } throws throwable
}

inline fun <reified T : Any> GraphqlRepository.stubRepository(
    onSuccess: T,
    onError: Map<Type, List<GraphqlError>>? = null
): MockKAdditionalAnswerScope<GraphqlResponse, GraphqlResponse> {
    val it = this
    val data = hashMapOf<Type, Any>(T::class.java to onSuccess)

    return coEvery {
        it.response(any(), any())
    } returns GraphqlResponse(
        data,
        onError,
        false
    )
}
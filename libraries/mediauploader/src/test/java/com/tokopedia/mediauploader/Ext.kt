package com.tokopedia.mediauploader

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import io.mockk.MockKAdditionalAnswerScope
import io.mockk.coEvery
import java.lang.reflect.Type

fun MediaRepository.stubDataPolicyRepository(
        onError: Map<Type, List<GraphqlError>>?
): MockKAdditionalAnswerScope<GraphqlResponse, GraphqlResponse> {
    return stubCommonRepository(
            mapOf(DataUploaderPolicy::class.java to DataUploaderPolicy()),
            onError
    )
}

fun MediaRepository.stubCommonRepository(
        onResult: Map<Type, Any>,
        onError: Map<Type, List<GraphqlError>>?
): MockKAdditionalAnswerScope<GraphqlResponse, GraphqlResponse> {
    val it = this
    return coEvery {
        it.response(any())
    } answers {
        GraphqlResponse(
                onResult,
                onError,
                false
        )
    }
}
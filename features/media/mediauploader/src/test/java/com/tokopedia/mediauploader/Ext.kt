package com.tokopedia.mediauploader

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.mediauploader.image.data.ImageUploadServices
import com.tokopedia.mediauploader.image.data.entity.ImageUploader
import io.mockk.MockKAdditionalAnswerScope
import io.mockk.coEvery
import java.lang.reflect.Type

fun ImageUploadServices.stubUploadFileServices(
    expectedResult: ImageUploader
): MockKAdditionalAnswerScope<ImageUploader, ImageUploader> {
    val it = this
    return coEvery {
        it.uploadImage(any(), any(), any())
    } answers {
        expectedResult
    }
}

inline fun <reified R: Throwable> GraphqlRepository.stubRepositoryAsThrow(
    `as`: R
): MockKAdditionalAnswerScope<GraphqlResponse, GraphqlResponse> {
    val it = this

    return coEvery {
        it.response(any(), any())
    } throws `as`
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
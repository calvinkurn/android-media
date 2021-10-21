package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import com.tokopedia.mediauploader.stubRepository
import com.tokopedia.mediauploader.stubRepositoryAsThrow
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DataPolicyUseCaseTest {

    private val repository = mockk<GraphqlRepository>()
    private val useCase = DataPolicyUseCase(repository)

    private val mockPolicy = DataUploaderPolicy()
    private val mockSourceId = "WXjxja"

    @Test fun `It should not able request data without requestId and throw exception`() {
        runBlocking {
            // When
            repository.stubRepositoryAsThrow(RuntimeException())

            // Then
            assertFailsWith<RuntimeException> {
                useCase("")
            }
        }
    }

    @Test fun `It should able to request data with sourceId`() {
        runBlocking {
            // When
            repository.stubRepository(
                onSuccess = DataUploaderPolicy(),
                onError = mapOf()
            )

            val result = useCase(mockSourceId)

            // Then
            assertEquals(mockPolicy, result)
        }
    }

}
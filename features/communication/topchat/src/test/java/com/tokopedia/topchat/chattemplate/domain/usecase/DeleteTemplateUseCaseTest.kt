package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatDeleteTemplateResponse
import com.tokopedia.topchat.stubRepository
import com.tokopedia.topchat.stubRepositoryAsThrow
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class DeleteTemplateUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var repository: GraphqlRepository

    private lateinit var deleteTemplateUseCase: DeleteTemplateUseCase
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val expectedThrowable = Throwable("Oops!")
    private val testIndex = 1

    @Before
    fun before() {
        MockKAnnotations.init(this)
        deleteTemplateUseCase = DeleteTemplateUseCase(repository, dispatchers)
    }

    @Test
    fun should_get_template_data_when_success_delete_template_buyer() {
        // Given
        val expectedResponse = ChatDeleteTemplateResponse()
        val param = DeleteTemplateUseCase.Param(false, testIndex)

        runBlocking {
            // When
            repository.stubRepository(
                onSuccess = expectedResponse,
                onError = mapOf()
            )
            val result = deleteTemplateUseCase(param)

            // Then
            Assert.assertEquals(result, expectedResponse)
        }
    }

    @Test
    fun should_get_template_data_when_success_delete_template_seller() {
        // Given
        val expectedResponse = ChatDeleteTemplateResponse()
        val param = DeleteTemplateUseCase.Param(true, testIndex)

        runBlocking {
            // When
            repository.stubRepository(
                onSuccess = expectedResponse,
                onError = mapOf()
            )
            val result = deleteTemplateUseCase(param)

            // Then
            Assert.assertEquals(result, expectedResponse)
        }
    }

    @Test
    fun should_get_error_when_fail_to_delete_template_buyer() {
        // Given
        val param = DeleteTemplateUseCase.Param(false, testIndex)

        assertThrows<Throwable> {
            // When
            repository.stubRepositoryAsThrow(
                throwable = expectedThrowable
            )

            // Then
            runBlocking {
                deleteTemplateUseCase(param)
            }
        }
    }

    @Test
    fun should_get_error_when_fail_to_delete_template_seller() {
        // Given
        val param = DeleteTemplateUseCase.Param(true, testIndex)

        // Then
        assertThrows<Throwable> {
            runBlocking {
                // When
                repository.stubRepositoryAsThrow(
                    throwable = expectedThrowable
                )

                // Then
                deleteTemplateUseCase(param)
            }
        }
    }
}

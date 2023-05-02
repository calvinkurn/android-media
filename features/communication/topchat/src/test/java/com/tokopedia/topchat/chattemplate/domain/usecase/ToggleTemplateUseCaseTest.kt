package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatToggleTemplateResponse
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

class ToggleTemplateUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var repository: GraphqlRepository

    private lateinit var templateUseCase: ToggleTemplateUseCase

    private val testException = Throwable("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        templateUseCase = ToggleTemplateUseCase(
            repository,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun should_get_template_data_when_success_set_availability_true_buyer() {
        // Given
        val isSeller = false
        val expectedResponse = ChatToggleTemplateResponse()

        // When
        repository.stubRepository(
            onSuccess = expectedResponse,
            onError = mapOf()
        )
        val result = runBlocking {
            templateUseCase(
                ToggleTemplateUseCase.Param(
                    isSeller
                )
            )
        }

        // Then
        Assert.assertEquals(expectedResponse, result)
    }

    @Test
    fun should_get_template_data_when_success_set_availability_false_buyer() {
        // Given
        val isSeller = false
        val expectedResponse = ChatToggleTemplateResponse()

        // When
        repository.stubRepository(
            onSuccess = expectedResponse,
            onError = mapOf()
        )
        val result = runBlocking {
            templateUseCase(
                ToggleTemplateUseCase.Param(
                    isSeller
                )
            )
        }

        // Then
        Assert.assertEquals(expectedResponse, result)
    }

    @Test
    fun should_get_template_data_when_success_set_availability_true_seller() {
        // Given
        val isSeller = true
        val expectedResponse = ChatToggleTemplateResponse()

        // When
        repository.stubRepository(
            onSuccess = expectedResponse,
            onError = mapOf()
        )
        val result = runBlocking {
            templateUseCase(
                ToggleTemplateUseCase.Param(
                    isSeller
                )
            )
        }

        // Then
        Assert.assertEquals(expectedResponse, result)
    }

    @Test
    fun should_get_template_data_when_success_set_availability_false_seller() {
        // Given
        val isSeller = true
        val expectedResponse = ChatToggleTemplateResponse()

        // When
        repository.stubRepository(
            onSuccess = expectedResponse,
            onError = mapOf()
        )
        val result = runBlocking {
            templateUseCase(
                ToggleTemplateUseCase.Param(
                    isSeller
                )
            )
        }

        // Then
        Assert.assertEquals(expectedResponse, result)
    }

    @Test
    fun should_get_error_when_set_availability_template_buyer() {
        // Given
        val isSeller = false

        // Then
        assertThrows<Throwable> {
            runBlocking {
                // When
                repository.stubRepositoryAsThrow(
                    throwable = testException
                )

                // Then
                templateUseCase(
                    ToggleTemplateUseCase.Param(
                        isSeller
                    )
                )
            }
        }
    }

    @Test
    fun should_get_error_when_set_availability_template_seller() {
        // Given
        val isSeller = true

        assertThrows<Throwable> {
            runBlocking {
                // When
                repository.stubRepositoryAsThrow(
                    throwable = testException
                )

                // Then
                templateUseCase(
                    ToggleTemplateUseCase.Param(
                        isSeller
                    )
                )
            }
        }
    }
}

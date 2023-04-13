package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatUpdateTemplateResponse
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

class UpdateTemplateUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var repository: GraphqlRepository

    private lateinit var updateTemplateUseCase: UpdateTemplateUseCase
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val testString = "Tokopedia saja!"
    private val expectedThrowable = Throwable("Oops!")
    private val testIndex = 1

    @Before
    fun before() {
        MockKAnnotations.init(this)
        updateTemplateUseCase = UpdateTemplateUseCase(repository, dispatchers)
    }

    @Test
    fun should_get_template_data_when_success_edit_template_buyer() {
        // Given
        val expectedResponse = ChatUpdateTemplateResponse()

        runBlocking {
            // When
            repository.stubRepository(
                onSuccess = expectedResponse,
                onError = mapOf()
            )
            val result = updateTemplateUseCase(
                UpdateTemplateUseCase.Param(
                    false,
                    testIndex,
                    testString
                )
            )

            // Then
            Assert.assertEquals(result, expectedResponse)
        }
    }

    @Test
    fun should_get_template_data_when_success_edit_template_seller() {
        // Given
        val expectedResponse = ChatUpdateTemplateResponse()

        runBlocking {
            // When
            repository.stubRepository(
                onSuccess = expectedResponse,
                onError = mapOf()
            )
            val result = updateTemplateUseCase(
                UpdateTemplateUseCase.Param(
                    true,
                    testIndex,
                    testString
                )
            )

            // Then
            Assert.assertEquals(result, expectedResponse)
        }
    }

    @Test
    fun should_get_error_when_fail_to_edit_template_buyer() {
        assertThrows<Throwable> {
            runBlocking {
                // When
                repository.stubRepositoryAsThrow(
                    throwable = expectedThrowable
                )

                // Then
                updateTemplateUseCase(
                    UpdateTemplateUseCase.Param(
                        false,
                        testIndex,
                        testString
                    )
                )
            }
        }
    }

    @Test
    fun should_get_error_when_fail_to_edit_template_seller() {
        assertThrows<Throwable> {
            runBlocking {
                // When
                repository.stubRepositoryAsThrow(
                    throwable = expectedThrowable
                )

                // Then
                updateTemplateUseCase(
                    UpdateTemplateUseCase.Param(
                        true,
                        testIndex,
                        testString
                    )
                )
            }
        }
    }
}

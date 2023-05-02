package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatAddTemplateResponse
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

class CreateTemplateUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var repository: GraphqlRepository

    private lateinit var createTemplateUseCase: CreateTemplateUseCase
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val testString = "Tokopedia saja!"
    private val expectedThrowable = Throwable("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        createTemplateUseCase = CreateTemplateUseCase(repository, dispatchers)
    }

    @Test
    fun should_get_template_data_when_success_create_template_buyer() {
        // Given
        val expectedResponse = ChatAddTemplateResponse()

        runBlocking {
            // When
            repository.stubRepository(
                onSuccess = expectedResponse,
                onError = mapOf()
            )
            val result = createTemplateUseCase(CreateTemplateUseCase.Param(false, testString))

            // Then
            Assert.assertEquals(result, expectedResponse)
        }
    }

    @Test
    fun should_get_template_data_when_success_create_template_seller() {
        // Given
        val expectedResponse = ChatAddTemplateResponse()
        runBlocking {
            // When
            repository.stubRepository(
                onSuccess = expectedResponse,
                onError = mapOf()
            )
            val result = createTemplateUseCase(CreateTemplateUseCase.Param(true, testString))

            // Then
            Assert.assertEquals(result, expectedResponse)
        }
    }

    @Test
    fun should_get_error_when_fail_to_create_template_buyer() {
        assertThrows<Throwable> {
            runBlocking {
                // When
                repository.stubRepositoryAsThrow(
                    throwable = expectedThrowable
                )

                // Then
                createTemplateUseCase(
                    CreateTemplateUseCase.Param(
                        isSeller = false,
                        value = testString
                    )
                )
            }
        }
    }

    @Test
    fun should_get_error_when_fail_to_create_template_seller() {
        assertThrows<Throwable> {
            runBlocking {
                // When
                repository.stubRepositoryAsThrow(
                    throwable = expectedThrowable
                )

                // Then
                createTemplateUseCase(
                    CreateTemplateUseCase.Param(
                        isSeller = true,
                        value = testString
                    )
                )
            }
        }
    }
}

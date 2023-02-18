package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
    private lateinit var templateRepository: EditTemplateRepository

    private lateinit var createTemplateUseCase: CreateTemplateUseCase
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val testString = "Tokopedia saja!"
    private val expectedThrowable = Throwable("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        createTemplateUseCase = CreateTemplateUseCase(templateRepository, dispatchers)
    }

    @Test
    fun should_get_template_data_when_success_create_template_buyer() {
        // Given
        val expectedResponse = TemplateData().apply {
            isIsEnable = true
            isSuccess = true
            templates = listOf(testString)
        }
        coEvery {
            templateRepository.createTemplate(any())
        } returns expectedResponse

        runBlocking {
            // When
            val result = createTemplateUseCase.createTemplate(testString, false)

            // Then
            Assert.assertEquals(result, expectedResponse)
        }
    }

    @Test
    fun should_get_template_data_when_success_create_template_seller() {
        // Given
        val expectedResponse = TemplateData().apply {
            isIsEnable = true
            isSuccess = true
            templates = listOf(testString)
        }
        coEvery {
            templateRepository.createTemplate(any())
        } returns expectedResponse

        runBlocking {
            // When
            val result = createTemplateUseCase.createTemplate(testString, true)

            // Then
            Assert.assertEquals(result, expectedResponse)
        }
    }

    @Test
    fun should_get_error_when_fail_to_create_template_buyer() {
        // Given
        coEvery {
            templateRepository.createTemplate(any())
        } throws expectedThrowable

        // Then
        assertThrows<Throwable> {
            runBlocking {
                createTemplateUseCase.createTemplate(testString, false)
            }
        }
    }

    @Test
    fun should_get_error_when_fail_to_create_template_seller() {
        // Given
        coEvery {
            templateRepository.createTemplate(any())
        } throws expectedThrowable

        // Then
        assertThrows<Throwable> {
            runBlocking {
                createTemplateUseCase.createTemplate(testString, true)
            }
        }
    }
}

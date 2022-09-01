package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
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

class DeleteTemplateUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var templateRepository: EditTemplateRepository

    private lateinit var deleteTemplateUseCase: DeleteTemplateUseCase
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val testString = "Tokopedia saja!"
    private val expectedThrowable = Throwable("Oops!")
    private val testIndex = 1

    @Before
    fun before() {
        MockKAnnotations.init(this)
        deleteTemplateUseCase = DeleteTemplateUseCase(templateRepository, dispatchers)
    }

    @Test
    fun should_get_template_data_when_success_delete_template_buyer() {
        //Given
        val expectedResponse = TemplateData().apply {
            isIsEnable = true
            isSuccess = true
            templates = listOf(testString)
        }
        coEvery {
            templateRepository.deleteTemplate(any(), any())
        } returns expectedResponse

        runBlocking {
            //When
            val result = deleteTemplateUseCase.deleteTemplate(testIndex, false)

            //Then
            Assert.assertEquals(result, expectedResponse)
        }
    }

    @Test
    fun should_get_template_data_when_success_delete_template_seller() {
        //Given
        val expectedResponse = TemplateData().apply {
            isIsEnable = true
            isSuccess = true
            templates = listOf(testString)
        }
        coEvery {
            templateRepository.deleteTemplate(any(), any())
        } returns expectedResponse

        runBlocking {
            //When
            val result = deleteTemplateUseCase.deleteTemplate(testIndex, true)

            //Then
            Assert.assertEquals(result, expectedResponse)
        }
    }

    @Test
    fun should_get_error_when_fail_to_delete_template_buyer() {
        //Given
        coEvery {
            templateRepository.deleteTemplate(any(), any())
        } throws expectedThrowable

        //Then
        assertThrows<Throwable> {
            runBlocking {
                deleteTemplateUseCase.deleteTemplate(testIndex, false)
            }
        }
    }

    @Test
    fun should_get_error_when_fail_to_delete_template_seller() {
        //Given
        coEvery {
            templateRepository.deleteTemplate(any(), any())
        } throws expectedThrowable

        //Then
        assertThrows<Throwable> {
            runBlocking {
                deleteTemplateUseCase.deleteTemplate(testIndex, true)
            }
        }
    }
}
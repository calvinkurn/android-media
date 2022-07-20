package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateDataWrapper
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

class GetTemplateUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var templateRepository: TemplateRepository

    private lateinit var getTemplateUseCase: GetTemplateUseCase

    private val testTemplate = arrayListOf("template1, template2, template3")
    private val testException = Exception("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        getTemplateUseCase = GetTemplateUseCase(
            templateRepository, CoroutineTestDispatchersProvider)
    }

    @Test
    fun should_get_template_data_when_success_get_template_buyer() {
        //Given
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData().also {
                it.isIsEnable = true
                it.isSuccess = true
                it.templates = testTemplate
            }
        )
        coEvery {
            templateRepository.getTemplateSuspend(any())
        } returns expectedResponse.data

        runBlocking {
            //When
            val result = getTemplateUseCase.getTemplate(false)

            //Then
            Assert.assertEquals(
                expectedResponse.data,
                result
            )
        }
    }

    @Test
    fun should_get_template_data_when_success_get_template_seller() {
        //Given
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData().also {
                it.isIsEnable = true
                it.isSuccess = true
                it.templates = testTemplate
            }
        )
        coEvery {
            templateRepository.getTemplateSuspend(any())
        } returns expectedResponse.data

        runBlocking {
            //When
            val result = getTemplateUseCase.getTemplate(true)

            //Then
            Assert.assertEquals(
                expectedResponse.data,
                result
            )
        }
    }

    @Test
    fun should_get_error_when_fail_to_get_template_buyer() {
        //Given
        coEvery {
            templateRepository.getTemplateSuspend(any())
        } throws testException

        //Then
        assertThrows<Exception> {
            runBlocking {
                getTemplateUseCase.getTemplate(false)
            }
        }
    }

    @Test
    fun should_get_error_when_fail_to_get_template_seller() {
        //Given
        coEvery {
            templateRepository.getTemplateSuspend(any())
        } throws testException

        //Then
        assertThrows<Exception> {
            runBlocking {
                getTemplateUseCase.getTemplate(true)
            }
        }
    }
}
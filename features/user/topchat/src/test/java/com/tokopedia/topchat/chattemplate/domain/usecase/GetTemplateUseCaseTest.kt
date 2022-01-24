package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository
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
    fun `get template buyer data`() {
        //Given
        val expectedResponse = TemplateDataWrapper().also {
            it.templateData.isIsEnable = true
            it.templateData.isSuccess = true
            it.templateData.templates = testTemplate
        }
        coEvery {
            templateRepository.getTemplateSuspend(any())
        } returns expectedResponse.templateData

        //When
        val result = runBlocking {
            getTemplateUseCase.getTemplate(false)
        }

        Assert.assertEquals(
            expectedResponse.templateData,
            result
        )
    }

    @Test
    fun `get template seller data`() {
        //Given
        val expectedResponse = TemplateDataWrapper().also {
            it.templateData.isIsEnable = true
            it.templateData.isSuccess = true
            it.templateData.templates = testTemplate
        }
        coEvery {
            templateRepository.getTemplateSuspend(any())
        } returns expectedResponse.templateData

        runBlocking {
            //When
            val result = getTemplateUseCase.getTemplate(true)

            //Then
            Assert.assertEquals(
                expectedResponse.templateData,
                result
            )
        }
    }

    @Test
    fun `get error template buyer data`() {
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
    fun `get error template seller data`() {
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
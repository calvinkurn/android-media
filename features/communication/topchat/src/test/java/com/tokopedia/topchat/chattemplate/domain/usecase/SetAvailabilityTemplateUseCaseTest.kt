package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateDataWrapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class SetAvailabilityTemplateUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var templateRepository: TemplateRepository

    private lateinit var templateUseCase: SetAvailabilityTemplateUseCase

    private val testException = Exception("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        templateUseCase = SetAvailabilityTemplateUseCase(
            templateRepository, CoroutineTestDispatchersProvider)
    }

    @Test
    fun should_get_template_data_when_success_set_availability_true_buyer() {
        //Given
        val isEnabled = true
        val isSeller = false
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData().also {
                it.isIsEnable = isEnabled
                it.isSuccess = true
            }
        )
        coEvery {
            templateRepository.setAvailabilityTemplate(any(), any())
        } returns expectedResponse.data

        //When
        val result = runBlocking {
            val params = SetAvailabilityTemplateUseCase.getAvailabilityJson(
                null, isSeller, isEnabled)
            templateUseCase.setAvailability(params)
        }

        //Then
        Assert.assertEquals(
            expectedResponse.data,
            result
        )
    }

    @Test
    fun should_get_template_data_when_success_set_availability_false_buyer() {
        //Given
        val isEnabled = false
        val isSeller = false
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData().also {
                it.isIsEnable = isEnabled
                it.isSuccess = true
            }
        )
        coEvery {
            templateRepository.setAvailabilityTemplate(any(), any())
        } returns expectedResponse.data

        //When
        val result = runBlocking {
            val params = SetAvailabilityTemplateUseCase.getAvailabilityJson(
                null, isSeller, isEnabled)
            templateUseCase.setAvailability(params)
        }

        //Then
        Assert.assertEquals(
            expectedResponse.data,
            result
        )
    }

    @Test
    fun should_get_template_data_when_success_set_availability_true_seller() {
        //Given
        val isEnabled = true
        val isSeller = true
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData().also {
                it.isIsEnable = isEnabled
                it.isSuccess = true
            }
        )
        coEvery {
            templateRepository.setAvailabilityTemplate(any(), any())
        } returns expectedResponse.data

        //When
        val result = runBlocking {
            val params = SetAvailabilityTemplateUseCase.getAvailabilityJson(
                null, isSeller, isEnabled)
            templateUseCase.setAvailability(params)
        }

        //Then
        Assert.assertEquals(
            expectedResponse.data,
            result
        )
    }

    @Test
    fun should_get_template_data_when_success_set_availability_false_seller() {
        //Given
        val isEnabled = false
        val isSeller = true
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData().also {
                it.isIsEnable = isEnabled
                it.isSuccess = true
            }
        )
        coEvery {
            templateRepository.setAvailabilityTemplate(any(), any())
        } returns expectedResponse.data

        //When
        val result = runBlocking {
            val params = SetAvailabilityTemplateUseCase.getAvailabilityJson(
                null, isSeller, isEnabled)
            templateUseCase.setAvailability(params)
        }

        //Then
        Assert.assertEquals(
            expectedResponse.data,
            result
        )
    }

    @Test
    fun should_get_error_when_set_availability_template_buyer() {
        //Given
        val isEnabled = true
        val isSeller = false
        coEvery {
            templateRepository.setAvailabilityTemplate(any(), any())
        } throws testException

        //Then
        assertThrows<Exception> {
            runBlocking {
                val params = SetAvailabilityTemplateUseCase.getAvailabilityJson(
                    null, isSeller, isEnabled)
                templateUseCase.setAvailability(params)
            }
        }
    }

    @Test
    fun should_get_error_when_set_availability_template_seller() {
        //Given
        val isEnabled = true
        val isSeller = true
        coEvery {
            templateRepository.setAvailabilityTemplate(any(), any())
        } throws testException

        //Then
        assertThrows<Exception> {
            runBlocking {
                val params = SetAvailabilityTemplateUseCase.getAvailabilityJson(
                    null, isSeller, isEnabled)
                templateUseCase.setAvailability(params)
            }
        }
    }
}
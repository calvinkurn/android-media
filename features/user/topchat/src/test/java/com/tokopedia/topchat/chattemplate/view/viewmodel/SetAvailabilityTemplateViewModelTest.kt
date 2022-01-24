package com.tokopedia.topchat.chattemplate.view.viewmodel

import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateDataWrapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class SetAvailabilityTemplateViewModelTest: BaseChatTemplateViewModel()  {

    @Test
    fun should_give_chat_template_response_when_success_switch_template_true_buyer() {
        //Given
        val isEnable = true
        val isSeller = false
        val expectedResponse = TemplateDataWrapper().also {
            it.templateData.isSuccess = true
            it.templateData.isIsEnable = isEnable
        }
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } returns expectedResponse.templateData

        //When
        viewModel.switchTemplateAvailability(isSeller, isEnable)

        //Then
        Assert.assertEquals(
            expectedResponse.templateData.mapToTemplateUiModel().isEnabled,
            (viewModel.templateAvailability.value?.second as Success).data.isEnabled
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_switch_template_true_seller() {
        //Given
        val isEnable = true
        val isSeller = true
        val expectedResponse = TemplateDataWrapper().also {
            it.templateData.isSuccess = true
            it.templateData.isIsEnable = isEnable
        }
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } returns expectedResponse.templateData

        //When
        viewModel.switchTemplateAvailability(isSeller, isEnable)

        //Then
        Assert.assertEquals(
            expectedResponse.templateData.mapToTemplateUiModel().isEnabled,
            (viewModel.templateAvailability.value?.second as Success).data.isEnabled
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_switch_template_false_buyer() {
        //Given
        val isEnable = false
        val isSeller = false
        val expectedResponse = TemplateDataWrapper().also {
            it.templateData.isSuccess = true
            it.templateData.isIsEnable = isEnable
        }
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } returns expectedResponse.templateData

        //When
        viewModel.switchTemplateAvailability(isSeller, isEnable)

        //Then
        Assert.assertEquals(
            expectedResponse.templateData.mapToTemplateUiModel().isEnabled,
            (viewModel.templateAvailability.value?.second as Success).data.isEnabled
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_switch_template_false_seller() {
        //Given
        val isEnable = false
        val isSeller = true
        val expectedResponse = TemplateDataWrapper().also {
            it.templateData.isSuccess = true
            it.templateData.isIsEnable = isEnable
        }
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } returns expectedResponse.templateData

        //When
        viewModel.switchTemplateAvailability(isSeller, isEnable)

        //Then
        Assert.assertEquals(
            expectedResponse.templateData.mapToTemplateUiModel().isEnabled,
            (viewModel.templateAvailability.value?.second as Success).data.isEnabled
        )
    }

    @Test
    fun should_give_error_when_fail_to_switch_template_buyer() {
        //Given
        val isEnable = true
        val isSeller = false
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } throws expectedThrowable

        //When
        viewModel.switchTemplateAvailability(isSeller, isEnable)

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.templateAvailability.value?.second as Fail).throwable.message
        )
    }

    @Test
    fun should_give_error_when_fail_to_switch_template_seller() {
        //Given
        val isEnable = true
        val isSeller = true
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } throws expectedThrowable

        //When
        viewModel.switchTemplateAvailability(isSeller, isEnable)

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.templateAvailability.value?.second as Fail).throwable.message
        )
    }
}
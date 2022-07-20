package com.tokopedia.topchat.chattemplate.viewmodel.chat_template

import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper.mapToTemplateUiModel
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateDataWrapper
import com.tokopedia.topchat.chattemplate.viewmodel.chat_template.base.BaseChatTemplateViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class SetAvailabilityTemplateViewModelTest: BaseChatTemplateViewModelTest()  {

    @Test
    fun should_give_chat_template_response_when_success_switch_template_true_buyer() {
        //Given
        val isEnable = true
        val isSeller = false
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData(
                isSuccess = true,
                isIsEnable = isEnable
            )
        )
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } returns expectedResponse.data

        //When
        viewModel.switchTemplateAvailability(isSeller, isEnable)

        //Then
        Assert.assertEquals(
            expectedResponse.data.mapToTemplateUiModel().isEnabled,
            (viewModel.templateAvailability.value?.second as Success).data.isEnabled
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_switch_template_true_seller() {
        //Given
        val isEnable = true
        val isSeller = true
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData(
                isSuccess = true,
                isIsEnable = isEnable
            )
        )
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } returns expectedResponse.data

        //When
        viewModel.switchTemplateAvailability(isSeller, isEnable)

        //Then
        Assert.assertEquals(
            expectedResponse.data.mapToTemplateUiModel().isEnabled,
            (viewModel.templateAvailability.value?.second as Success).data.isEnabled
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_switch_template_false_buyer() {
        //Given
        val isEnable = false
        val isSeller = false
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData(
                isSuccess = true,
                isIsEnable = isEnable
            )
        )
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } returns expectedResponse.data

        //When
        viewModel.switchTemplateAvailability(isSeller, isEnable)

        //Then
        Assert.assertEquals(
            expectedResponse.data.mapToTemplateUiModel().isEnabled,
            (viewModel.templateAvailability.value?.second as Success).data.isEnabled
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_switch_template_false_seller() {
        //Given
        val isEnable = false
        val isSeller = true
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData(
                isSuccess = true,
                isIsEnable = isEnable
            )
        )
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } returns expectedResponse.data

        //When
        viewModel.switchTemplateAvailability(isSeller, isEnable)

        //Then
        Assert.assertEquals(
            expectedResponse.data.mapToTemplateUiModel().isEnabled,
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
package com.tokopedia.topchat.chattemplate.viewmodel.chat_template

import com.tokopedia.topchat.chattemplate.domain.pojo.ChatToggleTemplate
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatToggleTemplateResponse
import com.tokopedia.topchat.chattemplate.viewmodel.chat_template.base.BaseChatTemplateViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class ToggleTemplateViewModelTest : BaseChatTemplateViewModelTest() {

    @Test
    fun should_give_chat_template_response_when_success_switch_template_true_buyer() {
        // Given
        val isEnable = true
        val isSeller = false
        val expectedResponse = ChatToggleTemplateResponse(
            chatToggleTemplate = ChatToggleTemplate(
                success = 1
            )
        )
        coEvery {
            toggleTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.toggleTemplate(isSeller, isEnable)

        // Then
        Assert.assertEquals(
            expectedResponse.chatToggleTemplate.success,
            (viewModel.templateAvailability.value?.second as Success).data
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_switch_template_true_seller() {
        // Given
        val isEnable = true
        val isSeller = true
        val expectedResponse = ChatToggleTemplateResponse(
            chatToggleTemplate = ChatToggleTemplate(
                success = 1
            )
        )
        coEvery {
            toggleTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.toggleTemplate(isSeller, isEnable)

        // Then
        Assert.assertEquals(
            expectedResponse.chatToggleTemplate.success,
            (viewModel.templateAvailability.value?.second as Success).data
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_switch_template_false_buyer() {
        // Given
        val isEnable = false
        val isSeller = false
        val expectedResponse = ChatToggleTemplateResponse(
            chatToggleTemplate = ChatToggleTemplate(
                success = 1
            )
        )
        coEvery {
            toggleTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.toggleTemplate(isSeller, isEnable)

        // Then
        Assert.assertEquals(
            expectedResponse.chatToggleTemplate.success,
            (viewModel.templateAvailability.value?.second as Success).data
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_switch_template_false_seller() {
        // Given
        val isEnable = false
        val isSeller = true
        val expectedResponse = ChatToggleTemplateResponse(
            chatToggleTemplate = ChatToggleTemplate(
                success = 1
            )
        )
        coEvery {
            toggleTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.toggleTemplate(isSeller, isEnable)

        // Then
        Assert.assertEquals(
            expectedResponse.chatToggleTemplate.success,
            (viewModel.templateAvailability.value?.second as Success).data
        )
    }

    @Test
    fun should_give_error_when_fail_to_switch_template_buyer() {
        // Given
        val isEnable = true
        val isSeller = false
        coEvery {
            toggleTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.toggleTemplate(isSeller, isEnable)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.templateAvailability.value?.second as Fail).throwable.message
        )
    }

    @Test
    fun should_give_error_when_fail_to_switch_template_seller() {
        // Given
        val isEnable = true
        val isSeller = true
        coEvery {
            toggleTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.toggleTemplate(isSeller, isEnable)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.templateAvailability.value?.second as Fail).throwable.message
        )
    }
}

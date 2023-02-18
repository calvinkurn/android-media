package com.tokopedia.topchat.chattemplate.viewmodel.chat_template

import com.tokopedia.topchat.chattemplate.domain.pojo.ChatMoveTemplateResponse
import com.tokopedia.topchat.chattemplate.viewmodel.chat_template.base.BaseChatTemplateViewModelTest
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class ArrangeTemplateViewModelTest : BaseChatTemplateViewModelTest() {

    @Test
    fun should_give_chat_template_response_when_success_arrange_template_buyer() {
        // Given
        val isSeller = false
        val expectedResponse = ChatMoveTemplateResponse().apply {
            this.chatMoveTemplate.success = 1
        }
        coEvery {
            rearrangeTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.rearrangeTemplate(isSeller, 0, 1)

        // Then
        Assert.assertEquals(
            null,
            viewModel.arrangeTemplate.value?.error
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_arrange_template_seller() {
        // Given
        val isSeller = true
        val expectedResponse = ChatMoveTemplateResponse().apply {
            this.chatMoveTemplate.success = 1
        }
        coEvery {
            rearrangeTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.rearrangeTemplate(isSeller, 0, 1)

        // Then
        Assert.assertEquals(
            null,
            viewModel.arrangeTemplate.value?.error
        )
    }

    @Test
    fun should_give_error_when_fail_arrange_template_buyer() {
        // Given
        val isSeller = false
        coEvery {
            rearrangeTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.rearrangeTemplate(isSeller, 0, 1)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            viewModel.arrangeTemplate.value?.error?.message
        )
    }

    @Test
    fun should_give_error_when_fail_arrange_template_seller() {
        // Given
        val isSeller = true
        coEvery {
            rearrangeTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.rearrangeTemplate(isSeller, 0, 1)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            viewModel.arrangeTemplate.value?.error?.message
        )
    }
}

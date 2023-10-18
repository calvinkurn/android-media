package com.tokopedia.topchat.chattemplate.viewmodel.chat_template

import com.tokopedia.topchat.chattemplate.domain.pojo.GetChatTemplate
import com.tokopedia.topchat.chattemplate.domain.pojo.GetChatTemplateResponse
import com.tokopedia.topchat.chattemplate.domain.pojo.TopchatChatTemplates
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import com.tokopedia.topchat.chattemplate.viewmodel.chat_template.base.BaseChatTemplateViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class GetChatTemplateViewModelTest : BaseChatTemplateViewModelTest() {

    private val testTemplates = arrayListOf("template1", "template2")

    @Test
    fun should_give_chat_template_response_when_get_template_buyer() {
        // Given
        val expectedResponse = GetChatTemplateResponse(
            chatTemplatesAll = GetChatTemplate(
                buyerTemplate = TopchatChatTemplates(
                    isSeller = false,
                    isEnable = true,
                    templates = testTemplates
                )
            )
        )
        coEvery {
            getTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.getTemplate(false)

        // Then
        Assert.assertEquals(
            expectedResponse.chatTemplatesAll.buyerTemplate.templates.first(),
            (
                (viewModel.chatTemplate.value as Success).data.listTemplate.first()
                    as TemplateChatUiModel
                ).message
        )
    }

    @Test
    fun should_give_error_when_fail_to_get_template_buyer() {
        // Given
        coEvery {
            getTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.getTemplate(false)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.chatTemplate.value as Fail).throwable.message
        )
    }

    @Test
    fun should_give_chat_template_response_when_get_template_seller() {
        // Given
        val expectedResponse = GetChatTemplateResponse(
            chatTemplatesAll = GetChatTemplate(
                sellerTemplate = TopchatChatTemplates(
                    isSeller = true,
                    isEnable = true,
                    templates = testTemplates
                )
            )
        )
        coEvery {
            getTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.getTemplate(true)

        // Then
        Assert.assertEquals(
            expectedResponse.chatTemplatesAll.sellerTemplate.templates.first(),
            (
                (viewModel.chatTemplate.value as Success).data.listTemplate.first()
                    as TemplateChatUiModel
                ).message
        )
    }

    @Test
    fun should_give_error_when_fail_to_get_template_seller() {
        // Given
        coEvery {
            getTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.getTemplate(true)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.chatTemplate.value as Fail).throwable.message
        )
    }
}

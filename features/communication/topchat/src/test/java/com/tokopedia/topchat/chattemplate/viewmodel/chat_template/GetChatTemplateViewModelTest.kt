package com.tokopedia.topchat.chattemplate.viewmodel.chat_template

import com.tokopedia.topchat.chattemplate.domain.mapper.TemplateChatMapper.mapToTemplateUiModel
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
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData(
                isSuccess = true,
                isIsEnable = true,
                templates = testTemplates
            )
        )
        coEvery {
            getTemplateUseCase.getTemplate(any())
        } returns expectedResponse.data

        // When
        viewModel.getTemplate(false)

        // Then
        Assert.assertEquals(
            expectedResponse.data.mapToTemplateUiModel().isSuccess,
            (viewModel.chatTemplate.value as Success).data.isSuccess
        )
    }

    @Test
    fun should_give_error_when_fail_to_get_template_buyer() {
        // Given
        coEvery {
            getTemplateUseCase.getTemplate(any())
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
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData(
                isSuccess = true,
                isIsEnable = true,
                templates = testTemplates
            )
        )
        coEvery {
            getTemplateUseCase.getTemplate(any())
        } returns expectedResponse.data

        // When
        viewModel.getTemplate(true)

        // Then
        Assert.assertEquals(
            expectedResponse.data.mapToTemplateUiModel().isSuccess,
            (viewModel.chatTemplate.value as Success).data.isSuccess
        )
    }

    @Test
    fun should_give_error_when_fail_to_get_template_seller() {
        // Given
        coEvery {
            getTemplateUseCase.getTemplate(any())
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

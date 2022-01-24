package com.tokopedia.topchat.chattemplate.view.viewmodel

import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateDataWrapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class GetChatTemplateViewModelTest: BaseChatTemplateViewModel() {

    private val testTemplates = arrayListOf("template1", "template2")

    @Test
    fun should_give_chat_template_response_when_get_template_buyer() {
        //Given
        val expectedResponse = TemplateDataWrapper().also {
            it.templateData.isSuccess = true
            it.templateData.isIsEnable = true
            it.templateData.templates = testTemplates
        }
        coEvery {
            getTemplateUseCase.getTemplate(any())
        } returns expectedResponse.templateData

        //When
        viewModel.getTemplate(false)

        //Then
        Assert.assertEquals(
            expectedResponse.templateData.mapToTemplateUiModel().isSuccess,
            (viewModel.chatTemplate.value as Success).data.isSuccess
        )
    }

    @Test
    fun should_give_error_when_fail_to_get_template_buyer() {
        //Given
        coEvery {
            getTemplateUseCase.getTemplate(any())
        } throws expectedThrowable

        //When
        viewModel.getTemplate(false)

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.chatTemplate.value as Fail).throwable.message
        )
    }

    @Test
    fun should_give_chat_template_response_when_get_template_seller() {
        //Given
        val expectedResponse = TemplateDataWrapper().also {
            it.templateData.isSuccess = true
            it.templateData.isIsEnable = true
            it.templateData.templates = testTemplates
        }
        coEvery {
            getTemplateUseCase.getTemplate(any())
        } returns expectedResponse.templateData

        //When
        viewModel.getTemplate(true)

        //Then
        Assert.assertEquals(
            expectedResponse.templateData.mapToTemplateUiModel().isSuccess,
            (viewModel.chatTemplate.value as Success).data.isSuccess
        )
    }

    @Test
    fun should_give_error_when_fail_to_get_template_seller() {
        //Given
        coEvery {
            getTemplateUseCase.getTemplate(any())
        } throws expectedThrowable

        //When
        viewModel.getTemplate(true)

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.chatTemplate.value as Fail).throwable.message
        )
    }
}
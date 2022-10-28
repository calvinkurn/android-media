package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateResultModel
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class ChatTemplateViewModelTest: BaseTopChatViewModelTest() {

    @Test
    fun should_get_template_list_when_success_get_template_buyer_and_template_enabled() {
        //Given
        val expectedResponse = GetTemplateResultModel().also {
            it.isSuccess = true
            it.isEnabled = true
            it.listTemplate = arrayListOf(TemplateChatUiModel("Test Buyer"))
        }
        coEvery {
            getTemplateChatRoomUseCase.getTemplateChat(any())
        } returns expectedResponse

        //When
        viewModel.getTemplate(false)

        //Then
        Assert.assertEquals(
            expectedResponse.listTemplate.first(),
            (viewModel.templateChat.value as Success).data.first()
        )
    }

    @Test
    fun should_get_template_list_when_success_get_template_buyer_and_template_disabled() {
        //Given
        val expectedResponse = GetTemplateResultModel().also {
            it.isSuccess = true
            it.isEnabled = false
            it.listTemplate = arrayListOf(TemplateChatUiModel("Test Buyer"))
        }
        coEvery {
            getTemplateChatRoomUseCase.getTemplateChat(any())
        } returns expectedResponse

        //When
        viewModel.getTemplate(false)

        //Then
        Assert.assertEquals(
            null,
            (viewModel.templateChat.value as Success).data.firstOrNull()
        )
    }

    @Test
    fun should_get_template_list_when_success_get_template_seller_and_template_enabled() {
        //Given
        val expectedResponse = GetTemplateResultModel().also {
            it.isSuccess = true
            it.isEnabled = true
            it.listTemplate = arrayListOf(TemplateChatUiModel("Test Seller"))
        }
        coEvery {
            getTemplateChatRoomUseCase.getTemplateChat(any())
        } returns expectedResponse

        //When
        viewModel.getTemplate(false)

        //Then
        Assert.assertEquals(
            expectedResponse.listTemplate.first(),
            (viewModel.templateChat.value as Success).data.first()
        )
    }

    @Test
    fun should_get_template_list_when_success_get_template_seller_and_template_disabled() {
        //Given
        val expectedResponse = GetTemplateResultModel().also {
            it.isSuccess = true
            it.isEnabled = false
            it.listTemplate = arrayListOf(TemplateChatUiModel("Test Seller"))
        }
        coEvery {
            getTemplateChatRoomUseCase.getTemplateChat(any())
        } returns expectedResponse

        //When
        viewModel.getTemplate(false)

        //Then
        Assert.assertEquals(
            null,
            (viewModel.templateChat.value as Success).data.firstOrNull()
        )
    }

    @Test
    fun should_get_template_list_when_success_get_template_but_empty() {
        //Given
        val expectedResponse = GetTemplateResultModel().also {
            it.isSuccess = true
            it.isEnabled = true
            it.listTemplate = arrayListOf()
        }
        coEvery {
            getTemplateChatRoomUseCase.getTemplateChat(any())
        } returns expectedResponse

        //When
        viewModel.getTemplate(false)

        //Then
        Assert.assertEquals(
            null,
            (viewModel.templateChat.value as Success).data.firstOrNull()
        )
    }

    @Test
    fun should_get_error_when_fail_to_get_template() {
        //Given
        coEvery {
            getTemplateChatRoomUseCase.getTemplateChat(any())
        } throws expectedThrowable

        //When
        viewModel.getTemplate(false)

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.templateChat.value as Fail).throwable.message
        )
    }
}
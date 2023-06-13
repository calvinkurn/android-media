package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.topchat.chattemplate.domain.pojo.GetChatTemplateResponse
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class ChatTemplateViewModelTest : BaseTopChatViewModelTest() {

    @Test
    fun should_get_template_list_when_success_get_template_buyer_and_template_enabled() {
        // Given
        val dummyTemplate = "Test Buyer"
        val expectedResponse = GetChatTemplateResponse().apply {
            this.chatTemplatesAll.buyerTemplate.isEnable = true
            this.chatTemplatesAll.buyerTemplate.templates = arrayListOf(dummyTemplate)
        }
        coEvery {
            getTemplateChatRoomUseCase(any())
        } returns expectedResponse

        // When
        viewModel.getTemplate(false)

        // Then
        Assert.assertEquals(
            dummyTemplate,
            ((viewModel.templateChat.value as Success).data.first() as TemplateChatUiModel).message
        )
    }

    @Test
    fun should_get_template_list_when_success_get_template_buyer_and_template_disabled() {
        // Given
        val dummyTemplate = "Test Buyer"
        val expectedResponse = GetChatTemplateResponse().apply {
            this.chatTemplatesAll.buyerTemplate.isEnable = false
            this.chatTemplatesAll.buyerTemplate.templates = arrayListOf(dummyTemplate)
        }
        coEvery {
            getTemplateChatRoomUseCase(any())
        } returns expectedResponse

        // When
        viewModel.getTemplate(false)

        // Then
        Assert.assertEquals(
            null,
            (viewModel.templateChat.value as Success).data.firstOrNull()
        )
    }

    @Test
    fun should_get_template_list_when_success_get_template_seller_and_template_enabled() {
        // Given
        val dummyTemplate = "Test Seller"
        val expectedResponse = GetChatTemplateResponse().apply {
            this.chatTemplatesAll.sellerTemplate.isEnable = true
            this.chatTemplatesAll.sellerTemplate.templates = arrayListOf(dummyTemplate)
        }
        coEvery {
            getTemplateChatRoomUseCase(any())
        } returns expectedResponse

        // When
        viewModel.getTemplate(true)

        // Then
        Assert.assertEquals(
            dummyTemplate,
            ((viewModel.templateChat.value as Success).data.first() as TemplateChatUiModel).message
        )
    }

    @Test
    fun should_get_template_list_when_success_get_template_seller_and_template_disabled() {
        // Given
        val dummyTemplate = "Test Seller"
        val expectedResponse = GetChatTemplateResponse().apply {
            this.chatTemplatesAll.sellerTemplate.isEnable = false
            this.chatTemplatesAll.sellerTemplate.templates = arrayListOf(dummyTemplate)
        }
        coEvery {
            getTemplateChatRoomUseCase(any())
        } returns expectedResponse

        // When
        viewModel.getTemplate(true)

        // Then
        Assert.assertEquals(
            null,
            (viewModel.templateChat.value as Success).data.firstOrNull()
        )
    }

    @Test
    fun should_get_template_list_when_success_get_template_but_empty() {
        // Given
        val expectedResponse = GetChatTemplateResponse().apply {
            this.chatTemplatesAll.buyerTemplate.isEnable = true
            this.chatTemplatesAll.buyerTemplate.templates = arrayListOf()
        }
        coEvery {
            getTemplateChatRoomUseCase(any())
        } returns expectedResponse

        // When
        viewModel.getTemplate(false)

        // Then
        Assert.assertEquals(
            null,
            (viewModel.templateChat.value as Success).data.firstOrNull()
        )
    }

    @Test
    fun should_get_error_when_fail_to_get_template() {
        // Given
        coEvery {
            getTemplateChatRoomUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.getTemplate(false)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.templateChat.value as Fail).throwable.message
        )
    }
}

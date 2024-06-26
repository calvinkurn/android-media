package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.FileUtil
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.pojo.preattach.PreAttachPayloadResponse
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Test

class PreloadProductAttachmentViewModelTest : BaseTopChatViewModelTest() {

    val realChatMapper = ChatAttachmentMapper()
    lateinit var defaultPreAttachResponse: PreAttachPayloadResponse

    override fun before() {
        super.before()
        defaultPreAttachResponse = FileUtil.parse(
            "/success_pre_attach_payload_response.json",
            PreAttachPayloadResponse::class.java
        )
    }

    @Test
    fun should_pre_load_product_attachment() {
        // Given
        val product = defaultPreAttachResponse.chatPreAttachPayload.list[0]
        coEvery { chatPreAttachPayload(any()) } returns defaultPreAttachResponse
        coEvery { chatAttachmentMapper.map(any<PreAttachPayloadResponse>()) } returns realChatMapper.map(defaultPreAttachResponse)
        viewModel.updateMessageId(testMessageId)

        // When
        viewModel.loadProductPreview(listOf(product.id))
        viewModel.loadProductPreview(listOf(product.id))

        // Then
        assertEquals(viewModel.attachmentPreviewData.size, 1)
    }

    @Test
    fun should_reload_pre_load_product_attachment() {
        // Given
        coEvery { chatPreAttachPayload(any()) } returns defaultPreAttachResponse
        coEvery { chatAttachmentMapper.map(any<PreAttachPayloadResponse>()) } returns realChatMapper.map(defaultPreAttachResponse)
        viewModel.updateMessageId(testMessageId)

        // When
        viewModel.loadProductPreview(listOf("1"))
        viewModel.reloadCurrentAttachment()

        // Then
        assertEquals(viewModel.attachmentPreviewData.size, 1)
    }

    @Test
    fun should_not_ready_when_attachment_preview_is_loading() {
        // Given
        coEvery { chatPreAttachPayload(any()) } returns defaultPreAttachResponse
        coEvery { chatAttachmentMapper.map(any<PreAttachPayloadResponse>()) } returns realChatMapper.map(defaultPreAttachResponse)

        // When
        viewModel.loadProductPreview(listOf("1"))

        // Then
        assertEquals(viewModel.isAttachmentPreviewReady(), false)
    }

    @Test
    fun should_give_false_even_when_attachment_preview_null() {
        // Given
        viewModel.setAttachmentsPreview(null)

        // When
        val result = viewModel.isAttachmentPreviewReady()

        // Then
        assertEquals(false, result)
    }

    @Test
    fun should_give_false_when_attachment_preview_is_loading() {
        // Given
        val productAttachment = TopchatProductAttachmentPreviewUiModel.Builder().build().apply {
            isLoading = true
            isError = false
        }
        viewModel.setAttachmentsPreview(arrayListOf(productAttachment))

        // When
        val result = viewModel.isAttachmentPreviewReady()

        // Then
        assertEquals(false, result)
    }

    @Test
    fun should_give_false_when_attachment_preview_is_error() {
        // Given
        val productAttachment = TopchatProductAttachmentPreviewUiModel.Builder().build().apply {
            isLoading = false
            isError = true
        }
        viewModel.setAttachmentsPreview(arrayListOf(productAttachment))

        // When
        val result = viewModel.isAttachmentPreviewReady()

        // Then
        assertEquals(false, result)
    }

    @Test
    fun should_give_false_when_attachment_preview_is_loading_and_error() {
        // Given
        val productAttachment = TopchatProductAttachmentPreviewUiModel.Builder().build().apply {
            isLoading = true
            isError = true
        }
        viewModel.setAttachmentsPreview(arrayListOf(productAttachment))

        // When
        val result = viewModel.isAttachmentPreviewReady()

        // Then
        assertEquals(false, result)
    }

    @Test
    fun should_pre_load_the_same_product_attachment_once_even_when_product_id_changed() {
        // Given
        val product = defaultPreAttachResponse.chatPreAttachPayload.list[0]
        coEvery {
            chatPreAttachPayload(any())
        } returns defaultPreAttachResponse
        coEvery {
            chatAttachmentMapper.map(any<PreAttachPayloadResponse>())
        } returns realChatMapper.map(defaultPreAttachResponse)
        viewModel.updateMessageId(testMessageId)

        // When
        viewModel.loadProductPreview(listOf("parent product id"))
        viewModel.loadProductPreview(listOf("parent product id"))

        // Then
        assertEquals(viewModel.attachmentPreviewData.size, 1)
        assertEquals(
            (
                viewModel.attachmentPreviewData[product.id]?.parsedAttributes as
                    ProductAttachmentAttributes
                ).productId,
            product.id
        )
    }

    @Test
    fun should_reload_pre_load_product_attachment_even_when_product_id_changed() {
        // Given
        val expectedThrowable = Throwable("Oops!")
        val product = defaultPreAttachResponse.chatPreAttachPayload.list[0]
        coEvery {
            chatAttachmentMapper.map(any<PreAttachPayloadResponse>())
        } returns realChatMapper.map(defaultPreAttachResponse)
        viewModel.updateMessageId(testMessageId)

        // When fail to pre load
        coEvery {
            chatPreAttachPayload(any())
        } throws expectedThrowable
        viewModel.loadProductPreview(listOf("parent product id"))

        // When success to pre load
        coEvery {
            chatPreAttachPayload(any())
        } returns defaultPreAttachResponse
        viewModel.reloadCurrentAttachment()

        // Then
        assertEquals(viewModel.attachmentPreviewData.size, 1)
        assertEquals(
            (
                viewModel.attachmentPreviewData[product.id]?.parsedAttributes as
                    ProductAttachmentAttributes
                ).productId,
            product.id
        )
    }

    @Test
    fun should_give_null_or_empty_when_attachment_preview_not_set() {
        // When
        val result = viewModel.hasEmptyAttachmentPreview()

        // Then
        assertEquals(true, result)
    }

    @Test
    fun should_do_nothing_when_pending_product_view_empty() {
        // Given
        viewModel.pendingLoadProductPreview.clear()

        // When
        viewModel.loadPendingProductPreview()

        // Then
        assertEquals(viewModel.attachmentsPreview.value, arrayListOf<SendablePreview>())
    }

    @Test
    fun should_call_preattach_when_pending_product_view_empty() {
        // Given
        viewModel.roomMetaData.value?.updateMessageId(testMessageId)
        viewModel.pendingLoadProductPreview = arrayListOf("testProductId")

        // When
        viewModel.loadPendingProductPreview()

        // Then
        coVerify(exactly = 1) {
            chatPreAttachPayload(any())
        }
    }

    @Test
    fun should_pre_load_product_attachment_even_when_user_location_null() {
        // Given
        val product = defaultPreAttachResponse.chatPreAttachPayload.list[0]
        coEvery { chatPreAttachPayload(any()) } returns defaultPreAttachResponse
        coEvery { chatAttachmentMapper.map(any<PreAttachPayloadResponse>()) } returns realChatMapper.map(defaultPreAttachResponse)
        viewModel.updateMessageId(testMessageId)
        viewModel.initUserLocation(null)

        // When
        viewModel.loadProductPreview(listOf(product.id))
        viewModel.loadProductPreview(listOf(product.id))

        // Then
        assertEquals(viewModel.attachmentPreviewData.size, 1)
    }

    @Test
    fun should_pre_load_product_attachment_when_user_location_complete_and_null_messageId() {
        // Given
        val product = defaultPreAttachResponse.chatPreAttachPayload.list[0]
        coEvery { chatPreAttachPayload(any()) } returns defaultPreAttachResponse
        coEvery {
            chatAttachmentMapper.map(any<PreAttachPayloadResponse>())
        } returns realChatMapper.map(defaultPreAttachResponse)
        viewModel.updateMessageId(testMessageId)
        viewModel.initUserLocation(
            LocalCacheModel(
                address_id = "123",
                district_id = "123"
            )
        )

        // When
        viewModel.loadProductPreview(listOf(product.id))

        // Then
        coVerify(exactly = 1) {
            chatPreAttachPayload(any())
        }
    }

    @Test
    fun should_not_pre_load_product_attachment_when_room_meta_data_message_id_zero() {
        // Given
        val product = defaultPreAttachResponse.chatPreAttachPayload.list[0]
        viewModel.initUserLocation(null)
        viewModel.setRoomMetaData(RoomMetaData(_msgId = "0"))

        // When
        viewModel.loadProductPreview(listOf(product.id))

        // Then
        coVerify(exactly = 0) {
            chatPreAttachPayload(any())
        }
    }

    @Test
    fun should_not_pre_load_product_attachment_when_room_meta_data_null() {
        // Given
        val product = defaultPreAttachResponse.chatPreAttachPayload.list[0]
        viewModel.setRoomMetaData(null)

        // When
        viewModel.loadProductPreview(listOf(product.id))

        // Then
        coVerify(exactly = 0) {
            chatPreAttachPayload(any())
        }
    }
}

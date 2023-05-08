package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.attachcommon.data.VoucherPreview
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.topchat.chatroom.view.uimodel.SendableVoucherPreviewUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Test

class AttachmentPreviewViewModelTest : BaseTopChatViewModelTest() {

    val resultProduct = arrayListOf(
        "1"
    )

    @Test
    fun should_add_product_attachment_preview_on_initialization_not_empty() {
        // Given
        viewModel.updateMessageId(testMessageId)

        // When
        viewModel.loadProductPreview(arrayListOf())
        viewModel.loadProductPreview(resultProduct)

        // Then
        assertEquals(viewModel.attachmentsPreview.value?.size, 1)
    }

    @Test
    fun should_map_product_preview_product_id() {
        // Given
        viewModel.updateMessageId(testMessageId)

        // When
        viewModel.loadProductPreview(resultProduct)
        val productIds = viewModel.getProductIdPreview()

        // Then
        assertEquals(productIds[0], resultProduct[0])
    }

    @Test
    fun should_get_attachment_preview_when_init() {
        // Given
        val sendablePreview = TopchatProductAttachmentPreviewUiModel.Builder()
            .build()

        // When
        viewModel.addAttachmentPreview(sendablePreview)
        viewModel.initAttachmentPreview()

        // Then
        assertEquals(
            sendablePreview,
            viewModel.showableAttachmentPreviews.value?.firstOrNull()
        )
    }

    @Test
    fun should_not_get_product_attachment_when_removed() {
        // Given
        val testSendAblePreviewOne = TopchatProductAttachmentPreviewUiModel.Builder().build()
        val testSendAblePreviewTwo = TopchatProductAttachmentPreviewUiModel.Builder().build()

        // When
        // Init attachment
        viewModel.addAttachmentPreview(testSendAblePreviewOne)
        viewModel.addAttachmentPreview(testSendAblePreviewTwo)
        viewModel.initAttachmentPreview()

        // Remove attachment
        viewModel.removeAttachmentPreview(testSendAblePreviewOne)
        val attachmentList = viewModel.attachmentsPreview.value

        // Then
        assertEquals(
            attachmentList?.size,
            1
        )
        assertEquals(
            attachmentList?.first(),
            testSendAblePreviewTwo
        )
    }

    @Test
    fun should_do_nothing_when_add_attachment_preview_but_null() {
        // Given
        val dummyPreview = TopchatProductAttachmentPreviewUiModel.Builder().build()
        viewModel.setAttachmentsPreview(null)

        // When
        viewModel.addAttachmentPreview(dummyPreview)

        // Then
        assertEquals(null, viewModel.attachmentsPreview.value)
    }

    @Test
    fun should_do_nothing_when_reload_attachment_but_null() {
        // Given
        viewModel.setRoomMetaData(RoomMetaData(_msgId = testMessageId))
        viewModel.setAttachmentsPreview(null)

        // When
        viewModel.reloadCurrentAttachment()

        // Then
        coVerify(exactly = 0) {
            chatPreAttachPayload(any())
        }
    }

    @Test
    fun should_do_nothing_when_reload_attachment_but_not_product() {
        // Given
        viewModel.setRoomMetaData(RoomMetaData(_msgId = testMessageId))
        viewModel.setAttachmentsPreview(
            arrayListOf(SendableVoucherPreviewUiModel(VoucherPreview()))
        )

        // When
        viewModel.reloadCurrentAttachment()

        // Then
        coVerify(exactly = 0) {
            chatPreAttachPayload(any())
        }
    }

    @Test
    fun should_do_nothing_when_load_product_preview_but_null_room_meta_data() {
        // Given
        viewModel.setRoomMetaData(RoomMetaData(_msgId = testMessageId))
        viewModel.setAttachmentsPreview(
            arrayListOf(SendableVoucherPreviewUiModel(VoucherPreview()))
        )

        // When
        viewModel.reloadCurrentAttachment()

        // Then
        coVerify(exactly = 0) {
            chatPreAttachPayload(any())
        }
    }
}

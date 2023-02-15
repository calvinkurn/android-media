package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
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
}

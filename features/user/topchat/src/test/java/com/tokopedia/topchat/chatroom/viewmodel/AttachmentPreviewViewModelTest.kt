package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AttachmentPreviewViewModelTest : BaseTopChatViewModelTest() {

    val resultProduct = arrayListOf(
            "1"
    )

    @Test
    fun should_add_product_attachment_preview_on_initialization_not_empty() {
        // When
        viewModel.loadProductPreview(arrayListOf())
        viewModel.loadProductPreview(resultProduct)

        // Then
        assertEquals(viewModel.getAttachmentsPreview().size, 1)
    }

    @Test
    fun should_map_product_preview_product_id() {
        // When
        viewModel.loadProductPreview(resultProduct)
        val productIds = viewModel.getProductIdPreview()

        // Then
        assertEquals(productIds[0], resultProduct[0])
    }

    @Test
    fun should_get_attachment_preview_when_init() {
        //Given
        val sendablePreview = TopchatProductAttachmentPreviewUiModel.Builder()
                .build()

        //When
        viewModel.addAttachmentPreview(sendablePreview)
        viewModel.initAttachmentPreview()

        //Then
        assertEquals(
            sendablePreview,
            viewModel.showableAttachmentPreviews.value?.firstOrNull()
        )
    }
}
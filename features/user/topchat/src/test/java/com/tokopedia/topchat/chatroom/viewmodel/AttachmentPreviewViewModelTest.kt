package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AttachmentPreviewViewModelTest : BaseTopChatViewModelTest() {

    val resultProduct = arrayListOf(
        ResultProduct(
            name = "a",
            productImageThumbnail = "h",
            price = "1",
            productId = "1"
        )
    )

    @Test
    fun should_add_product_attachment_preview_on_initialization_not_empty() {
        // When
        viewModel.initProductPreviewFromAttachProduct(arrayListOf())
        viewModel.initProductPreviewFromAttachProduct(resultProduct)

        // Then
        assertEquals(viewModel.getAttachmentsPreview().size, 1)
    }

}
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

    val notEnoughRequiredProductData = arrayListOf(
        ResultProduct(
            name = "",
            productImageThumbnail = "",
            price = "",
            productId = ""
        )
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
    fun should_not_add_product_attachment_preview_on_initialization_when_not_enough_required_data() {
        // When
        viewModel.loadProductPreview(notEnoughRequiredProductData)

        // Then
        assertEquals(viewModel.getAttachmentsPreview().size, 0)
    }

    @Test
    fun should_map_product_preview_product_id() {
        // When
        viewModel.loadProductPreview(resultProduct)
        val productIds = viewModel.getProductIdPreview()

        // Then
        assertEquals(productIds[0], resultProduct[0].productId)
    }
}
package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import org.junit.Assert
import org.junit.Test

class OnGoingStockUpdateViewModelTest: BaseTopChatViewModelTest() {

    @Test
    fun should_add_to_onGoingStockUpdate_list() {
        val testProductId = "productId1"
        val testProductAttachment = ProductAttachmentUiModel.Builder().build()
        val testPosition = 1

        //When
        viewModel.addOngoingUpdateProductStock(
            productId = testProductId,
            product = testProductAttachment,
            adapterPosition = testPosition,
            parentMetaData = null
        )

        //Then
        Assert.assertEquals(
            testProductAttachment,
            viewModel.onGoingStockUpdate[testProductId]?.product
        )
    }
}
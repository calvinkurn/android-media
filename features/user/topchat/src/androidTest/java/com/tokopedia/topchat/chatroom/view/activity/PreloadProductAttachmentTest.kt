package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.topchat.chatroom.view.activity.TopchatRoomBuyerProductAttachmentTest.Companion.putProductAttachmentIntent
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductPreviewResult
import org.junit.Test

class PreloadProductAttachmentTest: TopchatRoomTest() {

    @Test
    fun should_show_loading_product_preview() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase.response
        getChatPreAttachPayloadUseCase.delayResponseIndefinitely()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        ProductPreviewResult.isLoadingAt(0)
    }

    // TODO: should render actual product data for product preview when success pre attach product payload
    // TODO: should show error product preview when error pre attach product payload
    // TODO: should retry preload product attachment when user click retry
    // TODO: should send text only when attach product preview is loading
    // TODO: should send sticker only when attach product preview is loading
    // TODO: should send text only when attach product preview is error
    // TODO: should send sticker only when attach product preview is loading
    // TODO: should show srw preview when attach product preview is success
    // TODO: should hide srw preview when attach product preview is loading
    // TODO: should hide srw preview when attach product preview is error
    // TODO: should show srw preview when user retry attach product preview is success

}
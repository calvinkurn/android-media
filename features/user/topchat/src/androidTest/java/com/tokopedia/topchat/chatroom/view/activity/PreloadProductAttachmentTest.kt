package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.topchat.chatroom.view.activity.TopchatRoomBuyerProductAttachmentTest.Companion.exProductId
import com.tokopedia.topchat.chatroom.view.activity.TopchatRoomBuyerProductAttachmentTest.Companion.putProductAttachmentIntent
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductPreviewResult
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductPreviewRobot
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

    @Test
    fun should_render_actual_product_data_for_product_preview_when_success_pre_attach_product_payload() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
                .generatePreAttachPayload(exProductId)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        ProductPreviewResult.isNotLoadingAt(0)
        ProductPreviewResult.isNotErrorAt(0)
    }

    @Test
    fun should_show_error_product_preview_when_error_pre_attach_product_payload() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.setError()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        ProductPreviewResult.isErrorAt(0)
    }

    @Test
    fun should_retry_preload_product_attachment_when_user_click_retry() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.setError()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
                .generatePreAttachPayload(exProductId)
        ProductPreviewRobot.clickRetryButtonAt(0)

        // Then
        ProductPreviewResult.isNotErrorAt(0)
        ProductPreviewResult.isNotLoadingAt(0)
    }

    @Test
    fun should_send_text_only_when_attach_product_preview_is_loading() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.delayResponseIndefinitely()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickComposeArea()
        typeMessage("test")
        clickSendBtn()

        // Then
        ProductPreviewResult.isLoadingAt(0)
    }

    @Test
    fun should_send_sticker_only_when_attach_product_preview_is_loading() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.delayResponseIndefinitely()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickStickerIconMenu()
        clickStickerAtPosition(0)

        // Then
        ProductPreviewResult.isLoadingAt(0)
    }

    @Test
    fun should_send_text_only_when_attach_product_preview_is_error() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.setError()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickComposeArea()
        typeMessage("test")
        clickSendBtn()

        // Then
        ProductPreviewResult.isErrorAt(0)
    }

    @Test
    fun should_send_sticker_only_when_attach_product_preview_is_error() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.setError()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickStickerIconMenu()
        clickStickerAtPosition(0)

        // Then
        ProductPreviewResult.isErrorAt(0)
    }

    // TODO: should show srw preview when attach product preview is success
    // TODO: should hide srw preview when attach product preview is loading
    // TODO: should hide srw preview when attach product preview is error
    // TODO: should show srw preview when user retry attach product preview is success
    // TODO: should hide close button on loading state

}
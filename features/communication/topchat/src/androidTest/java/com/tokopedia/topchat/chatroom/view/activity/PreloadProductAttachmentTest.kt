package com.tokopedia.topchat.chatroom.view.activity

import android.content.Intent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductPreviewResult
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductPreviewRobot
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class PreloadProductAttachmentTest : TopchatRoomTest() {

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
        // should hide close button on loading state
        ProductPreviewResult.isNotCloseableAt(0)
        ProductPreviewResult.isLoadingAt(0)
    }

    @Test
    fun should_render_actual_product_data_for_product_preview_when_success_pre_attach_product_payload() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(EX_PRODUCT_ID)
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
        // should show close button on error state
        ProductPreviewResult.isCloseableAt(0)
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
            .generatePreAttachPayload(EX_PRODUCT_ID)
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

    @Test
    fun should_show_srw_preview_when_attach_product_preview_is_success() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwUseCase.defaultResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(EX_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        assertSrwPreviewContentIsVisible()
    }

    @Test
    fun should_hide_srw_preview_when_attach_product_preview_is_loading() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwUseCase.defaultResponse
        getChatPreAttachPayloadUseCase.delayResponseIndefinitely()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        assertTemplateChatVisibility(not(isDisplayed()))
        assertSrwPreviewContentIsHidden()
    }

    @Test
    fun should_hide_srw_preview_when_attach_product_preview_is_error() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwUseCase.defaultResponse
        getChatPreAttachPayloadUseCase.setError()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        assertTemplateChatVisibility(not(isDisplayed()))
        assertSrwPreviewContentIsHidden()
    }

    @Test
    fun should_show_srw_preview_when_user_retry_attach_product_preview_is_success() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwUseCase.defaultResponse
        getChatPreAttachPayloadUseCase.setError()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(EX_PRODUCT_ID)
        ProductPreviewRobot.clickRetryButtonAt(0)

        // Then
        assertSrwPreviewContentIsVisible()
    }

    @Test
    fun should_show_preview_product_attachment_even_when_product_ids_changed() {
        // Given
        val childProductId = "childProductId"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(childProductId)
        launchChatRoomActivity {
            putParentProductAttachmentIntent(it)
        }

        // Then
        ProductPreviewResult.isNotLoadingAt(0)
        ProductPreviewResult.isNotErrorAt(0)
    }

    @Test
    fun should_retry_preload_product_attachment_when_user_click_retry_even_when_product_ids_changed() {
        // Given
        val childProductId = "childProductId"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.setError()
        launchChatRoomActivity {
            putParentProductAttachmentIntent(it)
        }

        // When
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(childProductId)
        ProductPreviewRobot.clickRetryButtonAt(0)

        // Then
        ProductPreviewResult.isNotErrorAt(0)
        ProductPreviewResult.isNotLoadingAt(0)
    }

    @Test
    fun should_show_srw_preview_when_attach_product_preview_is_success_even_when_product_ids_changed() {
        // Given
        val childProductId = "childProductId"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwUseCase.defaultResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(childProductId)
        launchChatRoomActivity {
            putParentProductAttachmentIntent(it)
        }

        // Then
        assertSrwPreviewContentIsVisible()
    }

    @Test
    fun should_show_srw_preview_when_user_retry_attach_product_preview_is_success_even_when_product_ids_changed() {
        // Given
        val childProductId = "childProductId"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwUseCase.defaultResponse
        getChatPreAttachPayloadUseCase.setError()
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(childProductId)
        ProductPreviewRobot.clickRetryButtonAt(0)

        // Then
        assertSrwPreviewContentIsVisible()
    }

    private fun putParentProductAttachmentIntent(intent: Intent) {
        val parentProductId = "parentId"
        val productIds = listOf(parentProductId)
        val stringProductPreviews = CommonUtil.toJson(productIds)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }
}

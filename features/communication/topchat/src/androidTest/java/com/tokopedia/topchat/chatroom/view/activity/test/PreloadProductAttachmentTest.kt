package com.tokopedia.topchat.chatroom.view.activity.test

import android.content.Intent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaResult
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.productPreviewResult
import com.tokopedia.topchat.chatroom.view.activity.robot.productPreviewRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.srwResult
import com.tokopedia.topchat.chatroom.view.activity.test.buyer.TopchatRoomBuyerProductAttachmentTest.Companion.exProductId
import com.tokopedia.topchat.chatroom.view.activity.test.buyer.TopchatRoomBuyerProductAttachmentTest.Companion.putProductAttachmentIntent
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
        productPreviewResult {
            isNotCloseableAt(0)
            isLoadingAt(0)
        }
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
        productPreviewResult {
            isNotLoadingAt(0)
            isNotErrorAt(0)
        }
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
        productPreviewResult {
            isCloseableAt(0)
            isErrorAt(0)
        }
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
        productPreviewRobot {
            clickRetryButtonAt(0)
        }

        // Then
        productPreviewResult {
            isNotErrorAt(0)
            isNotLoadingAt(0)
        }
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
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("test")
            clickSendBtn()
        }

        // Then
        productPreviewResult {
            isLoadingAt(0)
        }
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
        composeAreaRobot {
            clickStickerIconMenu()
            clickStickerAtPosition(0)
        }

        // Then
        productPreviewResult {
            isLoadingAt(0)
        }
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
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("test")
            clickSendBtn()
        }

        // Then
        productPreviewResult {
            isErrorAt(0)
        }
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
        composeAreaRobot {
            clickStickerIconMenu()
            clickStickerAtPosition(0)
        }

        // Then
        productPreviewResult {
            isErrorAt(0)
        }
    }

    @Test
    fun should_show_srw_preview_when_attach_product_preview_is_success() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwUseCase.defaultResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(exProductId)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
        }
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
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
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
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
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
            .generatePreAttachPayload(exProductId)
        productPreviewRobot {
            clickRetryButtonAt(0)
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
        }
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
        productPreviewResult {
            isNotLoadingAt(0)
            isNotErrorAt(0)
        }
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
        productPreviewRobot {
            clickRetryButtonAt(0)
        }

        // Then
        productPreviewResult {
            isNotLoadingAt(0)
            isNotErrorAt(0)
        }
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
        srwResult {
            assertSrwPreviewContentIsVisible()
        }
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
        productPreviewRobot {
            clickRetryButtonAt(0)
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
        }
    }

    private fun putParentProductAttachmentIntent(intent: Intent) {
        val parentProductId = "parentId"
        val productIds = listOf(parentProductId)
        val stringProductPreviews = CommonUtil.toJson(productIds)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }
}

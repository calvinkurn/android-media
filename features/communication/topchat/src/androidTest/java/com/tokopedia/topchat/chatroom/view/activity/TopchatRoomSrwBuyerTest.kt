package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.changeTimeStampTo
import com.tokopedia.topchat.chatroom.view.activity.base.hasQuestion
import com.tokopedia.topchat.chatroom.view.activity.base.matchProductWith
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaResult
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.msgBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.productPreviewRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.srwResult
import com.tokopedia.topchat.chatroom.view.activity.robot.srwRobot
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.SOURCE_TOPCHAT
import com.tokopedia.topchat.common.websocket.FakeTopchatWebSocket
import com.tokopedia.utils.time.RfcDateTimeParser
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class TopchatRoomSrwBuyerTest : BaseBuyerTopchatRoomTest() {

    companion object {
        private const val DEFAULT_PRODUCT_ID = "1111"
    }

    @Test
    fun srw_preview_displayed_if_buyer_attach_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
            assertSrwTotalQuestion(1)
        }
    }

    @Test
    fun srw_preview_displayed_with_multiple_questions_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponseMultipleQuestion
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
            assertSrwTotalQuestion(3)
        }
    }

    @Test
    fun srw_preview_displayed_if_buyer_attach_from_attach_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity()
        intendingAttachProduct(1)

        // When
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
            assertSrwTotalQuestion(1)
        }
    }

    @Test
    fun srw_preview_no_question_not_displayed_if_buyer_attach_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun template_chat_shown_if_srw_preview_load_finish_first_with_no_question() {
        // Given
        val templateDelay = 500L
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        getTemplateChatRoomUseCase.response = getTemplateChatRoomUseCase.successGetTemplateResponseBuyer
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        // Simulate template chat load last after SRW
        Thread.sleep(templateDelay + 10)

        // Then
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun template_chat_remain_hidden_if_user_click_send_btn_when_srw_preview_shown() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        composeAreaRobot {
            clickSendBtn()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
        }
    }

    @Test
    fun srw_preview_no_question_not_displayed_if_buyer_attach_from_attach_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity()
        intendingAttachProduct(1)

        // When
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun srw_preview_loading_state_displayed_if_buyer_attach_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.setResponseWithDelay(
            chatSrwResponse,
            1500
        )
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
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_preview_error_state_displayed_if_buyer_attach_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.isError = true
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun assert_srw_preview_expand_collapse_interaction() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }

        // When
        srwRobot {
            clickOnSrwPartial()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewCollapsed()
        }

        // When
        srwRobot {
            clickOnSrwPartial()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }
    }

    @Test
    fun srw_preview_content_hidden_if_content_is_clicked() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
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
    fun collapse_srw_preview_when_user_open_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickComposeArea()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewCollapsed()
        }
    }

    @Test
    fun expand_srw_preview_when_user_hide_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickComposeArea()
        }
        generalRobot {
            pressBack()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }
    }

    @Test
    fun expand_srw_preview_when_keyboard_opened() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickComposeArea()
        }
        srwRobot {
            clickOnSrwPartial()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }
        generalResult {
            assertKeyboardIsNotVisible(activity)
        }
    }

    @Test
    fun collapse_srw_preview_when_keyboard_opened() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickComposeArea()
        }
        srwRobot {
            clickOnSrwPartial()
            clickOnSrwPartial()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewCollapsed()
        }
    }

    /**
     * SRW should expanded when reattach product
     * when previous product preview is collapsed and canceled
     */
    @Test
    fun srw_preview_should_expanded_when_reattach_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    getAttachProductData(1)
                )
            )

        // When
        composeAreaRobot {
            clickComposeArea()
        }
        productPreviewRobot {
            clickCloseAttachmentPreview(0)
        }
        generalRobot {
            pressBack()
        }
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }
    }

    @Test
    fun srw_bubble_should_displayed_after_click_srw_preview() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_always_stays_at_the_bottom_when_receive_response_msg_from_ws() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponse(wsMineResponseText)
        websocket.simulateResponse(wsMineResponseText)
        websocket.simulateResponse(wsInterlocutorResponseText)
        websocket.simulateResponse(wsInterlocutorResponseText)

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_always_stays_at_the_bottom_when_receive_response_different_day_msg_from_ws() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        websocket.simulateResponse(
            wsMineResponseText.changeTimeStampTo(today())
        )

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_removed_when_user_request_sent_invoice() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachInvoiceMenu()
            clickComposeArea()
            typeMessageComposeArea("Sending Invoice")
            clickSendBtn()
        }

        // Then
        srwResult {
            assertSrwBubbleDoesNotExist()
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun srw_bubble_should_removed_when_user_receive_invoice_event_from_ws() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        websocket.simulateResponse(wsSellerInvoiceResponse.changeTimeStampTo(today()))
        websocket.simulateResponse(wsSellerResponseText.changeTimeStampTo(today()))

        // Then
        srwResult {
            assertSrwBubbleDoesNotExist()
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun srw_preview_should_removed_when_user_re_attach_preview_with_invoice() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachInvoiceMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun srw_bubble_should_removed_when_user_receive_attach_image_event_from_ws() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData())
        )

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        websocket.simulateResponse(wsSellerImageResponse.changeTimeStampTo(today()))

        // Then
        srwResult {
            assertSrwBubbleDoesNotExist()
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun srw_preview_should_not_be_sent_when_user_attach_image() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData())
        )

        // When
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachImageMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleDoesNotExist()
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_removed_when_user_re_attach_product_in_preview_mode() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachProductResult(1))

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleDoesNotExist()
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun template_chat_should_be_hidden_when_srw_bubble_exist() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_preview_should_collapsed_when_user_open_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickStickerIconMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewCollapsed()
        }
    }

    @Test
    fun srw_preview_should_expanded_when_user_close_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickStickerIconMenu()
        }
        generalRobot {
            pressBack()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }
    }

    @Test
    fun srw_preview_should_collapsed_when_user_open_chat_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickPlusIconMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewCollapsed()
        }
    }

    @Test
    fun srw_preview_should_expanded_when_user_close_chat_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickPlusIconMenu()
        }
        composeAreaRobot {
            clickPlusIconMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }
    }

    @Test
    fun srw_preview_should_remain_collapsed_when_user_open_sticker_menu_and_click_open_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickStickerIconMenu()
            clickStickerIconMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewCollapsed()
        }
    }

    @Test
    fun srw_preview_should_remain_collapsed_when_user_open_keyboard_and_open_sticker_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickComposeArea()
            clickStickerIconMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewCollapsed()
        }
    }

    @Test
    fun chat_menu_should_be_hidden_when_SRW_preview_expanded() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickPlusIconMenu()
        }
        srwRobot {
            clickSrwPreviewExpandCollapse()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }
        composeAreaResult {
            assertChatMenuVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun chat_sticker_should_be_hidden_when_srw_preview_expanded() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickStickerIconMenu()
        }
        srwRobot {
            clickSrwPreviewExpandCollapse()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwPreviewExpanded()
        }
        composeAreaResult {
            assertChatMenuVisibility(not(isDisplayed()))
            assertChatStickerMenuVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_expanded_when_user_open_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickStickerIconMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertChatMenuVisibility((isDisplayed()))
            assertChatStickerMenuVisibility((isDisplayed()))
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_expanded_when_user_close_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickStickerIconMenu()
        }
        generalRobot {
            pressBack()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertChatMenuVisibility(not((isDisplayed())))
            assertChatStickerMenuVisibility(not((isDisplayed())))
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_expanded_when_user_open_chat_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickPlusIconMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertChatMenuVisibility(((isDisplayed())))
            assertChatAttachmentMenuVisibility(isDisplayed())
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_expanded_when_user_close_chat_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickPlusIconMenu()
        }
        composeAreaRobot {
            clickPlusIconMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertChatMenuVisibility(not((isDisplayed())))
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun chat_menu_should_be_hidden_when_srw_bubble_expanded() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickPlusIconMenu()
        }
        srwRobot {
            clickSrwBubbleExpandCollapse(0) // Collapse
            clickSrwBubbleExpandCollapse(0) // Expand
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertChatMenuVisibility(not((isDisplayed())))
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun chat_Sticker_should_be_hidden_when_srw_bubble_expanded() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickStickerIconMenu()
        }
        srwRobot {
            clickSrwBubbleExpandCollapse(0) // Collapse
            clickSrwBubbleExpandCollapse(0) // Expand
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertChatMenuVisibility(not((isDisplayed())))
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_remain_expanded_when_user_open_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickComposeArea()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_expanded_when_user_close_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickComposeArea()
        }
        generalRobot {
            pressBack()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_remain_expanded_when_user_click_sticker_menu_and_click_open_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickStickerIconMenu()
            clickStickerIconMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_remain_expanded_when_user_open_keyboard_and_open_sticker_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickComposeArea()
            clickStickerIconMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_Bubble_should_collapsed_when_preview_attachment_is_visible_other_than_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachInvoiceMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleCollapsed(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_Bubble_should_expanded_when_preview_attachment_is_not_visible() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachInvoiceMenu()
        }
        productPreviewRobot {
            clickCloseAttachmentPreview(0)
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_Bubble_should_remain_collapsed_when_invoice_preview_visible_and_user_toggle_chat_menu_on() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachInvoiceMenu()
            clickPlusIconMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleCollapsed(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_Bubble_should_remain_collapsed_when_invoice_preview_visible_and_user_toggle_chat_menu_on_then_off() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachInvoiceMenu()
            clickPlusIconMenu()
            clickPlusIconMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleCollapsed(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_maintain_state_expand_or_collapse_if_scrolled_far_top_and_back_at_it() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        srwRobot {
            clickSrwBubbleExpandCollapse(0)
        }
        generalRobot {
            scrollChatToPosition(10)
            scrollChatToPosition(0)
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleCollapsed(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_still_displayed_or_added_when_user_click_send_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickStickerIconMenu()
        }
        clickStickerAtPosition(0)

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_Bubble_should_displayed_when_user_send_manually_typed_msg_instead_of_click_srw_preview_question() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("Test Send Message")
            clickSendBtn()
        }

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
            assertSrwBubbleExpanded(0)
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_not_be_added_if_srw_preview_state_is_error_when_user_send_msg_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.isError = true
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("Test Send Message")
            clickSendBtn()
        }

        // Then
        srwResult {
            assertSrwBubbleDoesNotExist()
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    /**
     * (compare with productId, product no longer relevant) event from ws seller/himself.
     */
    @Test
    fun srw_bubble_should_removed_when_user_receive_attach_different_product_from_ws() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        websocket.simulateResponse(wsSellerProductResponse.changeTimeStampTo(today()))
        websocket.simulateResponse(wsSellerResponseText.changeTimeStampTo(today()))

        // Then
        srwResult {
            assertSrwBubbleDoesNotExist()
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    /**
     * (compare with productId, product no longer relevant) event from ws seller/himself.
     */
    @Test
    fun srw_bubble_should_not_be_removed_when_user_receive_attach_same_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        websocket.simulateResponse(
            wsSellerProductResponse
                .changeTimeStampTo(today())
                .matchProductWith(
                    DEFAULT_PRODUCT_ID,
                    ProductPreviewAttribute.productThumbnail,
                    ProductPreviewAttribute.productName,
                    ProductPreviewAttribute.productPrice
                )
        )
        websocket.simulateResponse(wsSellerResponseText.changeTimeStampTo(today()))

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
        }
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun srw_bubble_should_removed_when_user_return_from_attach_image_and_request_upload_image() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData())
        )

        // When
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachImageMenu()
        }

        // Then
        srwResult {
            assertSrwBubbleDoesNotExist()
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun should_go_back_to_the_bottom_of_the_page_if_click_srw_preview_item_and_not_clear_composed_msg() {
        // Given
        val typedMsg = "Hi seller"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        generalRobot {
            scrollChatToPosition(10)
        }
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea(typedMsg)
        }
        srwRobot {
            clickSrwBubbleExpandCollapse(0)
        }
        srwRobot {
            clickSrwPreviewItemAt(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        srwResult {
            assertSrwBubbleContentContainerVisibility(0, isDisplayed())
        }
        composeAreaResult {
            assertTypeMessageText(typedMsg)
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun should_re_render_SRW_preview_question_everytime_user_attach_new_preview_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity()

        // When
        intendingAttachProduct(1)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        chatSrwUseCase.response = chatSrwUseCase.multipleQuestions
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generate3PreAttachPayload()
        intendingAttachProduct(3)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwTotalQuestion(3)
        }
    }

    @Test
    fun should_re_render_SRW_preview_question_when_user_remove_one_of_the_preview_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity()

        // When
        intendingAttachProduct(3)
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generate3PreAttachPayload()
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }
        chatSrwUseCase.response = chatSrwUseCase.multipleQuestions
        productPreviewRobot {
            clickCloseAttachmentPreview(0)
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwTotalQuestion(3)
        }
    }

    /**
     * Simulate when messageId is empty
     */
    @Test
    fun should_render_srw_question_when_product_coming_from_chat_entry_point_such_as_pdp() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            it.putExtra(ApplinkConst.Chat.MESSAGE_ID, "")
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwTotalQuestion(1)
        }
    }

    @Test
    fun should_show_template_chat_if_first_success_load_srw_then_fail_the_second_time() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity()

        // When
        intendingAttachProduct(1)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()

            chatSrwUseCase.isError = true

            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun should_hide_label_if_label_is_empty() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
            assertSrwTotalQuestion(1)
            assertSrwLabelVisibility(false, 0)
        }
    }

    @Test
    fun should_show_label_if_label_is_not_empty() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwProductBundlingResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwPreviewContentIsVisible()
            assertSrwTitle(chatSrwProductBundlingResponse.chatSmartReplyQuestion.title)
            assertSrwTotalQuestion(1)
            assertSrwLabelVisibility(true, 0)
            assertSrwLabel(chatSrwProductBundlingResponse.chatSmartReplyQuestion.questions.first().label, 0)
        }
    }

    @Test
    fun should_show_coachmark_if_has_not_shown_yet() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwProductBundlingResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        CoachMarkPreference.setShown(context, SrwFrameLayout.TAG, false)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwCoachMark(true, context.getString(R.string.coach_product_bundling_title))
        }
    }

    @Test
    fun should_not_show_coachmark_if_has_shown() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwProductBundlingResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        CoachMarkPreference.setShown(context, SrwFrameLayout.TAG, true)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        srwResult {
            assertSrwCoachMark(false, context.getString(R.string.coach_product_bundling_title))
        }
    }

    @Test
    fun should_send_SRW_question() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwProductBundlingResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        CoachMarkPreference.setShown(context, SrwFrameLayout.TAG, false)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        srwRobot {
            clickSrwQuestion(0)
        }

        // Then
        msgBubbleResult {
            assertBubbleMsg(1, withText("Min, ada Paket Bundling?"))
        }
    }

    // TODO: SRW should hide broadcast handler if visible
    // TODO: SRW bubble should send delayed when user is in the middle of the page (from chat search)
    // TODO: SRW bubble should removed when user receive voucher event from ws.

    override fun getAttachProductData(totalProduct: Int): Intent {
        val products = ArrayList<ResultProduct>(totalProduct)
        for (i in 0 until totalProduct) {
            products.add(
                ResultProduct(
                    DEFAULT_PRODUCT_ID,
                    "tokopedia://product/1111",
                    ProductPreviewAttribute.productThumbnail,
                    "Rp ${i + 1}5.000.000",
                    "${i + 1} ${ProductPreviewAttribute.productName}"
                )
            )
        }
        return Intent().apply {
            putParcelableArrayListExtra(
                ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY,
                products
            )
        }
    }

    private fun getAttachInvoiceResult(): Instrumentation.ActivityResult {
        val intent = Intent().apply {
            putExtra(ApplinkConst.Chat.INVOICE_ID, "1")
            putExtra(ApplinkConst.Chat.INVOICE_CODE, "INV/20210506/MPL/1227273793")
            putExtra(ApplinkConst.Chat.INVOICE_TITLE, "Pr0duct T3st1n6 Out3r")
            putExtra(ApplinkConst.Chat.INVOICE_DATE, " 6 May 2021")
            putExtra(
                ApplinkConst.Chat.INVOICE_IMAGE_URL,
                "https://images.tokopedia.net/img/cache/100-square/VqbcmM/2020/11/5/adb0973e-48ce-484b-84f4-6653edc3cd6a.jpg"
            )
            putExtra(
                ApplinkConst.Chat.INVOICE_URL,
                "https://www.tokopedia.com/invoice.pl?id=785696850&pdf=Invoice-143252780-6996572-20210506113328-YmJiYmJiYmI2"
            )
            putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, "700")
            putExtra(ApplinkConst.Chat.INVOICE_STATUS, "Pesanan Selesai")
            putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, "Rp 10.000")
        }
        return Instrumentation.ActivityResult(
            Activity.RESULT_OK,
            intent
        )
    }

    private fun getAttachProductResult(totalProduct: Int): Instrumentation.ActivityResult {
        return Instrumentation.ActivityResult(
            Activity.RESULT_OK,
            getAttachProductData(totalProduct)
        )
    }

    private fun today(): Long {
        val stringDate = SendableUiModel.generateStartTime()
        return RfcDateTimeParser.parseDateString(
            stringDate,
            arrayOf(FakeTopchatWebSocket.START_TIME_FORMAT)
        ).time
    }

    private fun putProductAttachmentIntent(intent: Intent) {
        val productPreviews = listOf(DEFAULT_PRODUCT_ID)
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }
}

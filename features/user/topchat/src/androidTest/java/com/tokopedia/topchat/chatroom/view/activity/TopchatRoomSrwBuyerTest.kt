package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.attachcommon.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.changeTimeStampTo
import com.tokopedia.topchat.chatroom.view.activity.base.hasQuestion
import com.tokopedia.topchat.chatroom.view.activity.base.matchProductWith
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.SOURCE_TOPCHAT
import com.tokopedia.topchat.stub.chatroom.websocket.RxWebSocketUtilStub
import com.tokopedia.utils.time.RfcDateTimeParser
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

@UiTest
class TopchatRoomSrwBuyerTest : BaseBuyerTopchatRoomTest() {

    lateinit var productPreview: ProductPreview

    private val DEFAULT_PRODUCT_ID = "1111"

    @Before
    override fun before() {
        super.before()
        productPreview = ProductPreview(
            DEFAULT_PRODUCT_ID,
            ProductPreviewAttribute.productThumbnail,
            ProductPreviewAttribute.productName,
            "Rp 23.000.000",
            "",
            "",
            "",
            "",
            "",
            "tokopedia://product/1111",
            false,
            "",
            "Rp 50.000.000",
            500000.0,
            "50%",
            false
        )
    }


    @Test
    fun srw_preview_displayed_if_buyer_attach_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
        assertSrwTotalQuestion(1)
    }

    @Test
    fun srw_preview_displayed_with_multiple_questions_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponseMultipleQuestion
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
        assertSrwTotalQuestion(3)
    }

    @Test
    fun srw_preview_displayed_if_buyer_attach_from_attach_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity()
        intendingAttachProduct(1)

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
        assertSrwTotalQuestion(1)
    }

    @Test
    fun srw_preview_no_question_not_displayed_if_buyer_attach_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun template_chat_shown_if_srw_preview_load_finish_first_with_no_question() {
        // Given
        val templateChats = listOf(
            "Hi barang ini ready gk?", "Lorem Ipsum"
        )
        val templateResponse = generateTemplateResponse(templates = templateChats)
        val templateDelay = 500L
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        getTemplateChatRoomUseCase.setResponse(templateResponse, templateDelay)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        // Simulate template chat load last after SRW
        waitForIt(templateDelay + 10)

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun template_chat_remain_hidden_if_user_click_send_btn_when_srw_preview_shown() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        clickSendBtn()

        // Then
        assertSrwPreviewContentIsVisible()
    }

    @Test
    fun srw_preview_no_question_not_displayed_if_buyer_attach_from_attach_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        launchChatRoomActivity()
        intendingAttachProduct(1)

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun srw_preview_loading_state_displayed_if_buyer_attach_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.setResponseWithDelay(
            chatSrwResponse, 1500
        )
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        assertTemplateChatVisibility(not(isDisplayed()))
        assertSrwPreviewContentIsLoading()
    }

    @Test
    fun srw_preview_error_state_displayed_if_buyer_attach_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.isError = true
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        assertSrwPreviewContentIsError()
    }

    @Test
    fun load_srw_preview_from_error_state_if_buyer_attach_from_start_intent() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.isError = true
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        chatSrwUseCase.isError = false
        chatSrwUseCase.response = chatSrwResponse
        onView(withId(com.tokopedia.unifycomponents.R.id.refreshID))
            .perform(click())

        // Then
        assertSrwPreviewContentIsVisible()
    }

    @Test
    fun assert_srw_preview_expand_collapse_interaction() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()

        // When
        onView(withId(R.id.tp_srw_container_partial)).perform(click())

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewCollapsed()

        // When
        onView(withId(R.id.tp_srw_container_partial)).perform(click())

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()
    }

    @Test
    fun srw_preview_content_hidden_if_content_is_clicked() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(not(isDisplayed()))
    }

    @Test
    fun collapse_srw_preview_when_user_open_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickComposeArea()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewCollapsed()
    }

    @Test
    fun expand_srw_preview_when_user_hide_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickComposeArea()
        pressBack()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()
    }

    @Test
    fun expand_srw_preview_when_keyboard_opened() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickComposeArea()
        onView(withId(R.id.tp_srw_container_partial)).perform(click())

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()
        assertKeyboardIsNotVisible()
    }

    @Test
    fun collapse_srw_preview_when_keyboard_opened() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickComposeArea()
        onView(withId(R.id.tp_srw_container_partial)).perform(click())
        onView(withId(R.id.tp_srw_container_partial)).perform(click())

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewCollapsed()
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
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK, getAttachProductData(1)
                )
            )

        // When
        clickComposeArea()
        clickCloseAttachmentPreview(0)
        pressBack()
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()
    }

    @Test
    fun srw_bubble_should_displayed_after_click_srw_preview() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleExpanded(0)
    }

    @Test
    fun srw_bubble_should_always_stays_at_the_bottom_when_receive_response_msg_from_ws() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponse(wsMineResponseText)
        websocket.simulateResponse(wsMineResponseText)
        websocket.simulateResponse(wsInterlocutorResponseText)
        websocket.simulateResponse(wsInterlocutorResponseText)

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleExpanded(0)
    }

    @Test
    fun srw_bubble_should_always_stays_at_the_bottom_when_receive_response_different_day_msg_from_ws() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        websocket.simulateResponse(
            wsMineResponseText.changeTimeStampTo(today())
        )

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleExpanded(0)
    }

    @Test
    fun srw_bubble_should_removed_when_user_request_sent_invoice() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickPlusIconMenu()
        clickAttachInvoiceMenu()
        clickComposeArea()
        typeMessage("Sending Invoice")
        clickSendBtn()

        // Then
        assertSrwBubbleDoesNotExist()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun srw_bubble_should_removed_when_user_receive_invoice_event_from_ws() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        websocket.simulateResponse(wsSellerInvoiceResponse.changeTimeStampTo(today()))
        websocket.simulateResponse(wsSellerResponseText.changeTimeStampTo(today()))

        // Then
        assertSrwBubbleDoesNotExist()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun srw_preview_should_removed_when_user_re_attach_preview_with_invoice() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        clickPlusIconMenu()
        clickAttachInvoiceMenu()

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun srw_bubble_should_removed_when_user_receive_attach_image_event_from_ws() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData())
        )

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        websocket.simulateResponse(wsSellerImageResponse.changeTimeStampTo(today()))

        // Then
        assertSrwBubbleDoesNotExist()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun srw_preview_should_not_be_sent_when_user_attach_image() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData())
        )

        // When
        clickPlusIconMenu()
        clickAttachImageMenu()

        // Then
        assertSrwBubbleDoesNotExist()
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()
        assertTemplateChatVisibility(not(isDisplayed()))
    }

    @Test
    fun srw_bubble_should_removed_when_user_re_attach_product_in_preview_mode() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachProductResult(1))

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        assertSrwBubbleDoesNotExist()
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()
        assertTemplateChatVisibility(not(isDisplayed()))
    }

    @Test
    fun template_chat_should_be_hidden_when_srw_bubble_exist() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertTemplateChatVisibility(not(isDisplayed()))
    }

    @Test
    fun srw_preview_should_collapsed_when_user_open_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickStickerIconMenu()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewCollapsed()
    }

    @Test
    fun srw_preview_should_expanded_when_user_close_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickStickerIconMenu()
        pressBack()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()
    }

    @Test
    fun srw_preview_should_collapsed_when_user_open_chat_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickPlusIconMenu()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewCollapsed()
    }

    @Test
    fun srw_preview_should_expanded_when_user_close_chat_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickPlusIconMenu()
        clickPlusIconMenu()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()
    }

    @Test
    fun srw_preview_should_remain_collapsed_when_user_open_sticker_menu_and_click_open_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickStickerIconMenu()
        clickStickerIconMenu()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewCollapsed()
    }

    @Test
    fun srw_preview_should_remain_collapsed_when_user_open_keyboard_and_open_sticker_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickComposeArea()
        clickStickerIconMenu()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewCollapsed()
    }

    @Test
    fun chat_menu_should_be_hidden_when_SRW_preview_expanded() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickPlusIconMenu()
        clickSrwPreviewExpandCollapse()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()
        assertChatMenuVisibility(not(isDisplayed()))
    }

    @Test
    fun chat_sticker_should_be_hidden_when_srw_preview_expanded() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickStickerIconMenu()
        clickSrwPreviewExpandCollapse()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwPreviewExpanded()
        assertChatMenuVisibility(not(isDisplayed()))
        assertChatStickerMenuVisibility(not(isDisplayed()))
    }

    @Test
    fun srw_bubble_should_collapsed_when_user_open_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickStickerIconMenu()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
        assertChatMenuVisibility((isDisplayed()))
        assertChatStickerMenuVisibility((isDisplayed()))
    }

    @Test
    fun srw_bubble_should_expanded_when_user_close_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickStickerIconMenu()
        pressBack()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleExpanded(0)
        assertChatMenuVisibility(not((isDisplayed())))
        assertChatStickerMenuVisibility(not((isDisplayed())))
    }

    @Test
    fun srw_bubble_should_collapsed_when_user_open_chat_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickPlusIconMenu()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
        assertChatMenuVisibility(((isDisplayed())))
        assertChatAttachmentMenuVisibility(isDisplayed())
    }

    @Test
    fun srw_bubble_should_expanded_when_user_close_chat_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickPlusIconMenu()
        clickPlusIconMenu()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleExpanded(0)
        assertChatMenuVisibility(not((isDisplayed())))
    }

    @Test
    fun chat_menu_should_be_hidden_when_srw_bubble_expanded() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickPlusIconMenu()
        clickSrwBubbleExpandCollapse(0)

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleExpanded(0)
        assertChatMenuVisibility(not((isDisplayed())))
    }

    @Test
    fun chat_Sticker_should_be_hidden_when_srw_bubble_expanded() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickStickerIconMenu()
        clickSrwBubbleExpandCollapse(0)

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleExpanded(0)
        assertChatMenuVisibility(not((isDisplayed())))
    }

    @Test
    fun srw_bubble_should_collapsed_when_user_open_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickComposeArea()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
    }

    @Test
    fun srw_bubble_should_expanded_when_user_close_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickComposeArea()
        pressBack()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleExpanded(0)
    }

    @Test
    fun srw_bubble_should_remain_collapsed_when_user_click_sticker_menu_and_click_open_keyboard() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickStickerIconMenu()
        clickStickerIconMenu()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
    }

    @Test
    fun srw_bubble_should_remain_collapsed_when_user_open_keyboard_and_open_sticker_menu() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickComposeArea()
        clickStickerIconMenu()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
    }

    @Test
    fun srw_Bubble_should_collapsed_when_preview_attachment_is_visible_other_than_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickPlusIconMenu()
        clickAttachInvoiceMenu()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
    }

    @Test
    fun srw_Bubble_should_expanded_when_preview_attachment_is_not_visible() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickPlusIconMenu()
        clickAttachInvoiceMenu()
        clickCloseAttachmentPreview(0)

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleExpanded(0)
    }

    @Test
    fun srw_Bubble_should_remain_collapsed_when_invoice_preview_visible_and_user_toggle_chat_menu_on() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickPlusIconMenu()
        clickAttachInvoiceMenu()
        clickPlusIconMenu()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
    }

    @Test
    fun srw_Bubble_should_remain_collapsed_when_invoice_preview_visible_and_user_toggle_chat_menu_on_then_off() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(getAttachInvoiceResult())

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickPlusIconMenu()
        clickAttachInvoiceMenu()
        clickPlusIconMenu()
        clickPlusIconMenu()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
    }

    @Test
    fun srw_bubble_should_maintain_state_expand_or_collapse_if_scrolled_far_top_and_back_at_it() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickSrwBubbleExpandCollapse(0)
        scrollChatToPosition(10)
        scrollChatToPosition(0)

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
    }

    @Test
    fun srw_bubble_should_still_displayed_or_added_when_user_click_send_sticker() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickStickerIconMenu()
        clickStickerAtPosition(0)

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
    }

    @Test
    fun srw_Bubble_should_displayed_when_user_send_manually_typed_msg_instead_of_click_srw_preview_question() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickComposeArea()
        typeMessage("Test Send Message")
        clickSendBtn()

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertSrwBubbleCollapsed(0)
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
        clickComposeArea()
        typeMessage("Test Send Message")
        clickSendBtn()

        // Then
        assertSrwBubbleDoesNotExist()
        assertTemplateChatVisibility(isDisplayed())
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
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        websocket.simulateResponse(wsSellerProductResponse.changeTimeStampTo(today()))
        websocket.simulateResponse(wsSellerResponseText.changeTimeStampTo(today()))

        // Then
        assertSrwBubbleDoesNotExist()
        assertTemplateChatVisibility(isDisplayed())
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
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        websocket.simulateResponse(
            wsSellerProductResponse
                .changeTimeStampTo(today())
                .matchProductWith(productPreview)
        )
        websocket.simulateResponse(wsSellerResponseText.changeTimeStampTo(today()))

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
    }

    @Test
    fun srw_bubble_should_removed_when_user_return_from_attach_image_and_request_upload_image() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData())
        )

        // When
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        clickPlusIconMenu()
        clickAttachImageMenu()

        // Then
        assertSrwBubbleDoesNotExist()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun should_go_back_to_the_bottom_of_the_page_if_click_srw_preview_item_and_not_clear_composed_msg() {
        // Given
        val typedMsg = "Hi seller"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        scrollChatToPosition(10)
        clickComposeArea()
        typeMessage(typedMsg)
        clickSrwPreviewExpandCollapse()
        clickSrwPreviewItemAt(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        assertSrwBubbleContentIsVisibleAt(0)
        assertComposedTextValue(typedMsg)
    }

    @Test
    fun should_re_render_SRW_preview_question_everytime_user_attach_new_preview_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity()

        // When
        intendingAttachProduct(1)
        clickPlusIconMenu()
        clickAttachProductMenu()
        chatSrwUseCase.response = chatSrwUseCase.multipleQuestions
        intendingAttachProduct(3)
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwTotalQuestion(3)
    }

    @Test
    fun should_re_render_SRW_preview_question_when_user_remove_one_of_the_preview_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity()

        // When
        intendingAttachProduct(3)
        clickPlusIconMenu()
        clickAttachProductMenu()
        chatSrwUseCase.response = chatSrwUseCase.multipleQuestions
        clickCloseAttachmentPreview(0)

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwTotalQuestion(3)
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
        launchChatRoomActivity {
            it.putExtra(ApplinkConst.Chat.MESSAGE_ID, "")
            putProductAttachmentIntent(it)
        }

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwTotalQuestion(1)
    }

    // TODO: SRW should hide broadcast handler if visible
    // TODO: SRW bubble should send delayed when user is in the middle of the page (from chat search)
    // TODO: SRW bubble should removed when user receive voucher event from ws.

    private fun getAttachInvoiceResult(): Instrumentation.ActivityResult {
        val intent = Intent().apply {
            putExtra(ApplinkConst.Chat.INVOICE_ID, "1")
            putExtra(ApplinkConst.Chat.INVOICE_CODE, "INV/20210506/MPL/1227273793")
            putExtra(ApplinkConst.Chat.INVOICE_TITLE, "Pr0duct T3st1n6 Out3r")
            putExtra(ApplinkConst.Chat.INVOICE_DATE, " 6 May 2021")
            putExtra(
                ApplinkConst.Chat.INVOICE_IMAGE_URL,
                "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/11/5/adb0973e-48ce-484b-84f4-6653edc3cd6a.jpg"
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
            Activity.RESULT_OK, intent
        )
    }

    private fun getAttachProductResult(totalProduct: Int): Instrumentation.ActivityResult {
        return Instrumentation.ActivityResult(
            Activity.RESULT_OK, getAttachProductData(totalProduct)
        )
    }

    private fun today(): Long {
        val stringDate = SendableUiModel.generateStartTime()
        return RfcDateTimeParser.parseDateString(
            stringDate, arrayOf(RxWebSocketUtilStub.START_TIME_FORMAT)
        ).time
    }

    private fun putProductAttachmentIntent(intent: Intent) {
        val productPreviews = listOf(productPreview)
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }
}

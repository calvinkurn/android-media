package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.hasQuestion
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.SOURCE_TOPCHAT
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

class TopchatRoomSrwBuyerTest : BaseBuyerTopchatRoomTest() {

    // TODO: SRW bubble should displayed after click SRW preview
    // TODO: SRW bubble should always stays at the bottom when receive response msg from ws
    // TODO: SRW bubble should always stays at the bottom when receive response different-day msg from ws
    // TODO: SRW bubble should removed when user request sent invoice
    // TODO: SRW bubble should removed when user receive invoice event from ws.
    // TODO: SRW preview should removed when user re-attach preview with invoice
    // TODO: SRW bubble should removed when user return from attach image and request upload image.
    // TODO: SRW bubble should removed when user receive attach image event from ws.
    // TODO: SRW preview should not be sent when user attach image.
    // TODO: SRW bubble should removed when user receive voucher event from ws.
    // TODO: SRW bubble should removed when user re-attach product (in preview mode).
    // TODO: Template chat should be hidden when SRW bubble exist
    // TODO: SRW preview should collapsed when user open sticker
    // TODO: SRW preview should expanded when user close sticker
    // TODO: SRW preview should collapsed when user open chat menu
    // TODO: SRW preview should expanded when user close chat menu
    // TODO: SRW preview should remain collapsed when user open sticker menu and click open keyboard
    // TODO: SRW preview should remain collapsed when user open keyboard and open sticker menu
    // TODO: Chat Menu should be hidden when SRW preview expanded
    // TODO: Chat Sticker should be hidden when SRW preview expanded
    // TODO: SRW bubble should removed when user receive attach different product (compare with productId, product no longer relevant) event from ws seller/himself.
    // TODO: SRW bubble should not be removed when user receive attach same product (compare with productId, product attached still relevant to SRW) event from ws seller/himself.
    // TODO: SRW bubble should collapsed when user open sticker
    // TODO: SRW bubble should expanded when user close sticker
    // TODO: SRW bubble should collapsed when user open chat menu
    // TODO: SRW bubble should expanded when user close chat menu
    // TODO: Chat Menu should be hidden when SRW bubble expanded
    // TODO: Chat Sticker should be hidden when SRW bubble expanded
    // TODO: SRW bubble should collapsed when user open keyboard
    // TODO: SRW bubble should expanded when user close keyboard
    // TODO: SRW bubble should remain collapsed when user click sticker menu and click open keyboard
    // TODO: SRW bubble should remain collapsed when user open keyboard and open sticker menu
    // TODO: SRW Bubble should collapsed when preview attachment is visible other than product
    // TODO: SRW Bubble should expanded when preview attachment is not visible
    // TODO: SRW Bubble should remain collapsed when invoice preview visible and user toggle chat menu on then off
    // TODO: SRW bubble should maintain state (expand/collapse) if scrolled far top and back at it.

    // TODO: SRW bubble should still displayed/added when user click send sticker
    // TODO: SRW Bubble should displayed when user manually type msg and send instead of click SRW question
    // TODO: SRW should hide broadcast handler if visible
    // TODO: SRW bubble should send delayed when user is in the middle of the page (from chat search)
    // TODO-Alert: what if SRW in error state and user send msg/sticker

    // TODO-Note: What if BE create new ws event to indicate closing of SRW if it's no longer relevant
    // TODO: CTA on left msg - possibly different tasks

    lateinit var productPreview: ProductPreview

    @Before
    override fun before() {
        super.before()
        productPreview = ProductPreview(
            "1111",
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
            500000,
            "50%",
            false
        )
    }


    @Test
    fun srw_displayed_if_buyer_attach_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
        assertSrwTotalQuestion(1)
    }

    @Test
    fun srw_displayed_with_multiple_questions_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponseMultipleQuestion
        inflateTestFragment()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
        assertSrwTotalQuestion(3)
    }

    @Test
    fun srw_displayed_if_buyer_attach_from_attach_product() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()
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
    fun srw_no_question_not_displayed_if_buyer_attach_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        inflateTestFragment()

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun template_chat_shown_if_srw_load_finish_first_with_no_question() {
        // Given
        val templateChats = listOf(
            "Hi barang ini ready gk?", "Lorem Ipsum"
        )
        val templateResponse = generateTemplateResponse(templates = templateChats)
        val templateDelay = 500L
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        getTemplateChatRoomUseCase.setResponse(templateResponse, templateDelay)
        inflateTestFragment()

        // Then
        // Simulate template chat load last after SRW
        waitForIt(templateDelay + 10)

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun template_chat_remain_hidden_if_user_click_send_btn_when_srw_shown() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()

        // Then
        clickSendBtn()

        // Then
        assertSrwPreviewContentIsVisible()
    }

    @Test
    fun srw_no_question_not_displayed_if_buyer_attach_from_attach_product() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        inflateTestFragment()
        intendingAttachProduct(1)

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun srw_loading_state_displayed_if_buyer_attach_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.setResponseWithDelay(
            chatSrwResponse, 1500
        )
        inflateTestFragment()

        // Then
        assertTemplateChatVisibility(not(isDisplayed()))
        assertSrwPreviewContentIsLoading()
    }

    @Test
    fun srw_error_state_displayed_if_buyer_attach_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.isError = true
        inflateTestFragment()

        // Then
        assertSrwPreviewContentIsError()
    }

    @Test
    fun load_srw_from_error_state_if_buyer_attach_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.isError = true
        inflateTestFragment()

        // When
        chatSrwUseCase.isError = false
        chatSrwUseCase.response = chatSrwResponse
        onView(withId(com.tokopedia.unifycomponents.R.id.refreshID))
            .perform(click())

        // Then
        assertSrwPreviewContentIsVisible()
    }

    @Test
    fun assert_srw_expand_collapse_interaction() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwExpanded()

        // When
        onView(withId(R.id.tp_srw_container_partial)).perform(click())

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwCollapsed()

        // When
        onView(withId(R.id.tp_srw_container_partial)).perform(click())

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwExpanded()
    }

    @Test
    fun srw_content_hidden_if_content_is_clicked() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()

        // When
        clickSrwPreviewItemAt(0)

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(not(isDisplayed()))
    }

    @Test
    fun collapse_srw_when_user_open_keyboard() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()

        // When
        clickComposeArea()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwCollapsed()
    }

    @Test
    fun expand_srw_when_user_hide_keyboard() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()

        // When
        clickComposeArea()
        pressBack()

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwExpanded()
    }

    @Test
    fun expand_srw_when_keyboard_opened() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()

        // When
        clickComposeArea()
        onView(withId(R.id.tp_srw_container_partial)).perform(click())

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwExpanded()
        assertKeyboardIsNotVisible()
    }

    @Test
    fun collapse_srw_when_keyboard_opened() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()

        // When
        clickComposeArea()
        onView(withId(R.id.tp_srw_container_partial)).perform(click())
        onView(withId(R.id.tp_srw_container_partial)).perform(click())

        // Then
        assertSrwPreviewContentIsVisible()
        assertSrwCollapsed()
    }

    /**
     * SRW should expanded when reattach product
     * when previous product preview is collapsed and canceled
     */
    @Test
    fun srw_should_expanded_when_reattach_product() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()
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
        assertSrwExpanded()
    }

    private fun putProductAttachmentIntent(intent: Intent) {
        val productPreviews = listOf(productPreview)
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }
}
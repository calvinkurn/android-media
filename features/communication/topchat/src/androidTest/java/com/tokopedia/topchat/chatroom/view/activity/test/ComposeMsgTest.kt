package com.tokopedia.topchat.chatroom.view.activity.test

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.TopchatRoomInvoiceAttachmentTest.Companion.getInvoiceAttachmentIntent
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaResult
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.custom.ComposeTextWatcher.Companion.MAX_CHAR
import org.junit.Test

@UiTest
class ComposeMsgTest : TopchatRoomTest() {

    @Test
    fun user_can_not_sent_preview_invoice_when_text_is_empty() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        Intents.intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                getInvoiceAttachmentIntent(true)
            )
        )
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachInvoiceMenu()
            clickSendBtn()
        }

        // Then
        generalResult {
            assertToasterText(context.getString(R.string.topchat_desc_empty_text_box))
        }
    }

    @Test
    fun test_msg_sent_error_empty_text_click() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickSendBtn()
        }

        // Then
        generalResult {
            assertToasterText(context.getString(R.string.topchat_desc_empty_text_box))
        }
    }

    @Test
    fun should_show_error_msg_with_the_right_offset_when_composed_msg_exceed_limit() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        val offset = 10

        // When
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("a".repeat(MAX_CHAR + offset))
        }

        // Then
        composeAreaResult {
            assertSendBtnDisabled()
            assertTooLongErrorMsg(context.getString(R.string.desc_topchat_max_char_exceeded, offset))
        }
    }

    @Test
    fun should_show_error_msg_with_the_right_offset_format_when_composed_msg_exceed_limit() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        val offset = 10_000

        // When
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("a".repeat(MAX_CHAR + offset))
        }

        // Then
        composeAreaResult {
            assertSendBtnDisabled()
            assertTooLongErrorMsg(
                context.getString(R.string.desc_topchat_max_char_exceeded, "10.000")
            )
        }
    }

    @Test
    fun should_show_error_msg_with_the_right_offset_format_when_offset_exceed_limit() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        val offset = 10_001

        // When
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("a".repeat(MAX_CHAR + offset))
        }

        // Then
        composeAreaResult {
            assertSendBtnDisabled()
            assertTooLongErrorMsg(
                context.getString(R.string.desc_topchat_max_char_exceeded, "10.000+")
            )
        }
    }

    @Test
    fun should_hide_error_msg_if_composed_msg_is_not_empty_and_equal_or_less_than_limit() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        val offset = 10

        // When
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("a".repeat(MAX_CHAR + offset))
            typeMessageComposeArea("a".repeat(MAX_CHAR))
        }

        // Then
        composeAreaResult {
            assertSendBtnEnabled()
            assertNoTooLongErrorMsg()
        }
    }

    @Test
    fun should_not_show_error_toaster_when_send_btn_clicked_if_msg_exceed_limit() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        val offset = 10

        // When
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("a".repeat(MAX_CHAR + offset))
            clickSendBtn()
        }

        // Then
        generalResult {
            assertNoToasterText(context.getString(R.string.topchat_desc_empty_text_box))
        }
    }
}

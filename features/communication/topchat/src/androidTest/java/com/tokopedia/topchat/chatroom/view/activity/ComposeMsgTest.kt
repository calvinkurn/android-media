package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.TopchatRoomInvoiceAttachmentTest.Companion.getInvoiceAttachmentIntent
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composearea.ComposeAreaResult.assertNoTooLongErrorMsg
import com.tokopedia.topchat.chatroom.view.activity.robot.composearea.ComposeAreaResult.assertSendBtnDisabled
import com.tokopedia.topchat.chatroom.view.activity.robot.composearea.ComposeAreaResult.assertSendBtnEnabled
import com.tokopedia.topchat.chatroom.view.activity.robot.composearea.ComposeAreaResult.assertTooLongErrorMsg
import com.tokopedia.topchat.chatroom.view.activity.robot.composearea.ComposeAreaRobot.setComposedText
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
                Activity.RESULT_OK, getInvoiceAttachmentIntent(true)
            )
        )
        clickPlusIconMenu()
        clickAttachInvoiceMenu()
        clickSendBtn()

        // Then
        assertSnackbarText(context.getString(R.string.topchat_desc_empty_text_box))
    }

    @Test
    fun test_msg_sent_error_empty_text_click() {
        //Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        clickSendBtn()

        //Then
        assertSnackbarText(context.getString(R.string.topchat_desc_empty_text_box))
    }

    @Test
    fun should_show_error_msg_with_the_right_offset_when_composed_msg_exceed_limit() {
        //Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        val offset = 10

        //When
        clickComposeArea()
        setComposedText("a".repeat(MAX_CHAR + offset))

        //Then
        assertSendBtnDisabled()
        assertTooLongErrorMsg(context.getString(R.string.desc_topchat_max_char_exceeded, offset))
    }

    @Test
    fun should_show_error_msg_with_the_right_offset_format_when_composed_msg_exceed_limit() {
        //Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        val offset = 10_000

        //When
        clickComposeArea()
        setComposedText("a".repeat(MAX_CHAR + offset))

        //Then
        assertSendBtnDisabled()
        assertTooLongErrorMsg(
            context.getString(R.string.desc_topchat_max_char_exceeded, "10.000")
        )
    }

    @Test
    fun should_show_error_msg_with_the_right_offset_format_when_offset_exceed_limit() {
        //Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        val offset = 10_001

        //When
        clickComposeArea()
        setComposedText("a".repeat(MAX_CHAR + offset))

        //Then
        assertSendBtnDisabled()
        assertTooLongErrorMsg(
            context.getString(R.string.desc_topchat_max_char_exceeded, "10.000+")
        )
    }

    @Test
    fun should_hide_error_msg_if_composed_msg_is_not_empty_and_equal_or_less_than_limit() {
        //Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        val offset = 10

        //When
        clickComposeArea()
        setComposedText("a".repeat(MAX_CHAR + offset))
        setComposedText("a".repeat(MAX_CHAR))

        //Then
        assertSendBtnEnabled()
        assertNoTooLongErrorMsg()
    }

    @Test
    fun should_not_show_error_toaster_when_send_btn_clicked_if_msg_exceed_limit() {
        //Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        val offset = 10

        //When
        clickComposeArea()
        setComposedText("a".repeat(MAX_CHAR + offset))
        clickSendBtn()

        //Then
        assertNoSnackbarText(context.getString(R.string.topchat_desc_empty_text_box))
    }
}
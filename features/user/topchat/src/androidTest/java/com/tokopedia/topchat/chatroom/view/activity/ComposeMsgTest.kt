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
        onView(withId(R.id.send_but)).perform(ViewActions.click())

        //Then
        assertSnackbarText(context.getString(R.string.topchat_desc_empty_text_box))
    }

    // TODO: should_show_error_msg_with_the_right_offset_when_composed_msg_exceed_limit
    // TODO: should hide error msg if composed msg is not empty and equal or less than limit
    // TODO: should not show error toaster when send btn clicked if msg exceed limit
}
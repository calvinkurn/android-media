package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.header.HeaderRobot.clickThreeDotsMenu
import org.junit.Assert.assertTrue
import org.junit.Test

class TopchatRoomDeleteChatTest: TopchatRoomTest() {

    @Test
    fun should_close_chatroom_when_success_delete_chat(){
        //Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        moveChatToTrashUseCase.response = getChatDeleteResponse()
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        clickThreeDotsMenu()
        onView(withText(context.getString(R.string.delete_conversation))).perform(click())
        onView(withText(context.getString(R.string.topchat_chat_delete_confirm))).perform(click())

        //Then
        assertTrue(activity.isFinishing)
    }

    @Test
    fun should_show_error_message_when_fail_to_delete_chat(){
        //Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        moveChatToTrashUseCase.response = getChatDeleteResponse(isSuccess = false)
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        clickThreeDotsMenu()
        onView(withText(context.getString(R.string.delete_conversation))).perform(click())
        onView(withText(context.getString(R.string.topchat_chat_delete_confirm))).perform(click())

        //Then
        onView(withSubstring("Oops!"))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun should_show_error_message_when_error_to_delete_chat(){
        //Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        moveChatToTrashUseCase.errorMessage = "Oops!"
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        clickThreeDotsMenu()
        onView(withText(context.getString(R.string.delete_conversation))).perform(click())
        onView(withText(context.getString(R.string.topchat_chat_delete_confirm))).perform(click())
        //Then
        onView(withSubstring("Oops!"))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    private fun getChatDeleteResponse(isSuccess: Boolean = true): ChatDeleteStatus {
        val chatDeleteResponse: ChatDeleteStatus = AndroidFileUtil.parse(
            "success_delete_chat.json",
            ChatDeleteStatus::class.java
        )
        if (!isSuccess) {
            chatDeleteResponse.chatMoveToTrash.list.first().isSuccess = 0
            chatDeleteResponse.chatMoveToTrash.list.first().detailResponse = "Oops!"
        }
        return chatDeleteResponse
    }
}
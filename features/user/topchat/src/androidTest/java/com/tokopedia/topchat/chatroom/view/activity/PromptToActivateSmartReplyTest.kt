package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ReminderTickerViewHolder
import org.junit.Test

class PromptToActivateSmartReplyTest : TopchatRoomTest() {

    @Test
    fun should_show_ticker_below_matched_bubble_if_previous_msg_is_single_product() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultSrwPrompt
        reminderTickerUseCase.response = reminderTickerUseCase.defaultSrwPrompt
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultSrwPrompt
        launchChatRoomActivity()

        // Then
        assertChatRecyclerview(
            hasViewHolderItemAtPosition(
                1, ReminderTickerViewHolder::class.java
            )
        )
    }

    // TODO: should show ticker on the latest eligible position, should check bottom-up
    // TODO: Should show ticker below matched bubble if previous msg is carousel products
    // TODO: Should show ticker below matched bubble if success get GetReminderTicker and matched bubble is on page two or more
    // TODO: Should close ticker when close btn closed
    // TODO: Should not show ticker if user is in the middle of the page
    // TODO: Should not show ticker if no ticker available to exist from GetReminderTicker
    // TODO: Should go to chat setting if clicked from hamburger menu item
}
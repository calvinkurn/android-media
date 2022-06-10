package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder.TickerReminderRobot.clickTickerCloseButtonAt
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ReminderTickerViewHolder
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class PromptToActivateSmartReplyTest : TopchatRoomTest() {

    @Test
    fun should_show_ticker_below_matched_bubble_if_previous_msg_is_single_product() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.defaultSrwPrompt
        val triggerText = reminderTickerUseCase.response.getReminderTicker.regexMessage
        getChatUseCase.response = getChatUseCase.getSrwPromptWithTriggerText(triggerText)
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultSrwPrompt
        launchChatRoomActivity()

        // Then
        assertChatRecyclerview(
            hasViewHolderItemAtPosition(
                1, ReminderTickerViewHolder::class.java
            )
        )
    }

    @Test
    fun should_show_ticker_on_the_latest_eligible_position() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.defaultSrwPrompt
        val triggerText = reminderTickerUseCase.response.getReminderTicker.regexMessage
        getChatUseCase.response = getChatUseCase.multipleSrwPrompt(triggerText)
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultSrwPrompt
        launchChatRoomActivity()

        // Then
        assertChatRecyclerview(
            hasViewHolderItemAtPosition(
                1, ReminderTickerViewHolder::class.java
            )
        )
    }

    @Test
    fun should_show_ticker_below_matched_bubble_if_previous_msg_is_carousel_products() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.defaultSrwPrompt
        val triggerText = reminderTickerUseCase.response.getReminderTicker.regexMessage
        getChatUseCase.response = getChatUseCase.carouselSrwPrompt(triggerText)
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultSrwPrompt
        launchChatRoomActivity()

        // Then
        assertChatRecyclerview(
            hasViewHolderItemAtPosition(
                1, ReminderTickerViewHolder::class.java
            )
        )
    }

    @Test
    fun should_show_ticker_below_matched_bubble_if_eligible_position_found_in_page_2_or_more() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.defaultSrwPrompt
        val triggerText = reminderTickerUseCase.response.getReminderTicker.regexMessage
        getChatUseCase.response = getChatUseCase.noTriggerTextSrwPrompt
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultSrwPrompt
        launchChatRoomActivity()

        // When
        val lastIndex = getChatUseCase.getLastIndexOf(getChatUseCase.response)
        getChatUseCase.response = getChatUseCase.getSrwPromptWithTriggerText(triggerText)
        scrollChatToPosition(lastIndex)
        val newlastIndex = lastIndex + getChatUseCase.getLastIndexOf(getChatUseCase.response)
        scrollChatToPosition(newlastIndex)
        smoothScrollChatToPosition(newlastIndex)

        // Then
        val expectedTickerPosition = lastIndex + 2
        assertChatRecyclerview(
            hasViewHolderItemAtPosition(
                expectedTickerPosition, ReminderTickerViewHolder::class.java
            )
        )
    }

    @Test
    fun should_close_ticker_when_close_btn_closed() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.defaultSrwPrompt
        val triggerText = reminderTickerUseCase.response.getReminderTicker.regexMessage
        getChatUseCase.response = getChatUseCase.getSrwPromptWithTriggerText(triggerText)
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultSrwPrompt
        launchChatRoomActivity()

        // When
        clickTickerCloseButtonAt(1)

        // Then
        assertChatRecyclerview(
            not(hasViewHolderOf(ReminderTickerViewHolder::class.java))
        )
    }

    @Test
    fun should_not_show_ticker_if_user_is_in_the_middle_of_the_page() {
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.defaultSrwPrompt
        val triggerText = reminderTickerUseCase.response.getReminderTicker.regexMessage
        getChatUseCase.response = getChatUseCase.getInTheMiddleOfThePageSrwPrompt(triggerText)
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultSrwPrompt
        launchChatRoomActivity()

        // Then
        assertChatRecyclerview(
            not(hasViewHolderOf(ReminderTickerViewHolder::class.java))
        )
    }

    @Test
    fun should_not_show_ticker_if_no_ticker_available_to_exist(){
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.falseSrwPrompt
        val triggerText = reminderTickerUseCase.response.getReminderTicker.regexMessage
        getChatUseCase.response = getChatUseCase.getSrwPromptWithTriggerText(triggerText)
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultSrwPrompt
        launchChatRoomActivity()

        // Then
        assertChatRecyclerview(
            not(hasViewHolderOf(ReminderTickerViewHolder::class.java))
        )
    }

    @Test
    fun should_not_show_ticker_if_no_eligible_position_exist(){
        // Given
        reminderTickerUseCase.response = reminderTickerUseCase.noRegexMatchSrwPrompt
        getChatUseCase.response = getChatUseCase.defaultSrwPrompt
        chatAttachmentUseCase.response = chatAttachmentUseCase.defaultSrwPrompt
        launchChatRoomActivity()

        // Then
        assertChatRecyclerview(
            not(hasViewHolderOf(ReminderTickerViewHolder::class.java))
        )
    }

    // TODO: Should go to chat setting if clicked from hamburger menu item
}
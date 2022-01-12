package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.filters.FlakyTest
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleRobot
import org.hamcrest.Matchers.`is`
import org.junit.Test
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.onboarding.ReplyBubbleOnBoarding.Companion.KEY_REPLY_BUBBLE_ONBOARDING

@UiTest
class ReplyBubbleTest : TopchatRoomTest() {

    override fun before() {
        super.before()
        disableReplyBubbleOnBoarding()
    }

    private fun disableReplyBubbleOnBoarding() {
        cacheManager.saveState(KEY_REPLY_BUBBLE_ONBOARDING, true)
    }

    @Test
    fun should_show_bottomsheet_menu_on_long_click_normal_text_bubble() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)

        // Then
        assertLongClickMenu(isDisplayed())
    }

    @Test
    fun should_show_reply_bubble_compose_when_user_long_click_and_reply() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()

        // Then
        ReplyBubbleResult.hasVisibleReplyCompose()
    }

    @Test
    fun should_hide_or_cancel_reply_compose_when_user_click_close_icon() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()
        ReplyBubbleRobot.clickReplyComposeCloseIcon()

        // Then
        ReplyBubbleResult.hasNoVisibleReplyCompose()
    }

    @Test
    fun should_not_sent_closed_reply_compose_to_websocket() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()
        ReplyBubbleRobot.clickReplyComposeCloseIcon()
        clickComposeArea()
        typeMessage("reply this")
        clickSendBtn()

        // Then
        ReplyBubbleResult.hasNoVisibleReplyBubbleAt(0)
    }

    @Test
    @FlakyTest
    fun should_go_to_specific_bubble_when_reply_compose_is_clicked() {
        // Given
        val scrollWaitTime = 500L
        val lastBubbleIndex = getChatUseCase.getLastIndexOf(
            getChatUseCase.longReplyBubbleResponse
        )
        getChatUseCase.response = getChatUseCase.longReplyBubbleResponse
        launchChatRoomActivity()

        // When
        scrollChatToPosition(lastBubbleIndex)
        ReplyBubbleRobot.longClickBubbleAt(lastBubbleIndex)
        ReplyBubbleRobot.clickReplyItemMenu()
        scrollChatToPosition(0)
        ReplyBubbleRobot.clickReplyCompose()
        waitForIt(scrollWaitTime)

        // Then
        assertMsgBubbleAt(lastBubbleIndex, isDisplayed())
    }

    @Test
    fun should_go_to_specific_bubble_when_msg_bubble_local_id_is_exist() {
        // Given
        val lastBubbleIndex = getChatUseCase.getLastIndexOf(
            getChatUseCase.longReplyBubbleResponse
        )
        getChatUseCase.response = getChatUseCase.longReplyBubbleResponse
        launchChatRoomActivity()

        // When
        scrollChatToPosition(lastBubbleIndex)
        ReplyBubbleRobot.longClickBubbleAt(lastBubbleIndex)
        ReplyBubbleRobot.clickReplyItemMenu()
        scrollChatToPosition(0)
        clickComposeArea()
        typeMessage("reply this")
        clickSendBtn()
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        ReplyBubbleRobot.clickReplyBubbleAt(0)

        // Then
        assertMsgBubbleAt(lastBubbleIndex + 1, isDisplayed())
    }

    @Test
    fun should_sent_and_render_reply_bubble_when_user_sent_normal_text() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()
        clickComposeArea()
        typeMessage("reply this")
        clickSendBtn()
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleAt(0)
    }

    @Test
    fun should_show_normal_text_reply_bubble_when_parent_reply_is_not_null_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleAt(0)
    }

    @Test
    fun should_sent_and_render_reply_bubble_when_user_sent_sticker() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()
        clickStickerIconMenu()
        clickStickerAtPosition(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleStickerAt(0)
    }

    @Test
    fun should_show_sticker_reply_bubble_when_parent_reply_is_not_null_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        scrollChatToPosition(3)

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleStickerAt(3)
    }

    @Test
    fun should_match_the_senderId_name_with_contacts_from_chatReplies_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        val roomMetaData = getCurrentRoomMetaData(MSG_ID, getChatUseCase.response)
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()
        clickComposeArea()
        typeMessage("reply this")
        clickSendBtn()
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleAt(0)
        ReplyBubbleResult.hasReplyBubbleTitleAt(0, roomMetaData.sender.name)
    }

    @Test
    fun should_reset_chatroom_page_like_chat_search_when_click_reply_bubble_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        getChatUseCase.response = getChatUseCase.inTheMiddleReplyBubbleResponse
        ReplyBubbleRobot.clickReplyBubbleAt(0)

        // Then
        assertThat(getChatUseCase.isInTheMiddleOfThePage(), `is`(true))
    }

    @Test
    fun should_show_expired_toaster_when_user_click_expired_reply_bubble() {
        // Given
        getChatUseCase.response = getChatUseCase.expiredReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.clickReplyBubbleAt(0)

        // Then
        assertSnackbarText(context.getString(R.string.title_topchat_reply_bubble_expired))
    }

    @Test
    fun should_show_image_reply_bubble_when_parent_reply_is_not_null_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleImageAt(2)
    }

    @Test
    fun should_show_image_reply_bubble_when_parent_reply_is_not_null_from_websocket() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        val roomMetaData = getCurrentRoomMetaData(MSG_ID, getChatUseCase.response)
        launchChatRoomActivity()

        // When
        val incomingEventWs = websocket2.generateUploadImageResposne(roomMetaData)
        websocket2.simulateResponse(incomingEventWs)

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleImageAt(0)
    }

    private fun getCurrentRoomMetaData(msgId: String, chat: GetExistingChatPojo): RoomMetaData {
        return existingChatMapper.generateRoomMetaData(msgId, chat)
    }

    // TODO: should_go_to_specific_bubble_when_msg_bubble_reply_time_nano_exist
    // TODO: should_send_reply_bubble_when_user_send_srw
    // TODO: should show onboarding reply bubble
    // TODO: should send reply bubble preview when user click one of SRW preview question

}
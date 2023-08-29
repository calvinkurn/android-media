package com.tokopedia.topchat.chatroom.view.activity.test

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.filters.FlakyTest
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.replyBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.replyBubbleRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleRobot
import com.tokopedia.topchat.chatroom.view.onboarding.ReplyBubbleOnBoarding.Companion.KEY_REPLY_BUBBLE_ONBOARDING
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

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
        replyBubbleRobot {
            longClickBubbleAt(1)
        }

        // Then
        replyBubbleResult {
            assertLongClickMenu()
        }
    }

    @Test
    fun should_show_reply_bubble_compose_when_user_long_click_and_reply() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        replyBubbleRobot {
            longClickBubbleAt(1)
            clickReplyItemMenu()
        }

        // Then
        replyBubbleResult {
            hasVisibleReplyCompose()
        }
    }

    @Test
    fun should_hide_or_cancel_reply_compose_when_user_click_close_icon() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        replyBubbleRobot {
            longClickBubbleAt(1)
            clickReplyItemMenu()
            clickReplyComposeCloseIcon()
        }

        // Then
        replyBubbleResult {
            hasNoVisibleReplyCompose()
        }
    }

    @Test
    fun should_not_sent_closed_reply_compose_to_websocket() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        replyBubbleRobot {
            longClickBubbleAt(1)
            clickReplyItemMenu()
            clickReplyComposeCloseIcon()
        }
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("reply this")
            clickSendBtn()
        }
        Thread.sleep(5000)

        // Then
        replyBubbleResult {
            hasNoVisibleReplyBubbleAt(0)
        }
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
        generalRobot {
            scrollChatToPosition(lastBubbleIndex)
        }
        replyBubbleRobot {
            longClickBubbleAt(lastBubbleIndex)
            clickReplyItemMenu()
        }
        generalRobot {
            scrollChatToPosition(0)
        }
        replyBubbleRobot {
            clickReplyCompose()
        }
        Thread.sleep(scrollWaitTime)

        // Then
        replyBubbleResult {
            assertMsgBubbleAt(lastBubbleIndex, isDisplayed())
        }
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
        generalRobot {
            scrollChatToPosition(lastBubbleIndex)
        }
        replyBubbleRobot {
            longClickBubbleAt(lastBubbleIndex)
            clickReplyItemMenu()
        }
        generalRobot {
            scrollChatToPosition(0)
        }
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("reply this")
            clickSendBtn()
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        replyBubbleRobot {
            clickReplyBubbleAt(0)
        }

        // Then
        replyBubbleResult {
            assertMsgBubbleAt(lastBubbleIndex + 1, isDisplayed())
        }
    }

    @Test
    fun should_sent_and_render_reply_bubble_when_user_sent_normal_text() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        replyBubbleRobot {
            longClickBubbleAt(1)
            clickReplyItemMenu()
        }
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("reply this")
            clickSendBtn()
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        replyBubbleResult {
            hasVisibleReplyBubbleAt(0)
        }
    }

    @Test
    fun should_show_normal_text_reply_bubble_when_parent_reply_is_not_null_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // Then
        replyBubbleResult {
            hasVisibleReplyBubbleAt(0)
        }
    }

    @Test
    fun should_sent_and_render_reply_bubble_when_user_sent_sticker() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        replyBubbleRobot {
            longClickBubbleAt(1)
            clickReplyItemMenu()
        }
        composeAreaRobot {
            clickStickerIconMenu()
            clickStickerAtPosition(0)
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        replyBubbleResult {
            hasVisibleReplyBubbleStickerAt(0)
        }
    }

    @Test
    fun should_show_sticker_reply_bubble_when_parent_reply_is_not_null_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        generalRobot {
            scrollChatToPosition(3)
        }

        // Then
        replyBubbleResult {
            hasVisibleReplyBubbleStickerAt(3)
        }
    }

    @Test
    fun should_match_the_senderId_name_with_contacts_from_chatReplies_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        val roomMetaData = getCurrentRoomMetaData(MSG_ID, getChatUseCase.response)
        launchChatRoomActivity()

        // When
        replyBubbleRobot {
            longClickBubbleAt(1)
            clickReplyItemMenu()
        }
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("reply this")
            clickSendBtn()
        }
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        replyBubbleResult {
            hasVisibleReplyBubbleAt(0)
            hasReplyBubbleTitleAt(0, roomMetaData.sender.name)
        }
    }

    @Test
    fun should_reset_chatroom_page_like_chat_search_when_click_reply_bubble_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        getChatUseCase.response = getChatUseCase.inTheMiddleReplyBubbleResponse
        replyBubbleRobot {
            clickReplyBubbleAt(0)
        }

        // Then
        assertThat(getChatUseCase.isInTheMiddleOfThePage().value, `is`(true))
    }

    @Test
    fun should_show_expired_toaster_when_user_click_expired_reply_bubble() {
        // Given
        getChatUseCase.response = getChatUseCase.expiredReplyBubbleResponse
        launchChatRoomActivity()

        // When
        replyBubbleRobot {
            clickReplyBubbleAt(0)
        }

        // Then
        generalResult {
            assertToasterText(context.getString(R.string.title_topchat_reply_bubble_expired))
        }
    }

    @Test
    fun should_show_image_reply_bubble_when_parent_reply_is_not_null_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // Then
        replyBubbleResult {
            hasVisibleReplyBubbleImageAt(2)
        }
    }

    @Test
    fun should_show_image_reply_bubble_when_parent_reply_is_not_null_from_websocket() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        val roomMetaData = getCurrentRoomMetaData(MSG_ID, getChatUseCase.response)
        launchChatRoomActivity()

        // When
        val incomingEventWs = websocket.generateUploadImageResponse(roomMetaData, false)
        websocket.simulateResponse(incomingEventWs)

        // Then
        replyBubbleResult {
            hasVisibleReplyBubbleImageAt(0)
        }
    }

    private fun getCurrentRoomMetaData(msgId: String, chat: GetExistingChatPojo): RoomMetaData {
        return existingChatMapper.generateRoomMetaData(msgId, chat)
    }

    // TODO: should_go_to_specific_bubble_when_msg_bubble_reply_time_nano_exist
    // TODO: should_send_reply_bubble_when_user_send_srw
    // TODO: should show onboarding reply bubble
    // TODO: should send reply bubble preview when user click one of SRW preview question
}

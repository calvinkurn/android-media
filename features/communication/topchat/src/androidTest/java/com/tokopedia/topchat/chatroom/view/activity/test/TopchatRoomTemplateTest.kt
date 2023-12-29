package com.tokopedia.topchat.chatroom.view.activity.test

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaResult
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.msgBubbleResult
import com.tokopedia.topchat.matchers.withTotalItem
import com.tokopedia.topchat.stub.common.RemoteConfigStub
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class TopchatRoomTemplateTest : TopchatRoomTest() {

    @Test
    fun template_chat_shown_if_enabled() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = getTemplateChatRoomUseCase.getTemplateResponseBuyer(true)
        launchChatRoomActivity()
        setupRemoteConfigValue(false)

        // When
        composeAreaRobot {
            clickTemplateChatAt(0)
        }

        // Then
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
            assertTypeMessageText(" Hi Barang ini ready ga? ")
        }
    }

    @Test
    fun template_chat_hidden_if_disabled() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = getTemplateChatRoomUseCase.getTemplateResponseBuyer(false)
        getTemplateChatRoomUseCase.response
        launchChatRoomActivity()
        setupRemoteConfigValue(false)

        // Then
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    @Test
    fun should_enabled_send_msg_btn_after_choosing_template() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = getTemplateChatRoomUseCase.successGetTemplateResponseBuyer
        launchChatRoomActivity()
        setupRemoteConfigValue(false)

        // When
        composeAreaRobot {
            clickTemplateChatAt(0)
        }

        // Then
        composeAreaResult {
            assertTypeMessageText(" Hi Barang ini ready ga? ")
            assertSendBtnEnabled()
        }
    }

    @Test
    fun should_able_to_send_msg_after_choosing_template() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = getTemplateChatRoomUseCase.successGetTemplateResponseBuyer
        launchChatRoomActivity()
        setupRemoteConfigValue(false)

        // When
        val count = activityTestRule.activity
            .findViewById<RecyclerView>(R.id.recycler_view_chatroom)
            .adapter?.itemCount ?: 0
        composeAreaRobot {
            clickTemplateChatAt(0)
            clickSendBtn()
        }

        // Then
        generalResult {
            assertChatRecyclerview(withTotalItem(count + 1))
        }
        msgBubbleResult {
            assertBubbleMsg(0, withSubstring("Hi Barang ini ready ga?"))
        }
        composeAreaResult {
            assertTypeMessageText("")
        }
    }

    // Setup remoteconfig for toggle flexmode/not
    private fun setupRemoteConfigValue(isFlexMode: Boolean) {
        val remoteConfigStub = RemoteConfigStub()
        remoteConfigStub.setBooleanResult(isFlexMode)
        activity.remoteConfig = remoteConfigStub
    }
}

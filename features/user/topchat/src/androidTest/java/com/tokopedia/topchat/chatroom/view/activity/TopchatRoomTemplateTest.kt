package com.tokopedia.topchat.chatroom.view.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.DrawableMatcher
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class TopchatRoomTemplateTest : TopchatRoomTest() {

    @Test
    fun template_chat_shown_if_enabled() {
        // Given
        val templateChats = listOf(
                "Hi barang ini ready gk?", "Lorem Ipsum"
        )
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(templates = templateChats)
        launchChatRoomActivity()

        // When
        clickTemplateChatAt(0)

        // Then
        assertTemplateChatVisibility(isDisplayed())
        assertComposedTextValue(" Hi barang ini ready gk? ")
    }

    @Test
    fun template_chat_hidden_if_disabled() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(enable = false)
        launchChatRoomActivity()

        // Then

        assertTemplateChatVisibility(not(isDisplayed()))
    }

    @Test
    fun should_enabled_send_msg_btn_after_choosing_template() {
        // Given
        val templateChats = listOf(
                "Hi barang ini ready gk?", "Lorem Ipsum"
        )
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(templates = templateChats)
        launchChatRoomActivity()

        // When
        clickTemplateChatAt(0)

        // Then
        assertComposedTextValue(" Hi barang ini ready gk? ")
        DrawableMatcher.compareDrawable(R.id.send_but, R.drawable.bg_topchat_send_btn)
    }

    @Test
    fun should_able_to_send_msg_after_choosing_template() {
        // Given
        val templateChats = listOf(
                "Test"
        )
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(templates = templateChats)
        launchChatRoomActivity()

        // When
        val count = activityTestRule.activity
                .findViewById<RecyclerView>(R.id.recycler_view)
                .adapter?.itemCount?: 0
        clickTemplateChatAt(0)
        clickSendBtn()

        // Then
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        0, R.id.tvMessage
                ))
                .check(matches(withText("Test ")))
        onView(withId(R.id.recycler_view)).check(matches(withTotalItem(count+1)))
        assertComposedTextValue("")
    }
}
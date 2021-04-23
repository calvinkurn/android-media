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
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(templates = templateChats)
        inflateTestFragment()

        // When
        clickTemplateChatAt(0)

        // Then
        assertTemplateChatVisibility(isDisplayed())
        onView(withId(R.id.new_comment)).check(
                matches(withText(" Hi barang ini ready gk? "))
        )
    }

    @Test
    fun template_chat_hidden_if_disabled() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(enable = false)
        inflateTestFragment()

        // Then

        assertTemplateChatVisibility(not(isDisplayed()))
    }

    @Test
    fun template_chat_send_button_enabled() {
        // Given
        val templateChats = listOf(
                "Hi barang ini ready gk?", "Lorem Ipsum"
        )
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(templates = templateChats)
        inflateTestFragment()

        // When
        clickTemplateChatAt(0)

        // Then
        onView(withId(R.id.new_comment)).check(
                matches(withText(" Hi barang ini ready gk? "))
        )
        DrawableMatcher.compareDrawable(R.id.send_but, R.drawable.bg_topchat_send_btn)
    }

    @Test
    fun template_chat_send() {
        // Given
        val templateChats = listOf(
                "Test"
        )
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(templates = templateChats)
        inflateTestFragment()

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
        onView(withId(R.id.new_comment)).check(matches(withText("")))
    }
}
package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
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
        onView(withId(R.id.list_template)).check(
                matches(isDisplayed())
        )
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
        onView(withId(R.id.list_template)).check(
                matches(not(isDisplayed()))
        )
    }

}
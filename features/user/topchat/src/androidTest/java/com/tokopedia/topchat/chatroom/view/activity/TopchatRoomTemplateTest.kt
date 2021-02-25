package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
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

}
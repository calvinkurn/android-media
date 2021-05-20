package com.tokopedia.inbox.view.activity.chatlist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.inbox.R
import com.tokopedia.inbox.view.activity.base.chat.InboxChatBuyerTest
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class InboxActivityChatListTest : InboxChatBuyerTest() {

    @Test
    fun should_render_empty_view_when_response_is_empty() {
        // Given
        inboxChatDep.getChatList.response = inboxChatDep.getChatList_EmptyBuyerResponse
        startInboxActivity()

        // When
        onView(withId(R.id.thumbnail_empty_chat_list)).check(
                matches(isDisplayed())
        )
        onView(withId(R.id.title_empty_chat_list)).check(
                matches(withText("Belum ada chat, nih"))
        )
        onView(withId(R.id.subtitle)).check(
                matches(withText("Coba ngobrol dengan teman penjual, yuk!"))
        )
    }

}
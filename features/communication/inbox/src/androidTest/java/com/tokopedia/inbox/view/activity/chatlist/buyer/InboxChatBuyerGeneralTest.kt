package com.tokopedia.inbox.view.activity.chatlist.buyer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.viewmatcher.withTotalItem
import com.tokopedia.inbox.view.activity.base.chat.InboxChatBuyerTest
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class InboxChatBuyerGeneralTest : InboxChatBuyerTest() {

    @Test
    fun should_render_empty_view_when_response_is_empty() {
        // Given
        inboxChatDep.apply {
            getChatList.response = getChatList_EmptyBuyerResponse
        }
        startInboxActivity()

        // Then
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

    @Test
    fun should_have_1_filter_only_for_buyer() {
        // Given
        startInboxActivity()

        // Then
        onView(withId(R.id.rv_filter)).check(matches(withTotalItem(1)))
    }

    @Test
    fun should_show_different_empty_state_when_response_with_filter_is_empty() {
        // Given
        inboxChatDep.apply {
            getChatList.response = getChatList_BuyerSize3Response
        }
        startInboxActivity()

        // When
        inboxChatDep.apply {
            getChatList.response = getChatList_EmptyBuyerResponse
        }
        clickFilterPositionAt(0)

        // Then
        onView(withId(R.id.thumbnail_empty_chat_list)).check(
                matches(isDisplayed())
        )
        onView(withId(R.id.title_empty_chat_list)).check(
                matches(withText("Mantap! Semua chat sudah kamu baca!"))
        )
        onView(withId(R.id.subtitle)).check(
                matches(withText("Kalau ada chat baru buat kamu, bakal muncul disini."))
        )
    }

    // TODO: test mark read/unread chat
    // TODO: test delete chat

    // TODO: test receive chat from seller
    // TODO: test receive chat from myself sent from different platform
    // TODO: test nothing happened when chat received from buyer
    // TODO: test nothing happened when sending chat as seller from different platform
    // TODO: test ws message queued when receive event onStop fragment

}
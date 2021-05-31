package com.tokopedia.inbox.view.activity.chatlist.buyer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView
import com.tokopedia.inbox.view.activity.base.chat.InboxChatBuyerTest
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class InboxChatBuyerPinUnpinTest : InboxChatBuyerTest() {

    @Test
    fun should_show_pinned_icon_when_pin_status_is_true() {
        // Given
        inboxChatDep.apply {
            getChatList.response = getChatList_BuyerSize3Response
        }
        startInboxActivity()

        // Then

        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.ivPin)
        ).check(matches(isDisplayed()))
    }

    // TODO: test success pin unpin chat
    // TODO: test error pin/unpin chat

}
package com.tokopedia.inbox.view.activity.chatlist

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.inbox.view.activity.base.chat.InboxChatTest
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class InboxActivityChatListTest : InboxChatTest() {

    @Test
    fun test_inbox() {
        // Given
        setupInboxActivity()
    }

}
package com.tokopedia.topchat.matchers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAdapter
import org.hamcrest.Description

fun isSender(position: Int): BoundedMatcher<View, RecyclerView> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("sender at $position")
        }

        override fun matchesSafely(item: RecyclerView?): Boolean {
            val adapter = item!!.adapter as TopChatRoomAdapter
            return (adapter.data[position] as MessageUiModel).isSender
        }
    }
}
package com.tokopedia.topchat.matchers

import android.text.Layout
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
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

fun isTextTruncated(): ViewAssertion {
    return ViewAssertion { view, _ ->
        if (view !is TextView) {
            throw AssertionError("The view is not a TextView")
        }
        // Layout that holds the displayable text
        val layout: Layout? = view.layout
        // Check if layout exists and the last visible character is an ellipsis
        if (layout != null) {
            val lines: Int = layout.lineCount
            if (lines > 0) {
                val ellipsisCount: Int = layout.getEllipsisCount(lines - 1)
                if (ellipsisCount > 0) {
                    return@ViewAssertion // Text is truncated
                }
            }
        }
        throw AssertionError("Text is not truncated")
    }
}

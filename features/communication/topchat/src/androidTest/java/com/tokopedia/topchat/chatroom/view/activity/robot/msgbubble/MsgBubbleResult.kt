package com.tokopedia.topchat.chatroom.view.activity.robot.msgbubble

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.matchers.isTextTruncated
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import com.tokopedia.chat_common.R as chat_commonR

object MsgBubbleResult {

    fun assertMsgIsDeletedAt(position: Int) {
        assertIconVisibility(position, isDisplayed())
        assertBubbleMsg(position, withText("Pesan ini telah dihapus."))
    }

    private fun assertIconVisibility(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.iu_msg_icon, matcher)
        }
    }

    fun assertBubbleMsg(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.tvMessage, matcher)
        }
    }

    fun assertHeaderTitleMsgAtBubblePosition(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.tp_header_title, matcher)
        }
    }

    fun assertCtaHeaderMsgAtBubblePosition(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.tp_header_cta, matcher)
        }
    }

    fun assertDividerHeaderContainer(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.v_header_divider, matcher)
        }
    }

    fun assertMsgHeaderContainer(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.ll_msg_header, matcher)
        }
    }

    fun assertMsgInfo(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.txt_info, matcher)
        }
    }

    fun assertChatStatus(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(position, chat_commonR.id.chat_status, matcher)
        }
    }

    fun assertHeaderRightMsgBubbleAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.tvRole, matcher)
        }
    }

    fun assertHeaderRightMsgBubbleBlueDotAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_iv_header_role_blue_dot, matcher)
        }
    }

    fun assertAutoReplyWelcomeMessage(
        position: Int,
        text: String,
        checkTruncated: Boolean = false
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.tvMessage, withText(text))
            if (checkTruncated) {
                onView(
                    withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                        position,
                        R.id.tvMessage
                    )
                ).check(isTextTruncated())
            }
        }
    }

    fun assertAutoReplyListRvShownAt(position: Int) {
        generalResult {
            assertViewInRecyclerViewAt(
                position,
                R.id.topchat_chatroom_rv_auto_reply,
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        }
    }

    fun assertAutoReplyListRvGoneAt(position: Int) {
        generalResult {
            assertViewInRecyclerViewAt(
                position,
                R.id.topchat_chatroom_rv_auto_reply,
                withEffectiveVisibility(ViewMatchers.Visibility.GONE)
            )
        }
    }

    fun assertAutoReplyListItemCountAt(position: Int, count: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPosition(position)

        ).check(
            matches(
                hasDescendant(
                    allOf(
                        withId(R.id.topchat_chatroom_rv_auto_reply),
                        withTotalItem(count)
                    )
                )
            )
        )
    }

    fun assertAutoReplyBubbleLabelAt(position: Int) {
        generalResult {
            assertViewInRecyclerViewAt(
                position,
                R.id.txt_info,
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        }
    }

    fun assertAutoReplyReadMoreAt(position: Int, text: String) {
        generalResult {
            assertViewInRecyclerViewAt(
                position,
                R.id.topchat_tv_auto_reply_read_more,
                withText(text)
            )
        }
    }

    fun assertAutoReplyBottomSheetTitle(msg: String) {
        onView(withText(msg))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    fun assertAutoReplyListBottomSheet() {
        onView(withId(R.id.topchat_chatroom_rv_auto_reply_bs))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    fun assertAutoReplyListItemCount(count: Int) {
        onView(withId(R.id.topchat_chatroom_rv_auto_reply_bs))
            .check(matches(withTotalItem(count)))
    }
}

package com.tokopedia.topchat.chatroom.view.activity.robot.msgbubble

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.assertViewInRecyclerViewAt
import org.hamcrest.Matcher

object MsgBubbleResult {

    fun assertMsgIsDeletedAt(position: Int) {
        assertIconVisibility(position, isDisplayed())
        assertBubbleMsg(position, withText("Pesan ini telah dihapus."))
    }

    private fun assertIconVisibility(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.iu_msg_icon, matcher)
    }

    fun assertBubbleMsg(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.tvMessage, matcher)
    }

    fun assertHeaderTitleMsgAtBubblePosition(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.tp_header_title, matcher)
    }

    fun assertCtaHeaderMsgAtBubblePosition(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.tp_header_cta, matcher)
    }

    fun assertDividerHeaderContainer(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.v_header_divider, matcher)
    }

    fun assertMsgHeaderContainer(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.ll_msg_header, matcher)
    }

    fun assertMsgInfo(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.txt_info, matcher)
    }
}

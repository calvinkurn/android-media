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
        assertViewInRecyclerViewAt(0, R.id.iu_msg_icon, matcher)
    }

    private fun assertBubbleMsg(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(0, R.id.tvMessage, matcher)
    }
}
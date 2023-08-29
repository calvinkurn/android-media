package com.tokopedia.topchat.chatroom.view.activity.robot.broadcast

import android.view.View
import androidx.annotation.ColorRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasTextColor
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsInstanceOf
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.assertViewInRecyclerViewAt
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.BroadcastSpamHandlerUiModel
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

object BroadcastResult {

    fun assertBroadcastShown() {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(0, BroadCastUiModel::class.java)
        )
    }

    fun assertBroadcastSpamHandler() {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(0, BroadcastSpamHandlerUiModel::class.java)
        )
    }

    fun assertBroadcastCtaText(text: String, match: Boolean = true) {
        val matcher = if (match) {
            withText(text)
        } else {
            not(withText(text))
        }
        onView(withId(R.id.topchat_cta_broadcast_tv)).check(
            matches(matcher)
        )
    }

    fun assertBroadcastCtaLabel(isVisible: Boolean) {
        val matcher: ViewAssertion

        @ColorRes val color: Int
        if (isVisible) {
            matcher = matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            color = com.tokopedia.unifyprinciples.R.color.Unify_NN400
        } else {
            matcher = matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE))
            color = com.tokopedia.unifyprinciples.R.color.Unify_GN500
        }
        onView(withId(R.id.topchat_cta_broadcast_label)).check(matcher)
        onView(withId(R.id.topchat_cta_broadcast_tv)).check(
            matches(hasTextColor(color))
        )
    }

    fun assertBroadcastCampaignLabelAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.broadcast_campaign_label, matcher)
    }

    fun assertBroadcastCampaignLabelDescAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.tp_broadcast_campaign_status, matcher)
    }

    fun assertBroadcastCampaignLabelCountdownAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.tu_bc_countdown, matcher)
    }

    fun assertBroadcastCampaignLabelStartDateIconAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.iu_broadcast_start_date, matcher)
    }

    fun assertBroadcastCampaignLabelStartDateTextAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        assertViewInRecyclerViewAt(position, R.id.tp_broadcast_start_date, matcher)
    }
}

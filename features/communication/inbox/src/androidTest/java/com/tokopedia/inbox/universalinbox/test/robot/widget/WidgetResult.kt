package com.tokopedia.inbox.universalinbox.test.robot.widget

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView
import com.tokopedia.test.application.matcher.hasTotalItemOf

object WidgetResult {
    fun assertWidgetMetaTotal(total: Int) {
        onView(withId(R.id.inbox_rv_widget_meta))
            .check(matches(hasTotalItemOf(total)))
    }

    fun assertWidgetMetaGone() {
        onView(withId(R.id.inbox_rv_widget_meta))
            .check(doesNotExist())
    }

    fun assertWidgetMetaTitle(position: Int, titleText: String) {
        onView(
            withRecyclerView(R.id.inbox_rv_widget_meta)
                .atPositionOnView(position, R.id.inbox_tv_title_widget)
        ).check(matches(withText(titleText)))
    }

    fun assertWidgetMetaCounter(position: Int, counterText: String) {
        onView(
            withRecyclerView(R.id.inbox_rv_widget_meta)
                .atPositionOnView(position, R.id.inbox_notification_icon_widget)
        ).check(matches(withText(counterText)))
    }

    fun assertWidgetMetaCounterGone(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv_widget_meta)
                .atPositionOnView(position, R.id.inbox_notification_icon_widget)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun assertWidgetMetaLocalLoad(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position = position, R.id.inbox_local_load_widget_meta)
        ).check(matches(isDisplayed()))
    }

    fun assertWidgetIndividualLocalLoad(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv_widget_meta)
                .atPositionOnView(position = position, R.id.inbox_local_load_widget)
        ).check(matches(isDisplayed()))
    }

    fun assertApplinkHelp() {
        Intents.intended(
            IntentMatchers.hasData(
                ApplinkConst.INBOX_TICKET
            )
        )
    }

    fun assertApplinkChatListDriver() {
        Intents.intended(
            IntentMatchers.hasData(
                ApplinkConst.TOKO_CHAT_LIST
            )
        )
    }
}

package com.tokopedia.home_account.utils

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule.Companion.MODE_SUBSET
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.home_account.R
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat

class HomeAccountRobot {

    init {
        Thread.sleep(3000)
    }

    fun clickProfile() {
        onView(withId(R.id.home_account_profile_section)).perform(click())
    }

    fun clickMember() {
        onView(withId(R.id.home_account_member_layout_member_forward)).perform(click())
    }

    fun clickWalletWithText(text: String) {
        onView(withText(text)).perform(click())
    }

    fun scrollToPengaturanAkun() {
        val afterPengaturanAplikasiPosition = 5
        onView(withId(R.id.home_account_user_fragment_rv)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(
                afterPengaturanAplikasiPosition
            )
        )
        Thread.sleep(10000)
    }

    fun scrollToPengaturanAplikasi() {
        val afterPengaturanAplikasiPosition = 6
        onView(withId(R.id.home_account_user_fragment_rv)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(
                afterPengaturanAplikasiPosition
            )
        )
        onView(withText(R.string.menu_account_section_title_app_setting)).perform(click())
        onView(withId(R.id.home_account_user_fragment_rv)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(afterPengaturanAplikasiPosition)
        )
        Thread.sleep(1000)
    }

    fun switchShakeShake() {
        onView(
            CoreMatchers.allOf(
                ViewMatchers.hasSibling(withText("Shake Shake")),
                withId(R.id.account_user_item_common_switch)
            )
        ).perform(click())
    }

    fun assertClickTrackerAtFirstPage(rule: CassavaTestRule) {
        assertThat(rule.validate(CassavaQueries.clickTrackerFirstPage, MODE_SUBSET), hasAllSuccess())
    }

    fun assertClickTracketAtPengaturanAplikasi(rule: CassavaTestRule) {
        assertThat(rule.validate(CassavaQueries.clickTrackerPengaturanAplikasi, MODE_SUBSET), hasAllSuccess())
    }
}

fun homeAccountRobot(func: HomeAccountRobot.() -> Unit) = HomeAccountRobot().apply(func)

fun stubAllIntent() {
    intending(anyIntent()).respondWith(
        Instrumentation.ActivityResult(Activity.RESULT_OK, null)
    )
}

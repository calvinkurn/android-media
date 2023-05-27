package com.tokopedia.home_account.utils

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule.Companion.MODE_SUBSET
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.home_account.R
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.MatcherAssert.assertThat

class HomeAccountRobot {

    init {
        waitForIt(3.0f)
    }

    fun clickProfile() {
        onView(withId(R.id.home_account_profile_section)).perform(click())
    }

    fun clickMember() {
        onView(withId(R.id.home_account_member_layout_member_forward)).perform(click())
    }

    fun clickLihatSemuaSaldoPoint() {
        onView(
            allOf(
                hasSibling(withText("Saldo & Points")),
                withId(R.id.home_account_view_more)
            )
        ).perform(click())
    }

    fun clickWalletWithText(text: String) {
        onView(withText(text)).perform(click())
    }

    fun clickSectionWithText(id: Int) {
        onView(withText(id)).perform(click())
    }

    fun privacyCenterItemsDoNotExist() {
        onView(withText("Shake Shake")).check(doesNotExist())
        onView(withText("Geolokasi")).check(doesNotExist())
    }

    fun hasApplinkOf(applink: String) {
        intended(hasData(applink))
    }

    fun scrollToPengaturanAkun() {
        val afterPengaturanAkun = 5
        onView(withId(R.id.home_account_user_fragment_rv)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(
                afterPengaturanAkun
            )
        )
        waitForIt(2.0f)
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
        waitForIt(2.0f)
    }

    fun switchShakeShake() {
        onView(
            allOf(
                hasSibling(withText("Shake Shake")),
                withId(R.id.account_user_item_common_switch)
            )
        ).perform(click())
    }

    fun assertClickTrackerAtFirstPage(rule: CassavaTestRule) {
        assertThat(
            rule.validate(CassavaQueries.clickTrackerFirstPage, MODE_SUBSET),
            hasAllSuccess()
        )
    }

    fun assertClickTrackerAtPengaturanAkun(rule: CassavaTestRule) {
        assertThat(
            rule.validate(CassavaQueries.clickTrackerPengaturanAkun, MODE_SUBSET),
            hasAllSuccess()
        )
    }

    fun assertClickTracketAtPengaturanAplikasi(rule: CassavaTestRule) {
        assertThat(
            rule.validate(CassavaQueries.clickTrackerPengaturanAplikasi, MODE_SUBSET),
            hasAllSuccess()
        )
    }
}

private fun waitForIt(second: Float = 0.5f) = Thread.sleep((second * 1000).toLong())

fun homeAccountRobot(func: HomeAccountRobot.() -> Unit) = HomeAccountRobot().apply(func)

fun stubAllIntent() {
    intending(anyIntent()).respondWith(
        Instrumentation.ActivityResult(Activity.RESULT_OK, null)
    )
}

fun stubInternalIntent() {
    intending(isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
}

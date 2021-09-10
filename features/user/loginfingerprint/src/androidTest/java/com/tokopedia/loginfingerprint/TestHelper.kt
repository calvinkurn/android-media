package com.tokopedia.loginfingerprint

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.loginfingerprint.view.activity.VerifyFingerprintActivity

/**
 * Created by Yoris on 09/09/21.
 */

fun runTest(func: TestHelper.() -> Unit) = TestHelper().apply(func)

class TestHelper {

    fun launchFrom(rule: ActivityTestRule<VerifyFingerprintActivity>, screenName: String) {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, VerifyFingerprintActivity::class.java)
        rule.launchActivity(intent)
        waitForData()
    }

    private fun waitForData() {
        Thread.sleep(1000L)
    }

    fun check_fp_dialog_displayed() {
        Espresso.onView(ViewMatchers.withText((R.string.title_main)))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(1000)
    }

    fun scan_valid_fingerprint() {
        Runtime.getRuntime().exec("adb -e emu finger touch 1")
    }

    fun scan_invalid_fingerprint() {
        Runtime.getRuntime().exec("adb -e emu finger touch 2")
    }

    infix fun submit(func: TestResult.() -> Unit): TestResult {
//        Espresso.onView(withId(R.id.btn_save_address))
//            .perform(ViewActions.scrollTo(), ViewActions.click())
        return TestResult().apply(func)
    }
}

class TestResult {
    fun hasPassedAnalytics(rule: CassavaTestRule, path: String) {
        assertThat(
            rule.validate("42"),
            hasAllSuccess()
        )
    }
}
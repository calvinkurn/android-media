package com.tokopedia.loginfingerprint

import android.content.Intent
import androidx.biometric.BiometricPrompt
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
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
    }

    fun on_fingerprint_valid(rule: ActivityTestRule<VerifyFingerprintActivity>) {
        rule.runOnUiThread {
            rule.activity.onFingerprintValid()
        }
    }

    fun on_fingerprint_error(rule: ActivityTestRule<VerifyFingerprintActivity>) {
        rule.runOnUiThread {
            rule.activity.onFingerprintError(BiometricPrompt.ERROR_NEGATIVE_BUTTON, "")
        }
    }

    fun on_fingerprint_invalid(rule: ActivityTestRule<VerifyFingerprintActivity>) {
        rule.runOnUiThread {
            rule.activity.onFingerprintInvalid()
        }
    }

    infix fun submit(func: TestResult.() -> Unit): TestResult {
        return TestResult().apply(func)
    }
}

class TestResult {
    private val BIOMETRIC_QUERY_ID = "42"

    fun hasPassedAnalytics(rule: CassavaTestRule, path: String) {
        assertThat(
            rule.validate(BIOMETRIC_QUERY_ID),
            hasAllSuccess()
        )
    }
}
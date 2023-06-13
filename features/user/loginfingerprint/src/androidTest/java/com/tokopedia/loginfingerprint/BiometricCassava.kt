package com.tokopedia.loginfingerprint

import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.loginfingerprint.view.activity.VerifyFingerprintActivity
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris on 09/09/21.
 */

@CassavaTest
class BiometricCassava {

    @get:Rule
    var activityRule = ActivityTestRule(VerifyFingerprintActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    @Before
    fun setUp() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    @Test
    fun validate_fingerprint() {
        runTest {
            launchFrom(activityRule, "")
            on_fingerprint_valid(activityRule)
            on_fingerprint_invalid(activityRule)
            on_fingerprint_error(activityRule)
            activityRule.finishActivity()
        } submit {
            hasPassedAnalytics(cassavaTestRule, "")
        }
    }
}
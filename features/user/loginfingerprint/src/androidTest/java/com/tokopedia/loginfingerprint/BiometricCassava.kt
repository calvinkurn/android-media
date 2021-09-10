package com.tokopedia.loginfingerprint

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.loginfingerprint.view.activity.VerifyFingerprintActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris on 09/09/21.
 */

class BiometricCassava {

    @get:Rule
    var activityRule = ActivityTestRule(VerifyFingerprintActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    @Before
    fun setUp() {
        Intents.init()
//        setupGraphqlMockResponse {
//            addMockResponse(
//                KEY_QUERY_BANK_LIST,
//                ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_BANK_LIST),
//                MockModelConfig.FIND_BY_CONTAINS
//            )
//            addMockResponse(
//                KEY_QUERY_MENU_DETAIL,
//                ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_CATALOG_MENU_DETAIL),
//                MockModelConfig.FIND_BY_CONTAINS
//            )
//            addMockResponse(
//                KEY_QUERY_PREFIXES,
//                ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_CATALOG_PREFIXES),
//                MockModelConfig.FIND_BY_CONTAINS
//            )
//        }
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    @Test
    fun validate_close_dialog_tracker() {
        runTest {
            launchFrom(activityRule, "")
            check_fp_dialog_displayed()
            onView(withText("Tutup")).check(matches(isDisplayed())).perform(click())
        } submit {
            hasPassedAnalytics(cassavaTestRule, "")
        }
    }

    @Test
    fun validate_valid_fingerprint() {
        runTest {
            launchFrom(activityRule, "")
            check_fp_dialog_displayed()
            scan_valid_fingerprint()
        } submit {
            hasPassedAnalytics(cassavaTestRule, "")
        }
    }

    @Test
    fun validate_invalid_fingerprint() {
        runTest {
            launchFrom(activityRule, "")
            check_fp_dialog_displayed()
            scan_invalid_fingerprint()
        } submit {
            hasPassedAnalytics(cassavaTestRule, "")
        }
    }

    @After
    fun cleanUp() {
        Intents.release()
    }
}
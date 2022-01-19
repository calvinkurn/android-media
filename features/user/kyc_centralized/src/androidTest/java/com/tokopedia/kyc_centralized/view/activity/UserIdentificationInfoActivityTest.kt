package com.tokopedia.kyc_centralized.view.activity

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class UserIdentificationInfoActivityTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        UserIdentificationInfoActivity::class.java, false, false
    )


    @Test
    fun launchTest() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityTestRule.launchActivity(null)
        Thread.sleep(5000)
    }
}
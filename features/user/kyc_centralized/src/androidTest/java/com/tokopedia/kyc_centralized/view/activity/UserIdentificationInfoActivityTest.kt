package com.tokopedia.kyc_centralized.view.activity

import android.Manifest
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.kyc_centralized.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

@UiTest
@RunWith(AndroidJUnit4::class)
class UserIdentificationInfoActivityTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        UserIdentificationInfoActivity::class.java, false, false
    )

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA,
    )

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(
                "get_project_info",
                getRawString(
                    ApplicationProvider.getApplicationContext(),
                    R.raw.get_project_info_not_verified
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }
    }


    @Test
    fun launchTest() {
        activityTestRule.launchActivity(null)
        onView(withId(R.id.kyc_benefit_checkbox)).perform(click())
        onView(withId(R.id.kyc_benefit_btn)).perform(click())
        onView(withId(R.id.button)).perform(click())
        Thread.sleep(1_000)
        onView(withId(R.id.image_button_shutter)).perform(click())
        Thread.sleep(2_000)
        onView(withId(R.id.next_button)).perform(click())
        Thread.sleep(1_000)
        onView(withId(R.id.button)).perform(click())
        Thread.sleep(2_000)
        onView(withId(R.id.image_button_shutter)).perform(click())
        Thread.sleep(2_000)
    }
}
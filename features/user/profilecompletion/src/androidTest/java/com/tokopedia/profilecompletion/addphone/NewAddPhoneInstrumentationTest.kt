package com.tokopedia.profilecompletion.addphone

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.profilecompletion.addphone.view.activity.NewAddPhoneActivity
import com.tokopedia.profilecompletion.common.stub.di.TestComponentActivityFactory
import com.tokopedia.profilecompletion.di.ActivityComponentFactory
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class NewAddPhoneInstrumentationTest {

    private val testComponentFactory = TestComponentActivityFactory()

    @get:Rule
    var activityTestRule = IntentsTestRule(
        NewAddPhoneActivity::class.java, false, false
    )

    @Before
    fun before() {
        ActivityComponentFactory.instance = testComponentFactory
    }

    @After
    fun after() {
        activityTestRule.finishActivity()
    }

    @Test
    fun init_register_then_init_view_displayed() {
        activityTestRule.launchActivity(Intent())

        isInitViewDisplayed()
    }

    @Test
    fun field_phone_error_empty_is_displayed() {
        activityTestRule.launchActivity(Intent())

        isErrorFieldEmpty()
    }

    @Test
    fun field_phone_error_too_short_is_displayed() {
        activityTestRule.launchActivity(Intent())

        isErrorFieldPhoneTooShort()
    }

    @Test
    fun field_phone_error_exceed_length_is_displayed() {
        activityTestRule.launchActivity(Intent())

        isErrorFieldPhoneExceedLength()
    }

    @Test
    fun input_invalid_phone_and_submit_then_error_is_displayed() {
        activityTestRule.launchActivity(Intent())

        inputInvalidPhoneThenShowError()
    }
}

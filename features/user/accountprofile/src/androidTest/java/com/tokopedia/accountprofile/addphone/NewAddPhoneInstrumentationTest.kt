package com.tokopedia.accountprofile.addphone

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.accountprofile.settingprofile.addphone.view.activity.NewAddPhoneActivity
import com.tokopedia.accountprofile.common.helper.checkResultCode
import com.tokopedia.accountprofile.common.helper.respondWithFailed
import com.tokopedia.accountprofile.common.helper.respondWithOk
import com.tokopedia.accountprofile.common.stub.di.createProfileCompletionComponent
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class NewAddPhoneInstrumentationTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        NewAddPhoneActivity::class.java,
        false,
        false
    )

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Before
    fun before() {
        val fakeBaseComponent =
            createProfileCompletionComponent(applicationContext.applicationContext)

        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)
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

        inputInvalidValidatePhoneThenShowError()
    }

    @Test
    fun input_valid_phone_and_submit_then_error_update() {
        activityTestRule.launchActivity(Intent())

        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputInvalidUpdatePhoneThenShowError()
    }

    @Test
    fun input_valid_phone_and_submit_and_failed_otp_then_init_view_showing() {
        activityTestRule.launchActivity(Intent())

        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithFailed()

        inputValidUpdatePhoneThenSuccess()
        isViewFailedOtpDisplayed()
    }

    @Test
    fun input_valid_phone_and_submit_then_success_update() {
        activityTestRule.launchActivity(Intent())

        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputValidUpdatePhoneThenSuccess()
        checkResultCode(activityTestRule.activityResult, Activity.RESULT_OK)
    }

}

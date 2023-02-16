package com.tokopedia.profilecompletion.addphone

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.profilecompletion.addphone.view.activity.NewAddPhoneActivity
import com.tokopedia.profilecompletion.common.helper.respondWithOk
import com.tokopedia.profilecompletion.common.stub.di.createProfileCompletionComponent
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@CassavaTest
class NewAddPhoneCassavaTest {

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

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
        val fakeBaseComponent = createProfileCompletionComponent(applicationContext.applicationContext)

        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)
    }


    @After
    fun after() {
        activityTestRule.finishActivity()
    }

    @Test
    fun input_invalid_phone_and_submit_then_error_is_displayed() {
        activityTestRule.launchActivity(Intent())

        inputInvalidValidatePhoneThenShowError()

        ViewMatchers.assertThat(
            cassavaRule.validate(TEST_CASE_ID_564),
            hasAllSuccess()
        )
    }

    @Test
    fun input_valid_phone_and_submit_then_success_update() {
        activityTestRule.launchActivity(Intent())

        Intents.intending(IntentMatchers.hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputValidUpdatePhoneThenSuccess()

        ViewMatchers.assertThat(
            cassavaRule.validate(TEST_CASE_ID_564),
            hasAllSuccess()
        )
    }

    companion object {
        private const val TEST_CASE_ID_564 = "564"
    }
}

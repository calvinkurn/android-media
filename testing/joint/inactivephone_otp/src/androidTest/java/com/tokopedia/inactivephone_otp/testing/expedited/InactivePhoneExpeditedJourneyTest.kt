package com.tokopedia.inactivephone_otp.testing.expedited

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.inactivephone_otp.testing.common.robot.waitForData
import com.tokopedia.inactivephone_otp.testing.inactivephone.InactivePhoneInterceptor
import com.tokopedia.inactivephone_otp.testing.inactivephone.onboarding.expedited.InactivePhoneExpeditedOnboardingIntentRule
import com.tokopedia.inactivephone_otp.testing.inactivephone.onboarding.inactivePhoneOnboarding
import com.tokopedia.inactivephone_otp.testing.inactivephone.submitnewphone.inactivePhoneSubmitData
import com.tokopedia.inactivephone_otp.testing.otp.OtpInterceptor
import com.tokopedia.inactivephone_otp.testing.otp.modelist.otpModeList
import com.tokopedia.inactivephone_otp.testing.otp.verification.otpVerification
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.onboarding.withpin.InactivePhoneWithPinActivity
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InactivePhoneExpeditedJourneyTest {

    @get:Rule
    var activityRule = InactivePhoneExpeditedOnboardingIntentRule()

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    private var context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        GraphqlClient.reInitRetrofitWithInterceptors(
            listOf(
                InactivePhoneInterceptor(context),
                OtpInterceptor(context)
            ),
            context
        )
    }

    private fun launchOnboardingInactivePhone() {
        val userIdEnc = "7Bf2:ZhAOqSV7UZAGYtHF/U//0nvMcWPukzIjUzHcEUUoEyj9SOU="
        val phoneNumber = "084444123123"
        val email = "rivaldy.firmansyah+130@tokopedia.com"
        activityRule.launchActivity(InactivePhoneWithPinActivity.createIntent(
            context,
            InactivePhoneUserDataModel(
                userIdEnc = userIdEnc,
                oldPhoneNumber = phoneNumber,
                email = email,
                userIndex = 1
            )
        ))
    }

    private fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryId: String) {
        MatcherAssert.assertThat(cassavaTestRule.validate(queryId), hasAllSuccess())
    }

    @Test
    fun inactiveCantAccessEmail() {
        launchOnboardingInactivePhone()
        inactivePhoneOnboarding {
            clickOnButtonLanjut()
        }

        otpModeList {
            waitForData()
            clickOnUbahNomorHp()
        }

        waitForData()
        hasPassedAnalytics(cassavaRule, INACTIVEPHONE_CANT_ACCESS_EMAIL_USER_JOURNEY)
    }

    @Test
    fun inactiveGotoRegularFlow() {
        launchOnboardingInactivePhone()
        inactivePhoneOnboarding {
            clickOnButtonLanjut()
        }

        otpModeList {
            waitForData()
            clickOnOtpModeEmail()
        }

        otpVerification {
            clickOnUbahNomorHp()
        }

        waitForData()
        hasPassedAnalytics(cassavaRule, INACTIVEPHONE_GO_TO_REGULAR_FLOW_USER_JOURNEY)
    }

    @Test
    fun submitInactivePhoneWithExpeditedFlow() {
        val mockExtras = Intent().apply {
            putExtras(Bundle().apply {
                putExtra("isUseRegularFlow", false)
                putExtra("token", "token")
            })
        }

        launchOnboardingInactivePhone()

        Intents.intending(
            hasComponent(VerificationActivity::class.java.name)
        ).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, mockExtras)
        )

        inactivePhoneOnboarding {
            clickOnButtonLanjut()
        }

        inactivePhoneSubmitData {
            clickOnButtonSimpan()
            waitForData()
            clickOnButtonBack()
            waitForData()
            clickOnButtonLanjutUbah()
        }

        waitForData()
        hasPassedAnalytics(cassavaRule, INACTIVEPHONE_USER_JOURNEY)

    }

    @After
    fun tearDown() {
        activityRule.finishActivity()
    }

    companion object {
        private const val INACTIVEPHONE_CANT_ACCESS_EMAIL_USER_JOURNEY = "145"
        private const val INACTIVEPHONE_USER_JOURNEY = "146"
        private const val INACTIVEPHONE_GO_TO_REGULAR_FLOW_USER_JOURNEY = "147"
    }

}
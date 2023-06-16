package com.tokopedia.kyc_centralized

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.VerificationModes.times
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent
import com.tokopedia.abstraction.common.di.module.AppModule
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.fakes.FakeGraphqlRepository
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraActivity
import com.tokopedia.kyc_centralized.util.MockTimber
import com.tokopedia.kyc_centralized.util.hasRedirectUrlFinal
import com.tokopedia.kyc_centralized.util.waitOnView

class KycRobot {

    fun checkTermsAndCondition() {
        // waiting for user consent
        Thread.sleep(500)
        onView(withId(com.tokopedia.usercomponents.R.id.checkboxPurposes)).perform(click())
    }

    fun atInfoClickNext() {
        onView(withId(R.id.kyc_benefit_btn)).perform(click())
    }

    fun atKtpIntroClickNext() {
        onView(withId(R.id.button)).perform(click())
    }

    fun atFinalAlacarteClickOK() {
        onView(withId(R.id.uii_simple_button)).perform(click())
    }

    fun atCameraClickCapture() {
        Thread.sleep(500)
        onView(withId(R.id.image_button_shutter)).perform(click())
    }

    fun atCameraClickNext() {
        Thread.sleep(1_000)
        onView(withId(R.id.next_button)).perform(click())
    }

    fun atFaceIntroClickNext() {
        Thread.sleep(2_000)
        onView(withId(R.id.button)).perform(click())
    }

    fun atFinalPressCta() {
        Thread.sleep(2_000)
        onView(withId(R.id.upload_button)).perform(click())
    }

    fun atFinalPressErrorButton() {
        Thread.sleep(2_000)
        onView(withId(R.id.kyc_upload_error_button)).perform(click())
    }
}

class KycResultRobot {

    fun shouldShowPendingPage() {
        waitOnView(withText(R.string.kyc_pending_title)).check(matches(isDisplayed()))
    }

    fun hasCameraIntent(count: Int = 1) {
        intended(hasComponent(UserIdentificationCameraActivity::class.java.name), times(count))
    }

    fun hasLivenessIntent(count: Int = 1, projectId: String) {
        intended(hasData(ApplinkConstInternalUserPlatform.KYC_LIVENESS.replace("{projectId}", projectId)), times(count))
    }

    fun hasRedirectUrl(timber: MockTimber, url: String) {
        assertThat(timber, hasRedirectUrlFinal(url))
    }

    fun shouldShowErrorSnackbar(textError: Int) {
        onView(withText(textError)).check(matches(isDisplayed()))
    }
}

fun kycRobot(func: KycRobot.() -> Unit): KycRobot {
    return KycRobot().apply(func)
}

infix fun KycRobot.validate(func: KycResultRobot.() -> Unit): KycResultRobot {
    // in KYC, there is no manual CTA Button to upload, so we just wait
    Thread.sleep(2000)
    return KycResultRobot().apply(func)
}

fun stubAppGraphqlRepo() {
    val baseAppComponent = DaggerBaseAppComponent.builder()
        .appModule(object : AppModule(ApplicationProvider.getApplicationContext()) {
            override fun provideGraphqlRepository(): GraphqlRepository {
                return FakeGraphqlRepository()
            }
        }).build()
    ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(baseAppComponent)
}

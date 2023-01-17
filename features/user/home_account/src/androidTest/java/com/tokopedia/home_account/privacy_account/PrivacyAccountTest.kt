package com.tokopedia.home_account.privacy_account

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.home_account.privacy_account.stub.data.PrivacyAccountRepositoryStub
import com.tokopedia.home_account.privacy_account.stub.data.TestState
import com.tokopedia.home_account.privacy_account.stub.di.DaggerFakePrivacyAccountComponentStub
import com.tokopedia.home_account.privacy_account.stub.di.FakePrivacyAccountModule
import com.tokopedia.home_account.privacy_account.view.PrivacyAccountActivity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class PrivacyAccountTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        PrivacyAccountActivity::class.java, false, false
    )

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private lateinit var repositoryStub: PrivacyAccountRepositoryStub

    @Before
    fun before() {
        val fakeBaseComponent = DaggerFakePrivacyAccountComponentStub.builder()
            .fakePrivacyAccountModule(FakePrivacyAccountModule(applicationContext)).build()
        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)
        repositoryStub = fakeBaseComponent.repo() as PrivacyAccountRepositoryStub
    }

    @Test
    fun account_linked_displayed() {
        repositoryStub.setState(TestState.ACCOUNT_LINKED)
        activityTestRule.launchActivity(Intent())
        isAccountLinkingDisplayed()
    }

    @Test
    fun account_not_linked_displayed() {
        repositoryStub.setState(TestState.ACCOUNT_NOT_LINKED)
        activityTestRule.launchActivity(Intent())
        isAccountNotLinkingDisplayed()
    }

    @Test
    fun get_privacy_account_then_toggle_active() {
        repositoryStub.setState(TestState.GET_PRIVACY_ACCOUNT_ENABLED)
        activityTestRule.launchActivity(Intent())
        isConsentSocialNetworkDisplayed(true)
    }

    @Test
    fun get_privacy_account_then_toggle_inactive() {
        repositoryStub.setState(TestState.GET_PRIVACY_ACCOUNT_DISABLED)
        activityTestRule.launchActivity(Intent())
        isConsentSocialNetworkDisplayed(false)
    }

    @Test
    fun clarification_data_usage_displayed() {
        repositoryStub.setState(TestState.GET_PRIVACY_ACCOUNT_ENABLED)
        activityTestRule.launchActivity(Intent())
        clarificationDataUsageDisplayed()
    }

    @Test
    fun verification_enabled_data_usage_displayed() {
        repositoryStub.setState(TestState.GET_PRIVACY_ACCOUNT_DISABLED)
        activityTestRule.launchActivity(Intent())
        verificationEnabledDataUsageAction()
    }

    @Test
    fun submit_enabled_data_usage_success_displayed() {
        repositoryStub.setState(TestState.GET_PRIVACY_ACCOUNT_DISABLED)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SET_CONSENT_SOCIAL_NETWORK_SUCCESS)
        submitEnabledDataUsageSuccessAction()
    }

    @Test
    fun submit_enabled_data_usage_failed_displayed() {
        repositoryStub.setState(TestState.GET_PRIVACY_ACCOUNT_DISABLED)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SET_CONSENT_SOCIAL_NETWORK_FAILED)
        submitEnabledDataUsageFailedAction()
    }

    @Test
    fun verification_disabled_data_usage_displayed() {
        repositoryStub.setState(TestState.GET_PRIVACY_ACCOUNT_ENABLED)
        activityTestRule.launchActivity(Intent())
        verificationDisabledDataUsageAction()
    }

    @Test
    fun verification_dismiss_disabled_data_usage_displayed() {
        repositoryStub.setState(TestState.GET_PRIVACY_ACCOUNT_ENABLED)
        activityTestRule.launchActivity(Intent())
        verificationDismissDisabledDataUsageAction()
    }

    @Test
    fun verification_submit_disabled_data_usage_success_displayed() {
        repositoryStub.setState(TestState.GET_PRIVACY_ACCOUNT_ENABLED)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SET_CONSENT_SOCIAL_NETWORK_SUCCESS)
        verificationSubmitDisabledDataUsageSuccessAction()
    }

    @Test
    fun verification_submit_disabled_data_usage_failed_displayed() {
        repositoryStub.setState(TestState.GET_PRIVACY_ACCOUNT_ENABLED)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SET_CONSENT_SOCIAL_NETWORK_FAILED)
        verificationSubmitDisabledDataUsageFailedAction()
    }

}

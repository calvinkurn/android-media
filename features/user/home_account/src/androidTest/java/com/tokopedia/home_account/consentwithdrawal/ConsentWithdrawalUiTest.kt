package com.tokopedia.home_account.consentwithdrawal

import android.app.Activity
import android.app.Instrumentation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.home_account.common.di.FakeAppModule
import com.tokopedia.home_account.consentWithdrawal.ui.ConsentWithdrawalActivity
import com.tokopedia.home_account.consentwithdrawal.fakes.ConsentWithdrawalRepositoryStub
import com.tokopedia.home_account.consentwithdrawal.fakes.ConsentWithdrawalTestCase
import com.tokopedia.home_account.consentwithdrawal.fakes.di.DaggerFakeConsentWithdrawalComponent
import com.tokopedia.home_account.consentwithdrawal.fakes.di.FakeConsentWithdrawalModule
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class ConsentWithdrawalUiTest {

    @get:Rule
    var activityRule = IntentsTestRule(
        ConsentWithdrawalActivity::class.java,
        false,
        false
    )

    private var repositoryStub: ConsentWithdrawalRepositoryStub? = null
    private val applicationContext = InstrumentationRegistry
        .getInstrumentation()
        .context
        .applicationContext

    private val intent = RouteManager.getIntent(
        applicationContext,
        ApplinkConstInternalUserPlatform.CONSENT_WITHDRAWAL,
        "0"
    )

    private fun intendingAnyIntent() {
        Intents.intending(
            IntentMatchers.anyIntent()
        ).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
    }

    @Before
    fun setup() {
        val fakeBaseAppComponent = DaggerFakeConsentWithdrawalComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .fakeConsentWithdrawalModule(FakeConsentWithdrawalModule())
            .build()

        ApplicationProvider
            .getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseAppComponent)

        repositoryStub = fakeBaseAppComponent.repo() as ConsentWithdrawalRepositoryStub
    }

    @Test
    fun loadPurposeByGroup() {
        repositoryStub?.setTestCase(ConsentWithdrawalTestCase.LOAD_CONSENT_BY_GROUP_ID)
        activityRule.launchActivity(intent)

        consentWithdrawalRobot {
        } validateUi {
            shouldViewMandatoryPurposeSection()
            shouldViewOptionalPurposeSection()
        }
    }

    @Test
    fun optOutMandatoryPurpose() {
        repositoryStub?.setTestCase(ConsentWithdrawalTestCase.LOAD_CONSENT_BY_GROUP_ID)
        activityRule.launchActivity(intent)
        intendingAnyIntent()

        consentWithdrawalRobot {
            clickOnMandatoryPurpose()
        } validateUi {
            shouldViewMandatoryPurposeSection()
        }
    }

    @Test
    fun optInOptionalPurpose() {
        repositoryStub?.setTestCase(ConsentWithdrawalTestCase.LOAD_CONSENT_BY_GROUP_ID_WITH_OPT_OUT_LIST)
        activityRule.launchActivity(intent)

        consentWithdrawalRobot {
            clickOnOptionalPurpose()
        } validateUi {
            shouldToggleActive()
        }
    }

    @Test
    fun showDialogOptOutOnOptionalPurpose() {
        repositoryStub?.setTestCase(ConsentWithdrawalTestCase.LOAD_CONSENT_BY_GROUP_ID)
        activityRule.launchActivity(intent)

        consentWithdrawalRobot {
            clickOnOptionalPurpose()
        } validateUi {
            shouldViewPopupConfirmation()
        }
    }

    @Test
    fun optOutOptionalPurpose() {
        repositoryStub?.setTestCase(ConsentWithdrawalTestCase.LOAD_CONSENT_BY_GROUP_ID)
        activityRule.launchActivity(intent)

        consentWithdrawalRobot {
            clickOnOptionalPurpose()
            clickAgreeOnPopup()
        } validateUi {
            shouldToggleInactive()
        }
    }
}

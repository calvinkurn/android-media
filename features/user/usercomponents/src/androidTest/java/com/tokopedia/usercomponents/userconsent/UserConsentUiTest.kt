package com.tokopedia.usercomponents.userconsent

import android.app.Activity
import android.app.Instrumentation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.usercomponents.common.stub.di.FakeAppModule
import com.tokopedia.usercomponents.userconsent.common.ConsentCollectionResponse
import com.tokopedia.usercomponents.userconsent.common.PurposeDataModel
import com.tokopedia.usercomponents.userconsent.fakes.GET_COLLECTION_JSON
import com.tokopedia.usercomponents.userconsent.fakes.UserConsentRepositoryStub
import com.tokopedia.usercomponents.userconsent.fakes.UserConsentUiTestType.*
import com.tokopedia.usercomponents.userconsent.fakes.di.DaggerFakeUserConsentComponent
import com.tokopedia.usercomponents.userconsent.fakes.di.FakeUserConsentModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class UserConsentUiTest {

    @get:Rule
    var activityRule = IntentsTestRule(
        UserConsentDebugActivity::class.java, false, false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    private var repositoryStub: UserConsentRepositoryStub? = null
    private val applicationContext = InstrumentationRegistry
        .getInstrumentation()
        .context
        .applicationContext

    @Before
    fun setup() {
        val fakeBaseComponent = DaggerFakeUserConsentComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .fakeUserConsentModule(FakeUserConsentModule())
            .build()

        ApplicationProvider
            .getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)

        repositoryStub = fakeBaseComponent.repo() as UserConsentRepositoryStub
    }

    @After
    fun tearDown() {
        activityRule.finishActivity()
    }

    @Test
    fun loadConsentTnCAllMandatory() {
        repositoryStub?.setTestType(TNC_SINGLE_MANDATORY)
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_SINGLE_MANDATORY.name)
        }

        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )

        userConsentRobot {
            clickCheckBoxSingleView()
            clickTncHyperlink()
            clickActionButton()
        } validateComponent {
            shouldViewTnCMandatory(getFakeResponsePurposesData())
            shouldButtonActionEnable()
        } validateTracker {
            assertThat(cassavaRule.validate(UserConsentCassavaRobot.QUERY_TNC_SINGLE_MANDATORY_PURPOSE), hasAllSuccess())
        }
    }

    @Test
    fun loadConsentTnCAllMandatoryThenHideConsent() {
        repositoryStub?.setTestType(TNC_SINGLE_MANDATORY_HIDE_CONSENT)
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_SINGLE_MANDATORY_HIDE_CONSENT.name)
        }

        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )

        userConsentRobot {
        } validateComponent {
            shouldViewTnCMandatory(getFakeResponsePurposesData())
            shouldConsentHide()
            shouldButtonHide()
        } validateTracker {
        }
    }

    @Test
    fun loadConsentTnCAllOptional() {
        repositoryStub?.setTestType(TNC_SINGLE_OPTIONAL)
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_SINGLE_OPTIONAL.name)
        }

        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )

        userConsentRobot {
            clickTncHyperlink()
            clickActionButton()
        } validateComponent {
            shouldViewTnCOptional(getFakeResponsePurposesData())
            shouldButtonActionEnable()
        } validateTracker {
            assertThat(cassavaRule.validate(UserConsentCassavaRobot.QUERY_TNC_SINGLE_OPTIONAL_PURPOSE), hasAllSuccess())
        }
    }

    @Test
    fun loadConsentTnCPolicyAllMandatory() {
        repositoryStub?.setTestType(TNC_POLICY_SINGLE_MANDATORY)
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_POLICY_SINGLE_MANDATORY.name)
        }

        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )

        userConsentRobot {
            clickCheckBoxSingleView()
            clickTncHyperlink()
            clickPolicyHyperlink()
            clickActionButton()
        } validateComponent {
            shouldViewTncPolicyMandatory(getFakeResponsePurposesData())
            shouldButtonActionEnable()
        } validateTracker {
            assertThat(cassavaRule.validate(UserConsentCassavaRobot.QUERY_TNC_POLICY_SINGLE_MANDATORY_PURPOSE), hasAllSuccess())
        }
    }

    @Test
    fun loadConsentTnCPolicyAllOptional() {
        repositoryStub?.setTestType(TNC_POLICY_SINGLE_OPTIONAL)
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_POLICY_SINGLE_OPTIONAL.name)
        }

        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )

        userConsentRobot {
            clickTncHyperlink()
            clickPolicyHyperlink()
            clickActionButton()
        } validateComponent {
            shouldViewTncPolicyOptional(getFakeResponsePurposesData())
            shouldButtonActionEnable()
        } validateTracker {
            assertThat(cassavaRule.validate(UserConsentCassavaRobot.QUERY_TNC_POLICY_SINGLE_OPTIONAL_PURPOSE), hasAllSuccess())
        }
    }

    @Test
    fun loadConsentTnCMultipleOptional() {
        repositoryStub?.setTestType(TNC_MULTIPLE_SOME_ARE_OPTIONAL)
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_MULTIPLE_SOME_ARE_OPTIONAL.name)
        }

        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )

        userConsentRobot {
            clickTncHyperlinkOnMultipleOptional()
            clickCheckBoxMultipleView(0)
            clickCheckBoxMultipleView(1)
            clickCheckBoxMultipleView(2)
            clickActionButton()
        } validateComponent {
            shouldViewTnCMultipleOptional(getFakeResponsePurposesData())
            shouldButtonActionEnable()
        } validateTracker {
            assertThat(cassavaRule.validate(UserConsentCassavaRobot.QUERY_TNC_MULTIPLE_OPTIONAL_PURPOSE), hasAllSuccess())
        }
    }

    @Test
    fun loadConsentTnCPolicyMultipleOptional() {
        repositoryStub?.setTestType(TNC_POLICY_MULTIPLE_SOME_ARE_OPTIONAL)
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_POLICY_MULTIPLE_SOME_ARE_OPTIONAL.name)
        }

        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )

        userConsentRobot {
            clickTncHyperlinkOnMultipleOptional()
            clickPolicyHyperlinkOnMultipleOptional()
            clickCheckBoxMultipleView(0)
            clickCheckBoxMultipleView(1)
            clickCheckBoxMultipleView(2)
            clickActionButton()
        } validateComponent {
            shouldViewTnCPolicyMultipleOptional(getFakeResponsePurposesData())
            shouldButtonActionEnable()
        } validateTracker {
            assertThat(cassavaRule.validate(UserConsentCassavaRobot.QUERY_TNC_POLICY_MULTIPLE_OPTIONAL_PURPOSE), hasAllSuccess())
        }
    }

    private fun getFakeResponsePurposesData(): MutableList<PurposeDataModel> {
        return Gson()
            .fromJson(GET_COLLECTION_JSON, ConsentCollectionResponse::class.java)
            .data.collectionPoints.first().purposes
    }
}

package com.tokopedia.usercomponents.userconsent

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentInterceptor
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_MULTIPLE_SOME_ARE_OPTIONAL
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_POLICY_MULTIPLE_SOME_ARE_OPTIONAL
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_POLICY_SINGLE_MANDATORY
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_POLICY_SINGLE_OPTIONAL
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_SINGLE_MANDATORY
import com.tokopedia.usercomponents.userconsent.common.UserConsentUiTestCons.TNC_SINGLE_OPTIONAL
import com.tokopedia.usercomponents.userconsent.fakes.FakeGetCollectionResponseDataModel
import com.tokopedia.usercomponents.userconsent.fakes.GET_COLLECTION_JSON
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@CassavaTest
class UserConsentUiTest {

    @get:Rule
    var activityRule = IntentsTestRule(
        UserConsentDebugActivity::class.java, false, false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        GraphqlClient.reInitRetrofitWithInterceptors(
            listOf(UserConsentInterceptor()),
            context
        )
    }

    @After
    fun tearDown() {
        activityRule.finishActivity()
    }

    @Test
    fun loadConsentTnCAllMandatory() {
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_SINGLE_MANDATORY)
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
    fun loadConsentTnCAllOptional() {
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_SINGLE_OPTIONAL)
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
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_POLICY_SINGLE_MANDATORY)
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
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_POLICY_SINGLE_OPTIONAL)
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
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_MULTIPLE_SOME_ARE_OPTIONAL)
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
        activityRule.launchActivity(null)

        userUserConsentDebugViewRobot {
            loadConsentCollection(TNC_POLICY_MULTIPLE_SOME_ARE_OPTIONAL)
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

    private fun getFakeResponsePurposesData(): MutableList<UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel> {
        return Gson().fromJson(
            GET_COLLECTION_JSON,
            FakeGetCollectionResponseDataModel::class.java
        ).data.data.collectionPoints.first().purposes
    }
}
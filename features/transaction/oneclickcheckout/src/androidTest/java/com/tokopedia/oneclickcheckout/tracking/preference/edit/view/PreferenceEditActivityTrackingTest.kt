package com.tokopedia.oneclickcheckout.tracking.preference.edit.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.*
import com.tokopedia.oneclickcheckout.common.robot.addressListPage
import com.tokopedia.oneclickcheckout.common.robot.paymentMethodPage
import com.tokopedia.oneclickcheckout.common.robot.preferenceSummaryPage
import com.tokopedia.oneclickcheckout.common.robot.shippingDurationPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.oneclickcheckout.preference.edit.view.TestPreferenceEditActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockInterceptor
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PreferenceEditActivityTrackingTest {

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/one_click_checkout_preference_edit.json"
    }

    @get:Rule
    val activityRule = IntentsTestRule(TestPreferenceEditActivity::class.java, true, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private var idlingResource: IdlingResource? = null

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    private fun setupGraphqlMockResponse() {
        val mockModelConfig = createMockModelConfig()
        mockModelConfig.createMockModel(context)

        val testInterceptors = listOf(MockInterceptor(mockModelConfig))

        GraphqlClient.reInitRetrofitWithInterceptors(testInterceptors, context)
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse(GET_PREFERENCE_LIST_QUERY, ResourceUtils.getJsonFromResource(GET_PREFERENCE_LIST_DEFAULT_RESPONSE_PATH), FIND_BY_CONTAINS)
                addMockResponse(GET_ADDRESS_LIST_QUERY, ResourceUtils.getJsonFromResource(GET_ADDRESS_LIST_DEFAULT_RESPONSE_PATH), FIND_BY_CONTAINS)
                addMockResponse(GET_SHIPPING_DURATION_QUERY, ResourceUtils.getJsonFromResource(GET_SHIPPING_DURATION_DEFAULT_RESPONSE_PATH), FIND_BY_CONTAINS)
                addMockResponse(GET_LISTING_PARAM_QUERY, ResourceUtils.getJsonFromResource(GET_LISTING_PARAM_DEFAULT_RESPONSE_PATH), FIND_BY_CONTAINS)
                addMockResponse(GET_PREFERENCE_BY_ID_QUERY, ResourceUtils.getJsonFromResource(GET_PREFERENCE_BY_ID_DEFAULT_RESPONSE_PATH), FIND_BY_CONTAINS)
                addMockResponse(CREATE_PREFERENCE_QUERY, ResourceUtils.getJsonFromResource(CREATE_PREFERENCE_SUCCESS_RESPONSE_PATH), FIND_BY_CONTAINS)
                return this
            }
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()

        setupGraphqlMockResponse()

        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
        activityRule.launchActivity(
                Intent().apply {
                    putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, "Pilihan 1")
                    putExtra(PreferenceEditActivity.EXTRA_IS_EXTRA_PROFILE, true)
                }
        )

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))
    }

    @After
    fun cleanup() {
        gtmLogDBSource.deleteAll().subscribe()

        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun performPreferenceEditTrackingActions() {
        addressListPage {
            clickAddButton()
            clickAddress(0)
            clickSimpan()
        }
        shippingDurationPage {
            clickShippingDuration(0)
            clickSimpan()
        }
        paymentMethodPage {
            pretendChoosePayment(activityRule.activity)
        }
        preferenceSummaryPage {
            clickEditAddress()
        }
        addressListPage {
            clickBack()
        }
        preferenceSummaryPage {
            clickEditShipping()
        }
        shippingDurationPage {
            clickBack()
        }
        preferenceSummaryPage {
            clickEditPayment()
        }
        paymentMethodPage {
            clickBack()
        }
        preferenceSummaryPage {
            clickBack()
        }
        paymentMethodPage {
            pretendChoosePayment(activityRule.activity)
        }
        preferenceSummaryPage {
            clickSave()
        }

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME), hasAllSuccess())
    }
}
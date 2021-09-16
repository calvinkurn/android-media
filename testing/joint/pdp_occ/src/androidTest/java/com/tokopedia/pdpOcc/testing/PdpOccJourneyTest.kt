package com.tokopedia.pdpOcc.testing

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.atc_common.testing.interceptor.AtcInterceptor
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.testing.interceptor.OccCartTestInterceptor
import com.tokopedia.oneclickcheckout.testing.interceptor.OccCheckoutTestInterceptor
import com.tokopedia.oneclickcheckout.testing.interceptor.OccLogisticTestInterceptor
import com.tokopedia.oneclickcheckout.testing.interceptor.OccPromoTestInterceptor
import com.tokopedia.oneclickcheckout.testing.robot.orderSummaryPage
import com.tokopedia.product.detail.testing.*
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PdpOccJourneyTest {

    @get:Rule
    var activityRule = ProductDetailIntentRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = false)

    private val productDetailInterceptor = ProductDetailInterceptor()
    private var idlingResource: IdlingResource? = null

    @Before
    fun setup() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        productDetailInterceptor.customP1ResponsePath = RESPONSE_P1_PATH
        productDetailInterceptor.customP2DataResponsePath = RESPONSE_P2_DATA_PATH
        GraphqlClient.reInitRetrofitWithInterceptors(
                listOf(OccCartTestInterceptor(), OccLogisticTestInterceptor(), OccPromoTestInterceptor(),
                        OccCheckoutTestInterceptor(), AtcInterceptor(context), productDetailInterceptor),
                context)

        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun loadCartAndGoToShipment_PassedAnalyticsTest() {
        activityRule.launchActivity(ProductDetailActivity.createIntent(context, 123))

        ProductDetailRobot().apply {
            Thread.sleep(5_000)
            clickBeliLangsungOcc()
        }

        orderSummaryPage {
            Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
            pay()
        }

        assertThat(cassavaRule.validate("47"), hasAllSuccess())
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        activityRule.finishActivity()
    }

    companion object {

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/checkout.json"
    }
}
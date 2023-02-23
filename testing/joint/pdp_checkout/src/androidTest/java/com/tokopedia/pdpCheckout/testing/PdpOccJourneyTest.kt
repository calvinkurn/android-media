package com.tokopedia.pdpCheckout.testing

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.pdpCheckout.testing.atc_common.interceptor.AtcInterceptor
import com.tokopedia.pdpCheckout.testing.oneclickcheckout.interceptor.OccCartTestInterceptor
import com.tokopedia.pdpCheckout.testing.oneclickcheckout.interceptor.OccCheckoutTestInterceptor
import com.tokopedia.pdpCheckout.testing.oneclickcheckout.interceptor.OccLogisticTestInterceptor
import com.tokopedia.pdpCheckout.testing.oneclickcheckout.interceptor.OccPaymentTestInterceptor
import com.tokopedia.pdpCheckout.testing.oneclickcheckout.interceptor.OccPromoTestInterceptor
import com.tokopedia.pdpCheckout.testing.oneclickcheckout.robot.orderSummaryPage
import com.tokopedia.pdpCheckout.testing.product.detail.ProductDetailIntentRule
import com.tokopedia.pdpCheckout.testing.product.detail.ProductDetailInterceptor
import com.tokopedia.pdpCheckout.testing.product.detail.ProductDetailRobot
import com.tokopedia.pdpCheckout.testing.product.detail.RESPONSE_P1_PATH
import com.tokopedia.pdpCheckout.testing.product.detail.RESPONSE_P2_DATA_PATH
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
            listOf(
                OccCartTestInterceptor(),
                OccLogisticTestInterceptor(),
                OccPromoTestInterceptor(),
                OccPaymentTestInterceptor(),
                OccCheckoutTestInterceptor(),
                AtcInterceptor(context),
                productDetailInterceptor
            ),
            context
        )

        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun openPdpAndGoToOcc_PassedAnalyticsTest() {
        activityRule.launchActivity(ProductDetailActivity.createIntent(context, 123))

        ProductDetailRobot().apply {
            Thread.sleep(5_000)
            clickBeliLangsungOcc()
        }

        orderSummaryPage {
            Thread.sleep(5_000)
            Intents.intending(IntentMatchers.anyIntent())
                .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
            pay()
        }

        assertThat(cassavaRule.validate(ANALYTIC_VALIDATOR_QUERY_ID), hasAllSuccess())
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        activityRule.finishActivity()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_ID = "47"
    }
}

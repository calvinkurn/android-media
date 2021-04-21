package com.tokopedia.digital_checkout.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital_checkout.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.*

/**
 * @author by jessica on 03/02/21
 */
class DigitalCartActivityTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(DigitalCartActivity::class.java,
            false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun stubAllIntent() {
        Intents.init()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK,
                null))
    }

    @Test
    fun testDefaultCartView() {
        //Setup intent cart page & launch activity
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        setUpMockResponse()
        validateCartInfoOnUi()
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_DIGITAL_DEFAULT_CART),
                hasAllSuccess())
    }

    private fun setUpMockResponse() {
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_DG_CHECKOUT_GET_CART,
                    InstrumentationMockHelper.getRawString(context, com.tokopedia.digital_checkout.test.R.raw.response_mock_get_cart),
                    MockModelConfig.FIND_BY_CONTAINS
            )
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, DigitalCartActivity::class.java).apply {
            val passData = DigitalCheckoutPassData()
            passData.categoryId = "1"
            passData.clientNumber = "087855813456"
            passData.operatorId = "5"
            passData.productId = "30"
            passData.isPromo = "0"
            passData.needGetCart = true
            passData.isFromPDP = true
            passData.instantCheckout = "0"
            passData.idemPotencyKey = "17211378_d44feedc9f7138c1fd91015d5bd88810"
            putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, passData)
        }.setData(
                Uri.parse("tokopedia-android-internal://digital/checkout")
        )
        mActivityRule.launchActivity(intent)
    }

    private fun validateCartInfoOnUi() {
        //Info Cart Detail
        Thread.sleep(2000)
        onView(withId(R.id.productTitle)).check(matches(withText("Angsuran Kredit")))

        val detailRecyclerView: RecyclerView = mActivityRule.activity.findViewById(R.id.rvDetails)
        assert(detailRecyclerView.adapter?.itemCount ?: 0 > 0)

        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailLabel), withText("Nama"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailValue), withText("Tokopedia User"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailLabel), withText("Nomor Pelanggan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailValue), withText("191111410111"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailLabel), withText("Total Tagihan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailValue), withText("Rp 12.500"))).check(matches(isDisplayed()))

        onView(withId(R.id.tvSeeDetailToggle)).check(matches(isDisplayed()))
        onView(withId(R.id.tvSeeDetailToggle)).check(matches(withText("Lihat Detail")))

        //should show Additional Info
        onView(withId(R.id.tvSeeDetailToggle)).perform(click())
        Thread.sleep(1000)
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailSubtitle), withText("Data Pelanggan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailLabel), withText("Nomor Polisi"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailValue), withText("B1234ACD"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailLabel), withText("Jatuh Tempo"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailValue), withText("23/07/2020"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailLabel), withText("Tagihan Ke"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailValue), withText("103"))).check(matches(isDisplayed()))

        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailSubtitle), withText("Detail Tagihan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailLabel), withText("Denda"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailValue), withText("Rp 0"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailLabel), withText("Biaya Lain"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailValue), withText("Rp 1"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailLabel), withText("Admin"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailValue), withText("Rp 2.500"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailLabel), withText("Tagihan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutDetailValue), withText("Rp 10.000"))).check(matches(isDisplayed()))

        onView(withId(R.id.contentCheckout)).perform(swipeUp())

        // Checkout Summary
        onView(withId(R.id.tvCheckoutSummaryTitle)).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailLabel), withText("Subtotal Tagihan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailLabel), withText("egold"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailValue), withText("Rp12.500"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailValue), withText("Rp500"))).check(matches(isDisplayed()))

        onView(withId(R.id.tvTotalPaymentLabel)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTotalPaymentLabel)).check(matches(withText("Total Tagihan")))
        onView(withId(R.id.tvTotalPayment)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTotalPayment)).check(matches(withText("Rp13.000")))

        Thread.sleep(1000)
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        const val KEY_DG_CHECKOUT_GET_CART = "rechargeGetCart"
        const val ANALYTIC_VALIDATOR_DIGITAL_DEFAULT_CART = "tracker/recharge/digital_checkout/digital_default_checkout.json"
    }

}
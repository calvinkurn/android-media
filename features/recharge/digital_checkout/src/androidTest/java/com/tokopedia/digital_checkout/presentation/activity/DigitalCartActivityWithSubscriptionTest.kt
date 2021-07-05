package com.tokopedia.digital_checkout.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
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
import com.tokopedia.digital_checkout.utils.CustomActionUtils
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.*

/**
 * @author by jessica on 03/02/21
 */
class DigitalCartActivityWithSubscriptionTest {

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
        validateSubscriptionWidgetUi()
        validateFintechProductOnUi()
        validatePaymentPriceOnUi()
        validateTypeOnInputView()
        validateCheckoutSummaryOnUi()
        validateOnClickPromo()

        Thread.sleep(1000)
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_DIGITAL_SUBSCRIPTION_CART),
                hasAllSuccess())
    }

    private fun setUpMockResponse() {
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_DG_CHECKOUT_GET_CART,
                    InstrumentationMockHelper.getRawString(context, com.tokopedia.digital_checkout.test.R.raw.response_mock_get_cart_subscription),
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
        Thread.sleep(1000)
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
    }

    private fun validateSubscriptionWidgetUi() {
        //check subscription widget
        Thread.sleep(1000)
        val checkoutSubscriptionHeaderTitle = onView(AllOf.allOf(withText("Subscription body, this is a description (is subscribed)"),
                withId(R.id.tvCheckoutMyBillsHeaderTitle)) )
        checkoutSubscriptionHeaderTitle.perform(CustomActionUtils.nestedScrollTo())
        checkoutSubscriptionHeaderTitle.check(matches(isDisplayed()))

        val checkoutSubcriptionBody = onView(AllOf.allOf(withText("Text Before Checked"),
                withId(R.id.tvCheckoutMyBillsDescription)) )
        checkoutSubcriptionBody.check(matches(isDisplayed()))
    }

    private fun validateFintechProductOnUi() {
        //check fintech widget
        Thread.sleep(1000)
        val checkoutMyBillsHeaderTitle = onView(AllOf.allOf(withText("This is purchase Protection"),
                withId(R.id.tvCheckoutMyBillsHeaderTitle)) )
        checkoutMyBillsHeaderTitle.perform(CustomActionUtils.nestedScrollTo())
        checkoutMyBillsHeaderTitle.check(matches(isDisplayed()))

        val checkoutMyBillsSubtitle = onView(AllOf.allOf(withText("Rp 500"),
                withId(R.id.tvCheckoutMyBillsDescription)) )
        checkoutMyBillsSubtitle.check(matches(isDisplayed()))

        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).check(matches(not(isChecked())))
        Thread.sleep(1000)
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).check(matches(isChecked()))
    }

    private fun validatePaymentPriceOnUi() {
        //check payment price
        onView(withId(R.id.contentCheckout)).perform(ViewActions.swipeUp())
        onView(withId(R.id.tvTotalPaymentLabel)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTotalPaymentLabel)).check(matches(withText("Total Tagihan")))
        onView(withId(R.id.tvTotalPayment)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTotalPayment)).check(matches(withText("Rp13.000")))
    }

    private fun validateTypeOnInputView() {
        //type input view
        Thread.sleep(1000)
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).perform(click()).perform(ViewActions.clearText(),
                ViewActions.closeSoftKeyboard())
        Thread.sleep(1000)
        // if inputview invalid
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_wrapper)).check(matches(isDisplayed()))
        onView(withId(R.id.btnCheckout)).check(matches(not(isEnabled())))

        Thread.sleep(1000)
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).perform(click()).perform(ViewActions.typeText("10000"),
                ViewActions.closeSoftKeyboard())
        onView(withId(R.id.tvTotalPayment)).check(matches(withText("Rp10.500")))
    }

    private fun validateCheckoutSummaryOnUi() {
        onView(withId(R.id.contentCheckout)).perform(ViewActions.swipeUp())
        onView(withId(R.id.tvCheckoutSummaryTitle)).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailLabel), withText("Subtotal Tagihan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailLabel), withText("purchase-protection"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailValue), withText("Rp10.000"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailValue), withText("Rp500"))).check(matches(isDisplayed()))

        // uncheck protection bills
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).check(matches(not(isChecked())))

        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailLabel), withText("purchase-protection"))).check(doesNotExist())
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailValue), withText("Rp500"))).check(doesNotExist())
    }

    private fun validateOnClickPromo() {
        //click use promo
        Thread.sleep(1000)

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(AllOf.allOf(withId(R.id.digitalPromoBtnView))).perform(click())
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        const val KEY_DG_CHECKOUT_GET_CART = "rechargeGetCart"
        const val ANALYTIC_VALIDATOR_DIGITAL_SUBSCRIPTION_CART = "tracker/recharge/digital_checkout/digital_subscription_checkout.json"
    }

}
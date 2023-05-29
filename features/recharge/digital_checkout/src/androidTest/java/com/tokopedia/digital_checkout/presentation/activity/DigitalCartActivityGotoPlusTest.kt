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
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital_checkout.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * created by @bayazidnasir on 5/7/2022
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class DigitalCartActivityGotoPlusTest {
    @get:Rule
    val mActivityRule = ActivityTestRule(DigitalCartActivity::class.java, false, false)

    @get:Rule
    val cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun stubAllIntent() {
        Intents.init()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
    }

    @Test
    fun testDefaultCartView() {
        // Setup intent cart page & launch activity
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        setUpMockResponse()
        validateCartInfoOnUi()
        clickConsentWidget()
    }

    private fun setUpMockResponse() {
        setupGraphqlMockResponse {
            addMockResponse(
                KEY_DG_CHECKOUT_GET_CART,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_cart_goto_plus),
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
            passData.atcSource = "pg_checkout"
            putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, passData)
        }.setData(
            Uri.parse("tokopedia-android-internal://digital/checkout")
        )
        mActivityRule.launchActivity(intent)
    }

    private fun validateCartInfoOnUi() {
        // Info Cart Detail
        Thread.sleep(2000)
        onView(withId(com.tokopedia.digital_checkout.R.id.productTitle)).check(matches(withText("Angsuran Kredit")))

        val detailRecyclerView: RecyclerView = mActivityRule.activity.findViewById(com.tokopedia.digital_checkout.R.id.rvDetails)
        assert(detailRecyclerView.adapter?.itemCount ?: 0 > 0)

        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailLabel), withText("Nama"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailValue), withText("Tokopedia User"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailLabel), withText("Nomor Pelanggan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailValue), withText("191111410111"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailLabel), withText("Total Tagihan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailValue), withText("Rp 12.500"))).check(matches(isDisplayed()))

        onView(withId(com.tokopedia.digital_checkout.R.id.tvSeeDetailToggle)).check(matches(isDisplayed()))
        onView(withId(com.tokopedia.digital_checkout.R.id.tvSeeDetailToggle)).check(matches(withText("Lihat Detail")))

        // should show Additional Info
        onView(withId(com.tokopedia.digital_checkout.R.id.tvSeeDetailToggle)).perform(click())
        Thread.sleep(1000)
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailSubtitle), withText("Data Pelanggan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailLabel), withText("Nomor Polisi"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailValue), withText("B1234ACD"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailLabel), withText("Jatuh Tempo"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailValue), withText("23/07/2020"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailLabel), withText("Tagihan Ke"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailValue), withText("103"))).check(matches(isDisplayed()))

        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailSubtitle), withText("Detail Tagihan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailLabel), withText("Denda"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailValue), withText("Rp 0"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailLabel), withText("Biaya Lain"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailValue), withText("Rp 1"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailLabel), withText("Admin"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailValue), withText("Rp 2.500"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailLabel), withText("Tagihan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutDetailValue), withText("Rp 10.000"))).check(matches(isDisplayed()))

        onView(withId(com.tokopedia.digital_checkout.R.id.contentCheckout)).perform(swipeUp())

        // Checkout Summary
        onView(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutSummaryTitle)).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutSummaryDetailLabel), withText("Subtotal Tagihan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutSummaryDetailLabel), withText("egold"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutSummaryDetailValue), withText("Rp12.500"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(com.tokopedia.digital_checkout.R.id.tvCheckoutSummaryDetailValue), withText("Rp500"))).check(matches(isDisplayed()))

        onView(withId(com.tokopedia.digital_checkout.R.id.tvTotalPaymentLabel)).check(matches(isDisplayed()))
        onView(withId(com.tokopedia.digital_checkout.R.id.tvTotalPaymentLabel)).check(matches(withText("Total Tagihan")))
        onView(withId(com.tokopedia.digital_checkout.R.id.tvTotalPayment)).check(matches(isDisplayed()))
        onView(withId(com.tokopedia.digital_checkout.R.id.tvTotalPayment)).check(matches(withText("Rp13.000")))

        Thread.sleep(1000)
    }

    private fun clickConsentWidget() {
//        layout_digital_checkout_bottom_view
        // tick consent widget should enable button
        onView(allOf(withId(com.tokopedia.usercomponents.R.id.checkboxPurposes), isDisplayed())).perform(click())
        onView(allOf(withId(com.tokopedia.usercomponents.R.id.checkboxPurposes), isDisplayed())).check(matches(isChecked()))
        onView(getElementFromMatchAtPosition(withId(R.id.btnCheckout), 0)).check(matches(isEnabled()))
        Thread.sleep(1000)

        // untick consent widget should disable button
        onView(allOf(withId(com.tokopedia.usercomponents.R.id.checkboxPurposes), isDisplayed())).perform(click())
        onView(allOf(withId(com.tokopedia.usercomponents.R.id.checkboxPurposes), isDisplayed())).check(matches(not(isChecked())))
        onView(getElementFromMatchAtPosition(withId(R.id.btnCheckout), 0)).check(matches(not(isEnabled())))
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    private companion object {
        const val KEY_DG_CHECKOUT_GET_CART = "rechargeGetCart"
    }
}

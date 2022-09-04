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
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author by jessica on 03/02/21
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class DigitalCartActivityWithDisableVoucher {

    @get:Rule
    val mActivityRule = ActivityTestRule(DigitalCartActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

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
        validateOnClickPromoView()
        validateFintechWidgetOnUi()
        validateCheckoutSummaryOnUi()

        Thread.sleep(1000)
    }

    private fun setUpMockResponse() {
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_DG_CHECKOUT_GET_CART,
                    InstrumentationMockHelper.getRawString(context, com.tokopedia.digital_checkout.test.R.raw.response_mock_get_cart_disable_voucher),
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
            passData.isFromPDP = true
            passData.needGetCart = true
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
        //Info Cart Detail
        Thread.sleep(2000)

        onView(withId(R.id.dialog_content)).check(matches(isDisplayed()))
        onView(withId(R.id.dialog_title)).check(matches(withText("This is pop up")))
        onView(withId(R.id.dialog_description)).check(matches(withText("Please enter okay")))
        onView(withId(R.id.dialog_btn_primary)).check(matches(isDisplayed()))
        onView(withId(R.id.dialog_btn_primary)).check(matches(withText("Yes")))
        onView(withId(R.id.dialog_btn_primary)).perform(click())

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

        //should collapse ttext
        onView(withId(R.id.tvSeeDetailToggle)).check(matches(isDisplayed()))
        onView(withId(R.id.tvSeeDetailToggle)).check(matches(withText("Tutup")))

        //should show Additional Info
        onView(withId(R.id.tvSeeDetailToggle)).perform(click())

        validatePaymentPrice("Rp8.000")
    }

    private fun validateSubscriptionWidgetUi() {
        //check subscription widget
        Thread.sleep(1000)
        val checkoutSubscriptionHeaderTitle = onView(AllOf.allOf(withText("Body Title not subscribed"),
                withId(R.id.tvCheckoutMyBillsHeaderTitle)))
        checkoutSubscriptionHeaderTitle.perform(CustomActionUtils.nestedScrollTo())
        checkoutSubscriptionHeaderTitle.check(matches(isDisplayed()))

        val checkoutSubcriptionBody = onView(AllOf.allOf(withText("Body content before not subscribed"),
                withId(R.id.tvCheckoutMyBillsDescription)))
        checkoutSubcriptionBody.check(matches(isDisplayed()))

        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 0)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 0)).check(matches(isChecked()))
        onView(getElementFromMatchAtPosition(withId(R.id.tvCheckoutMyBillsDescription), 0))
                .check(matches(withText("Body content after subscribed")))

        Thread.sleep(1000)
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 0)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 0)).check(matches(not(isChecked())))
        checkoutSubcriptionBody.check(matches(withText("Body content before not subscribed")))
    }

    private fun validateFintechWidgetOnUi() {
        //check fintech widget
        Thread.sleep(1000)
        val checkoutMyBillsHeaderTitle = onView(AllOf.allOf(withText("Yuk mulai nabung emas"),
                withId(R.id.tvCheckoutMyBillsHeaderTitle)))
        checkoutMyBillsHeaderTitle.perform(CustomActionUtils.nestedScrollTo())
        checkoutMyBillsHeaderTitle.check(matches(isDisplayed()))

        val checkoutMyBillsSubtitle = onView(AllOf.allOf(withText("Rp 500"),
                withId(R.id.tvCheckoutMyBillsDescription)))
        checkoutMyBillsSubtitle.check(matches(isDisplayed()))

        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).check(matches(not(isChecked())))

        onView(withId(R.id.contentCheckout)).perform(ViewActions.swipeUp())
        validatePaymentPrice("Rp7.500")
        Thread.sleep(1000)
    }

    private fun validateCheckoutSummaryOnUi() {
        onView(withId(R.id.tvCheckoutSummaryTitle)).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailLabel), withText("Subtotal Tagihan"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailLabel), withText("emoney"))).check(doesNotExist())
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailValue), withText("Rp12.500"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailValue), withText("Rp500"))).check(doesNotExist())


        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailLabel), withText("PROMOO"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailValue), withText("-Rp5.000"))).check(matches(isDisplayed()))

        // check fintech product
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).check(matches(isChecked()))

        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 2)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 2)).check(matches(isChecked()))

        onView(withId(R.id.contentCheckout)).perform(ViewActions.swipeUp())

        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailLabel), withText("egold"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailValue), withText("Rp500"))).check(matches(isDisplayed()))

        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailLabel), withText("emoney"))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withId(R.id.tvCheckoutSummaryDetailValue), withText("Rp1.000"))).check(matches(isDisplayed()))

        validatePaymentPrice("Rp9.000")

        //untick and tick tebus murah
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 2)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 2)).check(matches(isNotChecked()))
        validatePaymentPrice("Rp8.000")

        Thread.sleep(1000)
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 2)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 2)).check(matches(isChecked()))
        validatePaymentPrice("Rp9.000")
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    private fun validateOnClickPromoView() {
        //check if it is not directed to anywhere when click
        Thread.sleep(1000)

        onView(AllOf.allOf(withId(R.id.digitalPromoBtnView))).perform(click())

        onView(withId(R.id.tv_promo_checkout_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_promo_checkout_title)).check(matches(withText(mActivityRule.activity.resources.getString(R.string.digital_checkout_promo_disabled_title))))
        onView(withId(R.id.tv_promo_checkout_desc)).check(matches(not(isDisplayed())))
        onView(withId(R.id.iv_promo_checkout_right)).check(matches(not(isDisplayed())))
        Thread.sleep(1000)
    }

    private fun validatePaymentPrice(price: String) {
        onView(withId(R.id.tvTotalPaymentLabel)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTotalPaymentLabel)).check(matches(withText("Total Tagihan")))
        onView(withId(R.id.tvTotalPayment)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTotalPayment)).check(matches(withText(price)))
    }

    companion object {
        const val KEY_DG_CHECKOUT_GET_CART = "rechargeGetCart"
    }

}
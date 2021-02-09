package com.tokopedia.digital_checkout.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewParent
import android.widget.FrameLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital_checkout.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matcher
import org.hamcrest.Matchers
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
            passData.instantCheckout = "0"
            passData.idemPotencyKey = "17211378_d44feedc9f7138c1fd91015d5bd88810"
            putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, passData)
        }.setData(
                Uri.parse("tokopedia-android-internal://digital/cart/baru")
        )
        mActivityRule.launchActivity(intent)

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

        //check subscription widget
        Thread.sleep(1000)
        val checkoutSubscriptionHeaderTitle = onView(AllOf.allOf(withText("Subscription body, this is a description (is subscribed)"),
                withId(R.id.tvCheckoutMyBillsHeaderTitle)) )
        checkoutSubscriptionHeaderTitle.perform(nestedScrollTo())
        checkoutSubscriptionHeaderTitle.check(matches(isDisplayed()))

        val checkoutSubcriptionBody = onView(AllOf.allOf(withText("Text Before Checked"),
                withId(R.id.tvCheckoutMyBillsDescription)) )
        checkoutSubcriptionBody.check(matches(isDisplayed()))

        //check fintech widget
        Thread.sleep(1000)
        val checkoutMyBillsHeaderTitle = onView(AllOf.allOf(withText("This is purchase Protection"),
                withId(R.id.tvCheckoutMyBillsHeaderTitle)) )
        checkoutMyBillsHeaderTitle.perform(nestedScrollTo())
        checkoutMyBillsHeaderTitle.check(matches(isDisplayed()))

        val checkoutMyBillsSubtitle = onView(AllOf.allOf(withText("Rp 500"),
                withId(R.id.tvCheckoutMyBillsDescription)) )
        checkoutMyBillsSubtitle.check(matches(isDisplayed()))

        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).check(matches(not(isChecked())))
        Thread.sleep(1000)
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.checkBoxCheckoutMyBills), 1)).check(matches(isChecked()))

        //check payment price
        onView(withId(R.id.tvTotalPaymentLabel)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTotalPaymentLabel)).check(matches(withText("Total Pembayaran")))
        onView(withId(R.id.tvTotalPayment)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTotalPayment)).check(matches(withText("Rp 13.000")))

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
        onView(withId(R.id.tvTotalPayment)).check(matches(withText("Rp 10.000")))

        //click use promo
        Thread.sleep(1000)

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(AllOf.allOf(withId(R.id.relativeLayoutUsePromoGlobal))).perform(click())

        Thread.sleep(1000)
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_DIGITAL_FINTECH_CART),
                hasAllSuccess())
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    private fun nestedScrollTo(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(
                        isDescendantOfA(isAssignableFrom(NestedScrollView::class.java)),
                        withEffectiveVisibility(Visibility.VISIBLE))
            }

            override fun getDescription(): String {
                return "View is not NestedScrollView"
            }

            override fun perform(uiController: UiController, view: View) {
                try {
                    val nestedScrollView = findFirstParentLayoutOfClass(view, NestedScrollView::class.java) as NestedScrollView?
                    if (nestedScrollView != null) {
                        nestedScrollView.scrollTo(0, view.top + view.measuredHeight + 250)
                    } else {
                        throw java.lang.Exception("Unable to find NestedScrollView parent.")
                    }
                } catch (e: java.lang.Exception) {
                    throw PerformException.Builder()
                            .withActionDescription(this.description)
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(e)
                            .build()
                }
                uiController.loopMainThreadUntilIdle()
            }
        }
    }

    private fun findFirstParentLayoutOfClass(view: View, parentClass: Class<out View>): View? {
        var parent: ViewParent = FrameLayout(view.context)
        var incrementView: ViewParent? = null
        var i = 0
        while (parent.javaClass != parentClass) {
            parent = if (i == 0) {
                findParent(view)
            } else {
                findParent(incrementView)
            }
            incrementView = parent
            i++
        }
        return parent as View
    }

    private fun findParent(view: View): ViewParent = view.parent
    private fun findParent(view: ViewParent?): ViewParent = view!!.parent

    companion object {
        const val KEY_DG_CHECKOUT_GET_CART = "rechargeGetCart"
        const val ANALYTIC_VALIDATOR_DIGITAL_FINTECH_CART = "tracker/recharge/digital_checkout/digital_subscription_checkout.json"
    }

}
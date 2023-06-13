package com.tokopedia.rechargegeneral.cases.add_bills

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralInputViewHolder
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralProductSelectViewHolder
import com.tokopedia.rechargegeneral.widget.RechargeGeneralProductSelectBottomSheet
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RechargeGeneralAddBillsActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var mActivityRule = ActivityTestRule(RechargeGeneralActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse(RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.ADD_BILLS_ERROR))

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, RechargeGeneralActivity::class.java).apply {
            putExtra(RechargeGeneralActivity.PARAM_MENU_ID, 113)
            putExtra(RechargeGeneralActivity.PARAM_CATEGORY_ID, 3)
            putExtra(RechargeGeneralActivity.PARAM_ADD_BILLS, "true")
        }
        mActivityRule.launchActivity(intent)
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validate_add_bills() {
        Thread.sleep(5000)
        close_ticker()
        validate_select_operator_token()
        validate_manual_input_token()
        validate_select_product_token()
        validate_select_product_token_close()
        click_inquiry()
        click_add_inquiry()
        click_back()
        MatcherAssert.assertThat(
                cassavaTestRule.validate(ANALYTIC_VALIDATOR_ADD_BILLS),
                hasAllSuccess()
        )
    }

    private fun close_ticker(){
        Thread.sleep(2000)
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.ticker_close_icon), hasSibling(AllOf.allOf(withId(R.id.ticker_content_single), withChild(withText("Tagihan baru akan otomatis terhapus pada 00:00 WIB malam ini kalau belum dibayar.")))))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
        Thread.sleep(2000)
    }

    fun validate_select_operator_token() {
        Espresso.onView(withId(R.id.operator_select)).perform(ViewActions.click())
        Espresso.onView(withText("Token Listrik")).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withText("Tagihan Listrik")).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withText("PLN Non-Taglis")).check(ViewAssertions.matches(isDisplayed()))

        Thread.sleep(1000)
        Espresso.onView(withId(R.id.vg_input_dropdown_recycler_view)).check(ViewAssertions.matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsInputDropdownWidget.TopupBillsInputDropdownViewHolder>(
                        0, ViewActions.click()
                )
        )
        Espresso.onView(withText("Token Listrik")).check(ViewAssertions.matches(isDisplayed()))
    }

    fun validate_manual_input_token() {
        Espresso.onView(withId(R.id.rv_digital_product)).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.rv_digital_product)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        0, ViewActions.typeText("11111111111")
                )
        )
        Thread.sleep(3000)
    }

    fun validate_select_product_token() {
        Espresso.onView(withId(R.id.rv_digital_product)).check(ViewAssertions.matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectViewHolder>(
                        1, ViewActions.click()
                )
        )
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.rv_product_select_dropdown)).check(ViewAssertions.matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectBottomSheet.DigitalProductSelectDropdownAdapter.DigitalProductSelectDropdownViewHolder>(
                        1, ViewActions.click()
                )
        )
        Thread.sleep(1000)
        Espresso.onView(withText("Rp 50.000")).check(ViewAssertions.matches(isDisplayed()))
    }

    fun validate_select_product_token_close() {
        Espresso.onView(withId(R.id.rv_digital_product)).check(ViewAssertions.matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectViewHolder>(
                        1, ViewActions.click()
                )
        )
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.bottom_sheet_close)).perform(click())
        Thread.sleep(1000)
    }

    private fun click_inquiry(){
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.recharge_general_enquiry_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun click_add_inquiry(){
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.btn_sbm_add_inquiry)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun click_back(){
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Thread.sleep(2000)
        Espresso.pressBackUnconditionally()
    }


    companion object{
        private const val ANALYTIC_VALIDATOR_ADD_BILLS = "tracker/recharge/recharge_general_template_add_bills.json"
    }

}
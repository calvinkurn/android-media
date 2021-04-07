package com.tokopedia.rechargegeneral.cases.listrik

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.RechargeGeneralMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralInputViewHolder
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralProductSelectViewHolder
import com.tokopedia.rechargegeneral.widget.RechargeGeneralProductSelectBottomSheet
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RechargeGeneralInstrumentTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var mActivityRule: IntentsTestRule<RechargeGeneralActivity> = object : IntentsTestRule<RechargeGeneralActivity>(RechargeGeneralActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(targetContext, RechargeGeneralActivity::class.java).apply {
                putExtra(RechargeGeneralActivity.PARAM_MENU_ID, 113)
                putExtra(RechargeGeneralActivity.PARAM_CATEGORY_ID, 3)
            }
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()

            setupGraphqlMockResponse(RechargeGeneralMockResponseConfig(RechargeGeneralProduct.LISTRIK))
        }
    }

    @Before
    fun stubAllExternalIntents() {
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validate_non_login() {
        validate_init_state()
        validate_select_operator_token()
        validate_select_product_token()
        validate_manual_input_token()
        select_operator_tagihan()
        validate_manual_input_tagihan()
    }

    fun validate_init_state() {
        onView(withId(R.id.operator_select)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_digital_product)).check(matches(isDisplayed()))
        onView(withText("Jenis Produk Listrik")).check(matches(isDisplayed()))
        onView(withText("No. Meter/ID Pel")).check(matches(isDisplayed()))
        onView(withText("Nominal")).check(matches(isDisplayed()))
    }

    fun validate_select_operator_token() {
        onView(withId(R.id.operator_select)).perform(click())
        onView(withText("Token Listrik")).check(matches(isDisplayed()))
        onView(withText("Tagihan Listrik")).check(matches(isDisplayed()))
        onView(withText("PLN Non-Taglis")).check(matches(isDisplayed()))

        Thread.sleep(1000)
        onView(withId(R.id.vg_input_dropdown_recycler_view)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsInputDropdownWidget.TopupBillsInputDropdownViewHolder>(
                        0, click()
                )
        )
        onView(withText("Token Listrik")).check(matches(isDisplayed()))
    }

    fun validate_manual_input_token() {
        onView(withId(R.id.rv_digital_product)).check(matches(isDisplayed()))
        onView(withId(R.id.recharge_general_enquiry_button)).check(matches(not(isEnabled())))


        onView(withId(R.id.rv_digital_product)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        0, ViewActions.typeText("")
                )
        ).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.rv_digital_product)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        0, ViewActions.typeText("0812")
                )
        ).perform(ViewActions.closeSoftKeyboard())
        validate_enquiry_button_enabled_after_delayed()

        onView(withId(R.id.rv_digital_product)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        0, ViewActions.typeText("3456")
                )
        ).perform(ViewActions.closeSoftKeyboard())
        validate_enquiry_button_enabled_after_delayed()

        onView(withId(R.id.rv_digital_product)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        0, ViewActions.typeText("7891")
                )
        ).perform(ViewActions.closeSoftKeyboard())
        validate_enquiry_button_enabled_after_delayed()
    }

    fun validate_select_product_token() {
        onView(withId(R.id.rv_digital_product)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectViewHolder>(
                        1, click()
                )
        )
        Thread.sleep(1000)
        onView(withId(R.id.rv_product_select_dropdown)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectBottomSheet.DigitalProductSelectDropdownAdapter.DigitalProductSelectDropdownViewHolder>(
                        1, click()
                )
        )
        Thread.sleep(1000)
        onView(withText("Rp 50.000")).check(matches(isDisplayed()))
    }

    fun validate_enquiry_button_enabled_after_delayed() {
        onView(withId(R.id.recharge_general_enquiry_button)).check(matches(not(isEnabled())))
        Thread.sleep(1000)
        onView(withId(R.id.recharge_general_enquiry_button)).check(matches(isEnabled()))
    }


    fun select_operator_tagihan() {
        onView(withId(R.id.operator_select)).perform(click())
        onView(withText("Token Listrik")).check(matches(isDisplayed()))
        onView(withText("Tagihan Listrik")).check(matches(isDisplayed()))
        onView(withText("PLN Non-Taglis")).check(matches(isDisplayed()))
        onView(withId(R.id.vg_input_dropdown_recycler_view)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsInputDropdownWidget.TopupBillsInputDropdownViewHolder>(
                        1, click()
                )
        )
        onView(withText("Tagihan Listrik")).check(matches(isDisplayed()))
    }

    fun validate_manual_input_tagihan() {
        onView(withId(R.id.rv_digital_product)).check(matches(isDisplayed()))
        onView(withId(R.id.recharge_general_enquiry_button)).check(matches(not(isEnabled())))

        onView(withId(R.id.rv_digital_product)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        0, ViewActions.typeText("1234567890")
                )
        ).perform(ViewActions.closeSoftKeyboard())
        validate_enquiry_button_enabled_after_delayed()
    }
}
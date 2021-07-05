package com.tokopedia.rechargegeneral.cases.pbb

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.rechargegeneral.R
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.adapter.TopupBillsRecentNumbersAdapter
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget
import com.tokopedia.rechargegeneral.RechargeGeneralMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralInputViewHolder
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralProductSelectViewHolder
import com.tokopedia.rechargegeneral.widget.RechargeGeneralProductSelectBottomSheet
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RechargeGeneralInstrumentTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var mActivityRule: IntentsTestRule<RechargeGeneralActivity> = object : IntentsTestRule<RechargeGeneralActivity>(RechargeGeneralActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(targetContext, RechargeGeneralActivity::class.java).apply {
                putExtra(RechargeGeneralActivity.PARAM_MENU_ID, 127)
                putExtra(RechargeGeneralActivity.PARAM_CATEGORY_ID, 22)
            }
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()

            setupGraphqlMockResponse(RechargeGeneralMockResponseConfig(RechargeGeneralProduct.PBB))
        }
    }

    @Before
    fun stubAllExternalIntents() {
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validate_non_login() {
        Thread.sleep(1000)
        validate_initial_state()
        validate_select_operator_cluster()
        validate_select_operator()
        validate_select_product()
        validate_manual_input()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY),
                hasAllSuccess())
    }

    fun validate_initial_state() {
        onView(withId(R.id.operator_cluster_select)).check(matches(isDisplayed()))
        onView(withText("Banten")).check(matches(isDisplayed()))
        onView(withId(R.id.operator_select)).check(matches(isDisplayed()))
        onView(withText("PBB Kota Cilegon")).check(matches(isDisplayed()))
        onView(withId(R.id.rv_digital_product)).check(matches(isDisplayed()))
        onView(withText("Bayar PBB Tahun")).check(matches(isDisplayed()))
        onView(withText("Nomor Objek Pajak")).check(matches(isDisplayed()))
    }

    fun validate_select_operator_cluster() {
        onView(withId(R.id.operator_cluster_select)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.vg_input_dropdown_recycler_view)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsInputDropdownWidget.TopupBillsInputDropdownViewHolder>(
                        1, click()
                )
        )
        Thread.sleep(1000)
        onView(withText("Daerah Istimewa Yogyakarta")).check(matches(isDisplayed()))
    }

    fun validate_select_operator() {
        onView(withId(R.id.operator_select)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.vg_input_dropdown_recycler_view)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsInputDropdownWidget.TopupBillsInputDropdownViewHolder>(
                        1, click()
                )
        )
        Thread.sleep(1000)
        onView(withText("PBB Kab. Bantul")).check(matches(isDisplayed()))
    }

    fun validate_select_product() {
        onView(withId(R.id.rv_digital_product)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectViewHolder>(
                        0, click()
                )
        )
        Thread.sleep(1000)
        onView(withId(R.id.rv_product_select_dropdown)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectBottomSheet.DigitalProductSelectDropdownAdapter.DigitalProductSelectDropdownViewHolder>(
                        0, click()
                )
        )
        Thread.sleep(1000)
        onView(withText("2016")).check(matches(isDisplayed()))
    }

    fun validate_manual_input() {
        onView(withId(R.id.rv_digital_product)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        1, typeText("123456789")
                )
        ).perform(closeSoftKeyboard())
        Thread.sleep(1000)
        onView(withText(R.string.input_error_message)).check(matches(isDisplayed()))
        onView(withId(R.id.recharge_general_enquiry_button)).check(matches(not(isEnabled())))

        onView(withId(R.id.rv_digital_product)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        1, typeText("012445000")
                )
        ).perform(closeSoftKeyboard())
        Thread.sleep(1000)
        onView(withId(R.id.recharge_general_enquiry_button)).check(matches(isEnabled()))
    }

    fun validate_recent_transaction() {
        onView(withId(R.id.recent_transaction_widget)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view_menu_component)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsRecentNumbersAdapter.RecentNumbersItemViewHolder>(
                        0, click()
                )
        )
        onView(withText("Banten")).check(matches(isDisplayed()))
        onView(withText("PBB Kab. Tangerang")).check(matches(isDisplayed()))
        onView(withText("2017")).check(matches(isDisplayed()))
        onView(withText("123456789012445000")).check(matches(isDisplayed()))
    }

    fun validate_promo() {
        onView(withId(R.id.promo_list_widget)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view_menu_component)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(
                        0, CommonActions.clickChildViewWithId(R.id.btn_copy_promo)
                )
        )
        Thread.sleep(1000)
    }

    fun validate_back_press() {
        onView(isRoot()).perform(pressBack())
    }

    companion object {
        private const val VALID_INPUT_NUMBER = "123456789012445111"
        private const val ANALYTIC_VALIDATOR_QUERY = "tracker/recharge/recharge_general_template_test.json"
    }
}
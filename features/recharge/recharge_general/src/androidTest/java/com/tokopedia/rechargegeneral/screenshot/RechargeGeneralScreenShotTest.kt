package com.tokopedia.rechargegeneral.screenshot

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.platform.util.TestOutputEmitter.takeScreenshot
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.RechargeGeneralMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralProductSelectViewHolder
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralFragment
import com.tokopedia.rechargegeneral.widget.RechargeGeneralProductSelectBottomSheet
import com.tokopedia.test.application.espresso_component.CommonActions.findViewAndScreenShot
import com.tokopedia.test.application.espresso_component.CommonActions.screenShotFullRecyclerView
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RechargeGeneralScreenShotTest {

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
            setupGraphqlMockResponse(RechargeGeneralMockResponseConfig(RechargeGeneralProduct.LISTRIK))
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
            Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        }
    }

    @Test
    fun screenshot() {
        // ss visible screen
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, filePrefix(), "visible_screen_pdp")
        }

        // ss full layout
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val test = mActivityRule.activity.findViewById<SwipeToRefresh>(R.id.recharge_general_swipe_refresh_layout)
            takeScreenShotVisibleViewInScreen(test, filePrefix(), "swipe_to_refresh")
        }

        // test
        takeScreenshot("test.png")

        // ss operator cluster select
        val operatorClusterView = mActivityRule.activity.findViewById<TopupBillsInputFieldWidget>(R.id.operator_cluster_select)
        takeScreenShotVisibleViewInScreen(operatorClusterView, filePrefix(), "operator_cluster_select")

        // ss operator select
        val operatorView = mActivityRule.activity.findViewById<TopupBillsInputFieldWidget>(R.id.operator_select)
        takeScreenShotVisibleViewInScreen(operatorView, filePrefix(), "operator_select")

        // ss recyclerview product
        screenShotFullRecyclerView(
                R.id.rv_digital_product,
                0,
                getRecyclerViewItemCount(R.id.rv_digital_product),
                "rv_digital_product")

        select_operator()
        select_product()
        see_promo()
    }

    private fun select_operator() {
        // Click "Jenis Produk Listrik"
        onView(withId(R.id.operator_select)).perform(ViewActions.click())

        takeScreenshot("operator_select")

        // Choose "Token Listrik"
        Thread.sleep(1000)
        onView(withId(R.id.vg_input_dropdown_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsInputDropdownWidget.TopupBillsInputDropdownViewHolder>(
                        0, ViewActions.click()
                )
        )
        takeScreenshot("operator_selected")
    }

    private fun select_product() {
        // Click "Nominal"
        onView(withId(R.id.rv_digital_product)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectViewHolder>(
                        1, ViewActions.click()
                )
        )
        takeScreenshot("product_select")
        Thread.sleep(1000)
        onView(withId(R.id.rv_product_select_dropdown)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectBottomSheet.DigitalProductSelectDropdownAdapter.DigitalProductSelectDropdownViewHolder>(
                        1, ViewActions.click()
                )
        )
        takeScreenshot("product_selected")
        Thread.sleep(1000)
    }

    private fun see_promo() {
        findViewAndScreenShot(R.id.product_view_pager, filePrefix(), "view_pager")
        findViewAndScreenShot(R.id.tab_layout, filePrefix(), "tab_layout")
    }

    private fun filePrefix() = "recharge_general"

    private fun getRecyclerViewItemCount(resId: Int): Int {
        val recyclerView = mActivityRule.activity.findViewById<RecyclerView>(resId)
        return recyclerView.adapter?.itemCount ?: 0
    }
}
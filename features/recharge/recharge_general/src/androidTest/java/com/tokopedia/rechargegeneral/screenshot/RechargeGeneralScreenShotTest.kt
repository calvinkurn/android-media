package com.tokopedia.rechargegeneral.screenshot

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.widget.FrameLayout
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
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.RechargeGeneralMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralInputViewHolder
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralProductSelectViewHolder
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralFragment
import com.tokopedia.rechargegeneral.widget.RechargeGeneralProductSelectBottomSheet
import com.tokopedia.test.application.espresso_component.CommonActions.findViewAndScreenShot
import com.tokopedia.test.application.espresso_component.CommonActions.screenShotFullRecyclerView
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RechargeGeneralScreenShotTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(RechargeGeneralActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        setupGraphqlMockResponse(RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.LISTRIK))

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, RechargeGeneralActivity::class.java).apply {
            putExtra(RechargeGeneralActivity.PARAM_MENU_ID, 113)
            putExtra(RechargeGeneralActivity.PARAM_CATEGORY_ID, 3)
        }
        mActivityRule.launchActivity(intent)
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun screenshot() {
        Thread.sleep(3000)

        // ss visible screen
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, filePrefix(), "visible_screen_pdp")
        }

        // ss full layout
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val test = mActivityRule.activity.findViewById<SwipeToRefresh>(R.id.recharge_general_swipe_refresh_layout)
            takeScreenShotVisibleViewInScreen(test, filePrefix(), "swipe_to_refresh")
        }

        // ss operator select
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val operatorView = mActivityRule.activity.findViewById<TopupBillsInputFieldWidget>(R.id.operator_select)
            takeScreenShotVisibleViewInScreen(operatorView, filePrefix(), "operator_select")
        }

        // ss recyclerview product
        screenShotFullRecyclerView(
                R.id.rv_digital_product,
                0,
                getRecyclerViewItemCount(R.id.rv_digital_product),
                "rv_digital_product")

        select_operator()
        select_phone_number()
        select_product()
        see_promo()
    }

    private fun select_operator() {
        // Click "Jenis Produk Listrik"
        onView(withId(R.id.operator_select)).perform(ViewActions.click())

        Thread.sleep(2000)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, filePrefix(), "recycler_view_operator_select")
        }

        // Choose "Token Listrik"
        Thread.sleep(2000)
        onView(withId(R.id.vg_input_dropdown_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsInputDropdownWidget.TopupBillsInputDropdownViewHolder>(
                        0, ViewActions.click()
                )
        )
    }

    private fun select_phone_number() {
        onView(withId(R.id.rv_digital_product)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        0, ViewActions.click()
                )
        )

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, filePrefix(), "favorite_number")
        }
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val frameLayout = mActivityRule.activity.findViewById<FrameLayout>(R.id.parent_view)
            takeScreenShotVisibleViewInScreen(frameLayout, filePrefix(), "favorite_number")
        }

        Thread.sleep(1000)
        onView(ViewMatchers.withText("08121111111"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun select_product() {
        // Click "Nominal"
        Thread.sleep(2000)
        onView(withId(R.id.rv_digital_product)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectViewHolder>(
                        1, ViewActions.click()
                )
        )
        Thread.sleep(2000)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, filePrefix(), "recycler_view_product_item")
        }

        onView(withId(R.id.rv_product_select_dropdown)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectBottomSheet.DigitalProductSelectDropdownAdapter.DigitalProductSelectDropdownViewHolder>(
                        1, ViewActions.click()
                )
        )

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, filePrefix(), "visible_screen_pdp_2")
        }
    }

    private fun see_promo() {
        findViewAndScreenShot(R.id.product_view_pager, filePrefix(), "view_pager")
        findViewAndScreenShot(R.id.tab_layout, filePrefix(), "view_pager")
    }

    private fun filePrefix() = "recharge_general"

    private fun getRecyclerViewItemCount(resId: Int): Int {
        val recyclerView = mActivityRule.activity.findViewById<RecyclerView>(resId)
        return recyclerView.adapter?.itemCount ?: 0
    }
}
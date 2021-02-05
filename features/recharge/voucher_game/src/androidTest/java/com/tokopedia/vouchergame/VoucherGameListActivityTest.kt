package com.tokopedia.vouchergame

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchergame.test.R
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import com.tokopedia.vouchergame.list.view.activity.VoucherGameListActivity
import com.tokopedia.vouchergame.list.view.adapter.viewholder.VoucherGameListViewHolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VoucherGameListActivityTest{

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var mActivityRule: IntentsTestRule<VoucherGameListActivity> = object : IntentsTestRule<VoucherGameListActivity>(VoucherGameListActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().toBlocking().first()
            setupGraphqlMockResponseWithCheck {
                addMockResponse(
                        KEY_QUERY_VOUCHER_DETAIL,
                        InstrumentationMockHelper.getRawString(targetContext,R.raw.mock_response_recharge_catalog_menu_detail),
                        MockModelConfig.FIND_BY_CONTAINS)
                addMockResponse(
                        KEY_QUERY_VOUCHER_LIST,
                        InstrumentationMockHelper.getRawString(targetContext, R.raw.mock_response_recharge_catalog_operator),
                        MockModelConfig.FIND_BY_CONTAINS)
            }
        }
        override fun getActivityIntent(): Intent {
            return VoucherGameListActivity.newInstance(targetContext, "6", "4")
        }
    }

    @Test
    fun validateToolbarMenu(){
        intending(isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Espresso.onView(withId(R.id.action_overflow_menu)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.menu_promo)).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.menu_help)).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.menu_order_list)).check(ViewAssertions.matches(isDisplayed()))
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.bottom_sheet_close)).perform(ViewActions.click())
    }

    @Test
    fun clearSearch(){
        intending(isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Espresso.onView(withId(R.id.searchbar_textfield)).perform(ViewActions.typeText(GAME_NAME), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.searchbar_icon)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.searchbar_textfield)).check(ViewAssertions.matches(ViewMatchers.withText("")))
    }

    @Test
    fun search_ItemIsAvailable(){
        intending(isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Thread.sleep(3000)
        Espresso.onView(withId(R.id.search_input_view)).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.searchbar_textfield)).check(ViewAssertions.matches(isDisplayed())).perform(ViewActions.click())
        Espresso.onView(withId(R.id.searchbar_textfield)).check(ViewAssertions.matches(isDisplayed())).perform(ViewActions.typeText(GAME_NAME))
        val itemCount = (mActivityRule.activity.findViewById(R.id.recycler_view) as RecyclerView).adapter?.itemCount ?: 0
        if (itemCount > 0) {
            Thread.sleep(3000)
            Espresso.onView(withId(R.id.recycler_view)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<VoucherGameListViewHolder>(0, ViewActions.click()))
        }
    }

    companion object {
        private const val GAME_NAME = "POKEMON GO"
        private const val KEY_QUERY_VOUCHER_LIST = "voucherGameProductList"
        private const val KEY_QUERY_VOUCHER_DETAIL = "catalogMenuDetail"
        private const val ANALYTICS = ""
    }
}


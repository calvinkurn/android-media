package com.tokopedia.rechargegeneral.cases.pbb

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.adapter.TopupBillsRecentNumbersAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralInputViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RechargeGeneralLoginInstrumentTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mActivityRule = ActivityTestRule(RechargeGeneralActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.PBB))

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, RechargeGeneralActivity::class.java).apply {
            putExtra(RechargeGeneralActivity.PARAM_MENU_ID, 127)
            putExtra(RechargeGeneralActivity.PARAM_CATEGORY_ID, 22)
        }
        mActivityRule.launchActivity(intent)
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun createOrderNumberTypeManual(): Instrumentation.ActivityResult {
        val orderClientNumber = TopupBillsFavNumberItem(clientNumber = VALID_INPUT_NUMBER)
        val resultData = Intent()
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber)
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE,
                TopupBillsSearchNumberFragment.InputNumberActionType.FAVORITE)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun stubSearchNumber() {
        Intents.intending(IntentMatchers.hasComponent(
                ComponentNameMatchers.hasShortClassName("com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity")))
                .respondWith(createOrderNumberTypeManual())
    }

    @Test
    fun validate_login() {
        stubSearchNumber()

        Thread.sleep(3000)
        validate_favorite_number()
        validate_recent_transaction()
        validate_promo()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_LOGIN),
                hasAllSuccess())
    }

    private fun validate_recent_transaction() {
        onView(withId(R.id.recent_transaction_widget)).check(matches(isDisplayed()))
        onView(AllOf.allOf(
                withId(R.id.recycler_view_menu_component),
                isDescendantOfA(withId(R.id.recent_transaction_widget))
        )).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsRecentNumbersAdapter.RecentNumbersItemViewHolder>(
                        0, click()
                )
        )
        Thread.sleep(1000)
        onView(withText("Banten")).check(matches(isDisplayed()))
        onView(withText("PBB Kab. Tangerang")).check(matches(isDisplayed()))
        onView(AllOf.allOf(withText("2017"), isDescendantOfA(withId(R.id.rv_digital_product)))).check(matches(isDisplayed()))
        onView(AllOf.allOf(withText(VALID_INPUT_NUMBER_2), isDescendantOfA(withId(R.id.rv_digital_product)))).check(matches(isDisplayed()))
        onView(withId(R.id.recharge_general_enquiry_button)).check(matches(isEnabled()))
        onView(withId(R.id.recent_transaction_widget)).check(matches(isDisplayed()))
    }

    private fun validate_favorite_number() {
        onView(withId(R.id.rv_digital_product)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        1, click()
                )
        )
        Thread.sleep(2000)
        onView(withId(R.id.recharge_general_enquiry_button)).check(matches(isEnabled()))
    }

    private fun validate_promo() {
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Promo"))).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.promo_list_widget)).check(matches(isDisplayed()))
        onView(AllOf.allOf(
                withId(R.id.recycler_view_menu_component),
                isDescendantOfA(withId(R.id.promo_list_widget))
        )).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(
                        0, CommonActions.clickChildViewWithId(R.id.btn_copy_promo)
                )
        )
        Thread.sleep(1000)
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val VALID_INPUT_NUMBER = "123456789012445111"
        private const val VALID_INPUT_NUMBER_2 = "123456789012445000"
        private const val ANALYTIC_VALIDATOR_QUERY_LOGIN = "tracker/recharge/recharge_general_template_test_login.json"
    }
}
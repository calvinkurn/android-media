package com.tokopedia.rechargegeneral.cases.listrik

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.RechargeGeneralMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.cases.pbb.RechargeGeneralLoginInstrumentTest
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralInputViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        setupGraphqlMockResponse(RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.LISTRIK))

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, RechargeGeneralActivity::class.java).apply {
            putExtra(RechargeGeneralActivity.PARAM_MENU_ID, 127)
            putExtra(RechargeGeneralActivity.PARAM_CATEGORY_ID, 22)
        }
        mActivityRule.launchActivity(intent)
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validate_login() {
        Thread.sleep(3000)

        stubSearchNumber()
        Thread.sleep(3000)
        validate_favorite_number()
        validate_promo()

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_LOGIN),
                hasAllSuccess())
    }

    private fun createOrderNumberTypeManual(): Instrumentation.ActivityResult {
        val orderClientNumber = TopupBillsFavNumberItem(clientNumber = "12345678910")
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

    private fun validate_favorite_number() {
        onView(withId(R.id.rv_digital_product)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        0, ViewActions.click()
                )
        )
        Thread.sleep(2000)
        onView(withId(R.id.recharge_general_enquiry_button)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }

    private fun validate_promo() {
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), ViewMatchers.withText("Promo"))).perform(ViewActions.click())
        Thread.sleep(1000)
        onView(withId(R.id.promo_list_widget)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(AllOf.allOf(
                withId(R.id.recycler_view_menu_component),
                ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.promo_list_widget))
        )).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(
                        0, CommonActions.clickChildViewWithId(R.id.btn_copy_promo)
                )
        )
        Thread.sleep(1000)
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_LOGIN = "tracker/recharge/recharge_general_template_test_login.json"
    }
}
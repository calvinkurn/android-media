package com.tokopedia.topupbills.prepaid

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsRecentNumbersAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.prepaid.activity.TelcoPrepaidActivity
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoPrepaidFragment
import com.tokopedia.topupbills.utils.ResourceUtils
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TelcoPrepaidLoginInstrumentTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule = ActivityTestRule(TelcoPrepaidActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse {
            addMockResponse(KEY_QUERY_MENU_DETAIL, ResourceUtils.getJsonFromResource(PATH_RESPONSE_PREPAID_MENU_DETAIL_LOGIN),
                    MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_FAV_NUMBER, ResourceUtils.getJsonFromResource(PATH_RESPONSE_PREPAID_FAV_NUMBER_LOGIN),
                    MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_PREFIX_SELECT, ResourceUtils.getJsonFromResource(PATH_RESPONSE_PREPAID_PREFIX_SELECT),
                    MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_PRODUCT_MULTI_TAB, ResourceUtils.getJsonFromResource(PATH_RESPONSE_PREPAID_PRODUCT_MULTITAB),
                    MockModelConfig.FIND_BY_CONTAINS)
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, TelcoPrepaidActivity::class.java).apply {
            putExtra(BaseTelcoActivity.PARAM_MENU_ID, TelcoComponentType.TELCO_PREPAID.toString())
            putExtra(BaseTelcoActivity.PARAM_CATEGORY_ID, TelcoCategoryType.CATEGORY_PULSA.toString())
            putExtra(BaseTelcoActivity.PARAM_PRODUCT_ID, "")
            putExtra(BaseTelcoActivity.PARAM_CLIENT_NUMBER, "")
        }
        mActivityRule.launchActivity(intent)

        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun createOrderNumberTypeManual(): Instrumentation.ActivityResult {
        val orderClientNumber = TopupBillsFavNumberItem(clientNumber = VALID_PHONE_NUMBER)
        val resultData = Intent()
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber)
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE,
                TopupBillsSearchNumberFragment.InputNumberActionType.MANUAL)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun stubSearchNumber() {
        Intents.intending(IntentMatchers.hasComponent(
                ComponentNameMatchers.hasShortClassName(".DigitalSearchNumberActivity")))
                .respondWith(createOrderNumberTypeManual())
    }

    @Test
    fun validate_prepaid_login() {
        stubSearchNumber()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        Thread.sleep(3000)

        validate_coachmark()
        click_on_fav_number_login()
        click_on_tab_menu_login()
        interaction_product_login()
        click_item_recent_widget_login()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_LOGIN),
                hasAllSuccess())
    }

    fun validate_coachmark() {
        Thread.sleep(4000)
        val localCacheHandler = LocalCacheHandler(context, DigitalTelcoPrepaidFragment.PREFERENCES_NAME)
        if (!localCacheHandler.getBoolean(DigitalTelcoPrepaidFragment.TELCO_COACH_MARK_HAS_SHOWN, false)) {
            onView(withText(R.string.Telco_title_showcase_client_number)).check(matches(isDisplayed()))
            onView(withId(R.id.text_next)).perform(click())
            onView(withText(R.string.telco_title_showcase_promo)).check(matches(isDisplayed()))
            onView(withId(R.id.text_previous)).perform(click())
            onView(withText(R.string.Telco_title_showcase_client_number)).check(matches(isDisplayed()))
            onView(withId(R.id.text_next)).perform(click())
            onView(withText(R.string.telco_title_showcase_promo)).check(matches(isDisplayed()))
            onView(withId(R.id.text_next)).perform(click())
        }
    }

    fun click_on_fav_number_login() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_ac_input_number)).perform(click())
        onView(withId(R.id.searchbar_icon)).perform(click())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
        onView(withId(R.id.searchbar_textfield)).perform(ViewActions.typeText(VALID_PHONE_NUMBER), ViewActions.pressImeActionButton())
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText(VALID_PHONE_NUMBER)))

        onView(withId(R.id.telco_ac_input_number)).perform(click())
        val viewInteraction = onView(withId(R.id.telco_search_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText(VALID_FAV_PHONE_NUMBER)))

        Thread.sleep(2000)
        onView(withId(R.id.telco_input_number)).perform(click())
        onView(withId(R.id.searchbar_icon)).perform(click())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
    }

    fun click_on_tab_menu_login() {
        onView(withId(R.id.telco_clear_input_number_btn)).perform(click())
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText("")))
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Recents"))).perform(click())
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Promo"))).perform(click())
    }

    fun interaction_product_login() {
        Thread.sleep(2000)
        choose_fav_number_from_list_fav_number()

        Thread.sleep(3000)
        onView(withId(R.id.telco_view_pager)).check(matches(isDisplayed()))

        // click on product item on pulsa
        Thread.sleep(2000)
        onView(withId(R.id.telco_product_view)).check(matches(isDisplayed()))
        val viewInteraction = onView(AllOf.allOf(isDisplayingAtLeast(30), withId(R.id.telco_product_rv))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1, click()))
        onView(withId(R.id.telco_buy_widget)).check(matches(isDisplayed()))

        //click tab roaming and swipe up
        Thread.sleep(2000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Roaming"))).perform(click())
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<TelcoProductViewHolder>(8))
        Thread.sleep(2000)
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<TelcoProductViewHolder>(1))

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        //click tab paket data, click lihat detail and close bottom sheet
        Thread.sleep(2000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Paket Data"))).perform(click())
        onView(withId(R.id.telco_buy_widget)).check(matches(IsNot.not(isDisplayed())))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1,
                CommonActions.clickChildViewWithId(R.id.telco_see_more_btn)))
        Thread.sleep(2000)
        onView(AllOf.allOf(withId(R.id.telco_button_select_item), isDisplayed())).check(matches(isDisplayed()))
        onView(withId(R.id.telco_button_select_item)).perform(click())
    }

    fun choose_fav_number_from_list_fav_number() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_input_number)).perform(click())
        onView(withId(R.id.searchbar_icon)).perform(click())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
        onView(withId(R.id.searchbar_textfield)).perform(ViewActions.typeText(VALID_PHONE_NUMBER), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText(VALID_PHONE_NUMBER)))
        val viewInteraction = onView(withId(R.id.telco_search_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText(VALID_PHONE_NUMBER)))
    }

    fun click_item_recent_widget_login() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        onView(withId(R.id.telco_clear_input_number_btn)).perform(click())
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText("")))

        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Recents"))).perform(click())
        val viewInteraction = onView(AllOf.allOf(isDescendantOfA(withId(R.id.layout_widget)),
                withId(R.id.recycler_view_menu_component), isDisplayed())).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions
                .actionOnItemAtPosition<TopupBillsRecentNumbersAdapter.RecentNumbersItemViewHolder>(0, click()))
        Thread.sleep(3000)
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        private const val KEY_QUERY_FAV_NUMBER = "favouriteNumber"
        private const val KEY_QUERY_PREFIX_SELECT = "telcoPrefixSelect"
        private const val KEY_QUERY_PRODUCT_MULTI_TAB = "telcoProductMultiTab"

        private const val PATH_RESPONSE_PREPAID_MENU_DETAIL_LOGIN = "prepaid/response_mock_data_prepaid_menu_detail_login.json"
        private const val PATH_RESPONSE_PREPAID_FAV_NUMBER_LOGIN = "prepaid/response_mock_data_prepaid_fav_number_login.json"
        private const val PATH_RESPONSE_PREPAID_PREFIX_SELECT = "prepaid/response_mock_data_prepaid_prefix_select.json"
        private const val PATH_RESPONSE_PREPAID_PRODUCT_MULTITAB = "prepaid/response_mock_data_prepaid_product_multitab.json"

        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_FAV_PHONE_NUMBER = "087855813789"
        private const val ANALYTIC_VALIDATOR_QUERY_LOGIN = "tracker/recharge/recharge_telco_prepaid_login.json"
    }
}
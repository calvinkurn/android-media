package com.tokopedia.topupbills.prepaid

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.view.adapter.TopupBillsRecentNumbersAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.R.id.*
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.prepaid.activity.TelcoPrepaidActivity
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoPrepaidFragment.Companion.PREFERENCES_NAME
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoPrepaidFragment.Companion.TELCO_COACH_MARK_HAS_SHOWN
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_clickClearBtn
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_typeNumber
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_validateText
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_validateBuyWidgetDisplayed
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_validateBuyWidgetNotDisplayed
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_validateProductViewDisplayed
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_validateViewPagerDisplayed
import com.tokopedia.topupbills.utils.CommonTelcoActions.productItemRv_scrollToPosition
import com.tokopedia.topupbills.utils.CommonTelcoActions.productItem_click
import com.tokopedia.topupbills.utils.CommonTelcoActions.productItem_clickSeeMore
import com.tokopedia.topupbills.utils.CommonTelcoActions.tabLayout_clickTabWithText
import com.tokopedia.topupbills.utils.CommonTelcoActions
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_clickFilterChip_withText
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
    val localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)

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

        localCacheHandler.apply {
            putBoolean(TELCO_COACH_MARK_HAS_SHOWN, true)
            applyEditor()
        }

        val intent = Intent(targetContext, TelcoPrepaidActivity::class.java).apply {
            putExtra(BaseTelcoActivity.PARAM_MENU_ID, TelcoComponentType.TELCO_PREPAID.toString())
            putExtra(BaseTelcoActivity.PARAM_CATEGORY_ID, TelcoCategoryType.CATEGORY_PULSA.toString())
            putExtra(BaseTelcoActivity.PARAM_PRODUCT_ID, "")
            putExtra(BaseTelcoActivity.PARAM_CLIENT_NUMBER, "")
        }
        mActivityRule.launchActivity(intent)

        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validate_prepaid_login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        Thread.sleep(3000)

        validate_coachmark()
        validate_pdp_client_number_widget_interaction()
        validate_filter_chip()
        validate_interaction_saved_number()
        validate_tab_menu_login()
        interaction_product_login()
        interaction_product_mccm()
        validate_recent_widget_login()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_LOGIN),
            hasAllSuccess())

    }

    fun validate_coachmark() {
        Thread.sleep(2000)
        if (!localCacheHandler.getBoolean(TELCO_COACH_MARK_HAS_SHOWN, false)) {
            Thread.sleep(1000)
            onView(withText(R.string.telco_title_showcase_client_number)).check(matches(isDisplayed()))
            onView(withId(text_next)).perform(click())

            Thread.sleep(1000)
            onView(withText(R.string.telco_title_showcase_promo)).check(matches(isDisplayed()))
            onView(withId(text_previous)).perform(click())

            Thread.sleep(1000)
            onView(withText(R.string.telco_title_showcase_client_number)).check(matches(isDisplayed()))
            onView(withId(text_next)).perform(click())

            Thread.sleep(1000)
            onView(withText(R.string.telco_title_showcase_promo)).check(matches(isDisplayed()))
            onView(withId(text_next)).perform(click())
        }
    }

    fun validate_pdp_client_number_widget_interaction() {
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_typeNumber(VALID_PHONE_NUMBER)
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER)

        clientNumberWidget_clickClearBtn()
        clientNumberWidget_typeNumber(VALID_PHONE_NUMBER)
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER)

        // validate autocomplete
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_typeNumber(PREFIX_PHONE_NUMBER)
        Thread.sleep(2000)
        onView(withText("Danur Kebab 2"))
            .inRoot(RootMatchers.isPlatformPopup())
            .check(matches(isDisplayed()))
            .perform(click())

        clientNumberWidget_clickClearBtn()
        clientNumberWidget_typeNumber(PREFIX_PHONE_NUMBER_2)
        Thread.sleep(2000)
        onView(withText("085612309812"))
            .inRoot(RootMatchers.isPlatformPopup())
            .check(matches(isDisplayed()))
            .perform(click())

        clientNumberWidget_clickClearBtn()
    }

    fun validate_filter_chip() {
        clientNumberWidget_clickFilterChip_withText("085612309812")
        clientNumberWidget_validateText("085612309812")

        onView(withId(telco_filter_chip)).perform(swipeLeft())
        Thread.sleep(1000)

        clientNumberWidget_clickFilterChip_withText("Tokopedia")
        clientNumberWidget_validateText("081232323239")
    }

    fun validate_interaction_saved_number() {
        CommonTelcoActions.stubAccessingSavedNumber(
            VALID_PHONE_NUMBER_2,
            TopupBillsSearchNumberFragment.InputNumberActionType.CONTACT,
            TelcoCategoryType.CATEGORY_PASCABAYAR.toString()
        )
        Thread.sleep(2000)
        CommonTelcoActions.clientNumberWidget_clickContactBook()
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER_2)

        CommonTelcoActions.stubAccessingSavedNumber(
            VALID_PHONE_NUMBER_3,
            TopupBillsSearchNumberFragment.InputNumberActionType.FAVORITE,
            TelcoCategoryType.CATEGORY_PASCABAYAR.toString()
        )
        Thread.sleep(2000)
        CommonTelcoActions.clientNumberWidget_clickContactBook()
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER_3)
    }

    fun validate_tab_menu_login() {
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_validateText(EMPTY_TEXT)

        Thread.sleep(3000)
        onView(AllOf.allOf(withId(tab_item_text_id), withText("Transaksi Terakhir"))).perform(click())
        Thread.sleep(2000)
        onView(AllOf.allOf(withId(tab_item_text_id), withText("Promo"))).perform(click())
    }

    fun interaction_product_login() {
        Thread.sleep(2000)
        clientNumberWidget_typeNumber(VALID_PHONE_NUMBER)

        Thread.sleep(3000)
        pdp_validateViewPagerDisplayed()

        // click on product item on pulsa
        Thread.sleep(2000)
        pdp_validateProductViewDisplayed()
        val viewInteraction = onView(AllOf.allOf(isDisplayingAtLeast(30), withId(telco_product_rv))).check(matches(isDisplayed()))

        productItem_click(viewInteraction, 1)
        Thread.sleep(2000)
        pdp_validateBuyWidgetDisplayed()

        //click tab roaming and swipe up
        Thread.sleep(2000)
        tabLayout_clickTabWithText("Roaming")
        productItemRv_scrollToPosition(viewInteraction, 8)
        Thread.sleep(2000)
        productItemRv_scrollToPosition(viewInteraction, 3)

        //click tab paket data, click lihat detail and close bottom sheet
        Thread.sleep(2000)
        tabLayout_clickTabWithText("Paket Data")
        pdp_validateBuyWidgetNotDisplayed()
        productItem_clickSeeMore(viewInteraction, 2)
        Thread.sleep(2000)
        onView(AllOf.allOf(withId(telco_button_select_item), isDisplayed())).check(matches(isDisplayed()))
        onView(withId(telco_button_select_item)).perform(click())

        productItemRv_scrollToPosition(viewInteraction, 0)
    }

    fun interaction_product_mccm() {
        // click tab paket data, click lihat detail and close bottom sheet
        val viewInteraction = onView(AllOf.allOf(isDisplayingAtLeast(30), withId(telco_mccm_rv))).check(matches(isDisplayed()))
        Thread.sleep(2000)

        // click item, should show buy widget
        productItem_click(viewInteraction, 0)
        Thread.sleep(2000)
        pdp_validateBuyWidgetDisplayed()
        onView(withId(txt_recharge_checkout_price)).check(matches(withText("Rp 15.500")))
        Thread.sleep(1000)

        // click see more button, should show see more item
        productItem_clickSeeMore(viewInteraction, 0)
        Thread.sleep(2000)
        onView(AllOf.allOf(withId(telco_button_select_item), isDisplayed())).check(matches(isDisplayed()))
        onView(withId(telco_button_select_item)).perform(click())
    }


    fun validate_recent_widget_login() {
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_validateText(EMPTY_TEXT)

        Thread.sleep(3000)
        tabLayout_clickTabWithText("Transaksi Terakhir")
        clickFirstRecentItem()
    }

    private fun clickFirstRecentItem() {
        val viewInteraction = onView(AllOf.allOf(isDescendantOfA(withId(layout_widget)),
            withId(recycler_view_menu_component), isDisplayed())).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions
            .actionOnItemAtPosition<TopupBillsRecentNumbersAdapter.RecentNumbersItemViewHolder>(0, click()))
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val EMPTY_TEXT = ""

        private const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        private const val KEY_QUERY_FAV_NUMBER = "rechargeFetchFavoriteNumber"
        private const val KEY_QUERY_PREFIX_SELECT = "telcoPrefixSelect"
        private const val KEY_QUERY_PRODUCT_MULTI_TAB = "telcoProductMultiTab"

        private const val PATH_RESPONSE_PREPAID_MENU_DETAIL_LOGIN = "prepaid/response_mock_data_prepaid_menu_detail_login.json"
        private const val PATH_RESPONSE_PREPAID_FAV_NUMBER_LOGIN = "prepaid/response_mock_data_prepaid_fav_number_login.json"
        private const val PATH_RESPONSE_PREPAID_PREFIX_SELECT = "prepaid/response_mock_data_prepaid_prefix_select.json"
        private const val PATH_RESPONSE_PREPAID_PRODUCT_MULTITAB = "prepaid/response_mock_data_prepaid_product_multitab_with_mccm.json"

        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_NUMBER_2 = "085252525252"
        private const val VALID_PHONE_NUMBER_3 = "081234567890"
        private const val PREFIX_PHONE_NUMBER = "0812"
        private const val PREFIX_PHONE_NUMBER_2 = "0856"
        private const val ANALYTIC_VALIDATOR_QUERY_LOGIN = "tracker/recharge/recharge_telco_prepaid_login.json"
    }
}
package com.tokopedia.topupbills.screenshot.prepaid.nonlogin

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.widget.RelativeLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.prepaid.activity.TelcoPrepaidActivity
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoPrepaidFragment
import com.tokopedia.topupbills.utils.CommonTelcoActions
import com.tokopedia.topupbills.utils.ResourceUtils
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

abstract class BaseTelcoPrepaidScreenShotTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule = ActivityTestRule<TelcoPrepaidActivity>(TelcoPrepaidActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupDarkModeTest(forceDarkMode())
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
        disableCoachMark()
        stubNonInternalIntent()
        mActivityRule.launchActivity(intent)
    }

    @Test
    fun screenshot() {
        take_screenshot_visible_screen()
        take_screenshot_interaction_menu()
        take_screenshot_input_number()
        take_screenshot_interaction_product_not_login()
        take_screenshot_interaction_promo()
    }

    fun take_screenshot_visible_screen() {
        // ss visible screen
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, generatePrefix(), "visible_screen_pdp")
        }

        // ss full layout
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val test = mActivityRule.activity.findViewById<RelativeLayout>(R.id.telco_page_container)
            CommonActions.takeScreenShotVisibleViewInScreen(test, generatePrefix(), "full_layout")
        }
    }

    fun take_screenshot_input_number() {
        CommonActions.findViewAndScreenShot(R.id.telco_input_number, generatePrefix(), "input_number_widget")
        CommonTelcoActions.clientNumberWidget_typeNumber(VALID_PHONE_NUMBER)
        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(R.id.telco_input_number, generatePrefix(), "input_number_widget_filled")
    }

    fun take_screenshot_interaction_product_not_login() {
        Thread.sleep(2000)

        // Pulsa
        CommonActions.findViewAndScreenShot(R.id.telco_product_rv, generatePrefix(),
            "product_view_pulsa")
        CommonActions.findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(),
            "product_item_pulsa")

        val viewInteraction = onView(AllOf.allOf(ViewMatchers.isDisplayingAtLeast(30),
            withId(R.id.telco_product_rv))).check(ViewAssertions.matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1, click()))

        CommonActions.findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(),
            "product_item_pulsa_clicked"
        )
        CommonActions.findViewAndScreenShot(R.id.telco_buy_widget, generatePrefix(), "buy_widget")

        // Roaming
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), ViewMatchers.withText("Roaming"))).perform(click())

        CommonActions.findViewAndScreenShot(R.id.telco_product_rv, generatePrefix(),
            "product_view_roaming")
        CommonActions.findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(),
            "product_item_roaming")

        viewInteraction.perform(RecyclerViewActions.scrollToPosition<TelcoProductViewHolder>(8))
        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(R.id.telco_product_rv, generatePrefix(),
            "product_view_roaming_scrolled")

        viewInteraction.perform(RecyclerViewActions.scrollToPosition<TelcoProductViewHolder>(1))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1, click()))

        CommonActions.findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(),
            "product_item_roaming_clicked")

        // Paket Data
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), ViewMatchers.withText("Paket Data"))).perform(click())

        CommonActions.findViewAndScreenShot(R.id.telco_product_rv, generatePrefix(),
            "product_view_paket_data")
        CommonActions.findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(),
            "product_item_paket_data")

        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1,
            CommonActions.clickChildViewWithId(R.id.telco_see_more_btn)))
        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(R.id.container_bottom_sheet_product_see_more, generatePrefix(),
            "bottom_sheet_paket_data_see_more")

        onView(withId(R.id.telco_button_select_item)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.bottom_sheet_close)).perform(click())
    }

    fun take_screenshot_interaction_promo() {
        onView(withId(R.id.text_field_icon_close)).perform(click())
        Thread.sleep(2000)
        val viewInteraction = onView(AllOf.allOf(
            CoreMatchers.allOf(
                withId(R.id.recycler_view_menu_component), withParent(withId(R.id.layout_widget)),
                isDisplayed()
            )
        )).check(ViewAssertions.matches(isDisplayed()))

        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
            R.id.layout_widget,
            generatePrefix(),
            "promo"
        )

        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(0,
            CommonActions.clickChildViewWithId(R.id.btn_copy_promo)))

        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
            R.id.layout_widget,
            generatePrefix(),
            "promo_chosen"
        )
    }

    fun take_screenshot_interaction_menu() {
        Thread.sleep(2000)

        onView(withId(R.id.action_overflow_menu)).perform(click())
        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
            R.id.container_menu,
            generatePrefix(),
            "interaction_menu"
        )
        Thread.sleep(2000)
        onView(withId(R.id.bottom_sheet_close)).perform(click())
    }

    private fun stubNonInternalIntent() {
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, null
            )
        )
    }

    private fun disableCoachMark() {
        LocalCacheHandler(context, DigitalTelcoPrepaidFragment.PREFERENCES_NAME).also {
            it.putBoolean(DigitalTelcoPrepaidFragment.TELCO_COACH_MARK_HAS_SHOWN, true)
            it.applyEditor()
        }
    }

    abstract fun forceDarkMode(): Boolean

    abstract fun filePrefix(): String

    protected fun generatePrefix(): String {
        val prefix = StringBuilder()
        prefix.append(filePrefix())
        prefix.append( when (forceDarkMode()) {
            true -> "-dark"
            false -> "-light"
        })
        return prefix.toString()
    }

    companion object {
        private const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        private const val KEY_QUERY_FAV_NUMBER = "rechargeFetchFavoriteNumber"
        private const val KEY_QUERY_PREFIX_SELECT = "telcoPrefixSelect"
        private const val KEY_QUERY_PRODUCT_MULTI_TAB = "telcoProductMultiTab"

        private const val PATH_RESPONSE_PREPAID_MENU_DETAIL_LOGIN = "prepaid/response_mock_data_prepaid_menu_detail.json"
        private const val PATH_RESPONSE_PREPAID_FAV_NUMBER_LOGIN = "response_mock_data_telco_fav_number.json"
        private const val PATH_RESPONSE_PREPAID_PREFIX_SELECT = "prepaid/response_mock_data_prepaid_prefix_select.json"
        private const val PATH_RESPONSE_PREPAID_PRODUCT_MULTITAB = "prepaid/response_mock_data_prepaid_product_multitab.json"

        private const val VALID_PHONE_NUMBER = "08123232323"
    }
}
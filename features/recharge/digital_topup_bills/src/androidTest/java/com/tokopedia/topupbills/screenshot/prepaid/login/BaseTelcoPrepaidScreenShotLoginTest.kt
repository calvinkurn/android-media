package com.tokopedia.topupbills.screenshot.prepaid.login

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonActions.findViewAndScreenShot
import com.tokopedia.test.application.espresso_component.CommonActions.findViewHolderAndScreenshot
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.prepaid.activity.TelcoPrepaidActivity
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoPrepaidFragment
import com.tokopedia.topupbills.utils.CommonTelcoActions.stubSearchNumber
import com.tokopedia.topupbills.utils.ResourceUtils
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

abstract class BaseTelcoPrepaidScreenShotLoginTest {
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

        val intent = Intent(context, TelcoPrepaidActivity::class.java).apply {
            putExtra(BaseTelcoActivity.PARAM_MENU_ID, TelcoComponentType.TELCO_PREPAID.toString())
            putExtra(BaseTelcoActivity.PARAM_CATEGORY_ID, TelcoCategoryType.CATEGORY_PULSA.toString())
            putExtra(BaseTelcoActivity.PARAM_PRODUCT_ID, "")
            putExtra(BaseTelcoActivity.PARAM_CLIENT_NUMBER, "")
        }
        mActivityRule.launchActivity(intent)
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, null))
    }

    @Test
    fun screenshot() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        stubSearchNumber(
            VALID_PHONE_NUMBER,
            TopupBillsSearchNumberFragment.InputNumberActionType.MANUAL
        )
        isCoachmarkDisabled(context, true)
        Thread.sleep(3000)

        take_screenshot_visible_screen()
        take_screenshot_prabayar_category()
        take_screenshot_interaction_product_login()
        take_screenshot_interaction_interactive_header()
        take_screenshot_recent_widget_login()
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

        // ss input number component
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val test = mActivityRule.activity.findViewById<ConstraintLayout>(R.id.telco_input_number_layout)
            CommonActions.takeScreenShotVisibleViewInScreen(test, generatePrefix(), "input_number_widget")
        }
    }

    fun take_screenshot_coachmark_and_its_placement() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, generatePrefix(), "visible_screen_pdp")
        }
        onView(withText("Lanjut"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click());
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, generatePrefix(), "visible_screen_pdp")
        }
        onView(withText("Mengerti"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click());
    }

    fun take_screenshot_prabayar_category() {
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Pulsa"))).perform(click())
        Thread.sleep(1000)
        findViewAndScreenShot(R.id.telco_tab_layout, generatePrefix(), "tab_layout_pulsa")
        Thread.sleep(1000)
        findViewAndScreenShot(R.id.telco_product_view, generatePrefix(), "prabayar_pulsa")

        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Paket Data"))).perform(click())
        Thread.sleep(1000)
        findViewAndScreenShot(R.id.telco_tab_layout, generatePrefix(), "tab_layout_paket_data")
        Thread.sleep(2000)
        findViewAndScreenShot(R.id.telco_product_view, generatePrefix(), "prabayar_paket_data")

        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Roaming"))).perform(click())
        Thread.sleep(1000)
        findViewAndScreenShot(R.id.telco_tab_layout, generatePrefix(), "tab_layout_roaming")
        Thread.sleep(2000)
        findViewAndScreenShot(R.id.telco_product_view, generatePrefix(), "prabayar_roaming")
    }

    fun take_screenshot_interaction_interactive_header() {
        val baseTelcoActivity = mActivityRule.activity as BaseTelcoActivity
        val bannerImage = mActivityRule.activity.findViewById<ImageView>(R.id.telco_bg_img_banner)

        val fadeOut = AlphaAnimation(1.0f, 0f)
        fadeOut.duration = 300
        fadeOut.fillAfter = true

        Thread.sleep(2000)
        val recyclerView = mActivityRule.activity.findViewById<RecyclerView>(R.id.telco_product_rv)
        scrollRecyclerViewToPosition(recyclerView, 10)

        mActivityRule.runOnUiThread {
            baseTelcoActivity.onCollapseAppBar()
            bannerImage.clearAnimation()
            bannerImage.startAnimation(fadeOut)
        }

        Thread.sleep(2000)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val test = mActivityRule.activity.findViewById<RelativeLayout>(R.id.telco_page_container)
            CommonActions.takeScreenShotVisibleViewInScreen(test, generatePrefix(), "full_layout_header_disappear")
        }
    }

    fun take_screenshot_interaction_product_login() {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).perform(click())
        Thread.sleep(3000)

        // Pulsa
        findViewAndScreenShot(R.id.telco_product_rv, generatePrefix(), "product_view_pulsa")
        findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(), "product_item_pulsa")

        val viewInteraction = onView(AllOf.allOf(ViewMatchers.isDisplayingAtLeast(30), withId(R.id.telco_product_rv))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1, click()))

        findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(), "product_item_pulsa_clicked")
        findViewAndScreenShot(R.id.telco_buy_widget, generatePrefix(), "buy_widget")

        // Roaming
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Roaming"))).perform(click())

        findViewAndScreenShot(R.id.telco_product_rv, generatePrefix(), "product_view_roaming")
        findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(), "product_item_roaming")

        viewInteraction.perform(RecyclerViewActions.scrollToPosition<TelcoProductViewHolder>(8))
        Thread.sleep(2000)
        findViewAndScreenShot(R.id.telco_product_rv, generatePrefix(), "product_view_roaming_scrolled")

        viewInteraction.perform(RecyclerViewActions.scrollToPosition<TelcoProductViewHolder>(1))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1, click()))

        findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(), "product_item_roaming_clicked")

        // Paket Data
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Paket Data"))).perform(click())

        findViewAndScreenShot(R.id.telco_product_rv, generatePrefix(), "product_view_paket_data")
        findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(), "product_item_paket_data")

        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1,
            CommonActions.clickChildViewWithId(R.id.telco_see_more_btn)))
        Thread.sleep(2000)
        findViewAndScreenShot(R.id.container_bottom_sheet_product_see_more, generatePrefix(), "bottom_sheet_paket_data_see_more")

        onView(withId(R.id.telco_button_select_item)).perform(click())
        Thread.sleep(2000)

        findViewHolderAndScreenshot(R.id.telco_product_rv, 1, generatePrefix(), "product_item_paket_data_clicked")
    }

    fun take_screenshot_recent_widget_login() {
        onView(withId(R.id.telco_clear_input_number_btn)).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).check(matches(withText("")))

        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Transaksi Terakhir"))).perform(click())
        findViewAndScreenShot(R.id.layout_widget, generatePrefix(), "transaksi_terakhir_view")

        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Promo"))).perform(click())
        findViewAndScreenShot(R.id.layout_widget, generatePrefix(), "promo_view")
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        mActivityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun isCoachmarkDisabled(context: Context, isDisabled: Boolean) {
        LocalCacheHandler(context, DigitalTelcoPrepaidFragment.PREFERENCES_NAME).also {
            it.putBoolean(DigitalTelcoPrepaidFragment.TELCO_COACH_MARK_HAS_SHOWN, isDisabled)
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

        private const val PATH_RESPONSE_PREPAID_MENU_DETAIL_LOGIN = "prepaid/response_mock_data_prepaid_menu_detail_login.json"
        private const val PATH_RESPONSE_PREPAID_FAV_NUMBER_LOGIN = "prepaid/response_mock_data_prepaid_fav_number_login.json"
        private const val PATH_RESPONSE_PREPAID_PREFIX_SELECT = "prepaid/response_mock_data_prepaid_prefix_select.json"
        private const val PATH_RESPONSE_PREPAID_PRODUCT_MULTITAB = "prepaid/response_mock_data_prepaid_product_multitab.json"

        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_FAV_PHONE_NUMBER = "087855813789"
    }
}
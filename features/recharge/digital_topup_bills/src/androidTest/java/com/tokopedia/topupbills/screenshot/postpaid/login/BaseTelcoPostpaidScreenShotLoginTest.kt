package com.tokopedia.topupbills.screenshot.postpaid.login

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonActions.findViewAndScreenShot
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.postpaid.activity.TelcoPostpaidActivity
import com.tokopedia.topupbills.utils.ResourceUtils
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.StringBuilder

abstract class BaseTelcoPostpaidScreenShotLoginTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule = ActivityTestRule(TelcoPostpaidActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        setupDarkModeTest(forceDarkMode())
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse {
            addMockResponse(KEY_QUERY_MENU_DETAIL, ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_MENU_DETAIL_LOGIN),
                    MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_FAV_NUMBER, ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_FAV_NUMBER),
                    MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_PREFIX_SELECT, ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_PREFIX_SELECT),
                    MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_ENQUIRY, ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_ENQUIRY),
                    MockModelConfig.FIND_BY_CONTAINS)
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, TelcoPostpaidActivity::class.java).apply {
            putExtra(BaseTelcoActivity.PARAM_MENU_ID, TelcoComponentType.TELCO_POSTPAID.toString())
            putExtra(BaseTelcoActivity.PARAM_CATEGORY_ID, TelcoCategoryType.CATEGORY_PASCABAYAR.toString())
            putExtra(BaseTelcoActivity.PARAM_PRODUCT_ID, "")
            putExtra(BaseTelcoActivity.PARAM_CLIENT_NUMBER, "")
        }
        mActivityRule.launchActivity(intent)
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    @Test
    fun screenshot() {
        stubSearchNumber()
        take_screenshot_visible_screen()
        take_screenshot_interaction_menu()
        take_screenshot_input_number()
        take_screenshot_enquiry_phone_number()
        take_screenshot_interaction_view_pager()
        take_screenshot_interaction_interactive_header()
    }

    fun take_screenshot_visible_screen() {
        Thread.sleep(5000)

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

    fun take_screenshot_interaction_menu() {
        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.action_overflow_menu)).perform(ViewActions.click())
        Thread.sleep(2000)
        findViewAndScreenShot(
                R.id.container_menu,
                generatePrefix(),
                "interaction_menu"
        )
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.bottom_sheet_close)).perform(ViewActions.click())
    }

    fun take_screenshot_input_number() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.telco_ac_input_number)).perform(ViewActions.click())
        Thread.sleep(2000)
        findViewAndScreenShot(
                R.id.container_search_number_telco,
                generatePrefix(),
                "search_number"
        )

        Espresso.onView(ViewMatchers.withId(R.id.searchbar_textfield)).perform(ViewActions.typeText(VALID_PHONE_NUMBER))
        Espresso.closeSoftKeyboard()
        Thread.sleep(2000)
        findViewAndScreenShot(
                R.id.container_search_number_telco,
                generatePrefix(),
                "search_number_filled"
        )
        Espresso.onView(ViewMatchers.withId(R.id.searchbar_textfield)).perform(ViewActions.click(), ViewActions.pressImeActionButton())
        Thread.sleep(2000)
        findViewAndScreenShot(
                R.id.telco_input_number_layout,
                generatePrefix(),
                "input_number_widget_filled"
        )
    }

    fun take_screenshot_enquiry_phone_number() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.telco_buy_widget)).check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.telco_enquiry_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val test = mActivityRule.activity.findViewById<RelativeLayout>(R.id.telco_page_container)
            CommonActions.takeScreenShotVisibleViewInScreen(test, generatePrefix(), "full_layout_enquiry")
        }

        Espresso.onView(ViewMatchers.withId(R.id.telco_clear_input_number_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
    }

    fun take_screenshot_interaction_view_pager() {
        findViewAndScreenShot(R.id.telco_tab_layout, generatePrefix(), "tab_layout")

        Thread.sleep(2000)
        findViewAndScreenShot(
                R.id.layout_widget,
                generatePrefix(),
                "pager_recent"
        )

        Espresso.onView(withText("Promo")).perform(ViewActions.click())

        Thread.sleep(2000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                CoreMatchers.allOf(ViewMatchers.withId(R.id.recycler_view_menu_component), ViewMatchers.withParent(ViewMatchers.withId(R.id.layout_widget)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)
        findViewAndScreenShot(
                R.id.layout_widget,
                generatePrefix(),
                "pager_promo"
        )

        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(0,
                CommonActions.clickChildViewWithId(R.id.btn_copy_promo)))

        Thread.sleep(2000)
        findViewAndScreenShot(
                R.id.layout_widget,
                generatePrefix(),
                "pager_promo_chosen"
        )
    }

    fun take_screenshot_interaction_interactive_header() {
        val baseTelcoActivity = mActivityRule.activity as BaseTelcoActivity
        val bannerImage = mActivityRule.activity.findViewById<ImageView>(R.id.telco_bg_img_banner)

        val fadeOut = AlphaAnimation(1.0f, 0f)
        fadeOut.duration = 300
        fadeOut.fillAfter = true

        Thread.sleep(2000)
        val recyclerView = mActivityRule.activity.findViewById<RecyclerView>(R.id.recycler_view_menu_component)
        scrollRecyclerViewToPosition(recyclerView, 5)

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

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        mActivityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
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
        private const val KEY_QUERY_FAV_NUMBER = "favouriteNumber"
        private const val KEY_QUERY_PREFIX_SELECT = "telcoPrefixSelect"
        private const val KEY_QUERY_ENQUIRY = "enquiry"

        private const val PATH_RESPONSE_POSTPAID_MENU_DETAIL_LOGIN = "postpaid/response_mock_data_postpaid_menu_detail_login.json"
        private const val PATH_RESPONSE_POSTPAID_FAV_NUMBER = "postpaid/response_mock_data_postpaid_fav_number_login.json"
        private const val PATH_RESPONSE_POSTPAID_PREFIX_SELECT = "postpaid/response_mock_data_postpaid_prefix_select.json"
        private const val PATH_RESPONSE_POSTPAID_ENQUIRY = "postpaid/response_mock_data_postpaid_enquiry.json"

        private const val VALID_PHONE_NUMBER = "08123232323"
    }
}
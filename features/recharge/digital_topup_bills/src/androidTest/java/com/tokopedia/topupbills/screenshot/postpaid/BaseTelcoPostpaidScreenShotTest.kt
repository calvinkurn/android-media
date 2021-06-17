package com.tokopedia.topupbills.screenshot.postpaid

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.ContactsContract
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
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
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topupbills.TelcoContactHelper
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.postpaid.activity.TelcoPostpaidActivity
import com.tokopedia.topupbills.utils.ResourceUtils
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.topupbills.R
import org.hamcrest.CoreMatchers.allOf

class BaseTelcoPostpaidScreenShotTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule = ActivityTestRule(TelcoPostpaidActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse {
            addMockResponse(KEY_QUERY_MENU_DETAIL, ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_MENU_DETAIL),
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
        take_screenshot_interaction_promo()
    }

    fun take_screenshot_visible_screen() {
        Thread.sleep(5000)

        // ss visible screen
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, filePrefix(), "visible_screen_pdp")
        }

        // ss full layout
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val test = mActivityRule.activity.findViewById<RelativeLayout>(R.id.telco_page_container)
            CommonActions.takeScreenShotVisibleViewInScreen(test, filePrefix(), "full_layout")
        }

        // ss input number component
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val test = mActivityRule.activity.findViewById<ConstraintLayout>(R.id.telco_input_number_layout)
            CommonActions.takeScreenShotVisibleViewInScreen(test, filePrefix(), "input_number_widget")
        }
    }

    fun take_screenshot_interaction_menu() {
        Thread.sleep(5000)

        onView(withId(R.id.action_overflow_menu)).perform(click())
        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
                R.id.container_menu,
                filePrefix(),
                "interaction_menu"
        )
        Thread.sleep(2000)
        onView(withId(R.id.bottom_sheet_close)).perform(click())
    }

    fun take_screenshot_input_number() {
        Thread.sleep(5000)
        onView(withId(R.id.telco_ac_input_number)).perform(click())
        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
                R.id.container_search_number_telco,
                filePrefix(),
                "search_number"
        )

        onView(withId(R.id.searchbar_textfield)).perform(typeText(VALID_PHONE_NUMBER), pressImeActionButton())
        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
                R.id.container_search_number_telco,
                filePrefix(),
                "search_number_filled"
        )
        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
                R.id.telco_input_number_layout,
                filePrefix(),
                "input_number_widget_filled"
        )
    }

    fun take_screenshot_interaction_promo() {
        Thread.sleep(5000)
        val viewInteraction = onView(AllOf.allOf(
                allOf(withId(R.id.recycler_view_menu_component), withParent(withId(R.id.layout_widget)),
                        isDisplayed()))).check(matches(isDisplayed()))

        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
                R.id.layout_widget,
                filePrefix(),
                "promo"
        )

        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(0,
                CommonActions.clickChildViewWithId(R.id.btn_copy_promo)))

        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
                R.id.layout_widget,
                filePrefix(),
                "promo_chosen"
        )
    }

    fun filePrefix(): String {
        return "telco-postpaid-nonlogin-light"
    }

    companion object {
        private const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        private const val KEY_QUERY_FAV_NUMBER = "favouriteNumber"
        private const val KEY_QUERY_PREFIX_SELECT = "telcoPrefixSelect"
        private const val KEY_QUERY_ENQUIRY = "enquiry"

        private const val PATH_RESPONSE_POSTPAID_MENU_DETAIL = "postpaid/response_mock_data_postpaid_menu_detail.json"
        private const val PATH_RESPONSE_POSTPAID_FAV_NUMBER = "response_mock_data_telco_fav_number.json"
        private const val PATH_RESPONSE_POSTPAID_PREFIX_SELECT = "postpaid/response_mock_data_postpaid_prefix_select.json"
        private const val PATH_RESPONSE_POSTPAID_ENQUIRY = "postpaid/response_mock_data_postpaid_enquiry.json"

        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_BOOK = "087821212121"
        private const val VALID_PHONE_BOOK_RAW = "0878-2121-2121"
        private const val ANALYTIC_VALIDATOR_QUERY_NON_LOGIN = "tracker/recharge/recharge_telco_postpaid.json"
    }
}
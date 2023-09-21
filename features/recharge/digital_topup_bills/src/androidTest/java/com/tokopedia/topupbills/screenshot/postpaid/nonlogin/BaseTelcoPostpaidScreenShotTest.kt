package com.tokopedia.topupbills.screenshot.postpaid.nonlogin

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
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
import com.tokopedia.topupbills.telco.postpaid.activity.TelcoPostpaidActivity
import com.tokopedia.topupbills.utils.ResourceUtils
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_typeNumber
import java.lang.StringBuilder

abstract class BaseTelcoPostpaidScreenShotTest {

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

    @After
    fun cleanUp() {
        Intents.release()
    }

    @Test
    fun screenshot() {
        clientNumberWidget_typeNumber(VALID_PHONE_NUMBER)
        take_screenshot_visible_screen()
        take_screenshot_interaction_menu()
        take_screenshot_interaction_promo()
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

        onView(withId(R.id.action_overflow_menu)).perform(click())
        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
            com.tokopedia.common.topupbills.R.id.container_menu,
                generatePrefix(),
                "interaction_menu"
        )
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)).perform(click())
    }

    fun take_screenshot_interaction_promo() {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).perform(click())
        Thread.sleep(2000)
        val viewInteraction = onView(AllOf.allOf(
                allOf(withId(com.tokopedia.common.topupbills.R.id.recycler_view_menu_component), withParent(withId(com.tokopedia.common.topupbills.R.id.layout_widget)),
                        isDisplayed()))).check(matches(isDisplayed()))

        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
            com.tokopedia.common.topupbills.R.id.layout_widget,
                generatePrefix(),
                "promo"
        )

        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(0,
                CommonActions.clickChildViewWithId(com.tokopedia.common.topupbills.R.id.btn_copy_promo)))

        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
            com.tokopedia.common.topupbills.R.id.layout_widget,
                generatePrefix(),
                "promo_chosen"
        )
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
        private const val KEY_QUERY_ENQUIRY = "enquiry"

        private const val PATH_RESPONSE_POSTPAID_MENU_DETAIL = "postpaid/response_mock_data_postpaid_menu_detail.json"
        private const val PATH_RESPONSE_POSTPAID_FAV_NUMBER = "response_mock_data_telco_fav_number.json"
        private const val PATH_RESPONSE_POSTPAID_PREFIX_SELECT = "postpaid/response_mock_data_postpaid_prefix_select.json"
        private const val PATH_RESPONSE_POSTPAID_ENQUIRY = "postpaid/response_mock_data_postpaid_enquiry.json"

        private const val VALID_PHONE_NUMBER = "08123232323"
    }
}

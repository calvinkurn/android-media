package com.tokopedia.common.topupbills

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.prefix_select.TelcoAttributesOperator
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.common.topupbills.util.TopupBillsFavoriteNumberMockResponseConfig
import com.tokopedia.common.topupbills.view.activity.TopupBillsFavoriteNumberActivity
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberViewHolder
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.view.fragment.TopupBillsFavoriteNumberFragment.Companion.CACHE_PREFERENCES_NAME
import com.tokopedia.common.topupbills.view.fragment.TopupBillsFavoriteNumberFragment.Companion.CACHE_SHOW_COACH_MARK_KEY
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.MatcherAssert
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class TopupBillsFavoriteNumberActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    var intent: Intent? = null

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var mActivityRule = ActivityTestRule(TopupBillsFavoriteNumberActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        val extras = Bundle()
        extras.putString(EXTRA_CLIENT_NUMBER_TYPE, ClientNumberType.TYPE_INPUT_TEL)
        extras.putString(EXTRA_CLIENT_NUMBER, CLIENT_NUMBER)
        extras.putStringArrayList(EXTRA_DG_CATEGORY_IDS, DG_CATEGORY_IDS)
        extras.putString(EXTRA_DG_CATEGORY_NAME, CATEGORY_NAME)
        extras.putParcelable(EXTRA_CATALOG_PREFIX_SELECT, operatorData)

        intent = RouteManager.getIntent(targetContext, APPLINK)
        intent?.putExtras(extras)

        stubContactNumber()
    }

    @After
    fun cleanUp() {
        Intents.release()
        intent = null
    }

    private fun stubContactNumber() {
        Intents.intending(AllOf.allOf(IntentMatchers.hasData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
            IntentMatchers.hasAction(Intent.ACTION_PICK))
        ).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validate_favorite_number_page_happy_flow() {
        setupGraphqlMockResponse(TopupBillsFavoriteNumberMockResponseConfig(isHappyTest = true, true))
        mActivityRule.launchActivity(intent)

        Thread.sleep(3000)
        validate_show_contents_favorite_number_page()
        validate_coachmark_favorite_number()
        validate_pick_number_from_contact_book()
        validate_menu_bottom_sheet_favorite_number()
        validate_modify_bottom_sheet_favorite_number()
        validate_delete_favorite_number()
        validate_undo_delete_favorite_number()

        MatcherAssert.assertThat(cassavaTestRule.validate(ANALYTICS_FAVORITE_NUMBER_HAPPY),
            hasAllSuccess())
    }

    @Test
    fun validate_favorite_number_empty_unhappy_flow() {
        setupGraphqlMockResponse(TopupBillsFavoriteNumberMockResponseConfig(isHappyTest = false, false))
        disableCoachMark(targetContext)
        mActivityRule.launchActivity(intent)

        Thread.sleep(3000)
        validate_empty_state()

        MatcherAssert.assertThat(cassavaTestRule.validate(ANALYTICS_FAVORITE_NUMBER_UNHAPPY),
            hasAllSuccess()
        )
    }

    @Test
    fun validate_favorite_number_page_favorite_detail_error_flow() {
        setupGraphqlMockResponse(TopupBillsFavoriteNumberMockResponseConfig(isHappyTest = true, false))
        disableCoachMark(targetContext)
        mActivityRule.launchActivity(intent)

        Thread.sleep(3000)
        validate_delete_favorite_number_fail()

        MatcherAssert.assertThat(cassavaTestRule.validate(ANALYTICS_FAVORITE_NUMBER_DETAIL_UNHAPPY),
            hasAllSuccess()
        )
    }

    fun validate_show_contents_favorite_number_page() {
        onView(withId(R.id.common_topupbills_search_number_input_view)).check(matches(isDisplayed()))
        onView(withId(R.id.common_topupbills_search_number_contact_picker)).check(matches(
            isDisplayed()))
        Thread.sleep(2000)
        onView(withId(R.id.common_topupbills_favorite_number_clue)).check(matches(isDisplayed()))
        onView(withId(R.id.common_topupbills_favorite_number_rv)).check(matches(isDisplayed()))
    }

    fun validate_coachmark_favorite_number() {
        onView(withText("Lanjut"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click());
        onView(withText("Mengerti"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click());
    }

    fun validate_pick_number_from_contact_book() {
        Thread.sleep(2000)
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
        onView(withId(R.id.common_topupbills_search_number_contact_picker)).perform(click())
        Thread.sleep(2000)
        intended(toPackage("com.android.contacts"))
    }

    fun validate_menu_bottom_sheet_favorite_number() {
        val viewInteraction = onView(withId(R.id.common_topupbills_favorite_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<FavoriteNumberViewHolder>(
            0, CommonActions.clickChildViewWithId(R.id.common_topupbills_favorite_number_menu)))

        Thread.sleep(1000)
        onView(withId(R.id.common_topupbills_favorite_number_change_name)).check(matches(isDisplayed()))
        onView(withId(R.id.common_topup_bills_favorite_number_delete)).check(matches(isDisplayed()))

        onView(withId(R.id.bottom_sheet_close)).perform(click())
    }

    fun validate_modify_bottom_sheet_favorite_number() {
        val viewInteraction = onView(withId(R.id.common_topupbills_favorite_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<FavoriteNumberViewHolder>(
            0, CommonActions.clickChildViewWithId(R.id.common_topupbills_favorite_number_menu)))

        onView(withId(R.id.common_topupbills_favorite_number_change_name)).perform(click())

        Thread.sleep(1000)
        onView(withId(R.id.common_topupbills_favorite_number_name_field)).check(matches(isDisplayed()))
        onView(withId(R.id.common_topupbills_favorite_number_phone_field)).check(matches(isDisplayed()))

        // Invalid Input
        Thread.sleep(3000)
        onView(withId(R.id.common_topupbills_favorite_number_name_field)).perform(click())
        modifyBottomSheet_typeNewClientName(INVALID_CLIENT_NAME_LESS_THAN_MIN)
        onView(withId(R.id.common_topupbills_favorite_number_modify_button)).perform(click())
        Thread.sleep(1000)
        onView(withText(R.string.common_topup_fav_number_validator_less_than_3_char))
            .check(matches(isDisplayed()))

        Thread.sleep(1000)
        modifyBottomSheet_typeNewClientName(INVALID_CLIENT_NAME_MORE_THAN_MAX)
        onView(withId(R.id.common_topupbills_favorite_number_modify_button)).perform(click())
        Thread.sleep(1000)
        onView(withText(R.string.common_topup_fav_number_validator_more_than_18_char))
            .check(matches(isDisplayed()))

        Thread.sleep(1000)
        modifyBottomSheet_typeNewClientName(INVALID_CLIENT_NAME_NON_ALPHANUMERIC)
        onView(withId(R.id.common_topupbills_favorite_number_modify_button)).perform(click())
        Thread.sleep(1000)
        onView(withText(R.string.common_topup_fav_number_validator_alphanumeric))
            .check(matches(isDisplayed()))

        // Valid input
        Thread.sleep(1000)
        modifyBottomSheet_typeNewClientName(VALID_CLIENT_NAME)
        onView(withId(R.id.common_topupbills_favorite_number_modify_button)).perform(click())
        Thread.sleep(1000)
    }

    fun validate_delete_favorite_number() {
        val viewInteraction = onView(withId(R.id.common_topupbills_favorite_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<FavoriteNumberViewHolder>(
            0, CommonActions.clickChildViewWithId(R.id.common_topupbills_favorite_number_menu)))

        // Cancel
        onView(withId(R.id.common_topup_bills_favorite_number_delete)).perform(click())
        onView(withId(R.id.dialog_btn_secondary)).perform(click())

        // Redo & Proceed
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<FavoriteNumberViewHolder>(
            0, CommonActions.clickChildViewWithId(R.id.common_topupbills_favorite_number_menu)))

        onView(withId(R.id.common_topup_bills_favorite_number_delete)).perform(click())
        onView(withId(R.id.dialog_btn_primary)).perform(click())
    }

    fun validate_undo_delete_favorite_number() {
        Thread.sleep(1000)
        onView(withText("Batalkan")).perform(click())
    }

    fun validate_empty_state() {
        onView(withId(R.id.common_topupbills_not_found_state_title)).check(matches(isDisplayed()))
        onView(withId(R.id.searchbar_textfield)).perform(typeText(CLIENT_NUMBER))
        onView(withId(R.id.common_topupbills_empty_state_button)).perform(click())
    }

    fun validate_delete_favorite_number_fail() {
        val viewInteraction = onView(withId(R.id.common_topupbills_favorite_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<FavoriteNumberViewHolder>(
            0, CommonActions.clickChildViewWithId(R.id.common_topupbills_favorite_number_menu)))

        onView(withId(R.id.common_topup_bills_favorite_number_delete)).perform(click())
        onView(withId(R.id.dialog_btn_primary)).perform(click())

        Thread.sleep(3000)
    }

    private fun modifyBottomSheet_typeNewClientName(name: String) {
        onView(allOf(withId(com.tokopedia.unifycomponents.R.id.text_field_input),
            isDescendantOfA(withId(R.id.common_topupbills_favorite_number_name_field))))
            .perform(
                clearText(),
                typeText(name),
                ViewActions.closeSoftKeyboard())
    }

    private fun disableCoachMark(context: Context) {
        LocalCacheHandler(context, CACHE_PREFERENCES_NAME).also {
            it.putBoolean(CACHE_SHOW_COACH_MARK_KEY, true)
            it.applyEditor()
        }
    }

    companion object {
        const val APPLINK = ApplinkConsInternalDigital.SEAMLESS_FAVORITE_NUMBER
        const val ANALYTICS_FAVORITE_NUMBER_HAPPY = "tracker/recharge/recharge_common_topup_bills/favorite_number_happy.json"
        const val ANALYTICS_FAVORITE_NUMBER_UNHAPPY = "tracker/recharge/recharge_common_topup_bills/favorite_number_unhappy.json"
        const val ANALYTICS_FAVORITE_NUMBER_DETAIL_UNHAPPY = "tracker/recharge/recharge_common_topup_bills/favorite_number_detail_unhappy.json"

        const val EXTRA_CLIENT_NUMBER_TYPE = "EXTRA_CLIENT_NUMBER_TYPE"
        const val EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER"
        const val EXTRA_DG_CATEGORY_NAME = "EXTRA_DG_CATEGORY_NAME"
        const val EXTRA_DG_CATEGORY_IDS = "EXTRA_DG_CATEGORY_IDS"
        const val EXTRA_CATALOG_PREFIX_SELECT = "EXTRA_CATALOG_PREFIX_SELECT"

        const val VALID_CLIENT_NAME = "Tokopedia"
        const val INVALID_CLIENT_NAME_LESS_THAN_MIN = "To"
        const val INVALID_CLIENT_NAME_MORE_THAN_MAX = "TokopediaTokopediaTokopedia"
        const val INVALID_CLIENT_NAME_NON_ALPHANUMERIC = "To|<oped|a"

        const val CLIENT_NUMBER = "081212341234"
        const val CATEGORY_NAME = "Pulsa"
        val DG_CATEGORY_IDS = arrayListOf("1", "2", "20")
        val operatorData = TelcoCatalogPrefixSelect(
            rechargeCatalogPrefixSelect = RechargeCatalogPrefixSelect(
                prefixes = listOf(
                    RechargePrefix(
                        key = "12",
                        value = "0812",
                        operator = TelcoOperator(
                            id = "12",
                            attributes = TelcoAttributesOperator(
                                name = "Telkomsel",
                                defaultProductId = "70",
                                imageUrl = ""
                            )
                        )
                    )
                )
            )
        )
    }
}
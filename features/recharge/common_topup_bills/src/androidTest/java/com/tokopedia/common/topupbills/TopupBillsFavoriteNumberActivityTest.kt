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
import androidx.test.espresso.action.ViewActions.*
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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoFavoriteNumberActivity
import com.tokopedia.common.topupbills.favoritepage.view.fragment.TopupBillsPersoFavoriteNumberFragment.Companion.CACHE_PREFERENCES_NAME
import com.tokopedia.common.topupbills.favoritepage.view.fragment.TopupBillsPersoFavoriteNumberFragment.Companion.CACHE_SHOW_COACH_MARK_KEY
import com.tokopedia.common.topupbills.favoritepage.view.viewholder.PersoFavoriteNumberViewHolder
import com.tokopedia.common.topupbills.util.TopupBillsFavoriteNumberMockResponseConfig
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
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
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var mActivityRule = ActivityTestRule(TopupBillsPersoFavoriteNumberActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        InstrumentationAuthHelper.userSession {
            /** content.prod.automation2+frontendtest@tokopedia.com */
            userId = "17211048"
            shopId = "3533069"
        }
        Intents.intending(IsNot.not(IntentMatchers.isInternal()))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        val extras = Bundle()
        extras.putString(TopupBillsPersoFavoriteNumberActivity.EXTRA_CLIENT_NUMBER_TYPE, ClientNumberType.TYPE_INPUT_TEL.value)
        extras.putString(TopupBillsPersoFavoriteNumberActivity.EXTRA_CLIENT_NUMBER, CLIENT_NUMBER)
        extras.putStringArrayList(TopupBillsPersoFavoriteNumberActivity.EXTRA_DG_CATEGORY_IDS, DG_CATEGORY_IDS)
        extras.putStringArrayList(TopupBillsPersoFavoriteNumberActivity.EXTRA_DG_OPERATOR_IDS, arrayListOf())
        extras.putString(TopupBillsPersoFavoriteNumberActivity.EXTRA_LOYALTY_STATUS, "")
        extras.putString(TopupBillsPersoFavoriteNumberActivity.EXTRA_DG_CATEGORY_NAME, CATEGORY_NAME)

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
        Intents.intending(
            AllOf.allOf(
                IntentMatchers.hasData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
                IntentMatchers.hasAction(Intent.ACTION_PICK)
            )
        ).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validate_favorite_number_page_happy_flow() {
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse(
            TopupBillsFavoriteNumberMockResponseConfig(
                isMockFilledFavoriteNumber = true,
                isMockUpdateFavoriteDetail = true
            )
        )
        isCoachmarkDisabled(targetContext, false)
        mActivityRule.launchActivity(intent)

        validate_show_contents_favorite_number_page()
        validate_coachmark_favorite_number()
        validate_menu_bottom_sheet_favorite_number()
        validate_modify_bottom_sheet_favorite_number()
        validate_delete_favorite_number()
        validate_undo_delete_favorite_number()
        validate_click_favorite_number()

        MatcherAssert.assertThat(
            cassavaTestRule.validate(ANALYTICS_FAVORITE_NUMBER_HAPPY),
            hasAllSuccess()
        )
    }

    @Test
    fun validate_favorite_number_empty_unhappy_flow() {
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse(
            TopupBillsFavoriteNumberMockResponseConfig(
                isMockFilledFavoriteNumber = false,
                isMockUpdateFavoriteDetail = false
            )
        )
        isCoachmarkDisabled(targetContext, true)
        mActivityRule.launchActivity(intent)

        validate_empty_state()

        MatcherAssert.assertThat(
            cassavaTestRule.validate(ANALYTICS_FAVORITE_NUMBER_UNHAPPY),
            hasAllSuccess()
        )
    }

    @Test
    fun validate_favorite_number_page_favorite_detail_error_flow() {
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse(
            TopupBillsFavoriteNumberMockResponseConfig(
                isMockFilledFavoriteNumber = true,
                isMockUpdateFavoriteDetail = false
            )
        )
        isCoachmarkDisabled(targetContext, true)
        mActivityRule.launchActivity(intent)

        validate_delete_favorite_number_fail()

        MatcherAssert.assertThat(
            cassavaTestRule.validate(ANALYTICS_FAVORITE_NUMBER_DETAIL_UNHAPPY),
            hasAllSuccess()
        )
    }

    fun validate_show_contents_favorite_number_page() {
        Thread.sleep(2000)
        onView(withId(R.id.common_topupbills_favorite_number_rv)).check(matches(isDisplayed()))
    }

    fun validate_coachmark_favorite_number() {
        coachMark_clickButtonWithText("Lanjut")
        coachMark_clickButtonWithText("Mengerti")
    }

    fun validate_menu_bottom_sheet_favorite_number() {
        favoriteNumberItem_clickMenu(0)

        Thread.sleep(1000)
        favoriteNumberMenu_validateContents()

        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)).perform(click())
    }

    fun validate_modify_bottom_sheet_favorite_number() {
        favoriteNumberItem_clickMenu(0)
        Thread.sleep(1000)

        onView(withId(R.id.common_topupbills_favorite_number_change_name)).perform(click())

        Thread.sleep(1000)
        modifyBottomSheet_validateTextFields()

        // Invalid Input
        Thread.sleep(2000)
        onView(withId(R.id.common_topupbills_favorite_number_name_field)).perform(click())
        modifyBottomSheet_typeNewClientName(INVALID_CLIENT_NAME_LESS_THAN_MIN)
        modifyBottomSheet_clickUpdateNameButton()
        modifyBottomSheet_validateErrorMessageText(R.string.common_topup_fav_number_validator_less_than_3_char)

        modifyBottomSheet_typeNewClientName(INVALID_CLIENT_NAME_MORE_THAN_MAX)
        modifyBottomSheet_clickUpdateNameButton()
        modifyBottomSheet_validateErrorMessageText(R.string.common_topup_fav_number_validator_more_than_18_char)

        modifyBottomSheet_typeNewClientName(INVALID_CLIENT_NAME_NON_ALPHANUMERIC)
        modifyBottomSheet_clickUpdateNameButton()
        modifyBottomSheet_validateErrorMessageText(R.string.common_topup_fav_number_validator_alphanumeric)

        // Valid input
        modifyBottomSheet_typeNewClientName(VALID_CLIENT_NAME)
        modifyBottomSheet_clickUpdateNameButton()
        Thread.sleep(1000)
    }

    fun validate_click_favorite_number() {
        favoriteNumberItem_clickNumber(0)
    }

    fun validate_delete_favorite_number() {
        favoriteNumberItem_clickMenu(0)

        // Cancel
        menuBottomSheet_clickDelete()
        deleteConfirmationDialog_clickCancel()

        // Redo & Proceed
        favoriteNumberItem_clickMenu(0)

        menuBottomSheet_clickDelete()
        deleteConfirmationDialog_clickConfirm()
    }

    fun validate_undo_delete_favorite_number() {
        Thread.sleep(1000)
        onView(withText("Batalkan")).perform(click())
    }

    fun validate_empty_state() {
        Thread.sleep(1000)
        onView(withId(R.id.common_topupbills_not_found_state_title)).check(matches(isDisplayed()))
        onView(withId(R.id.common_topupbills_not_found_state_button)).perform(click())
    }

    fun validate_delete_favorite_number_fail() {
        favoriteNumberItem_clickMenu(0)

        menuBottomSheet_clickDelete()
        deleteConfirmationDialog_clickConfirm()

        Thread.sleep(3000)
    }

    private fun modifyBottomSheet_typeNewClientName(name: String) {
        Thread.sleep(1000)
        onView(
            allOf(
                withId(com.tokopedia.unifycomponents.R.id.text_field_input),
                isDescendantOfA(withId(R.id.common_topupbills_favorite_number_name_field))
            )
        )
            .perform(
                clearText(),
                typeText(name),
                ViewActions.closeSoftKeyboard()
            )
    }

    private fun isCoachmarkDisabled(context: Context, isDisabled: Boolean) {
        LocalCacheHandler(context, CACHE_PREFERENCES_NAME).also {
            it.putBoolean(CACHE_SHOW_COACH_MARK_KEY, isDisabled)
            it.applyEditor()
        }
    }

    private fun modifyBottomSheet_clickUpdateNameButton() {
        onView(withId(R.id.common_topupbills_favorite_number_modify_button)).perform(click())
    }

    private fun modifyBottomSheet_validateErrorMessageText(stringRes: Int) {
        Thread.sleep(1000)
        onView(withText(stringRes)).check(matches(isDisplayed()))
    }

    private fun modifyBottomSheet_validateTextFields() {
        Thread.sleep(1000)
        onView(withId(R.id.common_topupbills_favorite_number_name_field)).check(matches(isDisplayed()))
        onView(withId(R.id.common_topupbills_favorite_number_phone_field)).check(matches(isDisplayed()))
    }

    private fun menuBottomSheet_clickDelete() {
        Thread.sleep(1000)
        onView(withId(R.id.common_topup_bills_favorite_number_delete)).perform(click())
    }

    private fun favoriteNumberItem_clickNumber(position: Int) {
        val viewInteraction =
            onView(withId(R.id.common_topupbills_favorite_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<PersoFavoriteNumberViewHolder>(
                position,
                click()
            )
        )
    }

    private fun favoriteNumberItem_clickMenu(position: Int) {
        val viewInteraction =
            onView(withId(R.id.common_topupbills_favorite_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<PersoFavoriteNumberViewHolder>(
                position,
                CommonActions.clickChildViewWithId(R.id.common_topupbills_favorite_number_menu)
            )
        )
    }

    private fun favoriteNumberMenu_validateContents() {
        Thread.sleep(1000)
        onView(withId(R.id.common_topupbills_favorite_number_change_name)).check(matches(isDisplayed()))
        onView(withId(R.id.common_topup_bills_favorite_number_delete)).check(matches(isDisplayed()))
    }

    private fun coachMark_clickButtonWithText(text: String) {
        Thread.sleep(1000)
        onView(withText(text))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click());
    }

    private fun deleteConfirmationDialog_clickCancel() {
        Thread.sleep(1000)
        onView(withId(com.tokopedia.dialog.R.id.dialog_btn_secondary)).perform(click())
    }

    private fun deleteConfirmationDialog_clickConfirm() {
        Thread.sleep(1000)
        onView(withId(com.tokopedia.dialog.R.id.dialog_btn_primary)).perform(click())
    }

    companion object {
        const val APPLINK = ApplinkConsInternalDigital.FAVORITE_NUMBER
        const val ANALYTICS_FAVORITE_NUMBER_HAPPY =
            "tracker/recharge/recharge_common_topup_bills/favorite_number_positive_flow.json"
        const val ANALYTICS_FAVORITE_NUMBER_UNHAPPY =
            "tracker/recharge/recharge_common_topup_bills/favorite_number_negative_flow.json"
        const val ANALYTICS_FAVORITE_NUMBER_DETAIL_UNHAPPY =
            "tracker/recharge/recharge_common_topup_bills/favorite_number_detail_negative_flow.json"

        const val VALID_CLIENT_NAME = "Tokopedia"
        const val INVALID_CLIENT_NAME_LESS_THAN_MIN = "To"
        const val INVALID_CLIENT_NAME_MORE_THAN_MAX = "TokopediaTokopediaTokopedia"
        const val INVALID_CLIENT_NAME_NON_ALPHANUMERIC = "To|<oped|a"

        const val CLIENT_NUMBER = "081212341234"
        const val CATEGORY_NAME = "Pulsa"
        val DG_CATEGORY_IDS = arrayListOf("1", "2", "20")
    }
}

package com.tokopedia.common.topupbills

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.prefix_select.TelcoAttributesOperator
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity
import com.tokopedia.common.topupbills.favoritepage.view.fragment.TopupBillsPersoFavoriteNumberFragment
import com.tokopedia.common.topupbills.util.TopupBillsFavoriteNumberMockResponseConfig
import com.tokopedia.common.topupbills.view.activity.TopupBillsSavedNumberActivityStub
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import junit.framework.Assert.assertTrue
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopupBillsSavedNumberActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    var intent: Intent? = null

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var mActivityRule = ActivityTestRule(TopupBillsSavedNumberActivityStub::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        Intents.intending(IsNot.not(IntentMatchers.isInternal()))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        val extras = Bundle()
        extras.putString(TopupBillsPersoSavedNumberActivity.EXTRA_CLIENT_NUMBER_TYPE, ClientNumberType.TYPE_INPUT_TEL.value)
        extras.putString(TopupBillsPersoSavedNumberActivity.EXTRA_CLIENT_NUMBER, CLIENT_NUMBER)
        extras.putStringArrayList(TopupBillsPersoSavedNumberActivity.EXTRA_DG_CATEGORY_IDS, DG_CATEGORY_IDS)
        extras.putStringArrayList(TopupBillsPersoSavedNumberActivity.EXTRA_DG_OPERATOR_IDS, arrayListOf())
        extras.putString(TopupBillsPersoSavedNumberActivity.EXTRA_DG_CATEGORY_NAME, CATEGORY_NAME)
        extras.putString(TopupBillsPersoSavedNumberActivity.EXTRA_LOYALTY_STATUS, "")
        extras.putBoolean(TopupBillsPersoSavedNumberActivity.EXTRA_IS_SWITCH_CHECKED, false)

        intent = Intent(targetContext, TopupBillsSavedNumberActivityStub::class.java).apply {
            putExtras(extras)
        }
    }

    @After
    fun cleanUp() {
        Intents.release()
        intent = null
    }

    @Test
    fun validate_saved_number_page() {
        isFavnumCoachmarkDisabled(targetContext, true)
        setupGraphqlMockResponse(
            TopupBillsFavoriteNumberMockResponseConfig(
                isMockFilledFavoriteNumber = true,
                isMockUpdateFavoriteDetail = true
            )
        )
        mActivityRule.launchActivity(intent)
        validate_tab_switching()
        validate_search_filter_interaction()
    }

    private fun validate_tab_switching() {
        onView(withId(R.id.common_topup_bills_contacts_rv)).perform(swipeLeft())
        Thread.sleep(1000)
        onView(withId(R.id.common_topupbills_favorite_number_rv)).perform(swipeRight())
        Thread.sleep(1000)
    }

    private fun validate_search_filter_interaction() {
        // Contact List
        onView(withId(R.id.searchbar_textfield))
            .perform(clearText())
            .perform(typeText(SEARCH_INPUT_NUMBER_PREFIX))
        assertTrue(getRecyclerViewItemCount(R.id.common_topup_bills_contacts_rv) == 4)

        onView(withId(R.id.searchbar_textfield))
            .perform(clearText())
            .perform(typeText(SEARCH_INPUT_NAME_CHARACTER))
        assertTrue(getRecyclerViewItemCount(R.id.common_topup_bills_contacts_rv) == 3)

        Espresso.closeSoftKeyboard()
        mActivityRule.activity.runOnUiThread {
            mActivityRule.activity.findViewById<EditText>(R.id.searchbar_textfield).clearFocus()
        }
        Thread.sleep(1000)
        validate_saved_number_empty_state()

        onView(withId(R.id.searchbar_textfield)).perform(clearText())
        Thread.sleep(1000)

        // Favorite Number
        // search keyword should be applied to both Kontak HP and Nomor Favorit
        onView(withId(R.id.common_topup_bills_contacts_rv)).perform(swipeUp())
        Thread.sleep(1000)
        onView(withId(R.id.common_topup_bills_contacts_rv)).perform(swipeLeft())
        Thread.sleep(2000)

        onView(withId(R.id.searchbar_textfield))
            .perform(clearText())
            .perform(typeText(SEARCH_INPUT_NUMBER_PREFIX))
        assertTrue(getRecyclerViewItemCount(R.id.common_topupbills_favorite_number_rv) == 3)

        onView(withId(R.id.searchbar_textfield))
            .perform(clearText())
            .perform(typeText(SEARCH_INPUT_NAME_CHARACTER))
        assertTrue(getRecyclerViewItemCount(R.id.common_topupbills_favorite_number_rv) == 1)

        onView(withId(R.id.searchbar_textfield))
            .perform(clearText())
            .perform(typeText(SEARCH_INPUT_NAME_CHARACTER))

        validate_saved_number_empty_state()
        Espresso.closeSoftKeyboard()
    }


    private fun validate_saved_number_empty_state() {
        onView(withId(R.id.searchbar_textfield))
            .perform(clearText())
            .perform(typeText(NON_EXISTING_FAVORITE_NUMBER))
        onView(withId(R.id.common_topupbills_saved_num_empty_state_image)).check(matches(isDisplayed()))
    }

    private fun getRecyclerViewItemCount(resId: Int): Int {
        val recyclerView = mActivityRule.activity.findViewById<RecyclerView>(resId)
        return recyclerView?.adapter?.itemCount ?: 0
    }


    private fun isFavnumCoachmarkDisabled(context: Context, isDisabled: Boolean) {
        LocalCacheHandler(context, TopupBillsPersoFavoriteNumberFragment.CACHE_PREFERENCES_NAME).also {
            it.putBoolean(TopupBillsPersoFavoriteNumberFragment.CACHE_SHOW_COACH_MARK_KEY, isDisabled)
            it.applyEditor()
        }
    }

    companion object {
        const val SEARCH_INPUT_NUMBER_PREFIX = "0812"
        const val SEARCH_INPUT_NAME_CHARACTER = "m"

        const val CLIENT_NUMBER = "081212341234"
        const val NON_EXISTING_FAVORITE_NUMBER = "08129890285088"
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
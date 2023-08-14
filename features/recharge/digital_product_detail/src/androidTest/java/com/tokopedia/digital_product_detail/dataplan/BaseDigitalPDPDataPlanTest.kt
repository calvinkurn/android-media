package com.tokopedia.digital_product_detail.dataplan

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsSavedNumber
import com.tokopedia.digital_product_detail.dataplan.utils.DigitalPDPDataPlanMockConfig
import com.tokopedia.digital_product_detail.presentation.activity.DigitalPDPDataPlanActivity
import com.tokopedia.digital_product_detail.utils.CustomViewAction.nestedScrollTo
import com.tokopedia.recharge_component.model.InputNumberActionType
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomFullViewHolder
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomGridViewHolder
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions.clickChildViewWithId
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.unifycomponents.R
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule

abstract class BaseDigitalPDPDataPlanTest {

    @get:Rule
    var mActivityRule: IntentsTestRule<DigitalPDPDataPlanActivity> = object: IntentsTestRule<DigitalPDPDataPlanActivity>(
        DigitalPDPDataPlanActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return RouteManager.getIntent(targetContext, getApplink())
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(getMockModelConfig())
        }
    }

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    protected fun favoriteNumberPage_stubContactNumber() {
        Intents.intending(
            IntentMatchers.hasComponent(
                ComponentNameMatchers.hasClassName(
                    TopupBillsPersoSavedNumberActivity::class.java.name
                )
            ))
            .respondWith(intentResult_returnContactNumber())
    }

    protected fun favoriteNumberPage_stubFavoriteNumber() {
        Intents.intending(
            IntentMatchers.hasComponent(
                ComponentNameMatchers.hasClassName(
                    TopupBillsPersoSavedNumberActivity::class.java.name
                )
            ))
            .respondWith(intentResult_returnFavoriteNumber())
    }

    private fun intentResult_returnContactNumber(): Instrumentation.ActivityResult {
        val savedNumber = TopupBillsSavedNumber(
            clientName = "tokopedia contact",
            clientNumber = "08120810812",
            inputNumberActionTypeIndex = InputNumberActionType.CONTACT.ordinal
        )
        val resultData = Intent()
        resultData.putExtra(
            TopupBillsPersoSavedNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER,
            savedNumber
        )
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun intentResult_returnFavoriteNumber(): Instrumentation.ActivityResult {
        val savedNumber = TopupBillsSavedNumber(
            clientName = "tokopedia favorite",
            clientNumber = "085708570857",
            inputNumberActionTypeIndex = InputNumberActionType.FAVORITE.ordinal,
            categoryId = "1"
        )
        val resultData = Intent()
        resultData.putExtra(
            TopupBillsPersoSavedNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER,
            savedNumber
        )
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    protected fun clientNumberWidget_typeNumber(number: String) {
        onView(withId(R.id.text_field_input)).perform(typeText(number))
    }

    protected fun clientNumberWidget_clickClearIcon() {
        onView(withId(com.tokopedia.recharge_component.R.id.text_field_icon_close)).perform(click())
    }

    protected fun clientNumberWidget_clickContactIcon() {
        onView(withId(com.tokopedia.recharge_component.R.id.text_field_icon_2)).perform(click())
    }

    protected fun favoriteChips_clickChip_withText(text: String) {
        onView(allOf(
            withId(com.tokopedia.recharge_component.R.id.chip_text),
            isDescendantOfA(withId(com.tokopedia.recharge_component.R.id.sort_filter_items)),
            withText(text))
        ).perform(click())
    }

    protected fun autoComplete_clickItem_withText(text: String) {
        onView(withText(text))
            .inRoot(RootMatchers.isPlatformPopup())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(click())
    }

    protected fun buyWidget_clickChevron() {
        onView(withId(com.tokopedia.recharge_component.R.id.icon_buy_widget_chevron)).perform(click())
    }

    protected fun recommendations_clickCard() {
        onView(withId(com.tokopedia.recharge_component.R.id.tg_title_recharge_recommendation_card_big)).perform(click())
    }

    protected fun mccm_clickCard_withIndex(index: Int) {
        onView(withId(com.tokopedia.recharge_component.R.id.rv_mccm_full)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(index, click()))
    }

    protected fun mccm_clickCardChevron_withIndex(index: Int) {
        onView(withId(com.tokopedia.recharge_component.R.id.rv_mccm_full)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(
                index,
                clickChildViewWithId(com.tokopedia.recharge_component.R.id.icon_cheveron_denom_full)
            )
        )
    }
    protected fun denom_clickCard_withIndex(index: Int) {
        onView(withId(com.tokopedia.recharge_component.R.id.tg_denom_full_widget_title)).perform(nestedScrollTo())
        onView(withId(com.tokopedia.recharge_component.R.id.rv_denom_full_card))
            .perform(
                scrollToPosition<DenomGridViewHolder>(index),
                RecyclerViewActions.actionOnItemAtPosition<DenomGridViewHolder>(index, click())
            )
    }

    protected fun denom_clickCardChevron_withIndex(index: Int) {
        onView(withId(com.tokopedia.recharge_component.R.id.rv_denom_full_card)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(
                index,
                clickChildViewWithId(com.tokopedia.recharge_component.R.id.icon_cheveron_denom_full)
            )
        )
    }

    protected fun productDetailBottomSheet_clickClose() {
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)).perform(click())
    }

    protected fun filterChip_clickChip_withText(text: String) {
        onView(allOf(
            withId(com.tokopedia.recharge_component.R.id.chip_text),
            isDescendantOfA(withId(com.tokopedia.recharge_component.R.id.sort_filter_items)),
            withText(text))
        ).perform(click())
    }

    protected fun mccm_vertical_clickShowMore() {
        onView(withId(com.tokopedia.recharge_component.R.id.tg_mccm_see_more)).perform(click())
    }

    protected fun mccm_vertical_clickCard_withIndex(index: Int) {
        onView(withId(com.tokopedia.recharge_component.R.id.rv_mccm_vertical_full)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(index, click()))
    }

    protected fun mccm_vertical_clickCardChevron_withIndex(index: Int) {
        onView(withId(com.tokopedia.recharge_component.R.id.rv_mccm_vertical_full)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(
                index,
                clickChildViewWithId(com.tokopedia.recharge_component.R.id.icon_cheveron_denom_full)
            )
        )
    }

    protected fun scroll_to_bottom_data_plan() {
        onView(withId(com.tokopedia.digital_product_detail.R.id.recharge_pdp_paket_data_sv_container))
            .perform(swipeUp())
    }

    abstract fun getApplink(): String

    abstract fun getMockModelConfig(): MockModelConfig

    companion object {
        const val APPLINK = "tokopedia://digital/form?category_id=2&menu_id=290&template=paketdatav2"
    }
}

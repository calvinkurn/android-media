package com.tokopedia.digital_product_detail.dataplan

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
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
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.presentation.activity.DigitalPDPDataPlanActivity
import com.tokopedia.digital_product_detail.presentation.webview.RechargeCheckBalanceWebViewActivity
import com.tokopedia.digital_product_detail.pulsa.DigitalPDPPulsaActivityStub
import com.tokopedia.digital_product_detail.utils.CustomViewAction
import com.tokopedia.digital_product_detail.utils.CustomViewAction.nestedScrollTo
import com.tokopedia.recharge_component.model.InputNumberActionType
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeCheckBalanceDetailViewHolder
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomFullViewHolder
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions.clickChildViewWithId
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import com.tokopedia.analyticsdebugger.R as analyticsdebuggerR
import com.tokopedia.digital_product_detail.R as digital_product_detailR
import com.tokopedia.recharge_component.R as recharge_componentR
import com.tokopedia.sortfilter.R as sortfilterR
import com.tokopedia.unifycomponents.R as unifycomponentsR

abstract class BaseDigitalPDPDataPlanTest {

    @get:Rule
    var mActivityRule: IntentsTestRule<DigitalPDPDataPlanActivity> = object : IntentsTestRule<DigitalPDPDataPlanActivity>(
        DigitalPDPDataPlanActivity::class.java
    ) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(targetContext, DigitalPDPDataPlanActivityStub::class.java).apply {
                val extras = Bundle()
                extras.putString(DigitalPDPConstant.PARAM_MENU_ID, "290")
                extras.putString(DigitalPDPConstant.PARAM_CATEGORY_ID, "2")
                putExtras(extras)
            }
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
            )
        )
            .respondWith(intentResult_returnContactNumber())
    }

    protected fun favoriteNumberPage_stubFavoriteNumber() {
        Intents.intending(
            IntentMatchers.hasComponent(
                ComponentNameMatchers.hasClassName(
                    TopupBillsPersoSavedNumberActivity::class.java.name
                )
            )
        )
            .respondWith(intentResult_returnFavoriteNumber())
    }

    protected fun checkBalanceWebView_stubIntentResult() {
        Intents.intending(
            IntentMatchers.hasComponent(
                ComponentNameMatchers.hasClassName(
                    RechargeCheckBalanceWebViewActivity::class.java.name
                )
            )
        )
            .respondWith(intentResult_returnAccessToken())
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

    private fun intentResult_returnAccessToken(): Instrumentation.ActivityResult {
        val accessToken = "access_token"
        val resultData = Intent()
        resultData.putExtra(
            TopupBillsPersoSavedNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER,
            accessToken
        )
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    protected fun clientNumberWidget_typeNumber(number: String) {
        onView(withId(unifycomponentsR.id.text_field_input)).perform(typeText(number))
    }

    protected fun clientNumberWidget_clearText() {
        onView(withId(unifycomponentsR.id.text_field_input)).perform(clearText())
    }

    protected fun clientNumberWidget_clickClearIcon() {
        onView(withId(unifycomponentsR.id.text_field_icon_close)).perform(click())
    }

    protected fun clientNumberWidget_clickContactIcon() {
        onView(withId(unifycomponentsR.id.text_field_icon_2)).perform(click())
    }

    protected fun clientNumberWidget_clickCheckBalanceOTPWidget() {
        onView(withId(recharge_componentR.id.check_balance_otp_title)).perform(click())
    }

    protected fun clientNumberWidget_clickCheckBalanceWidget() {
        onView(withId(recharge_componentR.id.check_balance_rv)).perform(click())
    }

    protected fun favoriteChips_clickChip_withText(text: String) {
        onView(
            allOf(
                withId(analyticsdebuggerR.id.chip_text),
                isDescendantOfA(withId(sortfilterR.id.sort_filter_items)),
                withText(text)
            )
        ).perform(click())
    }

    protected fun autoComplete_clickItem_withText(text: String) {
        onView(withText(text))
            .inRoot(RootMatchers.isPlatformPopup())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(click())
    }

    protected fun buyWidget_clickChevron() {
        onView(withId(recharge_componentR.id.icon_buy_widget_chevron)).perform(click())
    }

    protected fun recommendations_clickCard() {
        onView(withId(recharge_componentR.id.tg_title_recharge_recommendation_card_big)).perform(click())
    }

    protected fun mccm_clickCard_withIndex(index: Int) {
        onView(withId(recharge_componentR.id.rv_mccm_full)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(index, click())
        )
    }

    protected fun mccm_clickCardChevron_withIndex(index: Int) {
        onView(withId(recharge_componentR.id.rv_mccm_full)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(
                index,
                clickChildViewWithId(recharge_componentR.id.icon_cheveron_denom_full)
            )
        )
    }

    protected fun mccm_vertical_clickCard_withIndex(index: Int) {
        onView(withId(recharge_componentR.id.rv_mccm_vertical_full)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(index, click())
        )
    }

    protected fun mccm_vertical_clickCardChevron_withIndex(index: Int) {
        onView(withId(recharge_componentR.id.rv_mccm_vertical_full)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(
                index,
                clickChildViewWithId(recharge_componentR.id.icon_cheveron_denom_full)
            )
        )
    }

    protected fun mccm_vertical_clickShowMore() {
        onView(withId(recharge_componentR.id.tg_mccm_see_more)).perform(click())
    }

    protected fun scroll_to_bottom_data_plan() {
        onView(withId(digital_product_detailR.id.recharge_pdp_paket_data_sv_container))
            .perform(swipeUp())
    }
    protected fun denom_clickCard_withIndex(index: Int) {
        onView(withId(recharge_componentR.id.tg_denom_full_widget_title)).perform(nestedScrollTo())
        onView(withId(recharge_componentR.id.rv_denom_full_card)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(index, click())
        )
    }

    protected fun denom_clickCardChevron_withIndex(index: Int) {
        onView(withId(recharge_componentR.id.rv_denom_full_card)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DenomFullViewHolder>(
                index,
                clickChildViewWithId(recharge_componentR.id.icon_cheveron_denom_full)
            )
        )
    }

    protected fun productDetailBottomSheet_clickClose() {
        onView(withId(unifycomponentsR.id.bottom_sheet_close)).perform(click())
    }

    protected fun filterChip_clickChip_withText(text: String) {
        onView(
            allOf(
                withId(analyticsdebuggerR.id.chip_text),
                isDescendantOfA(withId(sortfilterR.id.sort_filter_items)),
                withText(text)
            )
        ).perform(click())
    }

    protected fun checkBalanceOTPBottomSheet_clickButton() {
        onView(withId(recharge_componentR.id.bottomsheet_otp_button)).perform(click())
    }

    protected fun checkBalanceBottomSheet_clickItem_withIndex(index: Int) {
        onView(withId(recharge_componentR.id.recharge_check_balance_detail_rv))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RechargeCheckBalanceDetailViewHolder>(
                        index,
                        CustomViewAction.clickChildViewWithId(recharge_componentR.id.check_balance_detail_buy_button)
                    )
            )
    }

    protected fun checkBalanceBottomSheet_clickCloseIcon() {
        onView(withId(unifycomponentsR.id.bottom_sheet_close)).perform(click())
    }

    abstract fun getMockModelConfig(): MockModelConfig

}

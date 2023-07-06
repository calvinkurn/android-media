package com.tokopedia.digital_product_detail.pulsa

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
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
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsSavedNumber
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.presentation.activity.DigitalPDPPulsaActivity
import com.tokopedia.digital_product_detail.presentation.webview.RechargeCheckBalanceWebViewActivity
import com.tokopedia.recharge_component.model.InputNumberActionType
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeCheckBalanceDetailViewHolder
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomGridViewHolder
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule

abstract class BaseDigitalPDPPulsaTest {

    @get:Rule
    var mActivityRule: IntentsTestRule<DigitalPDPPulsaActivity> = object : IntentsTestRule<DigitalPDPPulsaActivity>(DigitalPDPPulsaActivity::class.java) {
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

    abstract fun getMockModelConfig(): MockModelConfig

    protected fun favoriteNumberPage_stubContactNumber() {
        Intents.intending(
            IntentMatchers.hasComponent(hasClassName(TopupBillsPersoSavedNumberActivity::class.java.name))
        )
            .respondWith(intentResult_returnContactNumber())
    }

    protected fun favoriteNumberPage_stubFavoriteNumber() {
        Intents.intending(
            IntentMatchers.hasComponent(hasClassName(TopupBillsPersoSavedNumberActivity::class.java.name))
        )
            .respondWith(intentResult_returnFavoriteNumber())
    }

    protected fun checkBalanceWebView_stubIntentResult() {
        Intents.intending(
            IntentMatchers.hasComponent(hasClassName(RechargeCheckBalanceWebViewActivity::class.java.name))
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
            EXTRA_CALLBACK_CLIENT_NUMBER,
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
            EXTRA_CALLBACK_CLIENT_NUMBER,
            savedNumber
        )
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun intentResult_returnAccessToken(): Instrumentation.ActivityResult {
        val accessToken = "access_token"
        val resultData = Intent()
        resultData.putExtra(
            EXTRA_CALLBACK_CLIENT_NUMBER,
            accessToken
        )
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    protected fun clientNumberWidget_typeNumber(number: String) {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input))
            .perform(typeText(number))
    }

    protected fun clientNumberWidget_clickClearIcon() {
        onView(withId(R.id.text_field_icon_close)).perform(click())
    }

    protected fun clientNumberWidget_clickContactIcon() {
        onView(withId(R.id.text_field_icon_2)).perform(click())
    }

    protected fun clientNumberWidget_clickCheckBalanceOTPWidget() {
        onView(withId(R.id.check_balance_otp_title)).perform(click())
    }

    protected fun clientNumberWidget_clickCheckBalanceWidget() {
        onView(withId(R.id.check_balance_rv)).perform(click())
    }

    protected fun favoriteChips_clickChip_withText(text: String) {
        onView(
            allOf(
                withId(R.id.chip_text),
                isDescendantOfA(withId(R.id.sort_filter_items)),
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
        onView(withId(R.id.icon_buy_widget_chevron)).perform(click())
    }

    protected fun recommendations_clickCard() {
        onView(withId(R.id.rv_recharge_recommendation_card_title)).perform(click())
    }

    protected fun mccm_clickCard_withIndex(index: Int) {
        onView(withId(R.id.rv_mccm_grid)).perform(RecyclerViewActions.actionOnItemAtPosition<DenomGridViewHolder>(index, click()))
    }

    protected fun denom_clickCard_withIndex(index: Int) {
        onView(withId(R.id.rv_denom_grid_card)).perform(RecyclerViewActions.actionOnItemAtPosition<DenomGridViewHolder>(index, click()))
    }

    protected fun checkBalanceOTPBottomSheet_clickButton() {
        onView(withId(R.id.bottomsheet_otp_button)).perform(click())
    }

    protected fun checkBalanceBottomSheet_clickItem_withIndex(index: Int) {
        onView(withId(R.id.recharge_check_balance_detail_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<RechargeCheckBalanceDetailViewHolder>(index, click()))
    }

    abstract fun getApplink(): String
}

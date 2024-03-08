package com.tokopedia.digital_product_detail.dataplan

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.digital_product_detail.dataplan.utils.DigitalPDPDataPlanMockConfig
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalPDPDataPlanCassavaTest : BaseDigitalPDPDataPlanTest() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    override fun getMockModelConfig(): MockModelConfig = DigitalPDPDataPlanMockConfig()

    private fun stubIntent() {
        Intents.intending(IsNot.not(IntentMatchers.isInternal()))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Before
    fun setUp() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        stubIntent()
    }

    @Test
    fun validate_interact_with_check_balance() {
        Thread.sleep(2000)
        interactWithCheckBalanceWidget()
        MatcherAssert.assertThat(
            cassavaTestRule.validate(PATH_ANALYTICS_OTP),
            hasAllSuccess()
        )
    }

    @Test
    fun validate_interact_with_recommendation_widget() {
        Thread.sleep(2000)
        interactWithRecommendationWidget()
        MatcherAssert.assertThat(
            cassavaTestRule.validate(PATH_ANALYTICS_RECOMMENDATION),
            hasAllSuccess()
        )
    }

    @Test
    fun validate_interact_with_client_number_widget() {
        Thread.sleep(2000)
        interactWithClientNumberWidget()
        interactWithFavoriteChip()
        MatcherAssert.assertThat(
            cassavaTestRule.validate(PATH_ANALYTICS_FAVORITE),
            hasAllSuccess()
        )
    }

    @Test
    fun validate_interact_with_auto_complete() {
        interactWithAutoComplete()
        MatcherAssert.assertThat(
            cassavaTestRule.validate(PATH_ANALYTICS_AUTOCOMPLETE),
            hasAllSuccess()
        )
    }

    @Test
    fun validate_interact_with_filter_and_buy() {
        interactWithFilterChip()
        interactWithFullDenomWidget()
        interactWithBuyWidget()
        MatcherAssert.assertThat(
            cassavaTestRule.validate(PATH_ANALYTICS_FILTER_BUY_WIDGET),
            hasAllSuccess()
        )
    }

    @Test
    fun validate_interact_with_input_manual_number() {
        Thread.sleep(4000)
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_typeNumber("081228")
        Thread.sleep(4000)
        MatcherAssert.assertThat(
            cassavaTestRule.validate(PATH_ANALYTICS_INPUT_MANUAL),
            hasAllSuccess()
        )
    }

    private fun interactWithClientNumberWidget() {
        Thread.sleep(4000)
        favoriteNumberPage_stubContactNumber()
        clientNumberWidget_clickContactIcon()

        Thread.sleep(4000)
        favoriteNumberPage_stubFavoriteNumber()
        clientNumberWidget_clickContactIcon()

        Thread.sleep(4000)
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_typeNumber("0812")
        clientNumberWidget_typeNumber("3456")
        clientNumberWidget_typeNumber("7890")
    }

    private fun interactWithCheckBalanceWidget() {
        Thread.sleep(2000)
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_typeNumber("085808580858")
        Thread.sleep(4000)
        clientNumberWidget_clickCheckBalanceWidget()

        Thread.sleep(2000)
        checkBalanceBottomSheet_clickItem_withIndex(0)

        Thread.sleep(1000)
        checkBalanceBottomSheet_clickCloseIcon()
    }

    private fun interactWithFavoriteChip() {
        clientNumberWidget_clearText()
        Thread.sleep(1000)
        favoriteChips_clickChip_withText("08121111112")
        Thread.sleep(1000)
        favoriteChips_clickChip_withText("Danur rrrr")
    }

    private fun interactWithAutoComplete() {
        Thread.sleep(4000)
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_typeNumber("0812")
        autoComplete_clickItem_withText("Danur rrrr")
        Espresso.closeSoftKeyboard()

        Thread.sleep(4000)
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_typeNumber("Jessica")
        autoComplete_clickItem_withText("Jessica")
        Espresso.closeSoftKeyboard()
    }

    private fun interactWithRecommendationWidget() {
        favoriteNumberPage_stubContactNumber()
        Thread.sleep(400)
        recommendations_clickCard()
    }

    private fun interactWithBuyWidget() {
        Thread.sleep(3000)
        buyWidget_clickChevron()
    }

    private fun interactWithFullDenomWidget() {
        scroll_to_bottom_data_plan()
        Thread.sleep(2000)
        denom_clickCard_withIndex(0)
        denom_clickCardChevron_withIndex(0)

        Thread.sleep(1000)
        productDetailBottomSheet_clickClose()
    }

    private fun interactWithFilterChip() {
        Thread.sleep(1000)
        filterChip_clickChip_withText("< 1GB")
    }

    companion object {
        const val PATH_ANALYTICS_OTP = "tracker/recharge/digital_product_detail/digital_pdp_dataplan_check_balance.json"
        const val PATH_ANALYTICS_RECOMMENDATION = "tracker/recharge/digital_product_detail/digital_pdp_dataplan_recommendation.json"
        const val PATH_ANALYTICS_FAVORITE = "tracker/recharge/digital_product_detail/digital_pdp_dataplan_favorite.json"
        const val PATH_ANALYTICS_AUTOCOMPLETE = "tracker/recharge/digital_product_detail/digital_pdp_dataplan_autocomplete.json"
        const val PATH_ANALYTICS_FILTER_BUY_WIDGET = "tracker/recharge/digital_product_detail/digital_pdp_dataplan_filter.json"
        const val PATH_ANALYTICS_INPUT_MANUAL = "tracker/recharge/digital_product_detail/digital_pdp_dataplan_input_manual.json"
    }
}

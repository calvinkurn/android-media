package com.tokopedia.digital_product_detail.dataplan

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalPDPDataPlanCassavaTest: BaseDigitalPDPDataPlanTest() {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

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
    fun validate_cassava() {
        Thread.sleep(2000)
        interactWithClientNumberWidget()
        interactWithFavoriteChip()
        interactWithAutoComplete()
        interactWithRecommendationWidget()
        interactWithGridDenomWidget()
        interactWithMccmWidget()
        interactWithFilterChip()
        interactWithBuyWidget()

        MatcherAssert.assertThat(
            cassavaTestRule.validate(PATH_ANALYTICS),
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

    private fun interactWithFavoriteChip() {
        Thread.sleep(2000)
        favoriteChips_clickChip_withText("Danur rrrr")

        Thread.sleep(2000)
        favoriteChips_clickChip_withText("08121111112")
    }

    private fun interactWithAutoComplete() {
        Thread.sleep(4000)
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_typeNumber("0812")
        autoComplete_clickItem_withText("Danur rrrr")
        Espresso.closeSoftKeyboard()

        Thread.sleep(4000)
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_typeNumber("0812")
        autoComplete_clickItem_withText("08121111112")
        Espresso.closeSoftKeyboard()
    }

    private fun interactWithRecommendationWidget() {
        Thread.sleep(2000)
        recommendations_clickCard()
    }

    private fun interactWithBuyWidget() {
        Thread.sleep(3000)
        buyWidget_clickChevron()
    }

    private fun interactWithMccmWidget() {
        Thread.sleep(2000)
        mccm_clickCard_withIndex(0)
        mccm_clickCardChevron_withIndex(0)

        Thread.sleep(1000)
        productDetailBottomSheet_clickClose()
    }

    private fun interactWithGridDenomWidget() {
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

    override fun getApplink(): String = APPLINK

    companion object {
        const val APPLINK = "tokopedia://digital/form?category_id=2&menu_id=290&template=paketdatav2"
        const val PATH_ANALYTICS = "tracker/recharge/digital_product_detail/digital_pdp_dataplan.json"
    }
}
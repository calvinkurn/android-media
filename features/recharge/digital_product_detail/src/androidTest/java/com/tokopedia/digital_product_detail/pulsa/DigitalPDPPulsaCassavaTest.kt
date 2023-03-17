package com.tokopedia.digital_product_detail.pulsa

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

class DigitalPDPPulsaCassavaTest: BaseDigitalPDPPulsaTest() {

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
        interactWithMccmWidget()
        interactWithGridDenomWidget()
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
        Thread.sleep(2000)
        buyWidget_clickChevron()
    }

    private fun interactWithMccmWidget() {
        Thread.sleep(2000)
        mccm_clickCard_withIndex(0)
    }

    private fun interactWithGridDenomWidget() {
        Thread.sleep(2000)
        denom_clickCard_withIndex(0)
    }

    override fun getApplink(): String = APPLINK

    companion object {
        const val APPLINK = "tokopedia://digital/form?category_id=1&menu_id=289&template=pulsav2"
        const val PATH_ANALYTICS = "tracker/recharge/digital_product_detail/digital_pdp_pulsa.json"
    }
}
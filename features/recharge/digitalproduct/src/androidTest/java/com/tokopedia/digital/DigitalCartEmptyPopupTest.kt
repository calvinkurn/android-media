package com.tokopedia.digital

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital.newcart.presentation.activity.DigitalCartActivity
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupRestMockResponse
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalCartEmptyPopupTest {
    @get:Rule
    var mActivityRule = ActivityTestRule(DigitalCartActivity::class.java, false, false)

    @Before
    fun stubAllIntent() {
        Intents.init()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun testCartActivateAutodebet() {
        //Setup intent cart page & launch activity
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        setupRestMockResponse {
            addMockResponse(DigitalQueries.KEY_QUERY_CART,
                    ResourceUtils.getJsonFromResource(PATH_RESPONSE_CART_POPUP),
                    MockModelConfig.FIND_BY_CONTAINS)
        }
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, DigitalCartActivity::class.java).apply {
            val passData = DigitalCheckoutPassData()
            passData.categoryId = "1"
            passData.clientNumber = "087855813456"
            passData.operatorId = "5"
            passData.productId = "30"
            passData.isPromo = "0"
            passData.instantCheckout = "0"
            passData.idemPotencyKey = "17211378_d44feedc9f7138c1fd91015d5bd88810"
            putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, passData)
        }.setData(
                Uri.parse("tokopedia-android-internal://digital/cart")
        )
        mActivityRule.launchActivity(intent)

        //check popup
        Espresso.onView(ViewMatchers.withId(R.id.tv_title_dialog)).check(ViewAssertions
                .matches(ViewMatchers.withText("Cara Update Saldo")))
        Espresso.onView(ViewMatchers.withId(R.id.tv_desc_dialog)).check(ViewAssertions
                .matches(ViewMatchers.withText("Pastikan nomor kartu benar & update saldo lewat " +
                        "aplikasi Tokopedia (khusus HP ber-NFC) atau gerai/ATM/Bank Mandiri terdekat.")))
        Espresso.onView(ViewMatchers.withText("Oke")).perform(ViewActions.click())

        //Info Cart Detail
        Thread.sleep(5000)
        Espresso.onView(ViewMatchers.withId((R.id.view_cart_detail))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId((R.id.view_checkout_holder))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //Select Promo Kupon
        val resultDataKupon = Intent()
        resultDataKupon.putExtra(EXTRA_PROMO_DATA, PromoData(PromoData.TYPE_COUPON, "DGELECTRO",
                "Anda akan mendapatkan kupon belanja elektronik hingga Rp 25.000",
                "Kode Promo: DGELECTRO", 0, TickerCheckoutView.State.ACTIVE))
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, resultDataKupon))
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.layoutUsePromo), ViewMatchers.isDisplayed())).perform(ViewActions.click())
        Thread.sleep(3000)
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.layoutTicker))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //Select Promo Voucher
        Thread.sleep(3000)
        val resultDataVoucher = Intent()
        resultDataVoucher.putExtra(EXTRA_PROMO_DATA, PromoData(PromoData.TYPE_VOUCHER, "BAYARTERUS",
                "Promo voucher discount", "Kode Voucher: BAYARTERUS", 10000,
                TickerCheckoutView.State.ACTIVE))
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, resultDataVoucher))
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.layoutTicker), ViewMatchers.isDisplayed())).perform(ViewActions.click())
        Thread.sleep(3000)
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.layoutTicker))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.tv_see_detail_toggle))).check(ViewAssertions
                .matches(ViewMatchers.withText("Tutup")))
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.tv_see_detail_toggle), ViewMatchers.isDisplayed())).perform(ViewActions.click())
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.tv_see_detail_toggle))).check(ViewAssertions
                .matches(ViewMatchers.withText("Lihat Detail")))

        //checkout product
        Espresso.onView(ViewMatchers.withId((R.id.btn_checkout))).perform(ViewActions.click())
        Thread.sleep(10000)
    }


    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        const val PATH_RESPONSE_CART_POPUP = "response_mock_data_cart_popup.json"
    }
}
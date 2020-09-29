package com.tokopedia.product.detail.analytics

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.product.detail.analytics.ProductDetailActivityTestUtil.performClose
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductDetailActivityTest {

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    private val context = InstrumentationRegistry.getInstrumentation().context

    @get:Rule
    var activityRule: IntentsTestRule<ProductDetailActivity> = object: IntentsTestRule<ProductDetailActivity>(ProductDetailActivity::class.java) {
        override fun getActivityIntent(): Intent {
            return Intent(ProductDetailActivity.createIntent(context, PRODUCT_ID))
        }
    }

    private lateinit var gtmLogDbSource: GtmLogDBSource

    @Before
    fun setup() {
        gtmLogDbSource = GtmLogDBSource(context)
        gtmLogDbSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(ProductDetailMockResponse())
    }

    // click button buy when user is login
    @Test
    fun validateClickBuyIsLogin() {

    }

    //click  button buy when user is non login
    @Test
    fun validateClickBuyIsNonLogin() {
        actionTest {
            intendingIntent()
            waitForData()
            fakeLogin()
            clickButtonBuy()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDbSource, context, ADD_TO_BASKET_NON_LOGIN_PATH)
        }
    }

    //click add to cart when user is login
    @Test
    fun validateClickAddToCartIsLogin() {

    }

    //click add to cart when user is non login
    @Test
    fun validateClickAddToCartIsNonLogin() {

    }

    //click choose variant level 1
    @Test
    fun validateClickChooseVariantLevel1() {

    }

    //click see guide on size chart
    @Test
    fun validateClickGuideOnSizeChart()  {

    }

    //impression variant notification
    @Test
    fun validateImpressionVariantNotification() {

    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun fakeLogin() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    companion object {
        const val PRODUCT_ID = "1061061424"
        const val ADD_TO_BASKET_LOGIN_PATH = "tracker/merchant/product_detail/pdp_add_to_basket_login.json"
        const val ADD_TO_BASKET_NON_LOGIN_PATH = "tracker/merchant/product_detail/pdp_add_to_basket_non_login.json"
        const val BUTTON_BUY_LOGIN_PATH = "tracker/merchant/product_detail/pdp_button_buy_login.json"
        const val BUTTON_BUY_NON_LOGIN_PATH = "tracker/merchant/product_detail/pdp_button_buy_non_login.json"
        const val CHOOSE_VARIANT_LEVEL1_PATH = "tracker/merchant/product_detail/pdp_choose_variant_level1.json"
        const val GUIDE_ON_SIZE_CHART_PATH = "tracker/merchant/product_detail/pdp_guide_on_size_chart.json"
    }
}
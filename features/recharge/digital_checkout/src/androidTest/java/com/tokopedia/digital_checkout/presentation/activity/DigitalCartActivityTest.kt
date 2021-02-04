package com.tokopedia.digital_checkout.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.digital_checkout.test.R
import com.tokopedia.digital_checkout.utils.DigitalQueries
import com.tokopedia.digital_checkout.utils.ResourceUtils
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupRestMockResponse
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by jessica on 03/02/21
 */
class DigitalCartActivityTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(DigitalCartActivity::class.java,
            false, false)

    @Before
    fun stubAllIntent() {
        Intents.init()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK,
                null))
    }

    @Test
    fun testCartEgold() {
        //Setup intent cart page & launch activity
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        setupRestMockResponse {
            addMockResponse(DigitalQueries.KEY_QUERY_CART,
                    ResourceUtils.getJsonFromResource(PATH_RESPONSE_ADD_TO_CART),
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

        //Info Cart Detail
        Thread.sleep(5000)
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        const val PATH_RESPONSE_ADD_TO_CART = "response_mock_add_to_cart.json"
    }

}
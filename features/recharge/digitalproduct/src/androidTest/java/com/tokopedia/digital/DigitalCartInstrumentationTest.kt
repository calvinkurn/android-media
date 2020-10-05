package com.tokopedia.digital

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital.newcart.presentation.activity.DigitalCartActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupRestMockResponse
import org.junit.Rule
import org.junit.Test

class DigitalCartInstrumentationTest {
    @get:Rule
    var mActivityRule: IntentsTestRule<DigitalCartActivity> = object : IntentsTestRule<DigitalCartActivity>(DigitalCartActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(targetContext, DigitalCartActivity::class.java).apply {
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
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupRestMockResponse(DigitalCartMockResponseConfig())
        }
    }

    @Test
    fun testHomeLayout() {
        InstrumentationAuthHelper.clearUserSession()
        Thread.sleep(5000)
        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        Thread.sleep(10000)
    }
}
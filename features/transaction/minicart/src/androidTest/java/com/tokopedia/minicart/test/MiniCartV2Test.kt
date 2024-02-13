package com.tokopedia.minicart.test

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.minicart.MiniCartTestActivity
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.domain.GetMiniCartParam
import com.tokopedia.minicart.interceptor.MiniCartInterceptors
import com.tokopedia.minicart.robot.miniCartWidget
import com.tokopedia.minicart.v2.MiniCartV2Widget
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.minicart.test.R as minicarttestR

@UiTest
class MiniCartV2Test {

    @get:Rule
    var activityRule =
        object :
            IntentsTestRule<MiniCartTestActivity>(MiniCartTestActivity::class.java, false, false) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                InstrumentationAuthHelper.loginInstrumentationTestUser1()
            }
        }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        MiniCartInterceptors.setupGraphqlMockResponse(context)
        MiniCartInterceptors.resetAllCustomResponse()
    }

    @Test
    fun happyFlowTest_gwpWithGift() {
        activityRule.launchActivity(null)

        val widget =
            activityRule.activity.findViewById<MiniCartV2Widget>(minicarttestR.id.mini_cart_widget)

        miniCartWidget(widget) {
            widget.refresh(GetMiniCartParam(listOf("1"), MiniCartSource.TokonowHome.value))
            Thread.sleep(2000)
            assertWidgetVisible()
            assertWidgetTotalAmount("Rp39.400")
            assertButtonEnabled(true)
        }

        Thread.sleep(2000)
    }

    @After
    fun tearDown() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }
}

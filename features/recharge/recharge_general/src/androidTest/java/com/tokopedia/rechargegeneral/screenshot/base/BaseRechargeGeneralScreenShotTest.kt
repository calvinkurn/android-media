package com.tokopedia.rechargegeneral.screenshot.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions.findViewAndScreenShot
import com.tokopedia.test.application.espresso_component.CommonActions.screenShotFullRecyclerView
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupDarkModeTest
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.StringBuilder

abstract class BaseRechargeGeneralScreenShotTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(RechargeGeneralActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        setupDarkModeTest(forceDarkMode())
        setupGraphqlMockResponse(getMockConfig())

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, RechargeGeneralActivity::class.java).apply {
            putExtra(RechargeGeneralActivity.PARAM_MENU_ID, getMenuId())
            putExtra(RechargeGeneralActivity.PARAM_CATEGORY_ID, getCategoryId())
        }
        mActivityRule.launchActivity(intent)
        instrumentAuthLogin()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    @Test
    fun screenshot() {
        Thread.sleep(3000)

        // ss visible screen
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, generatePrefix(), "visible_screen_pdp")
        }

        // ss full layout
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val test = mActivityRule.activity.findViewById<SwipeToRefresh>(R.id.recharge_general_swipe_refresh_layout)
            takeScreenShotVisibleViewInScreen(test, generatePrefix(), "swipe_to_refresh")
        }

        // ss operator select
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val operatorView = mActivityRule.activity.findViewById<TopupBillsInputFieldWidget>(R.id.operator_select)
            takeScreenShotVisibleViewInScreen(operatorView, generatePrefix(), "operator_select")
        }

        Thread.sleep(3000)

        // ss recyclerview product
        screenShotFullRecyclerView(
                R.id.rv_digital_product,
                0,
                getRecyclerViewItemCount(R.id.rv_digital_product),
                "${generatePrefix()}-rv_digital_product")

        run_specific_product_test()
        see_promo()
    }

    private fun see_promo() {
        findViewAndScreenShot(R.id.product_view_pager, generatePrefix(), "view_pager")
        if (isLogin())
            findViewAndScreenShot(R.id.tab_layout, generatePrefix(), "tab_layout")
    }

    private fun instrumentAuthLogin() {
        if (isLogin()) InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    abstract fun run_specific_product_test()

    abstract fun getMockConfig(): MockModelConfig

    abstract fun getMenuId(): Int

    abstract fun getCategoryId(): Int

    abstract fun forceDarkMode(): Boolean

    abstract fun isLogin(): Boolean

    abstract fun filePrefix(): String

    protected fun generatePrefix(): String {
        val prefix = StringBuilder()
        prefix.append(filePrefix())
        prefix.append( when (isLogin()) {
            true -> "-login"
            false -> "-nonlogin"
        })
        prefix.append( when (forceDarkMode()) {
            true -> "-dark"
            false -> "-light"
        })
        return prefix.toString()
    }

    protected fun getRecyclerViewItemCount(resId: Int): Int {
        val recyclerView = mActivityRule.activity.findViewById<RecyclerView>(resId)
        return recyclerView?.adapter?.itemCount ?: 0
    }
}
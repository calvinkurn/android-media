package com.tokopedia.rechargegeneral.screenshot

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.platform.util.TestOutputEmitter.takeScreenshot
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.RechargeGeneralMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralFragment
import com.tokopedia.test.application.espresso_component.CommonActions.findViewAndScreenShot
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import org.junit.Rule
import org.junit.Test

class RechargeGeneralScreenShotTest {

    @get:Rule
    var mActivityRule: IntentsTestRule<RechargeGeneralActivity> = object : IntentsTestRule<RechargeGeneralActivity>(RechargeGeneralActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(targetContext, RechargeGeneralActivity::class.java).apply {
                putExtra(RechargeGeneralActivity.PARAM_MENU_ID, 113)
                putExtra(RechargeGeneralActivity.PARAM_CATEGORY_ID, 3)
            }
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(RechargeGeneralMockResponseConfig(RechargeGeneralProduct.LISTRIK))
        }
    }

    @Test
    fun screenshot() {
        val test = mActivityRule.activity.findViewById<SwipeToRefresh>(R.id.recharge_general_swipe_refresh_layout)
        takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, filePrefix(), "visible_screen_pdp")
        takeScreenShotVisibleViewInScreen(test, filePrefix(), "swipe_to_refresh")
        takeScreenshot("test.png")

        findViewAndScreenShot(R.id.product_view_pager, filePrefix(), "view_pager")

        onView(withId(R.id.operator_select)).perform(ViewActions.click())
        takeScreenShotVisibleViewInScreen(mActivityRule.activity.window.decorView, filePrefix(), "visible_screen_operator")

    }

//    private fun getPositionViewHolderByName(name: String): Int {
//        val fragment = mActivityRule.activity.supportFragmentManager.findFragmentByTag(PRODUCT_DETAIL_TAG) as RechargeGeneralFragment
//        return fragment.productAdapter?.currentList?.indexOfFirst {
//            it.name() == name
//        } ?: 0
//    }

    private fun filePrefix() = "recharge_general_"

}
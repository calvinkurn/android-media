package com.tokopedia.rechargegeneral.screenshot

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.internal.platform.util.TestOutputEmitter.takeScreenshot
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.rechargegeneral.RechargeGeneralMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.presentation.activity.RechargeGeneralActivity
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
        takeScreenshot("general_template.png")
    }

}
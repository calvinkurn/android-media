package com.tokopedia.product.manage.nonCassava

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.cassava.robot.actionTest
import org.junit.Test

class ProductManageTest : ProductManageTestFixture() {


    @Test
    fun validateShowStockInformationSuspend() {
        actionTest {
           isDisplayProductOnView(R.id.imageStockInformation,1)
            clickProductCardOnView(R.id.imageStockInformation,1)
        }
    }

    @Test
    fun validateEditStockInformationSuspendLevelOne() {
        actionTest {
            isDisplayProductOnView(R.id.imageStockInformation,0)
            clickEditStockButton(activityRule.activity)
            onView(withId(R.id.quickEditStockActivateSwitch)).check(matches(isNotChecked()))
        }
    }

    @Test
    fun validateShowButtonDetailWhenSuspendTwoUntilFour() {
        actionTest {
            isDisplayProductOnViewWithText(R.id.btnContactCS,1,
                activityRule.activity
                    .getString(R.string.product_manage_violation_or_suspend_button_text))

        }
    }

    @Test
    fun validateShowTickerWhenSuspendTwoUntilFour() {
        actionTest {
            isDisplayProductOnView(R.id.ticker_product_manage_violation,1)
        }
    }

    @Test
    fun validateShowInfoDetailWhenSuspendLevelTwoUntilFour() {
        actionTest {
            clickProductCardOnView(R.id.btnContactCS,1)
            onView(withId(R.id.tv_product_manage_suspend_title)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_product_manage_suspend_reason)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_product_manage_suspend_info_impact)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_product_manage_suspend_step_title)).check(matches(isDisplayed()))
            onView(withId(R.id.rv_product_manage_suspend_step)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_product_manage_suspend_info_foot_note)).check(matches(isDisplayed()))
        }
    }
}

package com.tokopedia.product.detail.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.ui.base.BaseProductDetailUiTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf
import org.junit.Test

/**
 * Created by Yehezkiel on 08/04/21
 */
class ProductDetailButtonTest : BaseProductDetailUiTest() {

    @Test
    fun sticky_login_non_login() {
        userSessionMock.setIsLogin(false)
        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.btn_pdp_container), ViewMatchers.isDisplayed()))
        onView(AllOf.allOf(withId(R.id.sticky_login_pdp), ViewMatchers.isDisplayed()))
    }

    @Test
    fun sticky_login_shows_login() {
        userSessionMock.setIsLogin(true)
        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.btn_pdp_container), ViewMatchers.isDisplayed()))
        onView(AllOf.allOf(withId(R.id.sticky_login_pdp), not(ViewMatchers.isDisplayed())))
    }

}
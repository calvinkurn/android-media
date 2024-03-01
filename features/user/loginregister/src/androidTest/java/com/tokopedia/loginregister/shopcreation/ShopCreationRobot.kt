package com.tokopedia.loginregister.shopcreation

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.loginregister.R

class ShopCreationRobot {
    fun clickAtOpenShop() {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btn_continue))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
    }
}

class ShopCreationResult {

}

fun shopCreationRobot(func: ShopCreationRobot.() -> Unit): ShopCreationRobot {
    return ShopCreationRobot().apply(func)
}

infix fun ShopCreationRobot.validate(func: ShopCreationResult.() -> Unit): ShopCreationResult {
    return ShopCreationResult().apply(func)
}


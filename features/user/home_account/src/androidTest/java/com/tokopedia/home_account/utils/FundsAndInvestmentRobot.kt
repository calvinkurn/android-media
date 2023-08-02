package com.tokopedia.home_account.utils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText

class FundsAndInvestmentRobot {

    init {
        Thread.sleep(1000)
    }

    fun displayText(text: String) {
        onView(withText(text)).check(matches(isDisplayed()))
    }

    fun back() {
        pressBack()
    }
}

fun fundsAndInvestmentRobot(func: FundsAndInvestmentRobot.() -> Unit) = FundsAndInvestmentRobot().apply(func)

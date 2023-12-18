package com.tokopedia.home_account.utils

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso.pressBack
import com.tokopedia.home_account.main.HomeAccountUiTest

class FundsAndInvestmentRobot(private val rule: ComposeTestRule) {

    init {
        Thread.sleep(1000)
    }

    fun displayText(text: String) {
        rule.onNodeWithText(text).assertIsDisplayed()
    }

    fun back() {
        pressBack()
    }
}

fun HomeAccountUiTest.fundsAndInvestmentRobot(func: FundsAndInvestmentRobot.() -> Unit) =
    FundsAndInvestmentRobot(composeTestRule).apply(func)

package com.tokopedia.home_account.utils

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso
import com.tokopedia.home_account.main.HomeAccountUiTest

class KeamananAkunRobot(private val rule: ComposeTestRule) {

    init {
        Thread.sleep(1000)
    }

    fun assertKeamananAkunPage() {
        rule.onNodeWithText("Keamanan Akun").assertIsDisplayed()
    }

    fun back() {
        Espresso.pressBack()
    }
}

fun HomeAccountUiTest.keamananAkunRobot(func: KeamananAkunRobot.() -> Unit) =
    KeamananAkunRobot(composeTestRule).apply(func)

package com.tokopedia.home_account.utils

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText

class KeamananAkunRobot {

    init {
        Thread.sleep(1000)
    }

    fun assertKeamananAkunPage() {
        onView(withText("Keamanan Akun")).check(matches(isDisplayed()))
    }

    fun back() {
        Espresso.pressBack()
    }
}

fun keamananAkunRobot(func: KeamananAkunRobot.() -> Unit) = KeamananAkunRobot().apply(func)

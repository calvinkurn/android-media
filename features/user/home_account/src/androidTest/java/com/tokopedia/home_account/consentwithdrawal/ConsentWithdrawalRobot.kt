package com.tokopedia.home_account.consentwithdrawal

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.home_account.common.ViewActionUtils.waitOnView
import com.tokopedia.home_account.test.R
import org.hamcrest.CoreMatchers.allOf

fun consentWithdrawalRobot(
    action: ConsentWithdrawalRobot.() -> Unit
): ConsentWithdrawalRobot {
    return ConsentWithdrawalRobot().apply(action)
}

infix fun ConsentWithdrawalRobot.validateUi(
    action: ConsentWithdrawalUiValidation.() -> Unit
): ConsentWithdrawalUiValidation {
    Thread.sleep(1000)
    return ConsentWithdrawalUiValidation().apply(action)
}

class ConsentWithdrawalRobot {
    fun clickOnMandatoryPurpose() {
        waitOnView(
            allOf(
            withId(R.id.itemButtonLayout),
            isDisplayed()
        )
        ).perform(click())
    }

    fun clickOnOptionalPurpose() {
        waitOnView(
            allOf(
            withId(R.id.itemSwitch),
            isDisplayed()
        )
        ).perform(click())
    }

    fun clickAgreeOnPopup() {
        waitOnView(
            allOf(
            withText("Ya, Matikan"),
            isDisplayed()
        )
        ).perform(click())
    }
}

class ConsentWithdrawalUiValidation {
    fun shouldViewMandatoryPurposeSection() {
        waitOnView(
            allOf(
            withId(R.id.itemTitle),
            withText("Penggunaan data utama"),
            isDisplayed()
        )
        )
    }

    fun shouldViewOptionalPurposeSection() {
        waitOnView(
            allOf(
            withId(R.id.itemTitle),
            withText("Penggunaan data tambahan"),
            isDisplayed()
        )
        )
    }

    fun shouldViewPopupConfirmation() {
        waitOnView(
            allOf(
            withId(com.tokopedia.dialog.R.id.dialog_title),
            withText("Matikan penggunaan datamu?"),
            isDisplayed()
        )
        )
    }

    private fun isSwitchToggleActive(isActive: Boolean) {
        waitOnView(
            allOf(
            withId(R.id.itemSwitch),
            isDisplayed()
        )
        ).check(matches(if (isActive) isNotChecked() else isChecked()))
    }

    fun shouldToggleActive() {
        isSwitchToggleActive(true)
    }

    fun shouldToggleInactive() {
        isSwitchToggleActive(false)
    }
}

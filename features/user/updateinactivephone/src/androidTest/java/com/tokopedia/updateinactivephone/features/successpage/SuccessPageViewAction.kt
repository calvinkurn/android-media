package com.tokopedia.updateinactivephone.features.successpage

import com.tokopedia.updateinactivephone.common.viewaction.clickOnButton
import com.tokopedia.updateinactivephone.common.viewaction.isDisplayed
import com.tokopedia.updateinactivephone.common.viewaction.isTextDisplayed
import com.tokopedia.updateinactivephone.test.R

object SuccessPageViewAction {

    fun checkSuccessPageIsDisplayed() {
        isDisplayed(R.id.txtTitle)
    }

    fun isTickerDisplayed() {
        isDisplayed(R.id.tickerInactivePhoneNumber)
    }

    fun isDescriptionForEmailOnly(text: String) {
        isDisplayed(R.id.textDescription)
        isTextDisplayed(R.id.textDescription, text)
    }

    fun isDescriptionForEmailAndPhoneNumber(text: String) {
        isDisplayed(R.id.textDescription)
        isTextDisplayed(R.id.textDescription, text)
    }

    fun clickOnHomeButton() {
        clickOnButton(R.id.btnGotoHome)
    }
}
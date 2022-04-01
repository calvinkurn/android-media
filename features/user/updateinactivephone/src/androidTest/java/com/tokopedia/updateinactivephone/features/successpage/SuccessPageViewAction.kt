package com.tokopedia.updateinactivephone.features.successpage

import com.tokopedia.updateinactivephone.common.viewaction.clickOnButton
import com.tokopedia.updateinactivephone.common.viewaction.isDisplayed
import com.tokopedia.updateinactivephone.common.viewaction.isTextDisplayed
import com.tokopedia.updateinactivephone.common.viewaction.scrollToView
import com.tokopedia.updateinactivephone.test.R

object SuccessPageViewAction {

    fun checkSuccessPageIsDisplayed() {
        isDisplayed(R.id.imgHeader)
        isDisplayed(R.id.text_title)
        scrollToView(R.id.btnGotoHome)
        isDisplayed(R.id.btnGotoHome)
    }

    fun isTickerDisplayed() {
        scrollToView(R.id.tickerInactivePhoneNumber)
        isDisplayed(R.id.tickerInactivePhoneNumber)
    }

    fun checkTickerContent(text: String) {
        scrollToView(R.id.tickerInactivePhoneNumber)
        isTextDisplayed(text)
    }

    fun isDescriptionForEmailOnly(text: String) {
        scrollToView(R.id.textDescription)
        isDisplayed(R.id.textDescription)
        isTextDisplayed(text)
    }

    fun isDescriptionForEmailAndPhoneNumber(text: String) {
        scrollToView(R.id.textDescription)
        isDisplayed(R.id.textDescription)
        isTextDisplayed(text)
    }

    fun isTitleForExpedited(text: String) {
        scrollToView(R.id.text_title)
        isDisplayed(R.id.text_title)
        isTextDisplayed(text)
    }

    fun isDescriptionForExpedited(text: String) {
        scrollToView(R.id.textDescription)
        isDisplayed(R.id.textDescription)
        isTextDisplayed(text)
    }

    fun clickOnGotoHomeButton() {
        scrollToView(R.id.btnGotoHome)
        clickOnButton(R.id.btnGotoHome)
    }
}
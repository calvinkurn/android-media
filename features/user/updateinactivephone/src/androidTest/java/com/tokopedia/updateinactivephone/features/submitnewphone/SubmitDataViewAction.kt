package com.tokopedia.updateinactivephone.features.submitnewphone

import com.tokopedia.updateinactivephone.common.viewaction.isDisplayed
import com.tokopedia.updateinactivephone.common.viewaction.scrollToView
import com.tokopedia.updateinactivephone.test.R

object SubmitDataViewAction {

    fun checkSubmitDataPageDisplayed() {
        isDisplayed(R.id.text_title)
        isDisplayed(R.id.txtInputPhoneNumber)
        scrollToView(R.id.button_next)
        isDisplayed(R.id.button_next)
    }
}
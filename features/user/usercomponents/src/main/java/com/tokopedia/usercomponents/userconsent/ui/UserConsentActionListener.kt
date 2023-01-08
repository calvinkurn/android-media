package com.tokopedia.usercomponents.userconsent.ui

import com.tokopedia.usercomponents.userconsent.common.UserConsentPayload

interface UserConsentActionListener {
    fun onCheckedChange(isChecked: Boolean)
    fun onActionClicked(payload: UserConsentPayload, isDefaultTemplate: Boolean = false)
    fun onFailed(throwable: Throwable)
}
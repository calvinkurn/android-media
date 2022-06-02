package com.tokopedia.usercomponents.userconsent.ui

import com.tokopedia.usercomponents.userconsent.common.UserConsentPayload

interface UserConsentActionClickListener {
    fun onChecklistClicked(isChecked: Boolean)
    fun onActionClicked(payload: UserConsentPayload)
    fun onFailed(throwable: Throwable)
}
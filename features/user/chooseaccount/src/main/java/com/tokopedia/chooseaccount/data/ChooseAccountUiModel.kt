package com.tokopedia.chooseaccount.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author by nisie on 12/5/17.
 */

@Parcelize
data class ChooseAccountUiModel (
        var phoneNumber: String = "",
        var accessToken: String = "",
        var loginType: String = "",
        var isFromRegister: Boolean = false,
        var key: String = "",
        var selectedEmail: String = ""
): Parcelable
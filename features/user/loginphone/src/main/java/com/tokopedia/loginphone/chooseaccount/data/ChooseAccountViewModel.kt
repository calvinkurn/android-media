package com.tokopedia.loginphone.chooseaccount.data

/**
 * @author by nisie on 12/5/17.
 */
object ChooseAccountViewModel {
        var phoneNumber: String? = ""
            get() = phoneNumber?.replace("-", "")
        var accessToken: String? = ""
        var loginType: String? = ""
        var accountList: AccountList? = AccountList()
        var isFromRegister: Boolean = false
        var isFacebook: Boolean = false
}
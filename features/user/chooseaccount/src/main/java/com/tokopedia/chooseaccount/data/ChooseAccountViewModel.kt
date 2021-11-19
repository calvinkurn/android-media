package com.tokopedia.chooseaccount.data

/**
 * @author by nisie on 12/5/17.
 */

class ChooseAccountViewModel {
        var phoneNumber: String? = ""
                get() = field?.replace("-", "")
        var accessToken: String? = ""
        var loginType: String? = ""
        var accountListDataModel: AccountListDataModel? = AccountListDataModel()
        var isFromRegister: Boolean = false
}
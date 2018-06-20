package com.tokopedia.settingbank.data

/**
 * @author by nisie on 6/8/18.
 */
class SettingBankUrl {


    companion object {

        var BASE_URL: String = "https://ws.tokopedia.com/"
        const val PATH_GET_BANK_ACCOUNT: String = "v4/people/get_bank_account.pl"
        const val PATH_SET_DEFAULT_BANK_ACCOUNT: String = "v4/action/people/edit_default_bank_account.pl"
    }

}
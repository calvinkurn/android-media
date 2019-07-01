package com.tokopedia.settingbank.banklist.data

import com.tokopedia.config.url.TokopediaUrl

/**
 * @author by nisie on 6/8/18.
 */
class SettingBankUrl {


    companion object {

        var BASE_URL: String = TokopediaUrl.getInstance().ACCOUNTS
        const val PATH_GET_BANK_ACCOUNT: String = "api/v2/bank-account/get-all"
        const val PATH_SET_DEFAULT_BANK_ACCOUNT: String = "api/v2/bank-account/set-primary"
        const val PATH_DELETE_BANK_ACCOUNT: String = "api/v2/bank-account/delete"

        const val IMAGE_EMPTY_BANK_LIST: String = "https://ecs7.tokopedia.net/img/android/others/page_1.png"
        const val IMAGE_BOTTOM_DIALOG_ADD_ACCOUNT: String = "https://ecs7.tokopedia.net/img/android/others/Passbook@2x.jpg"

    }

}
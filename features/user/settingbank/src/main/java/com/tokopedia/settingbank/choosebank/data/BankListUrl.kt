package com.tokopedia.settingbank.choosebank.data

import com.tokopedia.url.TokopediaUrl

/**
 * @author by nisie on 7/2/18.
 */

class BankListUrl {

    companion object {

        var BASE_URL: String = com.tokopedia.url.TokopediaUrl.getInstance().ACCOUNTS
        const val PATH_SEARCH_BANK_ACCOUNT: String = "api/v2/bank-account/get-bank-list"

    }
}
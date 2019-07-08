package com.tokopedia.settingbank.addeditaccount.data

import com.tokopedia.url.TokopediaUrl

/**
 * Created by Ade Fulki on 2019-05-16.
 * ade.hadian@tokopedia.com
 */

object AddEditAccountUrl{
    @JvmField
    var BASE_URL: String = TokopediaUrl.getInstance().ACCOUNTS

    const val PATH_ADD_BANK_ACCOUNT: String = "api/v2/bank-account/add"
    const val PATH_EDIT_BANK_ACCOUNT: String = "api/v2/bank-account/edit"
    const val PATH_VALIDATE_BANK_ACCOUNT: String = "api/v2/bank-account/validate"
}
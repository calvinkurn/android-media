package com.tokopedia.settingbank.addeditaccount.data

/**
 * Created by Ade Fulki on 2019-05-16.
 * ade.hadian@tokopedia.com
 */

class AddEditAccountUrl{
    companion object {
        const val BASE_URL: String = "https://accounts.tokopedia.com/"

        const val PATH_ADD_BANK_ACCOUNT: String = "api/v2/bank-account/add"
        const val PATH_EDIT_BANK_ACCOUNT: String = "api/v2/bank-account/edit"
        const val PATH_VALIDATE_BANK_ACCOUNT: String = "api/v2/bank-account/validate"
    }
}
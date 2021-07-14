package com.tokopedia.loginphone.chooseaccount.view.listener

import com.tokopedia.loginphone.chooseaccount.data.UserDetailDataModel

/**
 * @author by nisie on 12/4/17.
 */
interface ChooseAccountListener {
    fun onSelectedAccount(account: UserDetailDataModel, phone: String)
}
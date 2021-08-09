package com.tokopedia.chooseaccount.view.listener

import com.tokopedia.chooseaccount.data.UserDetailDataModel

/**
 * @author by nisie on 12/4/17.
 */
interface ChooseAccountListener {
    fun onSelectedAccount(account: UserDetailDataModel, phone: String)
}
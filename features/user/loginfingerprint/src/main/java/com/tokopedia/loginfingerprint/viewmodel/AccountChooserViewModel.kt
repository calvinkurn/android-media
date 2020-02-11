package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.loginfingerprint.data.model.AccountPojo
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-01-29.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AccountChooserViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                  userSession: UserSessionInterface)
    : BaseViewModel(dispatcher) {

    private val mutableLatestAccount = MutableLiveData(AccountPojo(userSession.email, userSession.name, userSession.profilePicture))
    val latestAccount: LiveData<AccountPojo>
        get() = mutableLatestAccount

//    private val mutableLatestAccount = MutableLiveData(ListItemUnify("",""))
//    val latestAccount: LiveData<AccountPojo>
//        get() = mutableLatestAccount

}
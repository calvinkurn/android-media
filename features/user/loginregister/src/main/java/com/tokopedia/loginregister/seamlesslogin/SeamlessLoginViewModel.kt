package com.tokopedia.loginregister.seamlesslogin

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-03-04.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SeamlessLoginViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                 dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {

}
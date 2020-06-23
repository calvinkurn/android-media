package com.tokopedia.loginregister.seamlesslogin

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.loginregister.seamlesslogin.data.model.GenerateKeyData
import com.tokopedia.loginregister.seamlesslogin.usecase.GenerateKeyUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-03-04.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SeamlessLoginViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                 private val generateKeyUseCase: GenerateKeyUseCase,
                                                 dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {
    fun getKey(onSuccess: (GenerateKeyData) -> kotlin.Unit, onError: (Throwable) -> kotlin.Unit) {
        generateKeyUseCase.executeCoroutines(onSuccess, onError)
    }
}
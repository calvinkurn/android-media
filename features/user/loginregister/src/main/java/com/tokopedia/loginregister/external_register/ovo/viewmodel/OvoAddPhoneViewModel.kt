package com.tokopedia.loginregister.external_register.ovo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.ovo.data.ActivateOvoResponse
import com.tokopedia.loginregister.external_register.ovo.data.CheckOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.CheckHasOvoAccUseCase
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 04/01/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */
class OvoAddPhoneViewModel @Inject constructor(
        private val checkHasOvoUseCase: CheckHasOvoAccUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val mutableCheckOvoResponse = MutableLiveData<Result<CheckOvoResponse>>()
    val checkOvoResponse: LiveData<Result<CheckOvoResponse>>
        get() = mutableCheckOvoResponse

    fun checkOvo(phoneNumber: String){
        launchCatchError(block = {
            checkHasOvoUseCase.setParams(phoneNumber = phoneNumber)
            checkHasOvoUseCase.executeOnBackground().run {
                mutableCheckOvoResponse.postValue(Success(this))
            }
        }, onError = {
            mutableCheckOvoResponse.postValue(Fail(it))
        })
    }

}
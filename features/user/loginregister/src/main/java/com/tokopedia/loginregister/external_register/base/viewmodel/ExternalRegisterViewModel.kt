package com.tokopedia.loginregister.external_register.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.base.domain.usecase.ExternalRegisterUseCase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestData
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 05/01/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */
class ExternalRegisterViewModel @Inject constructor(
        private val externalRegisterUseCase: ExternalRegisterUseCase,
        private val externalRegisterPreference: ExternalRegisterPreference,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val mutableRegisterRequestResponse = MutableLiveData<Result<RegisterRequestData>>()
    val registerRequestResponse: LiveData<Result<RegisterRequestData>>
    get() = mutableRegisterRequestResponse

    fun register(authCode: String){

        launchCatchError(block = {
            externalRegisterUseCase.setParams(
                phoneNumber = externalRegisterPreference.getPhone(),
                name = externalRegisterPreference.getName(),
                goalKey = externalRegisterPreference.getGoalKey(),
                authCode = authCode
            )

            externalRegisterUseCase.executeOnBackground().run {
                mutableRegisterRequestResponse.postValue(Success(data))
            }
        }, onError = {
            mutableRegisterRequestResponse.postValue(Fail(it))
        })
    }
}
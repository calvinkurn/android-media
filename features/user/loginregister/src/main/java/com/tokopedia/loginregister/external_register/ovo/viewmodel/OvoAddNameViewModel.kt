package com.tokopedia.loginregister.external_register.ovo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.ovo.data.ActivateOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.ActivateOvoUseCase
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 25/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

open class OvoAddNameViewModel @Inject constructor(
        private val activateOvoUseCase: ActivateOvoUseCase,
        private val externalRegisterPreference: ExternalRegisterPreference,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val mutableActivateOvoResponse = MutableLiveData<Result<ActivateOvoResponse>>()
    val activateOvoResponse: LiveData<Result<ActivateOvoResponse>>
        get() = mutableActivateOvoResponse

    fun activateOvo(phoneNumber: String, name: String){
        launchCatchError(block = {
            activateOvoUseCase.setParams(phoneNumber = phoneNumber, name = name, clientId = ExternalRegisterConstants.KEY.CLIENT_ID)
            activateOvoUseCase.executeOnBackground().run {
                externalRegisterPreference.saveGoalKey(this.activateOvoData.goalKey)
                externalRegisterPreference.saveName(name)
                externalRegisterPreference.savePhone(phoneNumber)
                mutableActivateOvoResponse.postValue(Success(this))
            }
        }, onError = {
            mutableActivateOvoResponse.postValue(Fail(it))
        })
    }
}
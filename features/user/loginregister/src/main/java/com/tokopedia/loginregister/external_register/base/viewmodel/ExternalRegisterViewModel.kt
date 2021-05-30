package com.tokopedia.loginregister.external_register.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.base.domain.usecase.ExternalRegisterUseCase
import com.tokopedia.loginregister.registerinitial.domain.data.ProfileInfoData
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Yoris Prayogo on 05/01/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class ExternalRegisterViewModel @Inject constructor(
        private val externalRegisterUseCase: ExternalRegisterUseCase,
        private val externalRegisterPreference: ExternalRegisterPreference,
        private val getProfileUseCase: GetProfileUseCase,
        @Named(SessionModule.SESSION_MODULE)
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val mutableRegisterRequestResponse = MutableLiveData<Result<RegisterPojo>>()
    val registerRequestResponse: LiveData<Result<RegisterPojo>>
    get() = mutableRegisterRequestResponse

    private val mutableGetUserInfoResponse = MutableLiveData<Result<ProfileInfoData>>()
    val getUserInfoResponse: LiveData<Result<ProfileInfoData>>
        get() = mutableGetUserInfoResponse

    fun register(authCode: String){
        launchCatchError(block = {
            userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
            externalRegisterUseCase.setParams(
                phoneNumber = externalRegisterPreference.getPhone(),
                name = externalRegisterPreference.getName(),
                goalKey = externalRegisterPreference.getGoalKey(),
                authCode = authCode
            )
            externalRegisterUseCase.executeOnBackground().run {
                if (register.accessToken.isNotEmpty() &&
                        register.refreshToken.isNotEmpty()){
                    userSession.clearToken()
                    mutableRegisterRequestResponse.value = Success(this)
                } else if (register.errors.isNotEmpty() && register.errors[0].message.isNotEmpty()) {
                    mutableRegisterRequestResponse.value =
                            Fail(com.tokopedia.network.exception.MessageErrorException(register.errors[0].message))
                } else mutableRegisterRequestResponse.value = Fail(RuntimeException())
            }
        }, onError = {
            mutableRegisterRequestResponse.postValue(Fail(it))
        })
    }

    fun getUserInfo() {
        getProfileUseCase.execute(GetProfileSubscriber(userSession,
                onSuccessGetUserInfo(),
                onFailedGetUserInfo()))
    }

    private fun onSuccessGetUserInfo(): (ProfilePojo) -> Unit {
        return {
            mutableGetUserInfoResponse.value = Success(ProfileInfoData(it.profileInfo))
        }
    }

    private fun onFailedGetUserInfo(): (Throwable) -> Unit {
        return {
            mutableGetUserInfoResponse.value = Fail(it)
        }
    }

}
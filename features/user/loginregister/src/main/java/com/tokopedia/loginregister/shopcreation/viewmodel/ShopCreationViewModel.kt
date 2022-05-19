package com.tokopedia.loginregister.shopcreation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.shopcreation.domain.param.RegisterCheckParam
import com.tokopedia.loginregister.shopcreation.domain.param.ShopInfoParam
import com.tokopedia.loginregister.shopcreation.domain.param.UpdateUserProfileParam
import com.tokopedia.loginregister.shopcreation.domain.param.ValidateUserProfileParam
import com.tokopedia.loginregister.shopcreation.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.shopcreation.domain.pojo.ShopInfoByID
import com.tokopedia.loginregister.shopcreation.domain.pojo.UserProfileCompletionData
import com.tokopedia.loginregister.shopcreation.domain.pojo.UserProfileUpdate
import com.tokopedia.loginregister.shopcreation.domain.pojo.UserProfileValidate
import com.tokopedia.loginregister.shopcreation.domain.usecase.GetUserProfileCompletionUseCase
import com.tokopedia.loginregister.shopcreation.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.shopcreation.domain.usecase.ShopInfoUseCase
import com.tokopedia.loginregister.shopcreation.domain.usecase.UpdateUserProfileUseCase
import com.tokopedia.loginregister.shopcreation.domain.usecase.ValidateUserProfileUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import com.tokopedia.shopadmin.common.domain.usecase.GetAdminTypeUseCaseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-12-10.
 * ade.hadian@tokopedia.com
 */

open class ShopCreationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val registerCheckUseCase: RegisterCheckUseCase,
    private val getUserProfileCompletionUseCase: GetUserProfileCompletionUseCase,
    private val validateUserProfileUseCase: ValidateUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val shopInfoUseCase: ShopInfoUseCase,
    private val getAdminTypeUseCaseCase: GetAdminTypeUseCaseCase,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _addNameResponse = MutableLiveData<Result<UserProfileUpdate>>()
    val addNameResponse: LiveData<Result<UserProfileUpdate>>
        get() = _addNameResponse

    private val _addPhoneResponse = MutableLiveData<Result<UserProfileUpdate>>()
    val addPhoneResponse: LiveData<Result<UserProfileUpdate>>
        get() = _addPhoneResponse

    private val _getUserProfileResponse = MutableLiveData<Result<UserProfileCompletionData>>()
    val getUserProfileResponse: LiveData<Result<UserProfileCompletionData>>
        get() = _getUserProfileResponse

    private val _registerPhoneAndName = MutableLiveData<Result<RegisterInfo>>()
    val registerPhoneAndName: LiveData<Result<RegisterInfo>>
        get() = _registerPhoneAndName

    private val _registerCheckResponse = MutableLiveData<Result<RegisterCheckData>>()
    val registerCheckResponse: LiveData<Result<RegisterCheckData>>
        get() = _registerCheckResponse

    private val _validateUserProfileResponse = MutableLiveData<Result<UserProfileValidate>>()
    val validateUserProfileResponse: LiveData<Result<UserProfileValidate>>
        get() = _validateUserProfileResponse

    private val _getUserInfoResponse = MutableLiveData<Result<ProfileInfo>>()
    val getUserInfoResponse: LiveData<Result<ProfileInfo>>
        get() = _getUserInfoResponse

    private val _getShopInfoResponse = MutableLiveData<Result<ShopInfoByID>>()
    val getShopInfoResponse: LiveData<Result<ShopInfoByID>>
        get() = _getShopInfoResponse

    fun addName(name: String) {
        launchCatchError(block = {
            val params = UpdateUserProfileParam(fullname = name)
            val result = updateUserProfileUseCase(params)
            result.data.let {
                _addNameResponse.value = when {
                    it.isSuccess == 1 && it.errors.isEmpty() -> {
                        Success(it)
                    }
                    it.errors.isNotEmpty() && it.errors[0].isNotEmpty() -> {
                        Fail(MessageErrorException(it.errors[0]))
                    }
                    else -> {
                        Fail(RuntimeException())
                    }
                }
            }
        }, onError = {
            _addNameResponse.value = Fail(it)
        })
    }

    fun addPhone(phone: String) {
        launchCatchError(block = {
            val params = UpdateUserProfileParam(phone = phone)
            val result = updateUserProfileUseCase(params)
            result.data.let {
                _addPhoneResponse.value = when {
                    it.isSuccess == 1 && it.errors.isEmpty() -> {
                        Success(it)
                    }
                    it.errors.isNotEmpty() && it.errors[0].isNotEmpty() -> {
                        Fail(MessageErrorException(it.errors[0]))
                    }
                    else -> {
                        Fail(RuntimeException())
                    }
                }
            }
        }, onError = {
            _addPhoneResponse.value = Fail(it)
        })
    }

    fun registerCheck(phone: String) {
        launchCatchError(block = {
            val params = RegisterCheckParam(id = phone)
            val registerCheckResult = registerCheckUseCase(params)
            registerCheckResult.data.let {
                _registerCheckResponse.value = when {
                    it.errors.isNotEmpty() && it.errors[0].isNotEmpty() -> {
                        Fail(MessageErrorException(it.errors[0]))
                    }
                    else -> {
                        Success(it)
                    }
                }
            }
        }, onError = {
            _registerCheckResponse.value = Fail(it)
        })
    }

    fun getShopInfo(shopId: Int) {
        launchCatchError(block = {
            val params = ShopInfoParam(shopID = shopId)
            val result = shopInfoUseCase(params)
            _getShopInfoResponse.value = Success(result.data)
        }, onError = {
            _getShopInfoResponse.value = Fail(it)
        })
    }

    fun registerPhoneAndName(phone: String, name: String) {
        registerUseCase.execute(
                RegisterUseCase.generateParamRegisterPhoneShopCreation(name, phone), object :
                Subscriber<GraphqlResponse>() {

            override fun onNext(graphqlResponse: GraphqlResponse?) {
                graphqlResponse?.run {
                    val registerPojo = graphqlResponse
                            .getData<RegisterPojo>(RegisterPojo::class.java)
                    val registerInfo = registerPojo.register
                    when {
                        registerInfo.accessToken.isNotEmpty() &&
                                registerInfo.refreshToken.isNotEmpty() &&
                                registerInfo.userId.isNotEmpty() -> {
                            _registerPhoneAndName.value = Success(registerInfo)
                        }
                        registerInfo.errors.isNotEmpty() &&
                                registerInfo.errors[0].message.isNotEmpty() -> {
                            _registerPhoneAndName.postValue(Fail(MessageErrorException(registerInfo.errors[0].message)))
                        }
                        else -> {
                            _registerPhoneAndName.postValue(Fail(RuntimeException()))
                        }
                    }
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                if (e != null) {
                    _registerPhoneAndName.postValue(Fail(e))
                }
            }
        })
    }

    fun getUserProfile() {
        launchCatchError(block = {
            val result = getUserProfileCompletionUseCase(Unit)
            _getUserProfileResponse.value = Success(result.data)
        }, onError = {
            _getUserProfileResponse.value = Fail(it)
        })
    }

    fun validateUserProfile(phone: String) {
        launchCatchError(block = {
            val params = ValidateUserProfileParam(phone = phone)
            val result = validateUserProfileUseCase(params)
            _validateUserProfileResponse.value = Success(result.data)
        }, onError = {
            _validateUserProfileResponse.value = Fail(it)
        })
    }

    fun getUserInfo() {
        getProfileUseCase.execute(GetProfileSubscriber(
                userSession,
                { _getUserInfoResponse.value = Success(it.profileInfo) },
                { _getUserInfoResponse.postValue(Fail(it)) }
        ))
    }

    fun clearBackgroundTask() {
        registerUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
    }

    override fun onCleared() {
        super.onCleared()
        clearBackgroundTask()
    }
}
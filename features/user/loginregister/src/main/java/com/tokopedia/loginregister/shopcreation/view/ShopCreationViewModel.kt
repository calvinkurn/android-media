package com.tokopedia.loginregister.shopcreation.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.common.domain.usecase.RegisterCheckParam
import com.tokopedia.loginregister.common.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.shopcreation.data.ShopInfoByID
import com.tokopedia.loginregister.shopcreation.data.ShopStatus
import com.tokopedia.loginregister.shopcreation.data.UserProfileCompletionData
import com.tokopedia.loginregister.shopcreation.data.UserProfileUpdate
import com.tokopedia.loginregister.shopcreation.data.UserProfileValidate
import com.tokopedia.loginregister.shopcreation.domain.GetShopStatusUseCase
import com.tokopedia.loginregister.shopcreation.domain.GetUserProfileCompletionUseCase
import com.tokopedia.loginregister.shopcreation.domain.ShopInfoParam
import com.tokopedia.loginregister.shopcreation.domain.ShopInfoUseCase
import com.tokopedia.loginregister.shopcreation.domain.UpdateUserProfileParam
import com.tokopedia.loginregister.shopcreation.domain.UpdateUserProfileUseCase
import com.tokopedia.loginregister.shopcreation.domain.ValidateUserProfileParam
import com.tokopedia.loginregister.shopcreation.domain.ValidateUserProfileUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
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
    private val getProfileUseCase: GetUserInfoAndSaveSessionUseCase,
    private val shopInfoUseCase: ShopInfoUseCase,
    private val getShopStatusUseCase: GetShopStatusUseCase,
    private val dispatchers: CoroutineDispatchers
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

    private val _shopStatus = MutableLiveData<ShopStatus>()
    val shopStatus: LiveData<ShopStatus>
        get() = _shopStatus

    fun addName(name: String, validateToken: String) {
        launchCatchError(block = {
            val params = UpdateUserProfileParam(fullname = name, validateToken = validateToken)
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

    fun addPhone(phone: String, validateToken: String) {
        launchCatchError(block = {
            val params = UpdateUserProfileParam(phone = phone, validateToken = validateToken)
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
            val result = registerCheckUseCase(params)
            result.data.let {
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

    fun getShopStatus() {
        launch {
            try {
                val resp = getShopStatusUseCase("")
                _shopStatus.value = resp
            } catch (e: Exception) {
                _shopStatus.value = ShopStatus.Error(e)
            }
        }
    }

    fun registerPhoneAndName(phone: String, name: String) {
        launch {
            try {
                val data = registerUseCase(
                    RegisterUseCase.generateParamRegisterPhoneShopCreation(name, phone)
                )
                val registerInfo = data.register
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
            } catch (e: Exception) {
                _registerPhoneAndName.postValue(Fail(e))
            }
        }
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
        launch {
            try {
                when (val profile = getProfileUseCase(Unit)) {
                    is Success -> {
                        _getUserInfoResponse.value = Success(profile.data.profileInfo)
                    }
                    is Fail -> {
                        _getUserInfoResponse.value = Fail(profile.throwable)
                    }
                }
            } catch (e: Exception) {
                _getUserInfoResponse.value = Fail(e)
            }
        }
    }
}

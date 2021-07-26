package com.tokopedia.loginregister.shopcreation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.shopcreation.domain.param.RegisterCheckParam
import com.tokopedia.loginregister.shopcreation.domain.param.ShopInfoParam
import com.tokopedia.loginregister.shopcreation.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.shopcreation.domain.pojo.ShopInfoByID
import com.tokopedia.loginregister.shopcreation.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.shopcreation.domain.usecase.ShopInfoUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecommon.domain.param.UpdateUserProfileParam
import com.tokopedia.profilecommon.domain.param.ValidateUserProfileParam
import com.tokopedia.profilecommon.domain.pojo.UserProfileCompletionData
import com.tokopedia.profilecommon.domain.pojo.UserProfileUpdate
import com.tokopedia.profilecommon.domain.pojo.UserProfileValidate
import com.tokopedia.profilecommon.domain.usecase.GetUserProfileCompletionUseCase
import com.tokopedia.profilecommon.domain.usecase.UpdateUserProfileUseCase
import com.tokopedia.profilecommon.domain.usecase.ValidateUserProfileUseCase
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
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
        private val userSession: UserSessionInterface,
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

    fun addName(name: String) {
        val updateUserProfileParam = UpdateUserProfileParam(fullname = name)

        launchCatchError(block = {
            val data = updateUserProfileUseCase.getData(updateUserProfileParam)
            _addNameResponse.value = data
        }, onError = {
            _addNameResponse.postValue(Fail(it))
        })
    }

    fun addPhone(phone: String) {
        val updateUserProfileParam = UpdateUserProfileParam(phone = phone)

        launchCatchError(block = {
            val data = updateUserProfileUseCase
                    .getData(updateUserProfileParam)
            _addPhoneResponse.value = data
        }, onError = {
            _addPhoneResponse.postValue(Fail(it))
        })
    }

    fun registerCheck(phone: String) {
        val registerCheckParam = RegisterCheckParam(id = phone)

        launchCatchError(block = {
            val data = registerCheckUseCase.getData(registerCheckParam)
            _registerCheckResponse.value = data
        }, onError = {
            _registerCheckResponse.postValue(Fail(it))
        })
    }

    fun getShopInfo(shopId: Int) {
        val shopInfoParam = ShopInfoParam(shopID = shopId)

        launchCatchError(block = {
            val data = shopInfoUseCase.getData(shopInfoParam)
            _getShopInfoResponse.value = data
        }, onError = {
            _getShopInfoResponse.postValue(Fail(it))
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
            val data = getUserProfileCompletionUseCase.getData()
            _getUserProfileResponse.value = data
        }, onError = {
            _getUserProfileResponse.postValue(Fail(it))
        })
    }

    fun validateUserProfile(phone: String) {
        val validateUserProfileParam = ValidateUserProfileParam(phone = phone)

        launchCatchError(block = {
            val data = validateUserProfileUseCase
                    .getData(validateUserProfileParam)
            _validateUserProfileResponse.value = data
        }, onError = {
            _validateUserProfileResponse.postValue(Fail(it))
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
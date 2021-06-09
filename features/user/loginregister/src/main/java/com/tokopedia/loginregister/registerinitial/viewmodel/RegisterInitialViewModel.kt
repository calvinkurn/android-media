package com.tokopedia.loginregister.registerinitial.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.CallbackManager
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.TkpdIdlingResourceProvider
import com.tokopedia.loginregister.common.data.ResponseConverter.resultUsecaseCoroutineToFacebookCredentialListener
import com.tokopedia.loginregister.common.data.ResponseConverter.resultUsecaseCoroutineToSubscriber
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.discover.data.DiscoverItemDataModel
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.external_register.ovo.data.CheckOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.CheckHasOvoAccUseCase
import com.tokopedia.loginregister.login.view.model.DiscoverDataModel
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialQueryConstant
import com.tokopedia.loginregister.registerinitial.domain.RegisterV2Query
import com.tokopedia.loginregister.registerinitial.domain.data.ProfileInfoData
import com.tokopedia.loginregister.registerinitial.domain.pojo.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenFacebookSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-10-14.
 * ade.hadian@tokopedia.com
 */

class RegisterInitialViewModel @Inject constructor(
        private val registerCheckUseCase: GraphqlUseCase<RegisterCheckPojo>,
        private val registerRequestUseCase: GraphqlUseCase<RegisterRequestPojo>,
        private val registerRequestUseCaseV2: GraphqlUseCase<RegisterRequestV2>,
        private val activateUserUseCase: ActivateUserUseCase,
        private val discoverUseCase: DiscoverUseCase,
        private val getFacebookCredentialUseCase: GetFacebookCredentialUseCase,
        private val loginTokenUseCase: LoginTokenUseCase,
        private val getProfileUseCase: GetProfileUseCase,
        private val tickerInfoUseCase: TickerInfoUseCase,
        private val dynamicBannerUseCase: DynamicBannerUseCase,
        private val checkHasOvoAccUseCase: CheckHasOvoAccUseCase,
        private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
        @Named(SessionModule.SESSION_MODULE)
        private val userSession: UserSessionInterface,
        private val rawQueries: Map<String, String>,
        dispatcherProvider: CoroutineDispatchers) : BaseViewModel(dispatcherProvider.main) {

    private val mutableGetProviderResponse = MutableLiveData<Result<ArrayList<DiscoverItemDataModel>>>()
    val getProviderResponse: LiveData<Result<ArrayList<DiscoverItemDataModel>>>
        get() = mutableGetProviderResponse

    private val mutableGetFacebookCredentialResponse = MutableLiveData<Result<FacebookCredentialData>>()
    val getFacebookCredentialResponse: LiveData<Result<FacebookCredentialData>>
        get() = mutableGetFacebookCredentialResponse

    private val mutableLoginTokenFacebookResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenFacebookResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenFacebookResponse

    private val mutableLoginTokenFacebookPhoneResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenFacebookPhoneResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenFacebookPhoneResponse

    private val mutableLoginTokenGoogleResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenGoogleResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenGoogleResponse

    private val mutableLoginTokenAfterSQResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenAfterSQResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenAfterSQResponse

    private val mutableValidateToken = MutableLiveData<String>()
    val validateToken: LiveData<String>
        get() = mutableValidateToken

    private val mutableShowPopup = MutableLiveData<PopupError>()
    val showPopup: LiveData<PopupError>
        get() = mutableShowPopup

    private val mutableGoToActivationPage = MutableLiveData<MessageErrorException>()
    val goToActivationPage: LiveData<MessageErrorException>
        get() = mutableGoToActivationPage

    private val mutableGoToSecurityQuestion = MutableLiveData<String>()
    val goToSecurityQuestion: LiveData<String>
        get() = mutableGoToSecurityQuestion

    private val mutableGoToActivationPageAfterRelogin = MutableLiveData<MessageErrorException>()
    val goToActivationPageAfterRelogin: LiveData<MessageErrorException>
        get() = mutableGoToActivationPageAfterRelogin

    private val mutableGoToSecurityQuestionAfterRelogin = MutableLiveData<String>()
    val goToSecurityQuestionAfterRelogin: LiveData<String>
        get() = mutableGoToSecurityQuestionAfterRelogin

    private val mutableGetUserInfoResponse = MutableLiveData<Result<ProfileInfoData>>()
    val getUserInfoResponse: LiveData<Result<ProfileInfoData>>
        get() = mutableGetUserInfoResponse

    private val mutableGetUserInfoAfterAddPinResponse = MutableLiveData<Result<ProfileInfoData>>()
    val getUserInfoAfterAddPinResponse: LiveData<Result<ProfileInfoData>>
        get() = mutableGetUserInfoAfterAddPinResponse

    private val mutableGetTickerInfoResponse = MutableLiveData<Result<List<TickerInfoPojo>>>()
    val getTickerInfoResponse: LiveData<Result<List<TickerInfoPojo>>>
        get() = mutableGetTickerInfoResponse

    private val mutableRegisterCheckResponse = MutableLiveData<Result<RegisterCheckData>>()
    val registerCheckResponse: LiveData<Result<RegisterCheckData>>
        get() = mutableRegisterCheckResponse

    private val mutableRegisterRequestResponse = MutableLiveData<Result<RegisterRequestData>>()
    val registerRequestResponse: LiveData<Result<RegisterRequestData>>
        get() = mutableRegisterRequestResponse

    private val mutableActivateUserResponse = MutableLiveData<Result<ActivateUserData>>()
    val activateUserResponse: LiveData<Result<ActivateUserData>>
        get() = mutableActivateUserResponse

    private val _dynamicBannerResponse = MutableLiveData<Result<DynamicBannerDataModel>>()
    val dynamicBannerResponse: LiveData<Result<DynamicBannerDataModel>>
        get() = _dynamicBannerResponse

    private val _checkOvoResponse = MutableLiveData<Result<CheckOvoResponse>>()
    val checkOvoResponse: LiveData<Result<CheckOvoResponse>>
        get() = _checkOvoResponse

    var idlingResourceProvider = TkpdIdlingResourceProvider.provideIdlingResource("REGISTER_INITIAL")

    fun getProvider() {
        discoverUseCase.execute(
                DiscoverUseCase.getParamRegister(),
                resultUsecaseCoroutineToSubscriber(
                        onSuccessGetProvider(),
                        onFailedGetProvider()
                )
        )
    }

    fun getFacebookCredential(fragment: Fragment, callbackManager: CallbackManager) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK
        idlingResourceProvider?.increment()
        getFacebookCredentialUseCase.execute(
                GetFacebookCredentialUseCase.getParam(
                        fragment,
                        callbackManager),
                GetFacebookCredentialSubscriber(
                        resultUsecaseCoroutineToFacebookCredentialListener(
                                onSuccessGetFacebookEmailCredential(),
                                onSuccessGetFacebookPhoneCredential(),
                                onFailedGetFacebookCredential()
                        )
                )
        )
    }

    fun registerFacebook(accessToken: String, email: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK
        idlingResourceProvider?.increment()
        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken, LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                LoginTokenSubscriber(
                        userSession,
                        onSuccessLoginTokenFacebook(),
                        onFailedLoginTokenFacebook(),
                        {
                            showPopup().invoke(it.loginToken.popupError)
                            idlingResourceProvider?.decrement()
                        },
                        onGoToActivationPage(),
                        onGoToSecurityQuestion(email)
                )
        )

    }

    fun registerFacebookPhone(accessToken: String, phone: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK

        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken, LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                LoginTokenFacebookSubscriber(
                        userSession,
                        onSuccessLoginTokenFacebookPhone(),
                        onFailedLoginTokenFacebookPhone(),
                        { showPopup().invoke(it.loginToken.popupError) },
                        onGoToSecurityQuestion("")
                )
        )

    }

    fun registerGoogle(accessToken: String, email: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_GOOGLE
        idlingResourceProvider?.increment()
        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken, LoginTokenUseCase.SOCIAL_TYPE_GOOGLE),
                LoginTokenSubscriber(
                        userSession,
                        onSuccessLoginTokenGoogle(),
                        onFailedLoginTokenGoogle(),
                        {
                            showPopup().invoke(it.loginToken.popupError)
                            idlingResourceProvider?.decrement()
                        },
                        onGoToActivationPage(),
                        onGoToSecurityQuestion(email)
                )
        )

    }

    fun getUserInfo() {
        idlingResourceProvider?.increment()
        getProfileUseCase.execute(GetProfileSubscriber(userSession,
                onSuccessGetUserInfo(),
                onFailedGetUserInfo()))
    }

    fun getUserInfoAfterAddPin() {
        getProfileUseCase.execute(GetProfileSubscriber(userSession,
                onSuccessGetUserInfoAfterAddPin(),
                onFailedGetUserInfoAfterAddPin()))
    }

    fun getTickerInfo() {
        tickerInfoUseCase.execute(
                TickerInfoUseCase.createRequestParam(TickerInfoUseCase.REGISTER_PAGE),
                resultUsecaseCoroutineToSubscriber(
                        onSuccessGetTickerInfo(),
                        onFailedGetTickerInfo()
                ))
    }

    fun registerCheck(id: String) {
        rawQueries[RegisterInitialQueryConstant.MUTATION_REGISTER_CHECK]?.let { query ->
            val params = mapOf(RegisterInitialQueryConstant.PARAM_ID to id)

            registerCheckUseCase.setTypeClass(RegisterCheckPojo::class.java)
            registerCheckUseCase.setRequestParams(params)
            registerCheckUseCase.setGraphqlQuery(query)
            idlingResourceProvider?.increment()
            registerCheckUseCase.execute(
                    onSuccessRegisterCheck(),
                    onFailedRegisterCheck()
            )
        }
    }

    private fun createRegisterBasicParams(
            email: String,
            password: String,
            fullname: String,
            validateToken: String
    ): MutableMap<String, String> {
        return mutableMapOf(
                RegisterInitialQueryConstant.PARAM_EMAIL to email,
                RegisterInitialQueryConstant.PARAM_PASSWORD to password,
                RegisterInitialQueryConstant.PARAM_OS_TYPE to OS_TYPE_ANDROID,
                RegisterInitialQueryConstant.PARAM_REG_TYPE to REG_TYPE_EMAIL,
                RegisterInitialQueryConstant.PARAM_FULLNAME to fullname,
                RegisterInitialQueryConstant.PARAM_VALIDATE_TOKEN to validateToken
        )
    }

    fun registerRequest(
            email: String,
            password: String,
            fullname: String,
            validateToken: String
    ){
        rawQueries[RegisterInitialQueryConstant.MUTATION_REGISTER_REQUEST]?.let { query ->
            val params = createRegisterBasicParams(email, password, fullname, validateToken)
            userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
            registerRequestUseCase.setTypeClass(RegisterRequestPojo::class.java)
            registerRequestUseCase.setRequestParams(params)
            registerRequestUseCase.setGraphqlQuery(query)
            registerRequestUseCase.execute(
                {
                    onSuccessRegisterRequest(it.data)
                }, {
                    onFailedRegisterRequest(it)
                }
            )
        }
    }

    fun registerRequestV2(
            email: String,
            password: String,
            fullname: String,
            validateToken: String
    ) {
        launchCatchError(coroutineContext, {
            val keyData = generatePublicKeyUseCase.executeOnBackground().keyData
            if(keyData.key.isNotEmpty()) {
                val encryptedPassword = RsaUtils.encrypt(password, keyData.key.decodeBase64(), true)

                val params = createRegisterBasicParams(email, encryptedPassword, fullname, validateToken)
                params[RegisterInitialQueryConstant.PARAM_HASH] = keyData.hash

                userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
                registerRequestUseCaseV2.setTypeClass(RegisterRequestV2::class.java)
                registerRequestUseCaseV2.setRequestParams(params)
                registerRequestUseCaseV2.setGraphqlQuery(RegisterV2Query.registerQuery)
                val result = registerRequestUseCaseV2.executeOnBackground()
                onSuccessRegisterRequest(result.data)
            }}, {
                onFailedRegisterRequest(it)
            })
    }

    fun activateUser(
            email: String,
            validateToken: String
    ) {
        launchCatchError(coroutineContext, {
            activateUserUseCase.setParams(email, validateToken)
            val result = activateUserUseCase.executeOnBackground().data
                when {
                    result.isSuccess == 1 -> {
                        onSuccessActivateUser().invoke(result)
                    }
                    result.message.isNotEmpty() -> {
                        onFailedActivateUser().invoke(MessageErrorException(result.message))
                    }
                    else -> {
                        onFailedActivateUser().invoke(Throwable())
                    }
                }
        }, {
            onFailedActivateUser().invoke(it)
        })
    }

    fun reloginAfterSQ(validateToken: String) {
        loginTokenUseCase.executeLoginAfterSQ(LoginTokenUseCase.generateParamLoginAfterSQ(
                userSession, validateToken), LoginTokenSubscriber(userSession,
                onSuccessLoginTokenAfterSQ(),
                onFailedLoginTokenAfterSQ(validateToken),
                {showPopup().invoke(it.loginToken.popupError)},
                onGoToActivationPageAfterRelogin(validateToken),
                onGoToSecurityQuestionAfterRelogin("")))
    }

    fun getDynamicBannerData(page: String) {
        launchCatchError(coroutineContext, {
            val params = DynamicBannerUseCase.createRequestParams(page)
            dynamicBannerUseCase.createParams(params)
            dynamicBannerUseCase.executeOnBackground().run {
                _dynamicBannerResponse.postValue(Success(this))
            }
        }, {
            _dynamicBannerResponse.postValue(Fail(it))
        })
    }

    fun checkHasOvoAccount(phone: String) {
        launchCatchError(block = {
            checkHasOvoAccUseCase.setParams(phone)
            checkHasOvoAccUseCase.executeOnBackground().run {
                _checkOvoResponse.postValue(Success(this))
            }
        }, onError = {
            _checkOvoResponse.postValue(Fail(it))
        })
    }

    private fun onSuccessGetProvider(): (DiscoverDataModel) -> Unit {
        return {
            if (!it.providers.isEmpty()) {
                mutableGetProviderResponse.value = Success(it.providers)
            } else {
                mutableGetProviderResponse.value = Fail(Throwable())
            }
        }
    }

    private fun onFailedGetProvider(): (Throwable) -> Unit {
        return {
            mutableGetProviderResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetFacebookEmailCredential(): (FacebookCredentialData) -> Unit {
        return {
            mutableGetFacebookCredentialResponse.value = Success(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onSuccessGetFacebookPhoneCredential(): (FacebookCredentialData) -> Unit {
        return {
            mutableGetFacebookCredentialResponse.value = Success(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onFailedGetFacebookCredential(): (Throwable) -> Unit {
        return {
            mutableGetFacebookCredentialResponse.value = Fail(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onSuccessLoginTokenFacebook(): (LoginTokenPojo) -> Unit {
        return {
            mutableLoginTokenFacebookResponse.value = Success(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onFailedLoginTokenFacebook(): (Throwable) -> Unit {
        return {
            mutableLoginTokenFacebookResponse.value = Fail(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onSuccessLoginTokenFacebookPhone(): (LoginTokenPojo) -> Unit {
        return {
            mutableLoginTokenFacebookPhoneResponse.value = Success(it)
        }
    }

    private fun onFailedLoginTokenFacebookPhone(): (Throwable) -> Unit {
        return {
            mutableLoginTokenFacebookPhoneResponse.value = Fail(it)
        }
    }

    private fun onSuccessLoginTokenGoogle(): (LoginTokenPojo) -> Unit {
        return {
            mutableLoginTokenGoogleResponse.value = Success(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onFailedLoginTokenGoogle(): (Throwable) -> Unit {
        return {
            mutableLoginTokenGoogleResponse.value = Fail(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onSuccessLoginTokenAfterSQ(): (LoginTokenPojo) -> Unit {
        return {
            mutableLoginTokenAfterSQResponse.value = Success(it)
        }
    }

    private fun onFailedLoginTokenAfterSQ(validateToken: String): (Throwable) -> Unit {
        return {
            mutableLoginTokenAfterSQResponse.value = Fail(it)
            mutableValidateToken.value = validateToken
        }
    }

    private fun onSuccessGetUserInfo(): (ProfilePojo) -> Unit {
        return {
            mutableGetUserInfoResponse.value = Success(ProfileInfoData(it.profileInfo))
            idlingResourceProvider?.decrement()
        }
    }

    private fun onFailedGetUserInfo(): (Throwable) -> Unit {
        return {
            mutableGetUserInfoResponse.value = Fail(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onSuccessGetUserInfoAfterAddPin(): (ProfilePojo) -> Unit {
        return {
            mutableGetUserInfoAfterAddPinResponse.value = Success(ProfileInfoData(it.profileInfo))
        }
    }

    private fun onFailedGetUserInfoAfterAddPin(): (Throwable) -> Unit {
        return {
            mutableGetUserInfoAfterAddPinResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetTickerInfo(): (List<TickerInfoPojo>) -> Unit {
        return {
            mutableGetTickerInfoResponse.value = Success(it)
        }
    }

    private fun onFailedGetTickerInfo(): (Throwable) -> Unit {
        return {
            mutableGetTickerInfoResponse.value = Fail(it)
        }
    }

    private fun onSuccessRegisterCheck(): (RegisterCheckPojo) -> Unit {
        return {
            if (it.data.errors.isEmpty())
                mutableRegisterCheckResponse.value = Success(it.data)
            else if (it.data.errors.isNotEmpty()) mutableRegisterCheckResponse.value =
                    Fail(com.tokopedia.network.exception.MessageErrorException(it.data.errors[0]))
            else mutableRegisterRequestResponse.value = Fail(RuntimeException())
            idlingResourceProvider?.decrement()
        }
    }

    private fun onFailedRegisterCheck(): (Throwable) -> Unit {
        return {
            mutableRegisterCheckResponse.value = Fail(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onSuccessRegisterRequest(result: RegisterRequestData) {
        userSession.clearToken()
        if (result.accessToken.isNotEmpty() &&
                result.refreshToken.isNotEmpty() &&
                result.tokenType.isNotEmpty()) {
            mutableRegisterRequestResponse.value = Success(result)
        } else if (result.errors.isNotEmpty() && result.errors[0].message.isNotEmpty()) {
            mutableRegisterRequestResponse.value =
                    Fail(com.tokopedia.network.exception.MessageErrorException(result.errors[0].message))
        } else mutableRegisterRequestResponse.value = Fail(RuntimeException())
    }

    private fun onFailedRegisterRequest (throwable: Throwable){
        userSession.clearToken()
        mutableRegisterRequestResponse.value = Fail(throwable)
    }

    private fun onSuccessActivateUser(): (ActivateUserData) -> Unit {
        return {
            userSession.clearToken()
            if (it.isSuccess == 1 &&
                    it.accessToken.isNotEmpty() &&
                    it.refreshToken.isNotEmpty() &&
                    it.tokenType.isNotEmpty()) {
                mutableActivateUserResponse.value = Success(it)
            } else if (it.message.isNotEmpty()) {
                mutableActivateUserResponse.value =
                        Fail(MessageErrorException(it.message))
            } else mutableActivateUserResponse.value = Fail(RuntimeException())
        }
    }

    private fun onFailedActivateUser(): (Throwable) -> Unit {
        return {
            userSession.clearToken()
            mutableActivateUserResponse.value = Fail(it)
        }
    }

    private fun showPopup(): (PopupError) -> Unit {
        return {
            mutableShowPopup.value = it
        }
    }

    private fun onGoToActivationPage(): (MessageErrorException) -> Unit {
        return {
            mutableGoToActivationPage.value = it
            idlingResourceProvider?.decrement()
        }
    }

    private fun onGoToSecurityQuestion(email: String): () -> Unit {
        return {
            mutableGoToSecurityQuestion.value = email
            idlingResourceProvider?.decrement()
        }
    }

    private fun onGoToActivationPageAfterRelogin(validateToken: String): (MessageErrorException) -> Unit {
        return {
            mutableValidateToken.value = validateToken
            mutableGoToActivationPageAfterRelogin.value = it
        }
    }

    private fun onGoToSecurityQuestionAfterRelogin(email: String): () -> Unit {
        return {
            mutableGoToSecurityQuestionAfterRelogin.value = email
        }
    }

    fun clearBackgroundTask() {
        registerRequestUseCase.cancelJobs()
        registerCheckUseCase.cancelJobs()
        tickerInfoUseCase.unsubscribe()
        discoverUseCase.unsubscribe()
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
    }

    override fun onCleared() {
        super.onCleared()
        clearBackgroundTask()
    }

    companion object {
        val OS_TYPE_ANDROID = "1"
        val REG_TYPE_EMAIL = "email"
    }
}
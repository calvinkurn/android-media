package com.tokopedia.loginregister.registerinitial.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.loginregister.common.data.ResponseConverter.resultUsecaseCoroutineToFacebookCredentialListener
import com.tokopedia.loginregister.common.data.ResponseConverter.resultUsecaseCoroutineToSubscriber
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialQueryConstant
import com.tokopedia.loginregister.registerinitial.domain.pojo.*
import com.tokopedia.loginregister.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenFacebookSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.RuntimeException
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
        private val activateUserUseCase: GraphqlUseCase<ActivateUserPojo>,
        private val discoverUseCase: DiscoverUseCase,
        private val getFacebookCredentialUseCase: GetFacebookCredentialUseCase,
        private val loginTokenUseCase: LoginTokenUseCase,
        private val getProfileUseCase: GetProfileUseCase,
        private val tickerInfoUseCase : TickerInfoUseCase,
        @Named(SessionModule.SESSION_MODULE)
        private val userSession: UserSessionInterface,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    private val mutableGetProviderResponse = MutableLiveData<Result<ArrayList<DiscoverItemViewModel>>>()
    val getProviderResponse: LiveData<Result<ArrayList<DiscoverItemViewModel>>>
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

    private val mutableGetUserInfoResponse = MutableLiveData<Result<ProfileInfo>>()
    val getUserInfoResponse: LiveData<Result<ProfileInfo>>
        get() = mutableGetUserInfoResponse

    private val mutableGetTickerInfoResponse = MutableLiveData<Result<List<TickerInfoPojo>>>()
    val getTickerInfoResponse: LiveData<Result<List<TickerInfoPojo>>>
        get() = mutableGetTickerInfoResponse

    private val mutableRegisterCheckResponse = MutableLiveData<Result<RegisterCheckData>>()
    val registerCheckResponse: LiveData<Result<RegisterCheckData>>
        get() = mutableRegisterCheckResponse

    private val mutableRegisterRequestResponse = MutableLiveData<Result<RegisterRequestData>>()
    val registerRequestResponse: LiveData<Result<RegisterRequestData>>
        get() = mutableRegisterRequestResponse

    private val mutableActivateUserResponse = MutableLiveData<Result<ActivateUserPojo>>()
    val activateUserResponse: LiveData<Result<ActivateUserPojo>>
        get() = mutableActivateUserResponse

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

    fun registerFacebook(accessToken: String, email: String){
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK

        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken, LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                LoginTokenSubscriber(
                        userSession,
                        onSuccessLoginTokenFacebook(),
                        onFailedLoginTokenFacebook(),
                        onGoToActivationPage(),
                        onGoToSecurityQuestion(email)
                )
        )

    }

    fun registerFacebookPhone(accessToken: String, phone: String){
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK

        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken, LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                LoginTokenFacebookSubscriber(
                        userSession,
                        onSuccessLoginTokenFacebookPhone(),
                        onFailedLoginTokenFacebookPhone(),
                        onGoToSecurityQuestion("")
                )
        )

    }

    fun registerGoogle(accessToken: String, email: String){
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_GOOGLE

        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken, LoginTokenUseCase.SOCIAL_TYPE_GOOGLE),
                LoginTokenSubscriber(
                        userSession,
                        onSuccessLoginTokenGoogle(),
                        onFailedLoginTokenGoogle(),
                        onGoToActivationPage(),
                        onGoToSecurityQuestion(email)
                )
        )

    }

    fun reloginAfterSQ(validateToken: String){
        loginTokenUseCase.executeLoginAfterSQ(LoginTokenUseCase.generateParamLoginAfterSQ(
                userSession, validateToken),
                LoginTokenSubscriber(
                        userSession,
                        onSuccessLoginAfterSQ(),
                        onFailedLoginAfterSQ(),
                        onFailedLoginAfterSQ(),
                        { onFailedLoginAfterSQ() }
                )
        )
    }

    fun getUserInfo() {
        getProfileUseCase.execute(GetProfileSubscriber(userSession,
                onSuccessGetUserInfo(),
                onFailedGetUserInfo()))
    }

    fun getTickerInfo() {
        tickerInfoUseCase.execute(
                TickerInfoUseCase.createRequestParam(TickerInfoUseCase.REGISTER_PAGE),
                resultUsecaseCoroutineToSubscriber(
                        onSuccessGetTickerInfo(),
                        onFailedGetTickerInfo()
                ))
    }

    fun registerCheck(id: String){
        rawQueries[RegisterInitialQueryConstant.MUTATION_REGISTER_CHECK]?.let { query ->
            val params = mapOf(RegisterInitialQueryConstant.PARAM_ID to id)

            registerCheckUseCase.setTypeClass(RegisterCheckPojo::class.java)
            registerCheckUseCase.setRequestParams(params)
            registerCheckUseCase.setGraphqlQuery(query)
            registerCheckUseCase.execute(
                    onSuccessRegisterCheck(),
                    onFailedRegisterCheck()
            )
        }
    }

    fun registerRequest(
            email: String,
            password: String,
            fullname: String,
            validateToken: String
    ){
        rawQueries[RegisterInitialQueryConstant.MUTATION_REGISTER_REQUEST]?.let { query ->
            val params = mapOf(
                    RegisterInitialQueryConstant.PARAM_EMAIL to email,
                    RegisterInitialQueryConstant.PARAM_PASSWORD to password,
                    RegisterInitialQueryConstant.PARAM_OS_TYPE to OS_TYPE_ANDROID,
                    RegisterInitialQueryConstant.PARAM_REG_TYPE to REG_TYPE_EMAIL,
                    RegisterInitialQueryConstant.PARAM_FULLNAME to fullname,
                    RegisterInitialQueryConstant.PARAM_VALIDATE_TOKEN to validateToken
            )

            userSession.setToken(TokenGenerator().createBasicTokenGQL(),"")
            registerRequestUseCase.setTypeClass(RegisterRequestPojo::class.java)
            registerRequestUseCase.setRequestParams(params)
            registerRequestUseCase.setGraphqlQuery(query)
            registerRequestUseCase.execute(
                    onSuccessRegisterRequest(),
                    onFailedRegisterRequest()
            )
        }
    }

    fun activateUser(
           email: String,
           validateToken: String
    ){
        rawQueries[RegisterInitialQueryConstant.MUTATION_ACTIVATE_USER]?.let { query ->
            val params = mapOf(
                    RegisterInitialQueryConstant.PARAM_EMAIL to email,
                    RegisterInitialQueryConstant.PARAM_VALIDATE_TOKEN to validateToken
            )

            userSession.setToken(TokenGenerator().createBasicTokenGQL(),"")
            activateUserUseCase.setTypeClass(ActivateUserPojo::class.java)
            activateUserUseCase.setRequestParams(params)
            activateUserUseCase.setGraphqlQuery(query)
            activateUserUseCase.execute(
                    onSuccessActivateUser(),
                    onFailedActivateUser()
            )
        }
    }

    fun reloginAfterSQ(validateToken: String) {
        loginTokenUseCase.executeLoginAfterSQ(LoginTokenUseCase.generateParamLoginAfterSQ(
                userSession, validateToken), LoginTokenSubscriber(userSession,
                onSuccessLoginTokenAfterSQ(),
                onFailedLoginTokenAfterSQ(validateToken),
                onGoToActivationPageAfterRelogin(validateToken),
                onGoToSecurityQuestionAfterRelogin("")))
    }

    private fun onSuccessGetProvider(): (DiscoverViewModel) -> Unit{
        return {
            if (!it.providers.isEmpty()) {
                mutableGetProviderResponse.value = Success(it.providers)
            } else {
                mutableGetProviderResponse.value = Fail(Throwable())
            }
        }
    }

    private fun onFailedGetProvider(): (Throwable) -> Unit{
        return {
            mutableGetProviderResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetFacebookEmailCredential(): (FacebookCredentialData) -> Unit{
        return {
            mutableGetFacebookCredentialResponse.value = Success(it)
        }
    }

    private fun onSuccessGetFacebookPhoneCredential(): (FacebookCredentialData) -> Unit{
        return {
            mutableGetFacebookCredentialResponse.value = Success(it)
        }
    }

    private fun onFailedGetFacebookCredential(): (Throwable) -> Unit{
        return {
            mutableGetFacebookCredentialResponse.value = Fail(it)
        }
    }

    private fun onSuccessLoginTokenFacebook(): (LoginTokenPojo) -> Unit{
        return {
            mutableLoginTokenFacebookResponse.value = Success(it)
        }
    }

    private fun onFailedLoginTokenFacebook(): (Throwable) -> Unit{
        return {
            mutableLoginTokenFacebookResponse.value = Fail(it)
        }
    }

    private fun onSuccessLoginTokenFacebookPhone(): (LoginTokenPojo) -> Unit{
        return {
            mutableLoginTokenFacebookPhoneResponse.value = Success(it)
        }
    }

    private fun onFailedLoginTokenFacebookPhone(): (Throwable) -> Unit{
        return {
            mutableLoginTokenFacebookPhoneResponse.value = Fail(it)
        }
    }

    private fun onSuccessLoginTokenGoogle(): (LoginTokenPojo) -> Unit{
        return {
            mutableLoginTokenGoogleResponse.value = Success(it)
        }
    }

    private fun onFailedLoginTokenGoogle(): (Throwable) -> Unit{
        return {
            mutableLoginTokenGoogleResponse.value = Fail(it)
        }
    }

    private fun onSuccessLoginTokenAfterSQ(): (LoginTokenPojo) -> Unit{
        return {
            mutableLoginTokenAfterSQResponse.value = Success(it)
        }
    }

    private fun onFailedLoginTokenAfterSQ(validateToken: String): (Throwable) -> Unit{
        return {
            mutableLoginTokenAfterSQResponse.value = Fail(it)
            mutableValidateToken.value = validateToken
        }
    }

    private fun onSuccessGetUserInfo(): (ProfilePojo) -> Unit{
        return {
            mutableGetUserInfoResponse.value = Success(it.profileInfo)
        }
    }

    private fun onFailedGetUserInfo(): (Throwable) -> Unit{
        return{
            mutableGetUserInfoResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetTickerInfo(): (List<TickerInfoPojo>) -> Unit{
        return {
            mutableGetTickerInfoResponse.value = Success(it)
        }
    }

    private fun onFailedGetTickerInfo(): (Throwable) -> Unit{
        return{
            mutableGetTickerInfoResponse.value = Fail(it)
        }
    }

    private fun onSuccessRegisterCheck(): (RegisterCheckPojo) -> Unit{
        return {
            if(it.data.errors.isEmpty())
                mutableRegisterCheckResponse.value = Success(it.data)
            else if(it.data.errors.isNotEmpty()) mutableRegisterCheckResponse.value =
                    Fail(com.tokopedia.network.exception.MessageErrorException(it.data.errors[0]))
            else mutableRegisterRequestResponse.value = Fail(RuntimeException())
        }
    }

    private fun onFailedRegisterCheck(): (Throwable) -> Unit{
        return{
            mutableRegisterCheckResponse.value = Fail(it)
        }
    }

    private fun onSuccessRegisterRequest(): (RegisterRequestPojo) -> Unit {
        return{
            userSession.clearToken()
            if(it.data.accessToken.isNotEmpty() &&
                    it.data.refreshToken.isNotEmpty() &&
                    it.data.tokenType.isNotEmpty()){
                mutableRegisterRequestResponse.value = Success(it.data)
            }else if(it.data.errors.isNotEmpty() && it.data.errors[0].message.isNotEmpty()){
                mutableRegisterRequestResponse.value =
                        Fail(com.tokopedia.network.exception.MessageErrorException(it.data.errors[0].message))
            }else mutableRegisterRequestResponse.value = Fail(RuntimeException())
        }
    }

    private fun onFailedRegisterRequest(): (Throwable) -> Unit{
        return{
            userSession.clearToken()
            mutableRegisterRequestResponse.value = Fail(it)
        }
    }

    private fun onSuccessActivateUser(): (ActivateUserPojo) -> Unit {
        return{
            userSession.clearToken()
            if(it.isSuccess &&
                    it.accessToken.isNotEmpty() &&
                    it.refreshToken.isNotEmpty() &&
                    it.tokenType.isNotEmpty()){
                mutableActivateUserResponse.value = Success(it)
            }else if(it.message.isNotEmpty()){
                mutableActivateUserResponse.value =
                        Fail(com.tokopedia.network.exception.MessageErrorException(it.message))
            }else mutableActivateUserResponse.value = Fail(RuntimeException())
        }
    }

    private fun onFailedActivateUser(): (Throwable) -> Unit{
        return{
            userSession.clearToken()
            mutableActivateUserResponse.value = Fail(it)
        }
    }

    private fun onGoToActivationPage(): (MessageErrorException) -> Unit{
        return {
            mutableGoToActivationPage.value = it
        }
    }

    private fun onGoToSecurityQuestion(email: String): () -> Unit{
        return {
            mutableGoToSecurityQuestion.value = email
        }
    }

    private fun onGoToActivationPageAfterRelogin(validateToken: String): (MessageErrorException) -> Unit{
        return {
            mutableValidateToken.value = validateToken
            mutableGoToActivationPageAfterRelogin.value = it
        }
    }

    private fun onGoToSecurityQuestionAfterRelogin(email: String): () -> Unit{
        return {
            mutableGoToSecurityQuestionAfterRelogin.value = email
        }
    }

    override fun onCleared() {
        super.onCleared()
        activateUserUseCase.cancelJobs()
        registerRequestUseCase.cancelJobs()
        registerCheckUseCase.cancelJobs()
        tickerInfoUseCase.unsubscribe()
        discoverUseCase.unsubscribe()
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
    }

    companion object {
        val OS_TYPE_ANDROID = "1"
        val REG_TYPE_EMAIL = "email"
    }
}
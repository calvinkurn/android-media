package com.tokopedia.loginregister.external_register.base.viewmodel

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

//open class BaseAddNameViewModel @Inject constructor(
//        @Named(SessionModule.SESSION_MODULE)
//        private val userSession: UserSessionInterface,
//        private val registerRequestUseCase: GraphqlUseCase<RegisterRequestPojo>,
//        dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {
//
//    private val mutableRegisterRequestResponse = MutableLiveData<Result<RegisterRequestData>>()
//    val registerRequestResponse: LiveData<Result<RegisterRequestData>>
//        get() = mutableRegisterRequestResponse
//
//    fun registerRequest(
//            email: String,
//            password: String,
//            fullname: String,
//            validateToken: String
//    ) {
//        rawQueries[RegisterInitialQueryConstant.MUTATION_REGISTER_REQUEST]?.let { query ->
//            val params = mapOf(
//                    RegisterInitialQueryConstant.PARAM_EMAIL to email,
//                    RegisterInitialQueryConstant.PARAM_PASSWORD to password,
//                    RegisterInitialQueryConstant.PARAM_OS_TYPE to RegisterInitialViewModel.OS_TYPE_ANDROID,
//                    RegisterInitialQueryConstant.PARAM_REG_TYPE to RegisterInitialViewModel.REG_TYPE_EMAIL,
//                    RegisterInitialQueryConstant.PARAM_FULLNAME to fullname,
//                    RegisterInitialQueryConstant.PARAM_VALIDATE_TOKEN to validateToken
//            )
//
//            userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
//            registerRequestUseCase.setTypeClass(RegisterRequestPojo::class.java)
//            registerRequestUseCase.setRequestParams(params)
//            registerRequestUseCase.setGraphqlQuery(query)
//            registerRequestUseCase.execute(
//                    onSuccessRegisterRequest(),
//                    onFailedRegisterRequest()
//            )
//        }
//    }
//
//    private fun onSuccessRegisterRequest(): (RegisterRequestPojo) -> Unit {
//        return {
//            userSession.clearToken()
//            if (it.data.accessToken.isNotEmpty() &&
//                    it.data.refreshToken.isNotEmpty() &&
//                    it.data.tokenType.isNotEmpty()) {
//                mutableRegisterRequestResponse.value = Success(it.data)
//            } else if (it.data.errors.isNotEmpty() && it.data.errors[0].message.isNotEmpty()) {
//                mutableRegisterRequestResponse.value =
//                        Fail(MessageErrorException(it.data.errors[0].message))
//            } else mutableRegisterRequestResponse.value = Fail(RuntimeException())
//        }
//    }
//
//    private fun onFailedRegisterRequest(): (Throwable) -> Unit {
//        return {
//            userSession.clearToken()
//            mutableRegisterRequestResponse.value = Fail(it)
//        }
//    }
//
//
//}
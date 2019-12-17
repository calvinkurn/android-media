package com.tokopedia.otp.validator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.otp.validator.data.*
import com.tokopedia.otp.validator.di.ValidatorQueryConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 */

class ValidatorViewModel @Inject constructor(
        private val otpRequestUseCase: GraphqlUseCase<OtpRequestPojo>,
        private val otpValidateUseCase: GraphqlUseCase<OtpValidatePojo>,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    private val mutableOtpRequestResponse = MutableLiveData<Result<OtpRequestData>>()
    val otpRequestResponse: LiveData<Result<OtpRequestData>>
        get() = mutableOtpRequestResponse

    private val mutableOtpResendRequestResponse = MutableLiveData<Result<OtpRequestData>>()
    val otpResendRequestResponse: LiveData<Result<OtpRequestData>>
        get() = mutableOtpResendRequestResponse

    private val mutableOtpValidateResponse = MutableLiveData<Result<OtpValidateData>>()
    val otpValidateResponse: LiveData<Result<OtpValidateData>>
        get() = mutableOtpValidateResponse

    fun otpRequestEmail(otpType: String, email: String, isResend: Boolean){
        rawQueries[ValidatorQueryConstant.QUERY_OTP_REQUEST]?.let { query ->
            val params = mapOf(
                    ValidatorQueryConstant.PARAM_OTP_TYPE to otpType,
                    ValidatorQueryConstant.PARAM_MODE to MODE_EMAIL,
                    ValidatorQueryConstant.PARAM_EMAIL to email
            )

            otpRequestUseCase.setTypeClass(OtpRequestPojo::class.java)
            otpRequestUseCase.setRequestParams(params)
            otpRequestUseCase.setGraphqlQuery(query)
            otpRequestUseCase.execute(
                    if(isResend) onSuccessOtpResendRequest() else onSuccessOtpRequest(),
                    if(isResend) onErrorOtpResendRequest() else onErrorOtpRequest()
            )
        }
    }

    fun otpValidateEmail(otpType: String, code: String, email: String){
        rawQueries[ValidatorQueryConstant.QUERY_OTP_VALIDATE]?.let { query ->
            val params = mapOf(
                    ValidatorQueryConstant.PARAM_OTP_TYPE to otpType,
                    ValidatorQueryConstant.PARAM_CODE to code,
                    ValidatorQueryConstant.PARAM_EMAIL to email
            )

            otpValidateUseCase.setTypeClass(OtpValidatePojo::class.java)
            otpValidateUseCase.setRequestParams(params)
            otpValidateUseCase.setGraphqlQuery(query)
            otpValidateUseCase.execute(
                    onSuccessValidate(),
                    onErrorValidate()
            )
        }
    }

    private fun onSuccessOtpRequest(): (OtpRequestPojo) -> Unit {
        return {
            when{
                it.data.success ->
                    mutableOtpRequestResponse.value = Success(it.data)
                it.data.errorMessage.isNotEmpty() ->
                    mutableOtpRequestResponse.value = Fail(MessageErrorException(it.data.errorMessage))
                else -> mutableOtpRequestResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onErrorOtpRequest(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableOtpRequestResponse.value = Fail(it)
        }
    }

    private fun onSuccessOtpResendRequest(): (OtpRequestPojo) -> Unit {
        return {
            when{
                it.data.success ->
                    mutableOtpResendRequestResponse.value = Success(it.data)
                it.data.errorMessage.isNotEmpty() ->
                    mutableOtpResendRequestResponse.value = Fail(MessageErrorException(it.data.errorMessage))
                else -> mutableOtpResendRequestResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onErrorOtpResendRequest(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableOtpResendRequestResponse.value = Fail(it)
        }
    }

    private fun onSuccessValidate(): (OtpValidatePojo) -> Unit {
        return {
            when{
                it.data.success ->
                    mutableOtpValidateResponse.value = Success(it.data)
                it.data.errorMessage.isNotEmpty() ->
                    mutableOtpValidateResponse.value = Fail(MessageErrorException(it.data.errorMessage))
                else -> mutableOtpValidateResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onErrorValidate(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableOtpValidateResponse.value = Fail(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        otpRequestUseCase.cancelJobs()
        otpValidateUseCase.cancelJobs()
    }

    companion object {
        const val MODE_EMAIL = "email"
    }
}
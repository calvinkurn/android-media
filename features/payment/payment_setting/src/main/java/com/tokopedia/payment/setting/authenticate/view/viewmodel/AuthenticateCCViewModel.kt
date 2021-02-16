package com.tokopedia.payment.setting.authenticate.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.authenticate.domain.CheckUpdateWhiteListCreditCartUseCase
import com.tokopedia.payment.setting.authenticate.model.*
import com.tokopedia.payment.setting.authenticate.view.presenter.AuthenticateCCPresenter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AuthenticateCCViewModel @Inject constructor(
        @ApplicationContext val context: Context,
        private val checkUpdateWhiteListCreditCartUseCase: CheckUpdateWhiteListCreditCartUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _whiteListStatusResultLiveData = MutableLiveData<Result<CheckWhiteListStatus>>()
    private val _whiteListResultLiveData = MutableLiveData<Result<List<TypeAuthenticateCreditCard>>>()
    val whiteListStatusResultLiveData: LiveData<Result<CheckWhiteListStatus>> = _whiteListStatusResultLiveData
    val whiteListResultLiveData: LiveData<Result<List<TypeAuthenticateCreditCard>>> = _whiteListResultLiveData

    fun updateWhiteList(
            authValue: Int, isNeedCheckOtp: Boolean,
            token: String?,
    ) {
        checkUpdateWhiteListCreditCartUseCase.cancelJobs()
        if (authValue == SINGLE_AUTH_VALUE && isNeedCheckOtp) {
            onUpdateError(AuthException.CheckOtpException(userSession.phoneNumber))
            return
        }
        checkUpdateWhiteListCreditCartUseCase.whiteListResponse(
                ::onUpdateSuccess,
                ::onUpdateError,
                authValue, isNeedCheckOtp, token
        )
    }

    fun updateWhiteList() {
        checkUpdateWhiteListCreditCartUseCase.cancelJobs()
        checkUpdateWhiteListCreditCartUseCase.whiteListResponse(
                ::onWhiteListDataSuccess,
                ::onWhiteListDataError,
                0, false, null
        )
    }

    private fun onUpdateSuccess(checkWhiteListResponse: CheckWhiteListResponse) {
        checkWhiteListResponse.checkWhiteListStatus?.let {
            _whiteListStatusResultLiveData.value = Success(it)
        } ?: onUpdateError(NullPointerException("CheckWhiteListStatus is null"))
    }

    private fun onWhiteListDataSuccess(checkWhiteListResponse: CheckWhiteListResponse) {
        checkWhiteListResponse.checkWhiteListStatus?.let {
            _whiteListResultLiveData.value = Success(generateDataListAuth(it.data))
        } ?: onWhiteListDataError(NullPointerException("List null"))
    }

    private fun onUpdateError(throwable: Throwable) {
        _whiteListStatusResultLiveData.value = Fail(throwable)
    }

    private fun onWhiteListDataError(throwable: Throwable) {
        _whiteListResultLiveData.value = Fail(throwable)
    }

    private fun generateDataListAuth(data: List<WhiteListData>?): List<TypeAuthenticateCreditCard> {
        val listAuth = ArrayList<TypeAuthenticateCreditCard>()
        data?.let {
            if (data.isNotEmpty()) {
                listAuth.add(initiateSingleAuthentication(data.first()))
                listAuth.add(initiateDoubleAuthentication(data.first()))
            }
        }
        return listAuth
    }

    private fun initiateSingleAuthentication(model: WhiteListData): TypeAuthenticateCreditCard {
        val singleAuthentication = TypeAuthenticateCreditCard()
        singleAuthentication.title = context.getString(R.string.payment_authentication_title_1)
        singleAuthentication.description = context.getString(R.string.payment_authentication_description_1)
        singleAuthentication.stateWhenSelected = SINGLE_AUTH_VALUE
        singleAuthentication.isSelected = model.state == SINGLE_AUTH_VALUE
        return singleAuthentication
    }

    private fun initiateDoubleAuthentication(model: WhiteListData): TypeAuthenticateCreditCard {
        val doubleAuthentication = TypeAuthenticateCreditCard()
        doubleAuthentication.title = context.getString(R.string.payment_authentication_title_2)
        doubleAuthentication.description = context.getString(R.string.payment_authentication_description_2)
        doubleAuthentication.stateWhenSelected = DOUBLE_AUTH_VALUE
        doubleAuthentication.isSelected = model.state == DOUBLE_AUTH_VALUE
        return doubleAuthentication
    }

    override fun onCleared() {
        checkUpdateWhiteListCreditCartUseCase.cancelJobs()
    }

    companion object {
        const val SINGLE_AUTH_VALUE = 1
        const val DOUBLE_AUTH_VALUE = 0
    }

}
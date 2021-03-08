package com.tokopedia.payment.setting.authenticate.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.payment.setting.authenticate.domain.CheckUpdateWhiteListCreditCartUseCase
import com.tokopedia.payment.setting.authenticate.model.AuthException
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListStatus
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard
import com.tokopedia.payment.setting.authenticate.model.WhiteListData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AuthenticateCCViewModel @Inject constructor(
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
            _whiteListStatusResultLiveData.postValue(Fail(AuthException
                    .CheckOtpException(userSession.phoneNumber)))
            return
        }
        checkUpdateWhiteListCreditCartUseCase.whiteListResponse(
                {
                    _whiteListStatusResultLiveData.value = Success(it)
                },
                {
                    _whiteListStatusResultLiveData.value = Fail(it)
                },
                authValue, isNeedCheckOtp, token
        )
    }

    fun checkWhiteList(singleAuthTitle: String, singleAuthDescription: String,
                       doubleAuthTitle: String, doubleAuthDescription: String, ) {
        checkUpdateWhiteListCreditCartUseCase.cancelJobs()
        checkUpdateWhiteListCreditCartUseCase.whiteListResponse(
                {
                    _whiteListResultLiveData.value = Success(generateDataListAuth(singleAuthTitle,
                            singleAuthDescription, doubleAuthTitle, doubleAuthDescription,
                            it.data))
                },
                {
                    _whiteListResultLiveData.value = Fail(it)
                },
                0, false, null
        )
    }


    private fun generateDataListAuth(singleAuthTitle: String, singleAuthDescription: String,
                                     doubleAuthTitle: String, doubleAuthDescription: String,
                                     data: List<WhiteListData>?): List<TypeAuthenticateCreditCard> {
        val listAuth = ArrayList<TypeAuthenticateCreditCard>()
        data?.let {
            if (data.isNotEmpty()) {
                listAuth.add(initiateSingleAuthentication(singleAuthTitle,
                        singleAuthDescription, data.first()))
                listAuth.add(initiateDoubleAuthentication(doubleAuthTitle,
                        doubleAuthDescription, data.first()))
            }
        }
        return listAuth
    }

    private fun initiateSingleAuthentication(title: String, description: String,
                                             model: WhiteListData): TypeAuthenticateCreditCard {
        val singleAuthentication = TypeAuthenticateCreditCard()
        singleAuthentication.title = title
        singleAuthentication.description = description
        singleAuthentication.stateWhenSelected = SINGLE_AUTH_VALUE
        singleAuthentication.isSelected = model.state == SINGLE_AUTH_VALUE
        return singleAuthentication
    }

    private fun initiateDoubleAuthentication(title: String, description: String,
                                             model: WhiteListData): TypeAuthenticateCreditCard {
        val doubleAuthentication = TypeAuthenticateCreditCard()
        doubleAuthentication.title = title
        doubleAuthentication.description = description
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
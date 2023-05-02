package com.tokopedia.payment.setting.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.payment.setting.list.domain.GetCreditCardListUseCase
import com.tokopedia.payment.setting.list.domain.GetSettingBannerUseCase
import com.tokopedia.payment.setting.list.model.PaymentQueryResponse
import com.tokopedia.payment.setting.list.model.SettingBannerModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SettingsListViewModel @Inject constructor(
        private val getCreditCardListUseCase: GetCreditCardListUseCase,
        private val getSettingBannerUseCase: GetSettingBannerUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _paymentQueryResultLiveData = MutableLiveData<Result<PaymentQueryResponse>>()
    private val _phoneVerificationStatusLiveData = MutableLiveData<Boolean>()
    private val _settingBannerResultLiveData = MutableLiveData<Result<SettingBannerModel>>()
    private val _bannerAndCardListResultLiveData =
        MediatorLiveData<Pair<Result<PaymentQueryResponse>?, Result<SettingBannerModel>?>>().apply {
            addSource(_paymentQueryResultLiveData) {
                value = Pair(_paymentQueryResultLiveData.value, _settingBannerResultLiveData.value)
            }
            addSource(_settingBannerResultLiveData) {
                value = Pair(_paymentQueryResultLiveData.value, _settingBannerResultLiveData.value)
            }
    }
    val phoneVerificationStatusLiveData: LiveData<Boolean> = _phoneVerificationStatusLiveData
    val bannerAndCardListResultLiveData: LiveData<Pair<Result<PaymentQueryResponse>?, Result<SettingBannerModel>?>> =
        _bannerAndCardListResultLiveData

    fun getCreditCardList() {
        getCreditCardListUseCase.cancelJobs()
        getCreditCardListUseCase.getCreditCardList(
                ::onCreditCardListSuccess,
                ::onCreditCardListError,
        )
    }

    fun getSettingBanner() {
        getSettingBannerUseCase.cancelJobs()
        getSettingBannerUseCase.getSettingBanner(
            ::onGetSettingBannerSuccess,
            ::onGetSettingBannerError,
        )
    }

    fun checkVerificationPhone() {
        _phoneVerificationStatusLiveData.value = userSession.isMsisdnVerified
    }

    private fun onCreditCardListSuccess(paymentQueryResponse: PaymentQueryResponse) {
        _paymentQueryResultLiveData.value = Success(paymentQueryResponse)
    }

    private fun onCreditCardListError(throwable: Throwable) {
        _paymentQueryResultLiveData.value = Fail(throwable)
    }

    private fun onGetSettingBannerSuccess(settingBannerModel: SettingBannerModel) {
        _settingBannerResultLiveData.value = Success(settingBannerModel)
    }

    private fun onGetSettingBannerError(throwable: Throwable) {
        _settingBannerResultLiveData.value = Fail(throwable)
    }

     override fun onCleared() {
         getCreditCardListUseCase.cancelJobs()
         getSettingBannerUseCase.cancelJobs()
     }
}

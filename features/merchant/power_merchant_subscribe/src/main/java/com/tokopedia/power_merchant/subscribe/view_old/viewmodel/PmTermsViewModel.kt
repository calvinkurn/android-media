package com.tokopedia.power_merchant.subscribe.view_old.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gm.common.domain.interactor.ActivatePowerMerchantUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.power_merchant.subscribe.domain.interactor.ValidatePowerMerchantUseCase
import com.tokopedia.power_merchant.subscribe.domain.model.ValidatePowerMerchantResponse
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantActivationResult
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantActivationResult.*
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState.HideLoading
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState.ShowLoading
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PmTermsViewModel @Inject constructor(
    private val activatePowerMerchantUseCase: ActivatePowerMerchantUseCase,
    private val validatePowerMerchantUseCase: ValidatePowerMerchantUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val viewState: LiveData<ViewState>
        get() = _viewState
    val activatePmResult: LiveData<Result<PowerMerchantActivationResult>>
        get() = _activatePmResult

    private val _viewState = MutableLiveData<ViewState>()
    private val _activatePmResult = MutableLiveData<Result<PowerMerchantActivationResult>>()

    fun activatePowerMerchant() {
        showLoading()

        launchCatchError(block = {
            val pmValidation = withContext(dispatchers.io) {
                validatePowerMerchantUseCase.execute().response
            }

            if(pmValidation.isValid()) {
                executePmActivation()
            } else {
                onActivationInvalid(pmValidation)
            }

            hideLoading()
        }) {
            _activatePmResult.value = Fail(it)
            hideLoading()
            Timber.d(it)
        }
    }

    fun detachView() {
        activatePowerMerchantUseCase.unsubscribe()
    }

    private suspend fun executePmActivation() {
        val isSuccess = withContext(dispatchers.io) {
            activatePowerMerchantUseCase.getData(RequestParams.EMPTY)
        }

        return if (isSuccess) {
            val result = ActivationSuccess
            _activatePmResult.value = Success(result)
        } else {
            throw RuntimeException()
        }
    }

    private fun onActivationInvalid(response: ValidatePowerMerchantResponse) {
        val result = when {
            response.kycNotVerified() -> KycNotVerified
            response.shopScoreNotEligible() -> ShopScoreNotEligible
            else -> GeneralError(response.getMessage())
        }

        _activatePmResult.value = Success(result)
    }

    private fun showLoading() {
        _viewState.value = ShowLoading
    }

    private fun hideLoading() {
        _viewState.value = HideLoading
    }
}
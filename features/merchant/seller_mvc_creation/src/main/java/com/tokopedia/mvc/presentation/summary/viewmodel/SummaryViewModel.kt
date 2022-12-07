package com.tokopedia.mvc.presentation.summary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.presentation.summary.helper.SummaryPageHelper
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase
) : BaseViewModel(dispatchers.main) {

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _configuration = MutableLiveData<VoucherConfiguration>()
    val configuration: LiveData<VoucherConfiguration> get() = _configuration
    val information = Transformations.map(configuration) { it.voucherInformation }
    val maxExpense = Transformations.map(configuration) { SummaryPageHelper.getMaxExpenses(it) }

    fun setConfiguration(configuration: VoucherConfiguration) {
        _configuration.value = configuration
    }

    fun setupEditMode(voucherId: String) {
        merchantPromotionGetMVDataByIDUseCase
        launchCatchError(
            dispatchers.io,
            block = {
                val param = MerchantPromotionGetMVDataByIDUseCase.Param(
                    voucherId = voucherId.toLongOrZero()
                )
                val result = merchantPromotionGetMVDataByIDUseCase.execute(param)
                _configuration.postValue(result.toVoucherConfiguration())
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
}

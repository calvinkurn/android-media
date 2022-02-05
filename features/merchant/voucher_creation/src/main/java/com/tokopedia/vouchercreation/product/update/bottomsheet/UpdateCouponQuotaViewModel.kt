package com.tokopedia.vouchercreation.product.update.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.UpdateQuotaUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCouponQuotaViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val updateQuotaUseCase: UpdateQuotaUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val MAX_QUOTA = 999
    }

    private val _validInput = MutableLiveData<QuotaState>()
    val validInput: LiveData<QuotaState>
        get() = _validInput

    private val _updateQuotaResult = MutableLiveData<Result<Boolean>>()
    val updateQuotaResult: LiveData<Result<Boolean>>
        get() = _updateQuotaResult


    private val _maxExpenseEstimation = MutableLiveData<Long>()
    val maxExpenseEstimation: LiveData<Long>
        get() = _maxExpenseEstimation


    sealed class QuotaState {
        data class BelowMinQuota(val minQuota: Int) : QuotaState()
        data class ExceedMaxAllowedQuota(val maxQuota : Int) : QuotaState()
        data class Valid(val newQuota: Int) : QuotaState()
    }

    fun updateQuota(voucherId: Int, quota: Int) {
        launchCatchError(
            block = {
                updateQuotaUseCase.params = UpdateQuotaUseCase.createRequestParam(voucherId, quota)
                val result =
                    withContext(dispatchers.io) { updateQuotaUseCase.executeOnBackground() }
                _updateQuotaResult.value = Success(result)
            },
            onError = {
                _updateQuotaResult.value = Fail(it)
            }
        )
    }


    fun validateInput(newQuota : Int, minQuota : Int) {
        if (newQuota < minQuota) {
            _validInput.value =  QuotaState.BelowMinQuota(minQuota)
            return
        }

        if (newQuota > MAX_QUOTA) {
            _validInput.value =  QuotaState.ExceedMaxAllowedQuota(MAX_QUOTA)
            return
        }

        _validInput.value = QuotaState.Valid(newQuota)
    }


    fun calculateMaxExpenseEstimation(maxDiscountAmount : Int, quota : Int) {
        _maxExpenseEstimation.value = (maxDiscountAmount * quota).toLong()
    }

}
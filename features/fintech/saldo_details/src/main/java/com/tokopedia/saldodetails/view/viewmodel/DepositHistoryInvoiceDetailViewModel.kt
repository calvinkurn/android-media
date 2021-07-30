package com.tokopedia.saldodetails.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.saldodetails.domain.GetDepositHistoryInfoUseCase
import com.tokopedia.saldodetails.response.model.saldo_detail_info.DepositHistoryData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DepositHistoryInvoiceDetailViewModel @Inject constructor(
    private val getDepositHistoryInfoUseCase: GetDepositHistoryInfoUseCase
) : BaseViewModel(Dispatchers.Main) {

    private val _depositHistoryLiveData = MutableLiveData<Result<DepositHistoryData>>()
    val depositHistoryLiveData: LiveData<Result<DepositHistoryData>> = _depositHistoryLiveData

    fun getInvoiceDetail(summaryId: String) {
        getDepositHistoryInfoUseCase.cancelJobs()
        getDepositHistoryInfoUseCase.getDepositHistoryInvoiceInfo(
            summaryId,
            ::onSuccessDepositHistoryInvoice,
            ::onErrorDepositHistoryInvoice
        )
    }

    private fun onSuccessDepositHistoryInvoice(depositHistoryData: DepositHistoryData) {
        _depositHistoryLiveData.postValue(Success(depositHistoryData))
    }

    private fun onErrorDepositHistoryInvoice(throwable: Throwable) {
        _depositHistoryLiveData.postValue(Fail(throwable))

    }

    fun getOrderDetailUrl(): String {
        return _depositHistoryLiveData.value?.let {
            if (it is Success) it.data.orderUrl
            else ""
        } ?: kotlin.run { "" }
    }

    fun getInvoiceDetailUrl(): String {
        return _depositHistoryLiveData.value?.let {
            if (it is Success) it.data.invoiceUrl
            else ""
        } ?: kotlin.run { "" }
    }

    override fun onCleared() {
        getDepositHistoryInfoUseCase.cancelJobs()
        super.onCleared()
    }

}
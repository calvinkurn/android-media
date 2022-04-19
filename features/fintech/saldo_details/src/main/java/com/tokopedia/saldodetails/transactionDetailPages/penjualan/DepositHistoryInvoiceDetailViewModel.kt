package com.tokopedia.saldodetails.transactionDetailPages.penjualan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.saldodetails.commom.di.module.DispatcherModule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class DepositHistoryInvoiceDetailViewModel @Inject constructor(
    private val getDepositHistoryInfoUseCase: GetDepositHistoryInfoUseCase,
    @Named(DispatcherModule.MAIN) val dispatcher: CoroutineDispatcher,
    ) : BaseViewModel(dispatcher) {

    private val _depositHistoryLiveData = MutableLiveData<Result<DepositHistoryData>>()
    val depositHistoryLiveData: LiveData<Result<DepositHistoryData>> = _depositHistoryLiveData

    fun getInvoiceDetail(summaryId: String) {
        getDepositHistoryInfoUseCase.cancelJobs()
        getDepositHistoryInfoUseCase.getDepositHistoryInvoiceInfo(
            ::onSuccessDepositHistoryInvoice,
            ::onErrorDepositHistoryInvoice,
            summaryId
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
        super.onCleared()
        getDepositHistoryInfoUseCase.cancelJobs()
    }

}
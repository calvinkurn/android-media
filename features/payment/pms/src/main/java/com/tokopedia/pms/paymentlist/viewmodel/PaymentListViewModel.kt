package com.tokopedia.pms.paymentlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pms.paymentlist.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pms.paymentlist.domain.data.*
import com.tokopedia.pms.paymentlist.domain.usecase.*
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PaymentListViewModel @Inject constructor(
    private val getPaymentListCountUseCase: GetPaymentListCountUseCase,
    private val paymentListUseCase: PaymentListUseCase,
    private val cancelPaymentDetailUseCase: PaymentCancelDetailUseCase,
    private val cancelPaymentUseCase: CancelPaymentUseCase,
    private val mapper: PaymentListMapperUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private var gqlPaymentList = arrayListOf<PaymentListInside>()

    private val _paymentListResultLiveData =
        MutableLiveData<PaymentResult<ArrayList<BasePaymentModel>>>()
    val paymentListResultLiveData: LiveData<PaymentResult<ArrayList<BasePaymentModel>>> =
        _paymentListResultLiveData
    private val _cancelPaymentDetailLiveData = MutableLiveData<PaymentResult<CancelDetailWrapper>>()
    val cancelPaymentDetailLiveData: LiveData<PaymentResult<CancelDetailWrapper>> =
        _cancelPaymentDetailLiveData
    private val _cancelPaymentLiveData = MutableLiveData<PaymentResult<CancelPayment>>()
    val cancelPaymentLiveData: LiveData<PaymentResult<CancelPayment>> = _cancelPaymentLiveData


    fun getPaymentListCount() {
        _paymentListResultLiveData.postValue(LoadingState)
        getPaymentListCountUseCase.getPaymentCount(
            ::onCountReceived,
            ::onCountFailed
        )
    }

    private fun onCountReceived(count: Int) {
        if (count >= 40) _paymentListResultLiveData.postValue(ProgressState)
        else _paymentListResultLiveData.postValue(LoadingState)
        getPaymentList("")
    }

    private fun onCountFailed(throwable: Throwable) {
        _paymentListResultLiveData.postValue(LoadingState)
        getPaymentList("")
    }

    private fun getPaymentList(lastCursor: String = "") {
        paymentListUseCase.cancelJobs()
        paymentListUseCase.getPaymentList(
            ::onPaymentListSuccess,
            ::onPaymentListError,
            lastCursor
        )
    }

    fun getCancelPaymentDetail(transactionId: String, merchantCode: String, productName: String?) {
        cancelPaymentDetailUseCase.cancelJobs()
        cancelPaymentDetailUseCase.gerCancelDetail(
            ::onCancelPaymentDetailSuccess,
            ::onCancelDetailError,
            transactionId,
            merchantCode,
            productName
        )
    }

    fun cancelPayment(transactionId: String, merchantCode: String) {
        cancelPaymentUseCase.cancelJobs()
        cancelPaymentUseCase.invokePaymentCancel(
            ::paymentCancelledSuccessful,
            ::paymentCancelFailed,
            transactionId,
            merchantCode
        )
    }

    private fun onPaymentListSuccess(paymentList: PaymentList?) {
        paymentList?.let {
            if (it.paymentList.isNullOrEmpty().not())
                gqlPaymentList.addAll(it.paymentList)

            if (it.isHasNextPage) getPaymentList(it.lastCursor)
            else combinePendingTransactions()

        }
    }

    private fun combinePendingTransactions() {
        if (gqlPaymentList.isNullOrEmpty())
            _paymentListResultLiveData.postValue(EmptyState)
        else
            mapper.mapResponseToRenderPaymentList(gqlPaymentList,
                onSuccess = {
                    showCombinedPaymentList(it)
                }, onError = { onPaymentListError(it) })
    }

    private fun showCombinedPaymentList(list: ArrayList<BasePaymentModel>) {
        _paymentListResultLiveData.postValue(Success(list))
    }

    private fun onCancelPaymentDetailSuccess(cancelDetail: CancelDetailWrapper) {
        _cancelPaymentDetailLiveData.postValue(Success(cancelDetail))
    }

    private fun paymentCancelledSuccessful(cancelPayment: CancelPayment) {
        _cancelPaymentLiveData.postValue(Success(cancelPayment))
    }

    private fun onPaymentListError(throwable: Throwable) {
        _paymentListResultLiveData.postValue(Fail(throwable))
    }

    private fun onCancelDetailError(throwable: Throwable) {
        _cancelPaymentDetailLiveData.postValue(Fail(throwable))
    }

    private fun paymentCancelFailed(throwable: Throwable) {
        _cancelPaymentLiveData.postValue(Fail(throwable))
    }

    override fun onCleared() {
        paymentListUseCase.cancelJobs()
        cancelPaymentUseCase.cancelJobs()
        cancelPaymentDetailUseCase.cancelJobs()
        super.onCleared()
    }

    fun refreshPage() {
        gqlPaymentList.clear()
    }
}
package com.tokopedia.pms.paymentlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pms.paymentlist.domain.data.CancelPayment
import com.tokopedia.pms.paymentlist.domain.data.PaymentList
import com.tokopedia.pms.paymentlist.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.CancelDetailWrapper
import com.tokopedia.pms.paymentlist.domain.usecase.CancelPaymentUseCase
import com.tokopedia.pms.paymentlist.domain.usecase.PaymentCancelDetailUseCase
import com.tokopedia.pms.paymentlist.domain.usecase.PaymentListMapperUseCase
import com.tokopedia.pms.paymentlist.domain.usecase.PaymentListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PaymentListViewModel @Inject constructor(
    private val paymentListUseCase: PaymentListUseCase,
    private val cancelPaymentDetailUseCase: PaymentCancelDetailUseCase,
    private val cancelPaymentUseCase: CancelPaymentUseCase,
    private val mapper: PaymentListMapperUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private var isHasNextPage: Boolean = true
    private var lastCursor = ""

    private val _paymentListResultLiveData = MutableLiveData<Result<ArrayList<BasePaymentModel>>>()
    val paymentListResultLiveData: LiveData<Result<ArrayList<BasePaymentModel>>> =
        _paymentListResultLiveData
    private val _cancelPaymentDetailLiveData = MutableLiveData<Result<CancelDetailWrapper>>()
    val cancelPaymentDetailLiveData: LiveData<Result<CancelDetailWrapper>> =
        _cancelPaymentDetailLiveData
    private val _cancelPaymentLiveData = MutableLiveData<Result<CancelPayment>>()
    val cancelPaymentLiveData: LiveData<Result<CancelPayment>> = _cancelPaymentLiveData

    fun getPaymentList() {
        paymentListUseCase.cancelJobs()
        if (isHasNextPage)
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

    private fun onPaymentListSuccess(paymentList: PaymentList) {
        lastCursor = paymentList.lastCursor
        isHasNextPage = paymentList.isHasNextPage
        if (paymentList.paymentList.isNullOrEmpty())
            onPaymentListError(NullPointerException("EMPTY"))
        else mapper.mapResponseToRenderPaymentList(paymentList.paymentList,
            onSuccess = {
                _paymentListResultLiveData.postValue(Success(it))
            }, onError = { onPaymentListError(it) })
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
        isHasNextPage = true
        lastCursor = ""
    }
}
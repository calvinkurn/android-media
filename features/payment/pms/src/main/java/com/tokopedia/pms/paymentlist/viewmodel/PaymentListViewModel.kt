package com.tokopedia.pms.paymentlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pms.payment.data.model.CancelDetail
import com.tokopedia.pms.payment.data.model.CancelPayment
import com.tokopedia.pms.payment.data.model.PaymentList
import com.tokopedia.pms.payment.view.model.PaymentListModel
import com.tokopedia.pms.paymentlist.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
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

    var isHasNextPage: Boolean = false
    var lastCursor = ""

    private val _paymentListResultLiveData = MutableLiveData<Result<ArrayList<BasePaymentModel>>>()
    val paymentListResultLiveData: LiveData<Result<ArrayList<BasePaymentModel>>> =
        _paymentListResultLiveData
    private val _cancelPaymentDetailLiveData = MutableLiveData<Result<String>>()
    val cancelPaymentDetailLiveData: LiveData<Result<String>> = _cancelPaymentDetailLiveData
    private val _cancelPaymentLiveData = MutableLiveData<Result<CancelPayment>>()
    val cancelPaymentLiveData: LiveData<Result<CancelPayment>> = _cancelPaymentLiveData

    fun getPaymentList(cursor: String) {
        paymentListUseCase.cancelJobs()
        paymentListUseCase.getPaymentList(::onPaymentListSuccess, ::onPaymentListError, cursor)
    }

    fun getCancelPaymentDetail(transactionId: String, merchantCode: String) {
        cancelPaymentDetailUseCase.cancelJobs()
        cancelPaymentDetailUseCase.gerCancelDetail(
            ::onCancelPaymentDetailSuccess,
            ::onCancelDetailError,
            transactionId,
            merchantCode
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
        mapper.mapResponseToRenderPaymentList(paymentList.paymentList, onSuccess = {
            _paymentListResultLiveData.postValue(Success(it))
        }, onError = { onPaymentListError(it) })
    }

    private fun onCancelPaymentDetailSuccess(cancelDetail: CancelDetail) {
        _cancelPaymentDetailLiveData.postValue(Success(cancelDetail.refundMessage))
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

}
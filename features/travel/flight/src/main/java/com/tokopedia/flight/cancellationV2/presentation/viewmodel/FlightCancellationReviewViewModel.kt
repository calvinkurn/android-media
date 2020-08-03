package com.tokopedia.flight.cancellationV2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.cancellationV2.domain.FlightCancellationEstimateRefundUseCase
import com.tokopedia.flight.cancellationV2.domain.FlightCancellationRequestCancelUseCase
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationReviewViewModel @Inject constructor(
        private val estimateUseCase: FlightCancellationEstimateRefundUseCase,
        private val requestUseCase: FlightCancellationRequestCancelUseCase,
        private val userSession: UserSessionInterface,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    var invoiceId: String = ""
    lateinit var cancellationWrapperModel: FlightCancellationWrapperModel

    private val mutableEstimateRefundFinish = MutableLiveData<Result<Boolean>>()
    val estimateRefundFinish: LiveData<Result<Boolean>>
        get() = mutableEstimateRefundFinish

    private val mutableRequestCancel = MutableLiveData<Result<Boolean>>()
    val requestCancel: LiveData<Result<Boolean>>
        get() = mutableRequestCancel

    init {
        mutableRequestCancel.value = Success(false)
        mutableEstimateRefundFinish.value = Success(false)
    }

    fun onInit() {
        val iterator = cancellationWrapperModel.cancellationList.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.passengerModelList.size == 0) {
                iterator.remove()
            }
        }

        fetchRefundEstimation()
    }

    fun fetchRefundEstimation() {
        launchCatchError(dispatcherProvider.ui(), block = {
            val estimateRefundData = estimateUseCase.execute(
                    estimateUseCase.createRequestParams(
                            invoiceId,
                            userSession.userId,
                            cancellationWrapperModel.cancellationList,
                            cancellationWrapperModel.cancellationReasonAndAttachmentModel.reasonId.toInt()
                    )
            )

            cancellationWrapperModel.cancellationReasonAndAttachmentModel.estimateRefund = estimateRefundData.totalValueNumeric
            cancellationWrapperModel.cancellationReasonAndAttachmentModel.estimateFmt = estimateRefundData.totalValue
            cancellationWrapperModel.cancellationReasonAndAttachmentModel.showEstimateRefund = estimateRefundData.showEstimate

            mutableEstimateRefundFinish.postValue(Success(true))
        }) {
            it.printStackTrace()
            mutableEstimateRefundFinish.postValue(Fail(it))
        }
    }

    fun isRefundable(): Boolean {
        var isRefundable = false

        for (cancellation in cancellationWrapperModel.cancellationList) {
            if (cancellation.flightCancellationJourney.isRefundable) {
                isRefundable = true
                break
            }
        }

        return isRefundable
    }

    fun requestCancellation() {
        launchCatchError(dispatcherProvider.ui(), block = {
            requestUseCase.execute(requestUseCase.createRequestParams(
                    cancellationWrapperModel.invoiceId,
                    cancellationWrapperModel.cancellationReasonAndAttachmentModel.reason,
                    cancellationWrapperModel.cancellationReasonAndAttachmentModel.reasonId,
                    cancellationWrapperModel.cancellationList)
            )
            mutableRequestCancel.postValue(Success(true))
        }) {
            it.printStackTrace()
            mutableRequestCancel.postValue(Fail(it))
        }
    }

    fun shouldShowAttachments(): Boolean {
        var shouldShow = true

        if (cancellationWrapperModel.cancellationReasonAndAttachmentModel.attachmentList.size > 0) {
            for (attachment in cancellationWrapperModel.cancellationReasonAndAttachmentModel.attachmentList) {
                if (attachment.filepath.isEmpty()) shouldShow = false
            }
        } else {
            shouldShow = false
        }

        return shouldShow
    }

}
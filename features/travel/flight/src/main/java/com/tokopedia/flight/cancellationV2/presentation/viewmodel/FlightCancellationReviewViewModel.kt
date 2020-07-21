package com.tokopedia.flight.cancellationV2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.cancellationV2.domain.FlightCancellationEstimateRefundUseCase
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
        private val userSession: UserSessionInterface,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    var invoiceId: String = ""
    lateinit var cancellationWrapperModel: FlightCancellationWrapperModel

    private val mutableEstimateRefundFinish = MutableLiveData<Result<Boolean>>()
    val estimateRefundFinish: LiveData<Result<Boolean>>
        get() = mutableEstimateRefundFinish

    init {
        mutableEstimateRefundFinish.value = Success(false)
    }

    fun onInit() {
        for ((index, item) in cancellationWrapperModel.cancellationList.withIndex()) {
            if (item.passengerModelList.size == 0) {
                cancellationWrapperModel.cancellationList.removeAt(index)
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
            cancellationWrapperModel.cancellationReasonAndAttachmentModel.showEstimateRefund = true

            mutableEstimateRefundFinish.postValue(Success(true))
        }) {
            it.printStackTrace()
            mutableEstimateRefundFinish.postValue(Fail(it))
        }
    }

}
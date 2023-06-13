package com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductSubmitAcknowledgeUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductSubmissionProgressUseCase
import javax.inject.Inject

class FlashSaleProductListSseSubmissionErrorBottomSheetViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleProductSubmissionProgressUseCase: GetFlashSaleProductSubmissionProgressUseCase,
    private val doFlashSaleProductSubmitAcknowledgeUseCase: DoFlashSaleProductSubmitAcknowledgeUseCase
) : BaseViewModel(dispatchers.main) {

    private val _productSubmissionSseError =
        MutableLiveData<FlashSaleProductSubmissionProgress>()
    val productSubmissionSseError: LiveData<FlashSaleProductSubmissionProgress> get() = _productSubmissionSseError

    fun getProductListSseSubmissionError(campaignId: String, page: Int) {
        launchCatchError(dispatchers.io, {
            val productListSubmissionSseErrorData = getProductListSseSubmissionErrorData(
                campaignId,
                page
            )
            _productSubmissionSseError.postValue(productListSubmissionSseErrorData)
        }) {

        }
    }

    private suspend fun getProductListSseSubmissionErrorData(
        campaignId: String,
        page: Int
    ): FlashSaleProductSubmissionProgress {
        return getFlashSaleProductSubmissionProgressUseCase.execute(
            GetFlashSaleProductSubmissionProgressUseCase.Param(
                campaignId,
                page,
                false
            )
        )
    }

    fun acknowledgeProductSubmissionSse(campaignId: String) {
        launchCatchError(dispatchers.io, {
            doAcknowledgeProductSubmission(campaignId)
        }) {}
    }

    private suspend fun doAcknowledgeProductSubmission(campaignId: String): Boolean {
        val param = DoFlashSaleProductSubmitAcknowledgeUseCase.Param(campaignId.toLongOrZero())
        return doFlashSaleProductSubmitAcknowledgeUseCase.execute(param)
    }

}

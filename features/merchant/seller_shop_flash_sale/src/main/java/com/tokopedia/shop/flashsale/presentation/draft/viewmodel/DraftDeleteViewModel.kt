package com.tokopedia.shop.flashsale.presentation.draft.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignCancellationUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignCancellationListUseCase
import com.tokopedia.shop.flashsale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DraftDeleteViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val cancellationListUseCase: GetSellerCampaignCancellationListUseCase,
    private val cancellationUseCase: DoSellerCampaignCancellationUseCase,
) : BaseViewModel(dispatchers.main) {

    private val _questionListData = SingleLiveEvent<Result<List<String>>>()
    val questionListData: LiveData<Result<List<String>>>
        get() = _questionListData

    private val _cancellationError = MutableLiveData<String>()
    val cancellationError: LiveData<String>
        get() = _cancellationError

    private val _cancellationStatus = MutableLiveData<Boolean>()
    val cancellationStatus: LiveData<Boolean>
        get() = _cancellationStatus

    private var cancellationReason = ""

    fun setCancellationReason(reason: String) {
        cancellationReason = reason
    }

    fun getQuestionListData() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                cancellationListUseCase.execute()
            }
            _questionListData.value = Success(result)
        }, onError = {
            _questionListData.value = Fail(it)
        })
    }

    fun doCancellation(draftItemModel: DraftItemModel) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                cancellationUseCase.execute(
                    draftItemModel.id,
                    cancellationReason
                )
            }
            _cancellationStatus.value = result
        }, onError = {
            _cancellationError.value = it.localizedMessage
        })
    }
}
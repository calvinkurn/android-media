package com.tokopedia.shop.flash_sale.presentation.draft.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignCancellationListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DraftDeleteViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val cancellationListUseCase: GetSellerCampaignCancellationListUseCase
) : BaseViewModel(dispatchers.main) {

    private val _questionListData = SingleLiveEvent<Result<List<String>>>()
    val questionListData: LiveData<Result<List<String>>>
        get() = _questionListData

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
}
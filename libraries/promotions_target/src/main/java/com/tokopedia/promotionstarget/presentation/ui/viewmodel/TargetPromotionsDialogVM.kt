package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.domain.usecase.AutoApplyUseCase
import com.tokopedia.promotionstarget.presentation.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class TargetPromotionsDialogVM @Inject constructor(@Named("Main")
                                                   val uiDispatcher: CoroutineDispatcher,
                                                   @Named("IO")
                                                   val workerDispatcher: CoroutineDispatcher,
                                                   val autoApplyUseCase: AutoApplyUseCase
) : BaseViewModel(uiDispatcher) {

    val autoApplyLiveData: MutableLiveData<Result<AutoApplyResponse>> = MutableLiveData()

    fun autoApply(code: String) {
        launchCatchError(block = {
            val response = withContext(workerDispatcher) {
                autoApplyUseCase.getResponse(autoApplyUseCase.getQueryParams(code))
            }
            autoApplyLiveData.value = Success(response)
        }, onError = {
            autoApplyLiveData.value = Fail(it)
        })
    }
}
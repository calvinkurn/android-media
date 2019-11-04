package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.promotionstarget.data.LiveDataResult
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

    val autoApplyLiveData: MutableLiveData<LiveDataResult<AutoApplyResponse>> = MutableLiveData()

    fun autoApply(code: String) {
        launchCatchError(block = {
            val response = withContext(workerDispatcher) {
                val map = autoApplyUseCase.getQueryParams(code)
                autoApplyUseCase.getResponse(map)
            }
            autoApplyLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            autoApplyLiveData.postValue(LiveDataResult.error(it))
        })
    }
}
package com.tokopedia.promotionstarget.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.launchCatchError
import com.tokopedia.promotionstarget.usecase.AutoApplyUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class TargetPromotionsDialogVM @Inject constructor(@Named("Main")
                                                   val dispatcher: CoroutineDispatcher,
                                                   val autoApplyUseCase: AutoApplyUseCase
) : BaseViewModel(dispatcher) {

    val autoApplyLiveData: MutableLiveData<Result<AutoApplyResponse>> = MutableLiveData()

    fun autoApply(code: String) {
        launchCatchError(block = {
            val response = autoApplyUseCase.getResponse(autoApplyUseCase.getQueryParams(code))
            autoApplyLiveData.value = Success(response)
        }, onError = {
            autoApplyLiveData.value = Fail(it)
        })
    }
}
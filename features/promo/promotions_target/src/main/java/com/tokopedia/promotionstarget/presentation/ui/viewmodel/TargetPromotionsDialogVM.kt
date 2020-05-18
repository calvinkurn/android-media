package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import android.app.Application
import com.tokopedia.promotionstarget.data.LiveDataResult
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.domain.usecase.AutoApplyUseCase
import com.tokopedia.promotionstarget.presentation.SingleLiveEvent
import com.tokopedia.promotionstarget.presentation.TargetedPromotionAnalytics
import com.tokopedia.promotionstarget.presentation.launchCatchError
import com.tokopedia.promotionstarget.presentation.ui.CustomToast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class TargetPromotionsDialogVM @Inject constructor(@Named("Main")
                                                   val uiDispatcher: CoroutineDispatcher,
                                                   @Named("IO")
                                                   val workerDispatcher: CoroutineDispatcher,
                                                   val autoApplyUseCase: AutoApplyUseCase,
                                                   val app: Application
) : BaseAndroidViewModel(uiDispatcher, app) {

    val autoApplyLiveData: SingleLiveEvent<LiveDataResult<AutoApplyResponse>> = SingleLiveEvent()

    fun autoApply(code: String) {
        launchCatchError(block = {
            val response = withContext(workerDispatcher) {
                val map = autoApplyUseCase.getQueryParams(code)
                autoApplyUseCase.getResponse(map)
            }
            showToast(LiveDataResult.success(response))
            autoApplyLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            autoApplyLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun showToast(it:LiveDataResult<AutoApplyResponse>) {
        when (it.status) {
            LiveDataResult.STATUS.SUCCESS -> {
                val messageList = it.data?.tokopointsSetAutoApply?.resultStatus?.message
                if (messageList != null && messageList.isNotEmpty()) {
                    CustomToast.show(app, messageList[0].toString())
                    TargetedPromotionAnalytics.claimSucceedPopup(messageList[0].toString())
                }
            }
        }
    }
}
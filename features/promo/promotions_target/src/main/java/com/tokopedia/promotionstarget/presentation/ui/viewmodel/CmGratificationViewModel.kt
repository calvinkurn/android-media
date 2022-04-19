package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import android.app.Application
import com.tokopedia.promotionstarget.data.LiveDataResult
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.data.di.IO
import com.tokopedia.promotionstarget.data.di.MAIN
import com.tokopedia.promotionstarget.domain.usecase.AutoApplyUseCase
import com.tokopedia.promotionstarget.domain.usecase.UpdateGratifNotification
import com.tokopedia.promotionstarget.presentation.SingleLiveEvent
import com.tokopedia.promotionstarget.presentation.launchCatchError
import com.tokopedia.promotionstarget.presentation.ui.Locks
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class CmGratificationViewModel @Inject constructor(@Named(MAIN)
                                                   val uiDispatcher: CoroutineDispatcher,
                                                   @Named(IO)
                                                   val workerDispatcher: CoroutineDispatcher,
                                                   val autoApplyUseCase: AutoApplyUseCase,
                                                   val updateGratifNotificationUsecase: UpdateGratifNotification,
                                                   val app: Application
) : BaseAndroidViewModel(uiDispatcher, app) {

    val autoApplyLiveData: SingleLiveEvent<LiveDataResult<AutoApplyResponse>> = SingleLiveEvent()

    fun autoApply(code: String) {
        launchCatchError(block = {
            autoApplyLiveData.postValue(LiveDataResult.loading())
            val response = withContext(workerDispatcher) {
                val map = autoApplyUseCase.getQueryParams(code)
                autoApplyUseCase.getResponse(map)
            }
            autoApplyLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            autoApplyLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun updateGratification(notificationID: String?, notificationEntryType: Int, popupType: Int, screenName: String, inAppId: Long?) {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                if (!notificationID.isNullOrEmpty()) {
                    Locks.notificationMutex.withLock {
                        val map = updateGratifNotificationUsecase.getQueryParams(notificationID.toInt(),
                                notificationEntryType,
                                popupType,
                                screenName)
                        updateGratifNotificationUsecase.getResponse(map)
                    }
                }
            }
        }, onError = {})
    }
}
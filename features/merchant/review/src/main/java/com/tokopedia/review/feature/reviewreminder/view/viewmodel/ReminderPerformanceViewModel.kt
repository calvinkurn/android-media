package com.tokopedia.review.feature.reviewreminder.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderStats
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderStatsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReminderPerformanceViewModel @Inject constructor(
        dispatcherProvider: CoroutineDispatchers,
        private val productrevGetReminderStatsUseCase: ProductrevGetReminderStatsUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private val reminderStats = MutableLiveData<Result<ProductrevGetReminderStats>>()
    fun getReminderStats(): LiveData<Result<ProductrevGetReminderStats>> = reminderStats

    fun fetchReminderStats() {
        launchCatchError(block = {
            val responseWrapper = productrevGetReminderStatsUseCase.executeOnBackground()
            reminderStats.postValue(Success(responseWrapper.productrevGetReminderStats))
        }, onError = {
            reminderStats.postValue(Fail(it))
        })
    }
}
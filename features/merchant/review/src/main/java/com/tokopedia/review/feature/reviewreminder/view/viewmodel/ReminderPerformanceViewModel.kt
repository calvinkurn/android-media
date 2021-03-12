package com.tokopedia.review.feature.reviewreminder.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderCounter
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderStats
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderCounterUseCase
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderStatsUseCase
import javax.inject.Inject

class ReminderPerformanceViewModel @Inject constructor(
        dispatcherProvider: CoroutineDispatchers,
        private val productrevGetReminderStatsUseCase: ProductrevGetReminderStatsUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private val reminderStats = MutableLiveData<ProductrevGetReminderStats>()
    fun getReminderStats(): LiveData<ProductrevGetReminderStats> = reminderStats

    fun fetchReminderStats() {
        launchCatchError(block = {
            val responseWrapper = productrevGetReminderStatsUseCase.executeOnBackground()
            reminderStats.postValue(responseWrapper.productrevGetReminderStats)
        }, onError = {

        })
    }


}
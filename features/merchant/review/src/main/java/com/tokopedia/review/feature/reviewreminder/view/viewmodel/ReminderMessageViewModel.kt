package com.tokopedia.review.feature.reviewreminder.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderCounter
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderTemplate
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderCounterUseCase
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderTemplateUseCase
import javax.inject.Inject

class ReminderMessageViewModel @Inject constructor(
        dispatcherProvider: CoroutineDispatchers,
        private val productrevGetReminderCounterUseCase: ProductrevGetReminderCounterUseCase,
        private val productrevGetReminderTemplateUseCase: ProductrevGetReminderTemplateUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private val estimation = MutableLiveData<ProductrevGetReminderCounter>()
    fun getEstimation(): LiveData<ProductrevGetReminderCounter> = estimation

    private val template = MutableLiveData<ProductrevGetReminderTemplate>()
    fun getTemplate(): LiveData<ProductrevGetReminderTemplate> = template

    fun fetchReminderCounter() {
        launchCatchError(block = {
            val responseWrapper = productrevGetReminderCounterUseCase.executeOnBackground()
            estimation.postValue(responseWrapper.productrevGetReminderCounter)
        }, onError = {})
    }

    fun fetchReminderTemplate() {
        launchCatchError(block = {
            val responseWrapper = productrevGetReminderTemplateUseCase.executeOnBackground()
            template.postValue(responseWrapper.productrevGetReminderTemplate)
        }, onError = {})
    }

    fun fetchProductList(){

    }

}
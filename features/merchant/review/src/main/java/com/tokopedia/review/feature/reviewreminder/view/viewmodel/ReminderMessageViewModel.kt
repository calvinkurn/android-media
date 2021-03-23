package com.tokopedia.review.feature.reviewreminder.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderCounter
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderList
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderTemplate
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderCounterUseCase
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderListUseCase
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderTemplateUseCase
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevSendReminderUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReminderMessageViewModel @Inject constructor(
        dispatcherProvider: CoroutineDispatchers,
        private val productrevGetReminderCounterUseCase: ProductrevGetReminderCounterUseCase,
        private val productrevGetReminderTemplateUseCase: ProductrevGetReminderTemplateUseCase,
        private val productrevGetReminderListUseCase: ProductrevGetReminderListUseCase,
        private val productrevSendReminderUseCase: ProductrevSendReminderUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private val estimation = MutableLiveData<ProductrevGetReminderCounter>()
    fun getEstimation(): LiveData<ProductrevGetReminderCounter> = estimation

    private val template = MutableLiveData<ProductrevGetReminderTemplate>()
    fun getTemplate(): LiveData<ProductrevGetReminderTemplate> = template

    private val products = MutableLiveData<Result<ProductrevGetReminderList>>()
    fun getProducts(): LiveData<Result<ProductrevGetReminderList>> = products

    private val error = MutableLiveData<String>()
    fun getError(): LiveData<String> = error

    fun fetchReminderCounter() {
        launchCatchError(block = {
            val responseWrapper = productrevGetReminderCounterUseCase.executeOnBackground()
            estimation.postValue(responseWrapper.productrevGetReminderCounter)
        }, onError = { error.postValue(it.message) })
    }

    fun fetchReminderTemplate() {
        launchCatchError(block = {
            val responseWrapper = productrevGetReminderTemplateUseCase.executeOnBackground()
            template.postValue(responseWrapper.productrevGetReminderTemplate)
        }, onError = { error.postValue(it.message) })
    }

    fun fetchProductList(lastProductId: String = "0") {
        launchCatchError(block = {
            productrevGetReminderListUseCase.setParams(lastProductId)
            val responseWrapper = productrevGetReminderListUseCase.executeOnBackground()
            products.postValue(Success(responseWrapper.productrevGetReminderList))
        }, onError = { products.postValue(Fail(it)) })
    }

    fun sendReminder(template: String?) {
        launchCatchError(block = {
            productrevSendReminderUseCase.setParams(template ?: "")
            val responseWrapper = productrevSendReminderUseCase.executeOnBackground()
            responseWrapper.productrevSendReminder
        }, onError = { error.postValue(it.message) })
    }

}
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
import javax.inject.Inject

class ReminderMessageViewModel @Inject constructor(
        dispatcherProvider: CoroutineDispatchers,
        private val productrevGetReminderCounterUseCase: ProductrevGetReminderCounterUseCase,
        private val productrevGetReminderTemplateUseCase: ProductrevGetReminderTemplateUseCase,
        private val productrevGetReminderListUseCase: ProductrevGetReminderListUseCase,
        private val productrevSendReminderUseCase: ProductrevSendReminderUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private var isLoadEstimation = false
    private var isLoadTemplate = false
    private var isLoadProducts = false

    private val isFetching = MutableLiveData<Boolean>()
    fun getFetchingStatus(): LiveData<Boolean> = isFetching

    private val estimation = MutableLiveData<ProductrevGetReminderCounter>()
    fun getEstimation(): LiveData<ProductrevGetReminderCounter> = estimation

    private val template = MutableLiveData<ProductrevGetReminderTemplate>()
    fun getTemplate(): LiveData<ProductrevGetReminderTemplate> = template

    private val products = MutableLiveData<ProductrevGetReminderList>()
    fun getProducts(): LiveData<ProductrevGetReminderList> = products

    private val error = MutableLiveData<String>()
    fun getError(): LiveData<String> = error

    fun fetchReminderCounter() {
        launchCatchError(block = {
            isLoadEstimation = true
            val responseWrapper = productrevGetReminderCounterUseCase.executeOnBackground()
            estimation.postValue(responseWrapper.productrevGetReminderCounter)
            isLoadEstimation = false
            checkFetchStatus()
        }, onError = {
            error.postValue(it.message)
            isLoadEstimation = false
            checkFetchStatus()
        })
    }

    fun fetchReminderTemplate() {
        launchCatchError(block = {
            isLoadTemplate = true
            val responseWrapper = productrevGetReminderTemplateUseCase.executeOnBackground()
            template.postValue(responseWrapper.productrevGetReminderTemplate)
            isLoadTemplate = false
            checkFetchStatus()
        }, onError = {
            error.postValue(it.message)
            isLoadTemplate = false
            checkFetchStatus()
        })
    }

    fun fetchProductList(lastProductId: String = "0") {
        launchCatchError(block = {
            isLoadProducts = true
            productrevGetReminderListUseCase.setParams(lastProductId)
            val responseWrapper = productrevGetReminderListUseCase.executeOnBackground()
            products.postValue(responseWrapper.productrevGetReminderList)
            isLoadProducts = false
            checkFetchStatus()
        }, onError = {
            error.postValue(it.message)
            isLoadProducts = false
            checkFetchStatus()
        })
    }

    fun sendReminder(template: String?) {
        launchCatchError(block = {
            productrevSendReminderUseCase.setParams(template ?: "")
            val responseWrapper = productrevSendReminderUseCase.executeOnBackground()
            responseWrapper.productrevSendReminder
        }, onError = { error.postValue(it.message) })
    }

    private fun checkFetchStatus() {
        if (!(isLoadEstimation || isLoadTemplate || isLoadProducts)) {
            isFetching.postValue(false)
        }
    }

}
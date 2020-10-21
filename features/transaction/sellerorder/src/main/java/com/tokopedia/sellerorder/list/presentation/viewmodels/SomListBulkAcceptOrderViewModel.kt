package com.tokopedia.sellerorder.list.presentation.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.list.domain.model.SomListBulkGetBulkAcceptOrderStatusParam
import com.tokopedia.sellerorder.list.domain.usecases.SomListBulkAcceptOrderUseCase
import com.tokopedia.sellerorder.list.domain.usecases.SomListGetBulkAcceptOrderStatusUseCase
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderStatusUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
import javax.inject.Inject

class SomListBulkAcceptOrderViewModel @Inject constructor(
        private val bulkAcceptOrderUseCase: SomListBulkAcceptOrderUseCase,
        private val bulkAcceptOrderStatusUseCase: SomListGetBulkAcceptOrderStatusUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: SomDispatcherProvider) : BaseViewModel(dispatcher.io()) {

    companion object {
        private const val MAX_RETRY = 20
        private const val DELAY = 1000L
    }

    private var retryCount = 0

    private val bulkAcceptOrderResult = MutableLiveData<Result<SomListBulkAcceptOrderUiModel>>()
    private var lastBulkAcceptOrderStatusSuccessResult: Result<SomListBulkAcceptOrderStatusUiModel>? = null

    private val _bulkAcceptOrderStatusResult = MediatorLiveData<Result<SomListBulkAcceptOrderStatusUiModel>>().apply {
        addSource(bulkAcceptOrderResult) {
            when (it) {
                is Success -> getBulkAcceptOrderStatus(it.data.data.batchId, 0L)
                is Fail -> Fail(it.throwable)
            }
        }
    }

    val bulkAcceptOrderStatusResult = MediatorLiveData<Result<SomListBulkAcceptOrderStatusUiModel>>()

    init {
        bulkAcceptOrderStatusResult.apply {
            addSource(_bulkAcceptOrderStatusResult) {
                when (it) {
                    is Success -> {
                        lastBulkAcceptOrderStatusSuccessResult = it
                        if (it.data.data.success == it.data.data.totalOrder) {
                            bulkAcceptOrderStatusResult.postValue(lastBulkAcceptOrderStatusSuccessResult)
                        } else if (retryCount < MAX_RETRY) {
                            retryCount++
                            getBulkAcceptOrderStatus((bulkAcceptOrderResult.value as Success).data.data.batchId, DELAY)
                        } else {
                            bulkAcceptOrderStatusResult.postValue(lastBulkAcceptOrderStatusSuccessResult)
                        }
                    }
                    is Fail -> {
                        if (retryCount < MAX_RETRY) {
                            retryCount++
                            getBulkAcceptOrderStatus((bulkAcceptOrderResult.value as Success).data.data.batchId, DELAY)
                        } else {
                            bulkAcceptOrderStatusResult.postValue(lastBulkAcceptOrderStatusSuccessResult)
                        }
                    }
                }
            }
        }
    }

    private fun getBulkAcceptOrderStatus(batchId: String, wait: Long) {
        launchCatchError(block = {
            delay(wait)
            bulkAcceptOrderStatusUseCase.setParams(SomListBulkGetBulkAcceptOrderStatusParam(
                    batchId = batchId,
                    shopId = userSession.shopId
            ))
            _bulkAcceptOrderStatusResult.postValue(Success(bulkAcceptOrderStatusUseCase.execute()))
        }, onError = {
            _bulkAcceptOrderStatusResult.postValue(Fail(it))
        })
    }

    fun bulkAcceptOrder(orderIds: List<String>) {
        launchCatchError(block = {
            retryCount = 0
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            bulkAcceptOrderResult.postValue(Success(bulkAcceptOrderUseCase.execute()))
        }, onError = {
            bulkAcceptOrderResult.postValue(Fail(it))
        })
    }

    fun reset() {
        lastBulkAcceptOrderStatusSuccessResult = null
        bulkAcceptOrderStatusResult.postValue(null)
    }
}
package com.tokopedia.product.manage.feature.stockreminder.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.domain.usecase.StockReminderDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class StockReminderViewModel @Inject constructor(private val stockReminderDataUseCase: StockReminderDataUseCase, coroutineDispatcher: CoroutineDispatcher): BaseViewModel(coroutineDispatcher) {

    val stockReminderLiveData = MutableLiveData<Result<GetStockReminderResponse>>()
    val stockReminderCreateLiveData = MutableLiveData<Result<CreateStockReminderResponse>>()

    fun getStockReminder(productId: String) {
        launchCatchError(block = {
            stockReminderDataUseCase.setGetStockParams(productId)
            val getStockReminder = stockReminderDataUseCase.executeGetStockReminder()
            stockReminderLiveData.postValue(Success(getStockReminder))
        }, onError = { errorThrowable ->
            stockReminderLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
        })
    }

    fun createStockReminder(shopId: String, productId: String, wareHouseId: String, threshold: String) {
        launchCatchError(block = {
            stockReminderDataUseCase.setCreateStockParams(shopId, productId, wareHouseId, threshold)
            val createStockReminder = stockReminderDataUseCase.executeCreateStockReminder()
            stockReminderCreateLiveData.postValue(Success(createStockReminder))
        }, onError = { errorThrowable ->
            stockReminderCreateLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
        })
    }

}
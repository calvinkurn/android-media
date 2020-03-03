package com.tokopedia.product.manage.feature.stockreminder.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.UpdateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.domain.usecase.StockReminderDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class StockReminderViewModel @Inject constructor(private val stockReminderDataUseCase: StockReminderDataUseCase, coroutineDispatcher: CoroutineDispatcher): BaseViewModel(coroutineDispatcher) {

    val getStockReminderLiveData = MutableLiveData<Result<GetStockReminderResponse>>()
    val createStockReminderLiveData = MutableLiveData<Result<CreateStockReminderResponse>>()
    val updateStockReminderLiveData = MutableLiveData<Result<UpdateStockReminderResponse>>()

    fun getStockReminder(productId: String) {
        launchCatchError(block = {
            stockReminderDataUseCase.setGetStockParams(productId)
            val getStockReminder = stockReminderDataUseCase.executeGetStockReminder()
            getStockReminderLiveData.postValue(Success(getStockReminder))
        }, onError = { errorThrowable ->
            getStockReminderLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
        })
    }

    fun createStockReminder(shopId: String, productId: String, wareHouseId: String, threshold: String) {
        launchCatchError(block = {
            stockReminderDataUseCase.setCreateStockParams(shopId, productId, wareHouseId, threshold)
            val createStockReminder = stockReminderDataUseCase.executeCreateStockReminder()
            createStockReminderLiveData.postValue(Success(createStockReminder))
        }, onError = { errorThrowable ->
            createStockReminderLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
        })
    }

    fun updateStockReminder(shopId: String, productId: String, wareHouseId: String, threshold: String) {
        launchCatchError(block = {
            stockReminderDataUseCase.setUpdateStockParams(shopId, productId, wareHouseId, threshold)
            val updateStockReminder = stockReminderDataUseCase.executeUpdateStockReminder()
            updateStockReminderLiveData.postValue(Success(updateStockReminder))
        }, onError = { errorThrowable ->
            updateStockReminderLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
        })
    }



}
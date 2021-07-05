package com.tokopedia.product.manage.feature.stockreminder.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.UpdateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.domain.usecase.StockReminderDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class StockReminderViewModel @Inject constructor(private val stockReminderDataUseCase: StockReminderDataUseCase, dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    val getStockReminderLiveData : LiveData<Result<GetStockReminderResponse>> get() = getStockReminderMutableLiveData
    val createStockReminderLiveData : LiveData<Result<CreateStockReminderResponse>> get() = createStockReminderMutableLiveData
    val updateStockReminderLiveData : LiveData<Result<UpdateStockReminderResponse>> get() = updateStockReminderMutableLiveData

    private val getStockReminderMutableLiveData = MutableLiveData<Result<GetStockReminderResponse>>()
    private val createStockReminderMutableLiveData = MutableLiveData<Result<CreateStockReminderResponse>>()
    private val updateStockReminderMutableLiveData = MutableLiveData<Result<UpdateStockReminderResponse>>()

    fun getStockReminder(productId: String) {
        launchCatchError(block = {
            stockReminderDataUseCase.setGetStockParams(productId)
            val getStockReminder = stockReminderDataUseCase.executeGetStockReminder()
            getStockReminderMutableLiveData.postValue(Success(getStockReminder))
        }, onError = { errorThrowable ->
            getStockReminderMutableLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
        })
    }

    fun createStockReminder(shopId: String, productId: String, wareHouseId: String, threshold: String) {
        launchCatchError(block = {
            stockReminderDataUseCase.setCreateStockParams(shopId, productId, wareHouseId, threshold)
            val createStockReminder = stockReminderDataUseCase.executeCreateStockReminder()
            createStockReminderMutableLiveData.postValue(Success(createStockReminder))
        }, onError = { errorThrowable ->
            createStockReminderMutableLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
        })
    }

    fun updateStockReminder(shopId: String, productId: String, wareHouseId: String, threshold: String) {
        launchCatchError(block = {
            stockReminderDataUseCase.setUpdateStockParams(shopId, productId, wareHouseId, threshold)
            val updateStockReminder = stockReminderDataUseCase.executeUpdateStockReminder()
            updateStockReminderMutableLiveData.postValue(Success(updateStockReminder))
        }, onError = { errorThrowable ->
            updateStockReminderMutableLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
        })
    }

}
package com.tokopedia.product.manage.feature.stockreminder.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.StockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.domain.usecase.GetStockReminderDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class StockReminderViewModel @Inject constructor(private val getStockReminderDataUseCase: GetStockReminderDataUseCase, coroutineDispatcher: CoroutineDispatcher): BaseViewModel(coroutineDispatcher) {

    val stockReminderLiveData = MutableLiveData<Result<StockReminderResponse>>()

    fun getStockReminderData() {
        launchCatchError(block = {
            getStockReminderDataUseCase.setParams("542830856,631603098")
            stockReminderLiveData.postValue(Success(getStockReminderDataUseCase.executeOnBackground()))
        }, onError = { errorThrowable ->
            stockReminderLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
        })
    }

}
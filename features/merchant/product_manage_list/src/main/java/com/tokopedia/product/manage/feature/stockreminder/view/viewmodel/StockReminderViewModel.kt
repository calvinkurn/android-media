package com.tokopedia.product.manage.feature.stockreminder.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.ProductWarehouseParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.domain.usecase.StockReminderDataUseCase
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel
import com.tokopedia.product.manage.feature.stockreminder.view.data.mapper.ProductStockReminderMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class StockReminderViewModel @Inject constructor(
    private val stockReminderDataUseCase: StockReminderDataUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val getProductLiveData: LiveData<Result<List<ProductStockReminderUiModel>>>
        get() = getProductMutableLiveData
    val getStockReminderLiveData: LiveData<Result<GetStockReminderResponse>>
        get() = getStockReminderMutableLiveData
    val createStockReminderLiveData: LiveData<Result<CreateStockReminderResponse>>
        get() = createStockReminderMutableLiveData
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val getProductMutableLiveData =
        MutableLiveData<Result<List<ProductStockReminderUiModel>>>()
    private val getStockReminderMutableLiveData =
        MutableLiveData<Result<GetStockReminderResponse>>()
    private val createStockReminderMutableLiveData =
        MutableLiveData<Result<CreateStockReminderResponse>>()
    private val _showLoading = MutableLiveData<Boolean>()


    fun getStockReminder(productId: String) {
        showLoading()
        launchCatchError(block = {
            stockReminderDataUseCase.setGetStockParams(productId)
            val getStockReminder = stockReminderDataUseCase.executeGetStockReminder()
            getStockReminderMutableLiveData.postValue(Success(getStockReminder))
            hideLoading()
        }, onError = { errorThrowable ->
            getStockReminderMutableLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
            hideLoading()
        })
    }

    fun createStockReminder(
        shopId: String,
        listProductWarehouseParam: ArrayList<ProductWarehouseParam>
    ) {
        showLoading()
        launchCatchError(block = {
            stockReminderDataUseCase.setCreateStockParams(shopId, listProductWarehouseParam)
            val createStockReminder = stockReminderDataUseCase.executeCreateStockReminder()
            createStockReminderMutableLiveData.postValue(Success(createStockReminder))
            hideLoading()
        }, onError = { errorThrowable ->
            createStockReminderMutableLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
            hideLoading()
        })
    }

    fun getProduct(productId: String, warehouseId: String) {
        showLoading()
        launchCatchError(block = {
            val response = stockReminderDataUseCase.executeGetProductStockReminder(productId, warehouseId)
            val product = response.getProductV3
            val data = ProductStockReminderMapper.mapToProductResult(product)
            getProductMutableLiveData.postValue(Success(data))
            hideLoading()
        }) {
            getProductMutableLiveData.postValue(Fail(it))
            hideLoading()
        }
    }

    fun showLoading() {
        _showLoading.value = true
    }

    fun hideLoading() {
        _showLoading.value = false
    }
}
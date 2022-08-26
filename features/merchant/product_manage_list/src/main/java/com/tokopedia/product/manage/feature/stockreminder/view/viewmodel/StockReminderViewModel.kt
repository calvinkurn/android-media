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
import com.tokopedia.shop.common.domain.interactor.GetMaxStockThresholdUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StockReminderViewModel @Inject constructor(
    private val stockReminderDataUseCase: StockReminderDataUseCase,
    private val getMaxStockThresholdUseCase: GetMaxStockThresholdUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val getProductLiveData: LiveData<Result<List<ProductStockReminderUiModel>>>
        get() = getProductMutableLiveData
    val getStockReminderLiveData: LiveData<Result<GetStockReminderResponse>>
        get() = getStockReminderMutableLiveData
    val createStockReminderLiveData: LiveData<Result<CreateStockReminderResponse>>
        get() = createStockReminderMutableLiveData
    val maxStockLiveData: LiveData<Int?>
        get() = maxStockMutableLiveData
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val getProductMutableLiveData =
        MutableLiveData<Result<List<ProductStockReminderUiModel>>>()
    private val getStockReminderMutableLiveData =
        MutableLiveData<Result<GetStockReminderResponse>>()
    private val createStockReminderMutableLiveData =
        MutableLiveData<Result<CreateStockReminderResponse>>()
    private val maxStockMutableLiveData =
        MutableLiveData<Int?>(null)
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

    fun getProduct(productId: String, warehouseId: String, shopId: String) {
        showLoading()
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                val productDeferred = async {
                    stockReminderDataUseCase.executeGetProductStockReminder(productId, warehouseId)
                }
                val maxStock = async {
                    try {
                        getMaxStockThresholdUseCase.execute(shopId)
                    } catch (ex: Exception) {
                        null
                    }
                }
                productDeferred.await().getProductV3 to maxStock.await()?.getMaxStockFromResponse()
            }
            val (product, maxStock) = response
            val data = ProductStockReminderMapper.mapToProductResult(product, maxStock)
            maxStockMutableLiveData.value = maxStock
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
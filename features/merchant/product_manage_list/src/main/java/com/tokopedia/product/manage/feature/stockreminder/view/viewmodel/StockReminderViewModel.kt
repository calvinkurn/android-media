package com.tokopedia.product.manage.feature.stockreminder.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageAccessMapper
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.ProductWarehouseParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.UpdateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.domain.usecase.GetProductStockReminderUseCase
import com.tokopedia.product.manage.feature.stockreminder.domain.usecase.StockReminderDataUseCase
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel
import com.tokopedia.product.manage.feature.stockreminder.view.data.mapper.ProductStockReminderMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StockReminderViewModel @Inject constructor(
    private val getProductUseCase: GetProductStockReminderUseCase,
    private val stockReminderDataUseCase: StockReminderDataUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val getProductLiveData: LiveData<Result<List<ProductStockReminderUiModel>>>
        get() = getProductMutableLiveData
    val getStockReminderLiveData: LiveData<Result<GetStockReminderResponse>>
        get() = getStockReminderMutableLiveData
    val createStockReminderLiveData: LiveData<Result<CreateStockReminderResponse>>
        get() = createStockReminderMutableLiveData
    val updateStockReminderLiveData: LiveData<Result<UpdateStockReminderResponse>>
        get() = updateStockReminderMutableLiveData
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val getProductMutableLiveData =
        MutableLiveData<Result<List<ProductStockReminderUiModel>>>()
    private val getStockReminderMutableLiveData =
        MutableLiveData<Result<GetStockReminderResponse>>()
    private val createStockReminderMutableLiveData =
        MutableLiveData<Result<CreateStockReminderResponse>>()
    private val updateStockReminderMutableLiveData =
        MutableLiveData<Result<UpdateStockReminderResponse>>()
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

    fun updateStockReminder(
        shopId: String,
        productId: String,
        wareHouseId: String,
        threshold: String
    ) {
        showLoading()
        launchCatchError(block = {
            stockReminderDataUseCase.setUpdateStockParams(shopId, productId, wareHouseId, threshold)
            val updateStockReminder = stockReminderDataUseCase.executeUpdateStockReminder()
            updateStockReminderMutableLiveData.postValue(Success(updateStockReminder))
            hideLoading()
        }, onError = { errorThrowable ->
            updateStockReminderMutableLiveData.postValue(Fail(errorThrowable))
            errorThrowable.printStackTrace()
            hideLoading()
        })
    }

    fun getProduct(productId: String, warehouseId: String) {
        showLoading()
        launchCatchError(block = {
            val requestParams =
                GetProductStockReminderUseCase.createRequestParams(productId, false, warehouseId)
            val response = getProductUseCase.execute(requestParams)
            val product = response.getProductV3
            val data = ProductStockReminderMapper.mapToProductResult(product)
            getProductMutableLiveData.postValue(Success(data))
            hideLoading()
        }) {
            getProductMutableLiveData.postValue(Fail(it))
            hideLoading()
        }
    }

    private fun showLoading() {
        _showLoading.value = true
    }

    private fun hideLoading() {
        _showLoading.value = false
    }
}
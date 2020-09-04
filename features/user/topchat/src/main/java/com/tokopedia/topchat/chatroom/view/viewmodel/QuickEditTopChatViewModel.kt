package com.tokopedia.topchat.chatroom.view.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.common.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.common.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper
import com.tokopedia.product.manage.common.feature.variant.domain.EditProductVariantUseCase
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.topchat.R
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuickEditTopChatViewModel @Inject constructor(
        private val editStockUseCase: EditStockUseCase,
        private val editProductVariantUseCase: EditProductVariantUseCase,
        private val dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    val editStockResult: LiveData<Result<EditStockResult>>
        get() = _editStockResult
    private val _editStockResult = MutableLiveData<Result<EditStockResult>>()

    val editVariantStockResult: LiveData<Result<EditVariantResult>>
        get() = _editVariantStockResult
    private val _editVariantStockResult = MutableLiveData<Result<EditVariantResult>>()

    fun editStock(shopId: String, productId: String, stock: Int, productName: String, status: ProductStatus) {
        launchCatchError(block =  {
            val result = withContext(dispatchers.io) {
                editStockUseCase.setParams(shopId, productId, stock, status)
                editStockUseCase.executeOnBackground()
            }
            when {
                result.productUpdateV3Data.isSuccess -> {
                    _editStockResult.postValue(Success(EditStockResult(productName, productId, stock, status)))
                }
                result.productUpdateV3Data.header.errorMessage.isNotEmpty() -> {
                    _editStockResult.postValue(Fail(EditStockResult(productName, productId, stock, status, Throwable(message = result.productUpdateV3Data.header.errorMessage.last()))))
                }
                else -> {
                    _editStockResult.postValue(Fail(EditStockResult(productName, productId, stock, status, NetworkErrorException(R.string.product_stock_reminder_toaster_failed_desc.toString()))))
                }
            }
        }) {
            _editStockResult.postValue(Fail(EditStockResult(productName, productId, stock, status, NetworkErrorException(R.string.product_stock_reminder_toaster_failed_desc.toString()))))
        }
    }

    fun editVariantsStock(shopId: String, result: EditVariantResult) {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                val variantInputParam = ProductManageVariantMapper.mapResultToUpdateParam(shopId, result)
                val requestParams = EditProductVariantUseCase.createRequestParams(variantInputParam)
                editProductVariantUseCase.execute(requestParams).productUpdateV3Data
            }

            if(response.isSuccess) {
                _editVariantStockResult.value = Success(result)
            } else {
                val message = response.header.errorMessage.lastOrNull().orEmpty()
                _editVariantStockResult.value = Fail(MessageErrorException(message))
            }
        }) {
            _editVariantStockResult.value = Fail(it)
        }
    }
}
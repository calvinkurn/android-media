package com.tokopedia.topchat.chatroom.view.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.common.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.common.feature.quickedit.stock.domain.EditStockUseCase
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
        private val dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    val editStockResult: LiveData<Result<EditStockResult>>
        get() = _editStockResult
    private val _editStockResult = MutableLiveData<Result<EditStockResult>>()

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
}
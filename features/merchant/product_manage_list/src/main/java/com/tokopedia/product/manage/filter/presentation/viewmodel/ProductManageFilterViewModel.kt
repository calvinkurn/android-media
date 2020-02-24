package com.tokopedia.product.manage.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.manage.filter.data.model.ProductListMetaData
import com.tokopedia.product.manage.filter.domain.GetProductListMetaUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductManageFilterViewModel @Inject constructor(
        private val getProductListMetaUseCase: GetProductListMetaUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _productListMetaData = MutableLiveData<Result<ProductListMetaData>>()
    val productListMetaData: LiveData<Result<ProductListMetaData>>
        get() = _productListMetaData

    fun getProductListMetaData(shopID: Int) {
        GetProductListMetaUseCase.createRequestParams(shopID.toString())
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val productListMetaDataResult = getProductListMetaUseCase.executeOnBackground()
                productListMetaDataResult.let {
                    _productListMetaData.postValue(Success(it.productListMetaData))
                }
            }
        }) {
            _productListMetaData.value = Fail(it)
        }
    }

}
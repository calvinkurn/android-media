package com.tokopedia.shopdiscount.manage.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.manage.data.mapper.ProductMapper
import com.tokopedia.shopdiscount.manage.domain.entity.ProductData
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceProductListUseCase: GetSlashPriceProductListUseCase,
    private val productMapper: ProductMapper
) : BaseViewModel(dispatchers.main) {

    private val _products = MutableLiveData<Result<ProductData>>()
    val products: LiveData<Result<ProductData>>
        get() = _products

    fun getSlashPriceProducts(page : Int, discountStatus : Int) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceProductListUseCase.setRequestParams(
                    page = page,
                    status = discountStatus
                )
                getSlashPriceProductListUseCase.executeOnBackground()
            }
            val formattedProduct = productMapper.map(result)
            val productData = ProductData(result.getSlashPriceProductList.totalProduct, formattedProduct)
            _products.value = Success(productData)
        }, onError = {
            _products.value = Fail(it)
        })
    }
}
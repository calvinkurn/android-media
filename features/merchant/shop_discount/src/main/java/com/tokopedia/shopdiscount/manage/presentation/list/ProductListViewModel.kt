package com.tokopedia.shopdiscount.manage.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.manage.data.mapper.ProductMapper
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.manage.domain.entity.ProductData
import com.tokopedia.shopdiscount.manage.domain.usecase.DeleteDiscountUseCase
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceProductListUseCase: GetSlashPriceProductListUseCase,
    private val productMapper: ProductMapper,
    private val deleteDiscountUseCase: DeleteDiscountUseCase
) : BaseViewModel(dispatchers.main) {

    private val _products = MutableLiveData<Result<ProductData>>()
    val products: LiveData<Result<ProductData>>
        get() = _products

    private val _deleteDiscount = MutableLiveData<Result<Boolean>>()
    val deleteDiscount: LiveData<Result<Boolean>>
        get() = _deleteDiscount

    private var totalProduct = 0
    private var selectedProduct : Product? = null

    fun getSlashPriceProducts(page : Int, discountStatus : Int) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceProductListUseCase.setRequestParams(
                    page = page,
                    status = discountStatus
                )
                val response = getSlashPriceProductListUseCase.executeOnBackground()
                val formattedProduct = productMapper.map(response)
                Pair(response.getSlashPriceProductList.totalProduct, formattedProduct)
            }

            val (totalProduct, formattedProduct) = result
            val productData = ProductData(totalProduct, formattedProduct)
            //val productData = ProductData((0..1).random(), emptyList())
            _products.value = Success(productData)

        }, onError = {
            _products.value = Fail(it)
        })
    }

    fun deleteDiscount(discountStatusId: Int, productId : String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                deleteDiscountUseCase.setParams(
                    discountStatusId = discountStatusId,
                    productIds = listOf(productId)
                )
                deleteDiscountUseCase.executeOnBackground()
            }

            _deleteDiscount.value = Success(result.doSlashPriceStop.responseHeader.success)

        }, onError = {
            _deleteDiscount.value = Fail(it)
        })
    }

    fun setTotalProduct(totalProduct : Int) {
        this.totalProduct = totalProduct
    }

    fun getTotalProduct(): Int {
        return this.totalProduct
    }

    fun setSelectedProduct(selectedProduct: Product) {
        this.selectedProduct = selectedProduct
    }

    fun getSelectedProduct() : Product?  {
        return selectedProduct
    }
}
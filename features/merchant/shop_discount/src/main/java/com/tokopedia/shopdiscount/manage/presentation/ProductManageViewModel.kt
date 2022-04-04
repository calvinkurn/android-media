package com.tokopedia.shopdiscount.manage.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.manage.data.mapper.ProductListMetaMapper
import com.tokopedia.shopdiscount.manage.data.mapper.ProductMapper
import com.tokopedia.shopdiscount.manage.domain.entity.DiscountStatusMeta
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListMetaUseCase
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListUseCase
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductManageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceProductListMetaUseCase: GetSlashPriceProductListMetaUseCase,
    private val productListMetaMapper: ProductListMetaMapper,
    private val getSlashPriceProductListUseCase: GetSlashPriceProductListUseCase,
    private val productMapper: ProductMapper
) : BaseViewModel(dispatchers.main) {

    private val _productsMeta = MutableLiveData<Result<List<DiscountStatusMeta>>>()
    val productsMeta: LiveData<Result<List<DiscountStatusMeta>>>
        get() = _productsMeta

    private val _products = MutableLiveData<Result<List<Product>>>()
    val products: LiveData<Result<List<Product>>>
        get() = _products

    private val _tabChanged = MutableLiveData<Int>()
    val tabChanged: LiveData<Int>
        get() = _tabChanged

    fun getSlashPriceProductsMeta() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceProductListMetaUseCase.setRequestParams()
                getSlashPriceProductListMetaUseCase.executeOnBackground()
            }
            val formattedProductMeta = productListMetaMapper.map(result.getSlashPriceProductListMeta.data.tab)

                val adjustedProductMeta = adjustOrdering(formattedProductMeta)
                _productsMeta.value = Success(adjustedProductMeta)

        }, onError = {
            _productsMeta.value = Fail(it)
        })
    }

    private fun adjustOrdering(formattedProductMeta: List<DiscountStatusMeta>): List<DiscountStatusMeta> {
        val comparator: Comparator<DiscountStatusMeta> =
            compareBy({ it.id == "PAUSED" }, { it.id == "SCHEDULED" }, { it.id == "ACTIVE" })
        return formattedProductMeta.sortedWith(comparator)
    }

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
            _products.value = Success(formattedProduct)
        }, onError = {
            _products.value = Fail(it)
        })
    }

    fun onTabChanged() {
        _tabChanged.value = DiscountStatus.ONGOING
    }
}
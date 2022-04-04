package com.tokopedia.shopdiscount.manage.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.manage.data.mapper.ProductListMetaMapper
import com.tokopedia.shopdiscount.manage.domain.entity.DiscountStatusMeta
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListMetaUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductManageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceProductListMetaUseCase: GetSlashPriceProductListMetaUseCase,
    private val productListMetaMapper: ProductListMetaMapper
) : BaseViewModel(dispatchers.main) {

    private val _productsMeta = MutableLiveData<Result<List<DiscountStatusMeta>>>()
    val productsMeta: LiveData<Result<List<DiscountStatusMeta>>>
        get() = _productsMeta


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
        return formattedProductMeta.sortedWith { o1, _ ->
            return@sortedWith when (o1?.name.orEmpty()) {
                "ACTIVE" -> 1
                "SCHEDULED" -> 0
                "PAUSED" -> -1
                else -> -2
            }
        }
    }
}
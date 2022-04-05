package com.tokopedia.shopdiscount.manage.presentation.container

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shopdiscount.manage.data.mapper.ProductListMetaMapper
import com.tokopedia.shopdiscount.manage.domain.entity.DiscountStatusMeta
import com.tokopedia.shopdiscount.manage.domain.entity.PageTab
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListMetaUseCase
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
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
                val response = getSlashPriceProductListMetaUseCase.executeOnBackground()
                productListMetaMapper.map(response.getSlashPriceProductListMeta.data.tab)
            }

            _productsMeta.value = Success(result)

        }, onError = {
            _productsMeta.value = Fail(it)
        })
    }

    fun findDiscountStatusCount(discountStatusMeta: List<DiscountStatusMeta>): List<PageTab> {
        val tabs = listOf(
            PageTab("Berlangsung", "ACTIVE", DiscountStatus.ONGOING),
            PageTab("Akan Datang", "SCHEDULED", DiscountStatus.SCHEDULED),
            PageTab("Dialihkan", "PAUSED", DiscountStatus.PAUSED)
        )

        return tabs.map { tab ->
            val match = discountStatusMeta.find { meta -> tab.status == meta.id }
            val tabName = "${match?.name} (${match?.productCount})"
            tab.copy(name = tabName)
        }
    }
}
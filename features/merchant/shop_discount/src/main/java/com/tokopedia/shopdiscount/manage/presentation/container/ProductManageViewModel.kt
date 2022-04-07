package com.tokopedia.shopdiscount.manage.presentation.container

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.manage.data.mapper.ProductListMetaMapper
import com.tokopedia.shopdiscount.manage.domain.entity.DiscountStatusMeta
import com.tokopedia.shopdiscount.manage.domain.entity.PageTab
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

    private var selectedTabPosition = 0
    private var tabs = mutableListOf<PageTab>()

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

    fun findDiscountStatusCount(tabs : List<PageTab>, discountStatusMeta: List<DiscountStatusMeta>): List<PageTab> {
        return tabs.map { tab ->
            val match = discountStatusMeta.find { meta -> tab.status == meta.id }
            tab.copy(count = match?.productCount.orZero())
        }
    }

    fun setTabs(tabs: List<PageTab>) {
        this.tabs.addAll(tabs)
    }

    fun setSelectedTabPosition(selectedTabPosition: Int) {
        this.selectedTabPosition = selectedTabPosition
    }

    fun getSelectedTab() : PageTab {
        return tabs[selectedTabPosition]
    }
}
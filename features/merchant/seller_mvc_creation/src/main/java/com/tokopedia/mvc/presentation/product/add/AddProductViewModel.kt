package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.usecase.GetShopWarehouseLocationUseCase
import com.tokopedia.mvc.domain.usecase.ProductListMetaUseCase
import com.tokopedia.mvc.domain.usecase.ShopShowcasesByShopIDUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class AddProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getShopWarehouseLocationUseCase: GetShopWarehouseLocationUseCase,
    private val getShopShowcasesByShopIDUseCase: ShopShowcasesByShopIDUseCase,
    private val getProductListMetaUseCase: ProductListMetaUseCase
) : BaseViewModel(dispatchers.main) {

    fun getShopWarehouseLocations() {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetShopWarehouseLocationUseCase.Param()
                val response = getShopWarehouseLocationUseCase.execute(param)
            },
            onError = { error ->

            }
        )

    }

    fun getShopShowcases() {
        launchCatchError(
            dispatchers.io,
            block = {
                val response = getShopShowcasesByShopIDUseCase.execute()
            },
            onError = { error ->

            }
        )

    }

    fun getProductListMeta(warehouseId : Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = ProductListMetaUseCase.Param(warehouseId)
                val response = getProductListMetaUseCase.execute(param)
            },
            onError = { error ->

            }
        )

    }
}

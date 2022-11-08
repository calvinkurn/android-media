package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetShopWarehouseLocationUseCase
import com.tokopedia.mvc.domain.usecase.ProductListMetaUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ShopShowcasesByShopIDUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class AddProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getShopWarehouseLocationUseCase: GetShopWarehouseLocationUseCase,
    private val getShopShowcasesByShopIDUseCase: ShopShowcasesByShopIDUseCase,
    private val getProductListMetaUseCase: ProductListMetaUseCase,
    private val getProductsUseCase: ProductListUseCase,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase
) : BaseViewModel(dispatchers.main) {

    fun getShopWarehouseLocations() {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetShopWarehouseLocationUseCase.Param()
                val response = getShopWarehouseLocationUseCase.execute(param)
                println()
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
                println()
            },
            onError = { error ->

            }
        )

    }

    fun getProductListMeta(warehouseId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = ProductListMetaUseCase.Param(warehouseId)
                val response = getProductListMetaUseCase.execute(param)
                println()
            },
            onError = { error ->

            }
        )

    }

    fun getProducts(
        warehouseId: Long,
        page: Int,
        sortId: String,
        sortDirection: String,
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = ProductListUseCase.Param(
                    warehouseId = warehouseId,
                    page = page,
                    pageSize = 10,
                    sortId = sortId,
                    sortDirection = sortDirection
                )
                val response = getProductsUseCase.execute(param)
                println()
            },
            onError = { error ->

            }
        )

    }

    fun getMaxProductSelection(
        action: GetInitiateVoucherPageUseCase.Param.Action,
        promoType: GetInitiateVoucherPageUseCase.Param.PromoType,
        isVoucherProduct: Boolean
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetInitiateVoucherPageUseCase.Param(action, promoType, isVoucherProduct)
                val response = getInitiateVoucherPageUseCase.execute(param)
                println()
            },
            onError = { error ->

            }
        )

    }
}

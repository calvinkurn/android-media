package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2Model
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred

abstract class BaseCategoryViewModel(
    private val getCategoryProductUseCase: GetCategoryProductUseCase,
    private val getProductAdsUseCase: GetProductAdsUseCase,
    private val addressData: TokoNowLocalAddress,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
): BaseTokoNowViewModel(
    getTargetedTickerUseCase = getTargetedTickerUseCase,
    getMiniCartUseCase = getMiniCartUseCase,
    addToCartUseCase = addToCartUseCase,
    updateCartUseCase = updateCartUseCase,
    deleteCartUseCase = deleteCartUseCase,
    affiliateService = affiliateService,
    addressData = addressData,
    userSession = userSession,
    dispatchers = dispatchers
) {

    protected abstract fun onSuccessGetCategoryProduct(
        response: AceSearchProductModel,
        categoryL2Model: CategoryL2Model
    )

    protected abstract fun onErrorGetCategoryProduct(
        error: Throwable,
        categoryL2Model: CategoryL2Model
    )

    protected abstract fun onSuccessGetProductAds(
        response: GetProductAdsResponse.ProductAdsResponse
    )

    protected abstract fun onErrorGetProductAds(
        error: Throwable
    )

    protected suspend fun getCategoryProductAsync(
        categoryL2Model: CategoryL2Model,
    ): Deferred<Unit?> = asyncCatchError(block = {
        val response = getCategoryProductUseCase.execute(
            chooseAddressData = getAddressData(),
            categoryIdL2 = categoryL2Model.id,
            uniqueId = getUniqueId()
        )
        onSuccessGetCategoryProduct(response, categoryL2Model)
    }) {
        onErrorGetCategoryProduct(it, categoryL2Model)
    }

    protected fun getProductAds(categoryId: String) {
        launchCatchError(block = {
            val params = GetProductAdsParam(
                categoryId = categoryId,
                warehouseIds = addressData.getWarehouseIds(),
                src = GetProductAdsParam.SRC_DIRECTORY_TOKONOW,
                userId = getUserId()
            )

            val response = getProductAdsUseCase.execute(params)
            onSuccessGetProductAds(response)
        }) {
            onErrorGetProductAds(it)
        }
    }

    private fun getUniqueId() = if (isLoggedIn()) {
        AuthHelper.getMD5Hash(getUserId())
    } else {
        AuthHelper.getMD5Hash(getDeviceId())
    }
}

package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.network.authentication.AuthHelper.Companion.getMD5Hash
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.DEFAULT_PRODUCT_QUANTITY
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryOpenScreenTrackerModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

open class TokoNowCategoryBaseViewModel @Inject constructor(
    addressData: TokoNowLocalAddress,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
): BaseTokoNowViewModel(
    addToCartUseCase,
    updateCartUseCase,
    deleteCartUseCase,
    getMiniCartUseCase,
    affiliateService,
    getTargetedTickerUseCase,
    addressData,
    userSession,
    dispatchers
) {
    init {
        miniCartSource = MiniCartSource.TokonowCategoryPage
    }

    private val _updateToolbarNotification: MutableLiveData<Boolean> = MutableLiveData()
    private val _openScreenTracker: MutableLiveData<CategoryOpenScreenTrackerModel> = MutableLiveData()
    private val _atcDataTracker: MutableLiveData<CategoryAtcTrackerModel> = MutableLiveData()

    val updateToolbarNotification: LiveData<Boolean> = _updateToolbarNotification
    val openScreenTracker: LiveData<CategoryOpenScreenTrackerModel> = _openScreenTracker
    val atcDataTracker: LiveData<CategoryAtcTrackerModel> = _atcDataTracker

    private fun updateToolbarNotification() {
        _updateToolbarNotification.postValue(true)
    }

    protected open fun updateProductCartQuantity(
        productId: String,
        quantity: Int,
        layoutType: CategoryLayoutType
    ) { /* nothing to do */ }

    protected fun sendOpenScreenTracker(detailResponse: CategoryDetailResponse) {
        _openScreenTracker.postValue(
            CategoryOpenScreenTrackerModel(
                id = detailResponse.categoryDetail.data.id,
                name = detailResponse.categoryDetail.data.name,
                url = detailResponse.categoryDetail.data.url
            )
        )
    }

    protected fun getUniqueId() = if (isLoggedIn()) getMD5Hash(getUserId()) else getMD5Hash(getDeviceId())

    fun onCartQuantityChanged(
        productId: String,
        quantity: Int,
        stock: Int,
        shopId: String,
        position: Int,
        isOos: Boolean,
        name: String,
        categoryIdL1: String,
        price: Int,
        headerName: String,
        layoutType: CategoryLayoutType,
    ) {
        onCartQuantityChanged(
            isVariant = false,
            productId = productId,
            shopId = shopId,
            quantity = quantity,
            stock = stock,
            onSuccessAddToCart = {
                updateProductCartQuantity(productId, quantity, layoutType)
                updateToolbarNotification()
                _atcDataTracker.postValue(
                    CategoryAtcTrackerModel(
                        categoryIdL1 = categoryIdL1,
                        index = position,
                        productId = productId,
                        warehouseId = getWarehouseId(),
                        isOos = isOos,
                        name = name,
                        price = price,
                        headerName = headerName,
                        quantity = quantity
                    )
                )
            },
            onSuccessUpdateCart = { _, _ ->
                updateProductCartQuantity(productId, quantity, layoutType)
                updateToolbarNotification()
            },
            onSuccessDeleteCart = { _, _ ->
                updateProductCartQuantity(productId, DEFAULT_PRODUCT_QUANTITY, layoutType)
                updateToolbarNotification()
            },
            onError = {
                updateProductCartQuantity(productId, quantity, layoutType)
            }
        )
    }
}

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
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

open class TokoNowCategoryBaseViewModel @Inject constructor(
    addressData: TokoNowLocalAddress,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
): BaseTokoNowViewModel(
    addToCartUseCase,
    updateCartUseCase,
    deleteCartUseCase,
    getMiniCartUseCase,
    addressData,
    userSession,
    dispatchers
) {
    init {
        miniCartSource = MiniCartSource.TokonowCategoryPage
    }

    private val _updateToolbarNotification: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val updateToolbarNotification: LiveData<Boolean> = _updateToolbarNotification

    protected open fun updateProductCartQuantity(
        productId: String,
        quantity: Int,
        layoutType: CategoryLayoutType
    ) { /* nothing to do */ }

    private fun updateToolbarNotification() {
        _updateToolbarNotification.postValue(true)
    }

    fun getUniqueId() = if (isLoggedIn()) getMD5Hash(getUserId()) else getMD5Hash(getDeviceId())

    fun onCartQuantityChanged(
        productId: String,
        quantity: Int,
        shopId: String,
        layoutType: CategoryLayoutType
    ) {
        onCartQuantityChanged(
            productId = productId,
            shopId = shopId,
            quantity = quantity,
            onSuccessAddToCart = {
                updateProductCartQuantity(productId, quantity, layoutType)
                updateToolbarNotification()
            },
            onSuccessUpdateCart = { miniCartItem, _ ->
                updateProductCartQuantity(productId, quantity, layoutType)
                updateToolbarNotification()
            },
            onSuccessDeleteCart = { miniCartItem, _ ->
                updateProductCartQuantity(productId, DEFAULT_PRODUCT_QUANTITY, layoutType)
                updateToolbarNotification()
            },
            onError = {
                updateProductCartQuantity(productId, quantity, layoutType)
            }
        )
    }
}

package com.tokopedia.minicart.common.simplified

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.promo.data.request.ValidateUseMvcOrderParam
import com.tokopedia.minicart.common.promo.data.request.ValidateUseMvcParam
import com.tokopedia.minicart.common.promo.data.request.ValidateUseMvcProductParam
import com.tokopedia.minicart.common.promo.domain.data.ValidateUseMvcData
import com.tokopedia.minicart.common.promo.domain.usecase.ValidateUseMvcUseCase
import com.tokopedia.purchase_platform.common.constant.CartConstant
import timber.log.Timber
import javax.inject.Inject

class MiniCartSimplifiedViewModel @Inject constructor(
    private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    private val validateUseMvcUseCase: ValidateUseMvcUseCase
) :
    ViewModel() {

    // Global Data
    internal var currentShopIds: List<String> = emptyList()
    internal var currentPromoId: String = "0"
    internal var currentPromoCode: String = ""
    internal var currentPageSource: MiniCartAnalytics.Page = MiniCartAnalytics.Page.MVC_PAGE
    internal var currentBusinessUnit: String = ""
    internal var currentSite: String = ""
    internal var source: MiniCartSource = MiniCartSource.MVC

    // State Data
    private val _miniCartSimplifiedState = MutableLiveData<MiniCartSimplifiedState>()
    val miniCartSimplifiedState: LiveData<MiniCartSimplifiedState>
        get() = _miniCartSimplifiedState

    // Widget Data
    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()
    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData

    // Promo MVC Data
    private val _validateUseMvcData = MutableLiveData<ValidateUseMvcData>()
    val validateUseMvcData: LiveData<ValidateUseMvcData>
        get() = _validateUseMvcData

    // Flag
    internal var isFirstValidate: Boolean = true

    // API Call & Callback

    fun getLatestWidgetState() {
        if (currentShopIds.isEmpty()) {
            if (_miniCartSimplifiedData.value == null) {
                _miniCartSimplifiedData.value = MiniCartSimplifiedData()
            }
            return
        }
        getMiniCartListSimplifiedUseCase.setParams(currentShopIds, currentPromoId, currentPromoCode, source)
        getMiniCartListSimplifiedUseCase.execute(
            onSuccess = {
                // hide mini cart simplified if has no available products
                it.isShowMiniCartWidget = it.miniCartWidgetData.totalProductCount > 0
                _miniCartSimplifiedData.value = it
                validateUseMvc(false)
            },
            onError = {
                if (_miniCartSimplifiedData.value == null) {
                    _miniCartSimplifiedData.value = MiniCartSimplifiedData()
                }
                _miniCartSimplifiedState.value = MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_FAILED_MINICART, throwable = it)
            }
        )
    }

    private fun generateValidateUseMvcParam(miniCartItems: Map<MiniCartItemKey, MiniCartItem>, isMoveToCart: Boolean): ValidateUseMvcParam {
        val firstAvailableItem = miniCartItems.values.first { it is MiniCartItem.MiniCartItemProduct && !it.isError } as MiniCartItem.MiniCartItemProduct
        val cartString = firstAvailableItem.cartString
        val shopId = firstAvailableItem.shopId
        return ValidateUseMvcParam(
            apply = isMoveToCart,
            codes = listOf(currentPromoCode),
            promoIds = listOf(currentPromoId),
            promoType = PROMO_TYPE_MVC,
            state = CartConstant.PARAM_CART,
            cartType = CartConstant.PARAM_DEFAULT,
            orders = listOf(
                ValidateUseMvcOrderParam(
                    uniqueId = cartString,
                    shopId = shopId,
                    productDetails = miniCartItems.values.mapNotNull {
                        if (it is MiniCartItem.MiniCartItemProduct && !it.isError) {
                            ValidateUseMvcProductParam(it.productId, it.quantity)
                        } else {
                            null
                        }
                    }
                )
            )
        )
    }

    fun moveToCart() {
        validateUseMvc(true)
    }

    private fun validateUseMvc(isMoveToCart: Boolean) {
        val miniCartSimplifiedData = _miniCartSimplifiedData.value
        if (miniCartSimplifiedData == null ||
            miniCartSimplifiedData.miniCartItems.isEmpty() ||
            miniCartSimplifiedData.miniCartWidgetData.totalProductCount <= 0
        ) {
            _miniCartSimplifiedState.value = MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_FAILED_VALIDATE_USE)
            return
        }
        validateUseMvcUseCase.setParam(generateValidateUseMvcParam(miniCartSimplifiedData.miniCartItems, isMoveToCart)).execute(
            onSuccess = {
                onSuccessValidateUseMvc(it, isMoveToCart)
            },
            onError = {
                onErrorValidateUseMvc(it, isMoveToCart)
            }
        )
    }

    private fun onSuccessValidateUseMvc(validateUseMvcData: ValidateUseMvcData, isMoveToCart: Boolean) {
        _validateUseMvcData.value = validateUseMvcData
        if (isMoveToCart) {
            _miniCartSimplifiedState.value = MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_MOVE_TO_CART)
        }
    }

    private fun onErrorValidateUseMvc(throwable: Throwable, isMoveToCart: Boolean) {
        Timber.d(throwable)
        if (isMoveToCart) {
            _miniCartSimplifiedState.value = MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_FAILED_VALIDATE_USE_MOVE_TO_CART, throwable = throwable)
        } else {
            _miniCartSimplifiedState.value = MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_FAILED_VALIDATE_USE, throwable = throwable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        getMiniCartListSimplifiedUseCase.cancelJobs()
        validateUseMvcUseCase.cancelJobs()
    }

    companion object {
        private const val PROMO_TYPE_MVC = "mvc"
    }
}

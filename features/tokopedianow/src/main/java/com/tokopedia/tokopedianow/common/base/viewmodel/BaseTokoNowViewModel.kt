package com.tokopedia.tokopedianow.common.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.tokopedianow.common.model.NowAffiliateAtcData
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.CoroutineUtil.launchWithDelay
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job

open class BaseTokoNowViewModel(
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val affiliateService: NowAffiliateService,
    private val addressData: TokoNowLocalAddress,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val CHANGE_QUANTITY_DELAY = 500L
        private const val OOC_WAREHOUSE_ID = 0L
        private const val INVALID_SHOP_ID = 0L
    }

    val addItemToCart: LiveData<Result<AddToCartDataModel>>
        get() = _addItemToCart
    val removeCartItem: LiveData<Result<Pair<String, String>>>
        get() = _removeCartItem
    val updateCartItem: LiveData<Result<Triple<String, UpdateCartV2Data, Int>>>
        get() = _updateCartItem
    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart

    var miniCartData: MiniCartSimplifiedData? = null
        private set

    private val _addItemToCart = MutableLiveData<Result<AddToCartDataModel>>()
    private val _removeCartItem = MutableLiveData<Result<Pair<String, String>>>()
    private val _updateCartItem = MutableLiveData<Result<Triple<String, UpdateCartV2Data, Int>>>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()

    private var changeQuantityJob: Job? = null
    private var getMiniCartJob: Job? = null

    open fun setMiniCartData(miniCartData: MiniCartSimplifiedData) {
        this.miniCartData = miniCartData
    }

    fun onQuantityChanged(
        productId: String,
        shopId: String,
        quantity: Int,
        stock: Int,
        isVariant: Boolean
    ) {
        changeQuantityJob?.cancel()
        launchWithDelay(block = {
            val miniCartItem = getMiniCartItem(productId)
            val cartQuantity = miniCartItem?.quantity
            if (cartQuantity == quantity) return@launchWithDelay

            when {
                miniCartItem == null && quantity.isMoreThanZero() -> {
                    addItemToCart(productId, shopId, quantity, stock, isVariant)
                }
                miniCartItem != null && quantity.isMoreThanZero() -> {
                    updateCartItem(miniCartItem, quantity, stock, isVariant)
                }
                miniCartItem != null -> {
                    val cartId = miniCartItem.cartId
                    deleteCartItem(productId, cartId)
                }
            }
        }, delay = CHANGE_QUANTITY_DELAY).let {
            changeQuantityJob = it
        }
    }

    fun addItemToCart(
        productId: String,
        shopId: String,
        quantity: Int,
        stock: Int,
        isVariant: Boolean
    ) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            checkAtcAffiliateCookie(productId, stock, isVariant, quantity)
            _addItemToCart.postValue(Success(it))
        }, {
            _addItemToCart.postValue(Fail(it))
        })
    }

    fun deleteCartItem(productId: String, cartId: String) {
        deleteCartUseCase.setParams(cartIdList = listOf(cartId))
        deleteCartUseCase.execute({
            val message = it.data.message.joinToString(separator = ", ")
            val data = Pair(productId, message)
            _removeCartItem.postValue(Success(data))
        }, {
            _removeCartItem.postValue(Fail(it))
        })
    }

    fun getMiniCart() {
        val shopId = getShopId()

        if (shouldGetMiniCart(shopId)) {
            getMiniCartJob?.cancel()
            launchCatchError(block = {
                val shopIds = listOf(shopId.toString())
                getMiniCartUseCase.setParams(shopIds, MiniCartSource.TokonowRecipe)

                val miniCartData = getMiniCartUseCase.executeOnBackground()
                val showMiniCart = miniCartData.isShowMiniCartWidget
                val outOfCoverage = addressData.isOutOfCoverage()
                val data = miniCartData.copy(isShowMiniCartWidget = showMiniCart && !outOfCoverage)

                setMiniCartData(miniCartData)
                _miniCart.postValue(Success(data))
            }) {
                _miniCart.postValue(Fail(it))
            }.let {
                getMiniCartJob = it
            }
        }
    }

    fun getMiniCartItem(productId: String): MiniCartItem.MiniCartItemProduct? {
        val items = miniCartData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
    }

    fun getShopId(): Long = addressData.getShopId()

    fun createAffiliateLink(url: String) = affiliateService.createAffiliateLink(url)

    fun getAffiliateShareInput() = affiliateService.createShareInput()

    fun initAffiliateCookie(affiliateUuid: String = "", affiliateChannel: String = "") {
        launchCatchError(block = {
            affiliateService.initAffiliateCookie(
                affiliateUuid,
                affiliateChannel
            )
        }) {
        }
    }

    protected fun checkAtcAffiliateCookie(
        productId: String,
        stock: Int,
        isVariant: Boolean,
        quantity: Int
    ) {
        val miniCartItem = getMiniCartItem(productId)
        val currentQuantity = miniCartItem?.quantity.orZero()
        val data = NowAffiliateAtcData(productId, stock, isVariant, quantity, currentQuantity)

        launchCatchError(block = {
            affiliateService.checkAtcAffiliateCookie(data)
        }) {
        }
    }

    protected fun updateMiniCartItemQuantity(
        miniCartItem: MiniCartItem.MiniCartItemProduct,
        quantity: Int
    ) {
        miniCartItem.quantity = quantity
    }

    private fun updateCartItem(
        miniCartItem: MiniCartItem.MiniCartItemProduct,
        quantity: Int,
        stock: Int,
        isVariant: Boolean
    ) {
        val productId = miniCartItem.productId
        val cartId = miniCartItem.cartId
        val notes = miniCartItem.notes
        val updateCartRequest = UpdateCartRequest(
            cartId = cartId,
            quantity = quantity,
            notes = notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
        )
        updateCartUseCase.execute({
            val data = Triple(productId, it, quantity)
            checkAtcAffiliateCookie(productId, stock, isVariant, quantity)
            updateMiniCartItemQuantity(miniCartItem, quantity)
            _updateCartItem.postValue(Success(data))
        }, {
            _updateCartItem.postValue(Fail(it))
        })
    }

    private fun shouldGetMiniCart(shopId: Long): Boolean {
        val warehouseId = addressData.getWarehouseId()
        val outOfCoverage = warehouseId == OOC_WAREHOUSE_ID
        return shopId != INVALID_SHOP_ID && !outOfCoverage && userSession.isLoggedIn
    }
}

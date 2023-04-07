package com.tokopedia.productcard.compact.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem.MiniCartItemProduct
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.productcard.compact.common.util.CoroutineUtil.launchWithDelay
import com.tokopedia.productcard.compact.common.helper.LocalAddressHelper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job

open class BaseCartViewModel(
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val addressData: LocalAddressHelper,
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

    protected var miniCartData: MiniCartSimplifiedData? = null
        private set

    private val _addItemToCart = MutableLiveData<Result<AddToCartDataModel>>()
    private val _removeCartItem = MutableLiveData<Result<Pair<String, String>>>()
    private val _updateCartItem = MutableLiveData<Result<Triple<String, UpdateCartV2Data, Int>>>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()

    private var changeQuantityJob: Job? = null
    private var getMiniCartJob: Job? = null

    var miniCartSource: MiniCartSource? = null

    open fun onSuccessGetMiniCartData(miniCartData: MiniCartSimplifiedData) {
        setMiniCartData(miniCartData)
    }

    fun onCartQuantityChanged(
        productId: String,
        shopId: String,
        quantity: Int,
        onSuccessAddToCart: (AddToCartDataModel) -> Unit = {},
        onSuccessUpdateCart: (MiniCartItemProduct, UpdateCartV2Data) -> Unit = { _, _ -> },
        onSuccessDeleteCart: (MiniCartItemProduct, RemoveFromCartData) -> Unit = { _, _ -> },
        onError: (Throwable) -> Unit = {}
    ) {
        changeQuantityJob?.cancel()

        val miniCartItem = getMiniCartItem(productId)
        val cartQuantity = miniCartItem.quantity
        if (cartQuantity == quantity) return

        launchWithDelay(block = {
            miniCartItem.quantity = quantity
            when {
                cartQuantity.isZero() -> addItemToCart(productId, shopId, quantity, onSuccessAddToCart, onError)
                quantity.isZero() -> deleteCartItem(miniCartItem, onSuccessDeleteCart, onError)
                else -> updateCartItem(miniCartItem, quantity, onSuccessUpdateCart, onError)
            }
        }, delay = CHANGE_QUANTITY_DELAY).let {
            changeQuantityJob = it
        }
    }

    fun getMiniCart() {
        getMiniCartJob?.cancel()

        val source = miniCartSource ?: return
        val shopId = getShopId()

        if (shouldGetMiniCart(shopId)) {
            launchCatchError(block = {
                val shopIds = listOf(shopId.toString())
                getMiniCartUseCase.setParams(shopIds, source)

                val miniCartData = getMiniCartUseCase.executeOnBackground()
                val showMiniCart = miniCartData.isShowMiniCartWidget
                val outOfCoverage = addressData.isOutOfCoverage()
                val data = miniCartData.copy(isShowMiniCartWidget = showMiniCart && !outOfCoverage)

                onSuccessGetMiniCartData(miniCartData)
                _miniCart.postValue(Success(data))
            }) {
                _miniCart.postValue(Fail(it))
            }.let {
                getMiniCartJob = it
            }
        }
    }

    fun getMiniCartItem(productId: String): MiniCartItemProduct {
        val items = miniCartData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId) ?: MiniCartItemProduct()
    }

    fun setMiniCartData(miniCartData: MiniCartSimplifiedData) {
        this.miniCartData = miniCartData
    }

    fun getShopId(): Long = addressData.getShopId()

    private fun addItemToCart(
        productId: String,
        shopId: String,
        quantity: Int,
        onSuccessAddToCart: (AddToCartDataModel) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            onSuccessAddToCart.invoke(it)
            _addItemToCart.postValue(Success(it))
        }, {
            onError.invoke(it)
            _addItemToCart.postValue(Fail(it))
        })
    }

    private fun updateCartItem(
        miniCartItem: MiniCartItemProduct,
        quantity: Int,
        onSuccessUpdateCart: (MiniCartItemProduct, UpdateCartV2Data) -> Unit,
        onError: (Throwable) -> Unit
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
            onSuccessUpdateCart.invoke(miniCartItem, it)
            _updateCartItem.postValue(Success(Triple(productId, it, quantity)))
        }, {
            onError.invoke(it)
            _updateCartItem.postValue(Fail(it))
        })
    }

    private fun deleteCartItem(
        miniCartItem: MiniCartItemProduct,
        onSuccessDeleteCart: (MiniCartItemProduct, RemoveFromCartData) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val cartId = miniCartItem.cartId
        val productId = miniCartItem.productId
        deleteCartUseCase.setParams(cartIdList = listOf(cartId))
        deleteCartUseCase.execute({
            val message = it.data.message.joinToString(separator = ", ")
            val data = Pair(productId, message)
            onSuccessDeleteCart.invoke(miniCartItem, it)
            _removeCartItem.postValue(Success(data))
        }, {
            onError.invoke(it)
            _removeCartItem.postValue(Fail(it))
        })
    }

    private fun shouldGetMiniCart(shopId: Long): Boolean {
        val warehouseId = addressData.getWarehouseId()
        val outOfCoverage = warehouseId == OOC_WAREHOUSE_ID
        return shopId != INVALID_SHOP_ID && !outOfCoverage && userSession.isLoggedIn
    }
}

package com.tokopedia.tokopedianow.common.base.viewmodel

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
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem.MiniCartItemProduct
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.common.domain.model.GetTickerData
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.NowAffiliateAtcData
import com.tokopedia.tokopedianow.common.model.ProductCartItem
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.CoroutineUtil.launchWithDelay
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

open class BaseTokoNowViewModel(
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val affiliateService: NowAffiliateService,
    private val getTargetedTickerUseCase: GetTargetedTickerUseCase,
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
    val openLoginPage: LiveData<Unit>
        get() = _openLoginPage

    protected var hasBlockedAddToCart: Boolean = false
    protected var miniCartData: MiniCartSimplifiedData? = null
        private set

    protected val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()
    protected val _blockAddToCart = MutableLiveData<Unit>()

    private val _addItemToCart = MutableLiveData<Result<AddToCartDataModel>>()
    private val _removeCartItem = MutableLiveData<Result<Pair<String, String>>>()
    private val _updateCartItem = MutableLiveData<Result<Triple<String, UpdateCartV2Data, Int>>>()
    private val _openLoginPage = MutableLiveData<Unit>()

    private var changeQuantityJob: Job? = null
    private var getMiniCartJob: Job? = null

    var miniCartSource: MiniCartSource? = null

    open fun onSuccessGetMiniCartData(miniCartData: MiniCartSimplifiedData) {
        setMiniCartData(miniCartData)
    }

    fun isLoggedIn(): Boolean = userSession.isLoggedIn

    fun getUserId(): String = userSession.userId

    fun getDeviceId(): String = userSession.deviceId

    fun onCartQuantityChanged(
        productId: String,
        shopId: String,
        quantity: Int,
        stock: Int,
        isVariant: Boolean,
        onSuccessAddToCart: (AddToCartDataModel) -> Unit = {},
        onSuccessUpdateCart: (MiniCartItemProduct, UpdateCartV2Data) -> Unit = { _, _ -> },
        onSuccessDeleteCart: (MiniCartItemProduct, RemoveFromCartData) -> Unit = { _, _ -> },
        onError: (Throwable) -> Unit = {}
    ) {
        if (userSession.isLoggedIn) {
            val product = ProductCartItem(productId, shopId, quantity, stock, isVariant)
            updateCartQuantity(product, onSuccessAddToCart, onSuccessUpdateCart, onSuccessDeleteCart, onError)
        } else {
            _openLoginPage.postValue(Unit)
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

    fun getWarehouseId(): String = addressData.getWarehouseId().toString()

    fun updateAddressData() = addressData.updateLocalDataIfAddressHasUpdated()

    fun createAffiliateLink(url: String) = affiliateService.createAffiliateLink(url)

    fun getAffiliateShareInput() = affiliateService.createShareInput()

    fun setAddressData(data: LocalCacheModel) {
        addressData.setLocalData(data)
    }

    fun initAffiliateCookie(affiliateUuid: String = "", affiliateChannel: String = "") {
        launchCatchError(block = {
            affiliateService.initAffiliateCookie(
                affiliateUuid,
                affiliateChannel
            )
        }) {
        }
    }

    suspend fun getTickerDataAsync(
        warehouseId: String,
        page: String
    ): Deferred<GetTickerData?> {
        return asyncCatchError(block = {
            val tickerList = getTargetedTickerUseCase.execute(
                warehouseId = warehouseId,
                page = page
            )
            TickerMapper.mapTickerData(tickerList)
        }) {
            null
        }
    }

    private fun checkAtcAffiliateCookie(
        productId: String,
        shopId: String,
        stock: Int,
        isVariant: Boolean,
        quantity: Int
    ) {
        val miniCartItem = getMiniCartItem(productId)
        val currentQuantity = miniCartItem.quantity
        val data = NowAffiliateAtcData(
            productId,
            shopId,
            stock,
            isVariant,
            quantity,
            currentQuantity
        )

        launchCatchError(block = {
            affiliateService.checkAtcAffiliateCookie(data)
        }) {
        }
    }

    private fun updateMiniCartItemQuantity(miniCartItem: MiniCartItemProduct, quantity: Int) {
        miniCartItem.quantity = quantity
    }

    private fun updateCartQuantity(
        product: ProductCartItem,
        onSuccessAddToCart: (AddToCartDataModel) -> Unit,
        onSuccessUpdateCart: (MiniCartItemProduct, UpdateCartV2Data) -> Unit,
        onSuccessDeleteCart: (MiniCartItemProduct, RemoveFromCartData) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if (hasBlockedAddToCart) {
            // this only blocks add to cart when using repurchase widget
            _blockAddToCart.postValue(Unit)
        } else {
            changeQuantityJob?.cancel()
            val productId = product.id
            val quantity = product.quantity

            val miniCartItem = getMiniCartItem(productId)
            val cartQuantity = miniCartItem.quantity
            if (cartQuantity == quantity) return

            launchWithDelay(block = {
                when {
                    cartQuantity.isZero() -> addItemToCart(product, onSuccessAddToCart, onError)
                    quantity.isZero() -> deleteCartItem(miniCartItem, onSuccessDeleteCart, onError)
                    else -> updateCartItem(product, miniCartItem, onSuccessUpdateCart, onError)
                }
            }, delay = CHANGE_QUANTITY_DELAY).let {
                changeQuantityJob = it
            }
        }
    }

    private fun addItemToCart(
        product: ProductCartItem,
        onSuccessAddToCart: (AddToCartDataModel) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val productId = product.id
        val shopId = product.shopId
        val quantity = product.quantity
        val stock = product.stock
        val isVariant = product.isVariant

        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            checkAtcAffiliateCookie(productId, shopId, stock, isVariant, quantity)
            onSuccessAddToCart.invoke(it)
            _addItemToCart.postValue(Success(it))
        }, {
            onError.invoke(it)
            _addItemToCart.postValue(Fail(it))
        })
    }

    private fun updateCartItem(
        product: ProductCartItem,
        miniCartItem: MiniCartItemProduct,
        onSuccessUpdateCart: (MiniCartItemProduct, UpdateCartV2Data) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val productId = product.id
        val shopId = product.shopId
        val quantity = product.quantity
        val stock = product.stock
        val isVariant = product.isVariant
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
            checkAtcAffiliateCookie(productId, shopId, stock, isVariant, quantity)
            updateMiniCartItemQuantity(miniCartItem, quantity)
            onSuccessUpdateCart.invoke(miniCartItem, it)
            _updateCartItem.postValue(Success(data))
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

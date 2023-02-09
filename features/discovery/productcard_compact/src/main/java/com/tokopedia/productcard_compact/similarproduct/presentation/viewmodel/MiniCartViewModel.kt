package com.tokopedia.productcard_compact.similarproduct.presentation.viewmodel

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
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.productcard_compact.common.util.CoroutineUtil.launchWithDelay
import com.tokopedia.productcard_compact.common.util.LocalAddress
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job

open class MiniCartViewModel(
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val addressData: LocalAddress,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

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

    fun onQuantityChanged(productId: String, shopId: String, quantity: Int) {
        changeQuantityJob?.cancel()
        launchWithDelay(block = {
            val miniCartItem = getMiniCartItem(productId)
            val cartId = miniCartItem?.cartId.orEmpty()
            val notes = miniCartItem?.notes.orEmpty()

            when {
                miniCartItem == null -> addItemToCart(productId, shopId, quantity)
                quantity.isZero() -> deleteCartItem(productId, cartId)
                else -> updateCartItem(productId, cartId, notes, quantity)
            }
        }, delay = CHANGE_QUANTITY_DELAY).let {
            changeQuantityJob = it
        }
    }

    fun addItemToCart(productId: String, shopId: String, quantity: Int) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
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

        if(shouldGetMiniCart(shopId)) {
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

    private fun updateCartItem(productId: String, cartId: String, notes: String, quantity: Int) {
        val updateCartRequest = UpdateCartRequest(
            cartId = cartId,
            quantity = quantity,
            notes = notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
        )
        updateCartUseCase.execute({
            _updateCartItem.postValue(Success(Triple(productId, it, quantity)))
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

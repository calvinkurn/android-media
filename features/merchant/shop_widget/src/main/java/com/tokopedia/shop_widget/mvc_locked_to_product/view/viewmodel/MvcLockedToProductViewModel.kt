package com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel

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
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.model.MvcLockedToProductAddToCartTracker
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductRequest
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductResponse
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.usecase.MvcLockedToProductUseCase
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductMapper
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductUtil
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class MvcLockedToProductViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val mvcLockedToProductUseCase: MvcLockedToProductUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {
    val isUserLogin: Boolean
        get() = userSession.isLoggedIn
    val userId: String
        get() = userSession.userId

    val nextPageLiveData: LiveData<Int>
        get() = _nextPageLiveData
    private val _nextPageLiveData = MutableLiveData<Int>()

    val mvcLockToProductLiveData: LiveData<Result<MvcLockedToProductLayoutUiModel>>
        get() = _mvcLockToProductLiveData
    private val _mvcLockToProductLiveData =
        MutableLiveData<Result<MvcLockedToProductLayoutUiModel>>()

    val productListDataProduct: LiveData<Result<List<MvcLockedToProductGridProductUiModel>>>
        get() = _productListData
    private val _productListData =
        MutableLiveData<Result<List<MvcLockedToProductGridProductUiModel>>>()

    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()

    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()

    val miniCartRemove: LiveData<Result<Pair<String,String>>>
        get() = _miniCartRemove
    private val _miniCartRemove = MutableLiveData<Result<Pair<String,String>>>()

    val mvcAddToCartTracker: LiveData<MvcLockedToProductAddToCartTracker>
        get() = _mvcAddToCartTracker
    private val _mvcAddToCartTracker = MutableLiveData<MvcLockedToProductAddToCartTracker>()

    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData
    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()

    fun getMvcLockedToProductData(mvcLockedToProductRequestUiModel: MvcLockedToProductRequestUiModel) {
        launchCatchError(dispatcherProvider.io, block = {
            val response = getMvcLockToProductResponse(mvcLockedToProductRequestUiModel)
            val uiModel = MvcLockedToProductMapper.mapToMvcLockedToProductLayoutUiModel(
                response.shopPageMVCProductLock,
                mvcLockedToProductRequestUiModel.selectedSortData
            )
            _mvcLockToProductLiveData.postValue(Success(uiModel))
            _nextPageLiveData.postValue(response.shopPageMVCProductLock.nextPage)
        }) {
            _mvcLockToProductLiveData.postValue(Fail(it))
        }
    }

    fun getProductListData(mvcLockedToProductRequestUiModel: MvcLockedToProductRequestUiModel) {
        launchCatchError(dispatcherProvider.io, block = {
            val response = getMvcLockToProductResponse(mvcLockedToProductRequestUiModel)
            val uiModel = MvcLockedToProductMapper.mapToMvcLockedToProductProductListUiModel(
                response.shopPageMVCProductLock.productList
            )
            _productListData.postValue(Success(uiModel))
            _nextPageLiveData.postValue(response.shopPageMVCProductLock.nextPage)
        }) {
            _productListData.postValue(Fail(it))
        }
    }

    fun isSellerView(shopId: String): Boolean {
        return MvcLockedToProductUtil.isSellerView(shopId, userSession.shopId)
    }

    private suspend fun getMvcLockToProductResponse(
        mvcLockedToProductRequestUiModel: MvcLockedToProductRequestUiModel
    ): MvcLockedToProductResponse {
        mvcLockedToProductUseCase.setParams(
            MvcLockedToProductRequest(
                mvcLockedToProductRequestUiModel.shopID,
                mvcLockedToProductRequestUiModel.promoID,
                mvcLockedToProductRequestUiModel.page,
                mvcLockedToProductRequestUiModel.perPage,
                mvcLockedToProductRequestUiModel.selectedSortData.value,
                mvcLockedToProductRequestUiModel.localCacheModel.district_id,
                mvcLockedToProductRequestUiModel.localCacheModel.city_id,
                mvcLockedToProductRequestUiModel.localCacheModel.lat,
                mvcLockedToProductRequestUiModel.localCacheModel.long
            )
        )
        return mvcLockedToProductUseCase.executeOnBackground()
    }

    fun setMiniCartData(miniCart: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.value = miniCart
    }

    fun handleAtcFlow(productId: String, quantity: Int, shopId: String) {
        val miniCartItem = getMiniCartItem(productId)
        when {
            miniCartItem == null -> addItemToCart(productId, shopId, quantity)
            quantity.isZero() -> removeItemCart(miniCartItem)
            else -> updateItemCart(miniCartItem, quantity)
        }
    }

    private fun addItemToCart(
        productId: String,
        shopId: String,
        quantity: Int
    ) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            trackAddToCart(
                it.data.cartId,
                it.data.productId.toString(),
                it.data.quantity,
                MvcLockedToProductAddToCartTracker.AtcType.ADD
            )
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
        })
    }

    private fun trackAddToCart(
        cartId: String,
        productId: String,
        quantity: Int,
        atcType: MvcLockedToProductAddToCartTracker.AtcType
    ) {
        val mvcLockedToProductAddToCartTracker = MvcLockedToProductAddToCartTracker(
            cartId,
            productId,
            quantity,
            atcType
        )
        _mvcAddToCartTracker.postValue(mvcLockedToProductAddToCartTracker)
    }

    private fun removeItemCart(miniCartItem: MiniCartItem) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            val productId = miniCartItem.productId
            val data = Pair(productId, it.data.message.joinToString(separator = ", "))
            trackAddToCart(
                miniCartItem.cartId,
                miniCartItem.productId,
                miniCartItem.quantity,
                MvcLockedToProductAddToCartTracker.AtcType.REMOVE
            )
            _miniCartRemove.postValue(Success(data))
        }, {
            _miniCartRemove.postValue(Fail(it))
        })
    }

    private fun updateItemCart(
        miniCartItem: MiniCartItem,
        quantity: Int
    ) {
        val existingQuantity = miniCartItem.quantity
        miniCartItem.quantity = quantity
        val cartId = miniCartItem.cartId
        val updateCartRequest = UpdateCartRequest(
            cartId = cartId,
            quantity = miniCartItem.quantity,
            notes = miniCartItem.notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
        )
        updateCartUseCase.execute({
            val atcType = if(quantity < existingQuantity){
                MvcLockedToProductAddToCartTracker.AtcType.UPDATE_REMOVE
            } else {
                MvcLockedToProductAddToCartTracker.AtcType.UPDATE_ADD
            }
            trackAddToCart(
                miniCartItem.cartId,
                miniCartItem.productId,
                miniCartItem.quantity,
                atcType
            )
            _miniCartUpdate.value = Success(it)
        }, {
            _miniCartUpdate.postValue(Fail(it))
        })
    }

    private fun getMiniCartItem(productId: String): MiniCartItem? {
        val items = miniCartSimplifiedData.value?.miniCartItems.orEmpty()
        return items.firstOrNull { it.productId == productId }
    }
}
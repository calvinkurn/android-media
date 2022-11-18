package com.tokopedia.tokopedianow.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
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
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationViewUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowProductRecommendationViewModel @Inject constructor(
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    private val _productRecommendation = MutableLiveData<Result<TokoNowProductRecommendationViewUiModel>>()
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()
    private val _miniCartRemove = MutableLiveData<Result<Pair<String,String>>>()
    private val _productModelsUpdate = MutableLiveData<List<Visitable<*>>>()

    private var productModels : MutableList<Visitable<*>> = mutableListOf()
    private var mMiniCartSimplifiedData: MiniCartSimplifiedData? = null
    private var job: Job? = null

    val productRecommendation: LiveData<Result<TokoNowProductRecommendationViewUiModel>>
        get() = _productRecommendation
    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    val miniCartRemove: LiveData<Result<Pair<String,String>>>
        get() = _miniCartRemove
    val productModelsUpdate: LiveData<List<Visitable<*>>>
        get() = _productModelsUpdate

    private fun getMiniCartItem(productId: String): MiniCartItem.MiniCartItemProduct? {
        val items = mMiniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
    }

    fun getRecommendationCarousel(
        requestParam: GetRecommendationRequestParam
    ) {
        job?.cancel()
        job = launchCatchError(
            block = {
                val result = getRecommendationUseCase.getData(requestParam)
                if (result.isNotEmpty()) {
                    val productRecommendation = ProductRecommendationMapper.mapResponseToProductRecommendation(
                        recommendationWidget = result.first(),
                        miniCartData = mMiniCartSimplifiedData
                    )
                    if (productRecommendation.productModels.isEmpty()) {
                        _productRecommendation.postValue(Fail(Throwable()))
                    } else {
                        productModels = productRecommendation.productModels.toMutableList()
                        if (productRecommendation.seeMoreModel.appLink.isNotEmpty()) {
                            productModels.add(productRecommendation.seeMoreModel)
                        }
                        _productRecommendation.postValue(Success(productRecommendation))
                    }
                } else {
                    _productRecommendation.postValue(Fail(Throwable()))
                }
            }, onError = { throwable ->
                _productRecommendation.postValue(Fail(throwable))
            }
        )
    }

    fun addItemToCart(
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
            updateProductQuantity(
                productId = productId,
                quantity = quantity
            )
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
        })
    }

    private fun updateItemCart(
        miniCartItem: MiniCartItem.MiniCartItemProduct,
        quantity: Int
    ) {
        miniCartItem.quantity = quantity
        val productId = miniCartItem.productId
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
            updateProductQuantity(
                productId = productId,
                quantity = quantity
            )
            _miniCartUpdate.value = Success(it)
        }, {
            _miniCartUpdate.postValue(Fail(it))
        })
    }

    private fun removeItemCart(
        miniCartItem: MiniCartItem.MiniCartItemProduct
    ) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            val productId = miniCartItem.productId
            val data = Pair(productId, it.data.message.joinToString(separator = ", "))
            updateProductQuantity(
                productId = productId,
                quantity = 0
            )
            _miniCartRemove.postValue(Success(data))
        }, {
            _miniCartRemove.postValue(Fail(it))
        })
    }

    private fun updateProductQuantity(
        productId: String,
        quantity: Int
    ) {
        productModels.filterIsInstance<TokoNowProductCardCarouselItemUiModel>().firstOrNull { it.productCardModel.productId == productId }?.let { model ->
            val index = productModels.indexOf(model)
            productModels[index] = model.copy(
                productCardModel = model.productCardModel.copy(
                    orderQuantity = quantity
                )
            )
        }
    }

    private fun resetAllProductQuantities() {
        productModels.filterIsInstance<TokoNowProductCardCarouselItemUiModel>().forEachIndexed { index, model ->
            productModels[index] = model.copy(
                productCardModel = model.productCardModel.copy(
                    orderQuantity = 0
                )
            )
        }
    }

    private fun updateProductQuantityBasedOnMiniCart(miniCartSimplifiedData: MiniCartSimplifiedData) {
        miniCartSimplifiedData.miniCartItems.values.forEach { miniCartItem ->
            if (miniCartItem is MiniCartItem.MiniCartItemProduct) {
                val productId = miniCartItem.productId
                val quantity = HomeLayoutMapper.getAddToCartQuantity(productId, mMiniCartSimplifiedData)

                updateProductQuantity(
                    productId = productId,
                    quantity = quantity
                )
            }
        }
    }

    private fun resetProductQuantityBasedOnMiniCart(miniCartSimplifiedData: MiniCartSimplifiedData) {
        val cartProductIds = miniCartSimplifiedData.miniCartItems.values.mapNotNull {
            if (it is MiniCartItem.MiniCartItemProduct) it.productId else null
        }
        val unavailableProducts = productModels.filter { it is TokoNowProductCardCarouselItemUiModel && it.productCardModel.productId !in cartProductIds }
        unavailableProducts.forEach { model ->
            if ((model as TokoNowProductCardCarouselItemUiModel).parentId != "0") {
                val totalQuantity = miniCartSimplifiedData.miniCartItems.getMiniCartItemParentProduct(model.parentId)?.totalQuantity.orZero()
                if (totalQuantity == HomeLayoutMapper.DEFAULT_QUANTITY) {
                    updateProductQuantity(
                        productId = model.productCardModel.productId,
                        quantity = 0
                    )
                } else {
                    updateProductQuantity(
                        productId = model.productCardModel.productId,
                        quantity = totalQuantity
                    )
                }
            } else {
                updateProductQuantity(
                    productId = model.productCardModel.productId,
                    quantity = 0
                )
            }
        }
    }

    fun addProductToCart(
        productId: String,
        quantity: Int,
        shopId: String
    ) {
        val miniCartItem = getMiniCartItem(productId)
        when {
            miniCartItem == null && quantity.isZero() -> { /* do nothing */ }
            miniCartItem == null -> addItemToCart(productId, shopId, quantity)
            quantity.isZero() -> removeItemCart(miniCartItem)
            else -> updateItemCart(miniCartItem, quantity)
        }
    }

    fun updateUi(
        productId: String,
        quantity: Int
    ) {
        val miniCartItem = getMiniCartItem(productId)
        if (quantity.isZero() && miniCartItem != null) {
            removeItemCart(
                miniCartItem = miniCartItem
            )
        } else {
            updateProductQuantity(
                productId = productId,
                quantity = quantity
            )

            _productModelsUpdate.value = productModels
        }
    }

    fun updateMiniCartSimplified(miniCartSimplifiedData: MiniCartSimplifiedData?) {
        mMiniCartSimplifiedData = miniCartSimplifiedData

        launch {
            mMiniCartSimplifiedData?.apply {
                if (miniCartItems.values.isEmpty()) {
                    resetAllProductQuantities()
                } else {
                    updateProductQuantityBasedOnMiniCart(this)
                    resetProductQuantityBasedOnMiniCart(this)
                }
                _productModelsUpdate.postValue(productModels)
            }
        }
    }

    fun updateProductRecommendation(requestParam: GetRecommendationRequestParam) {
        if (job != null) {
            getRecommendationCarousel(requestParam)
        }
    }
}

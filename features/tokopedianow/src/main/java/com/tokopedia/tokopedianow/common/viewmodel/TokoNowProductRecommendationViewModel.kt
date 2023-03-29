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
import com.tokopedia.tokopedianow.common.analytics.model.AddToCartDataTrackerModel
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper.mapResponseToProductRecommendation
import com.tokopedia.tokopedianow.common.domain.mapper.VisitableMapper.NO_ORDER_QUANTITY
import com.tokopedia.tokopedianow.common.domain.mapper.VisitableMapper.resetAllProductCardCarouselItemQuantities
import com.tokopedia.tokopedianow.common.domain.mapper.VisitableMapper.updateProductCardCarouselItemQuantity
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationViewUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowProductRecommendationViewModel @Inject constructor(
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {
    companion object {
        private const val NON_VARIANT_PRODUCT = "0"
    }

    private val _productRecommendation = MutableLiveData<Result<TokoNowProductRecommendationViewUiModel>>()
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()
    private val _miniCartRemove = MutableLiveData<Result<Pair<String, String>>>()
    private val _productModelsUpdate = MutableLiveData<List<Visitable<*>>>()
    private val _atcDataTracker = MutableLiveData<AddToCartDataTrackerModel>()
    private val _loadingState = MutableLiveData<Boolean>()

    private var productModels: MutableList<Visitable<*>> = mutableListOf()
    private var productRecommendationPageNames: MutableList<String> = mutableListOf()
    private var mMiniCartSimplifiedData: MiniCartSimplifiedData? = null
    private var job: Job? = null

    val productRecommendation: LiveData<Result<TokoNowProductRecommendationViewUiModel>>
        get() = _productRecommendation
    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    val miniCartRemove: LiveData<Result<Pair<String, String>>>
        get() = _miniCartRemove
    val productModelsUpdate: LiveData<List<Visitable<*>>>
        get() = _productModelsUpdate
    val atcDataTracker: LiveData<AddToCartDataTrackerModel>
        get() = _atcDataTracker
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    val isLogin: Boolean
        get() = userSession.isLoggedIn
    val userId: String
        get() = userSession.userId

    private fun getMiniCartItem(productId: String): MiniCartItem.MiniCartItemProduct? {
        val items = mMiniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
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
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
        )
        updateCartUseCase.execute({
            productModels.updateProductCardCarouselItemQuantity(
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
            productModels.updateProductCardCarouselItemQuantity(
                productId = productId,
                quantity = NO_ORDER_QUANTITY
            )
            _miniCartRemove.postValue(Success(data))
        }, {
            _miniCartRemove.postValue(Fail(it))
        })
    }

    private fun updateProductQuantityBasedOnMiniCart(miniCartSimplifiedData: MiniCartSimplifiedData) {
        miniCartSimplifiedData.miniCartItems.values.forEach { miniCartItem ->
            if (miniCartItem is MiniCartItem.MiniCartItemProduct) {
                val productId = miniCartItem.productId
                val quantity = HomeLayoutMapper.getAddToCartQuantity(productId, mMiniCartSimplifiedData)

                productModels.updateProductCardCarouselItemQuantity(
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
        unavailableProducts.forEach { product ->
            if ((product as TokoNowProductCardCarouselItemUiModel).parentId != NON_VARIANT_PRODUCT) {
                val totalQuantity = miniCartSimplifiedData.miniCartItems.getMiniCartItemParentProduct(product.parentId)?.totalQuantity.orZero()
                if (totalQuantity == HomeLayoutMapper.DEFAULT_QUANTITY) {
                    productModels.updateProductCardCarouselItemQuantity(
                        productId = product.productCardModel.productId,
                        quantity = NO_ORDER_QUANTITY
                    )
                } else {
                    productModels.updateProductCardCarouselItemQuantity(
                        productId = product.productCardModel.productId,
                        quantity = totalQuantity
                    )
                }
            } else {
                productModels.updateProductCardCarouselItemQuantity(
                    productId = product.productCardModel.productId,
                    quantity = NO_ORDER_QUANTITY
                )
            }
        }
    }

    fun setRecommendationPageName(type: String) {
        if (type !in productRecommendationPageNames) productRecommendationPageNames.add(type)
    }

    fun getRecommendationCarousel(
        requestParam: GetRecommendationRequestParam
    ) {
        job?.cancel()
        job = launchCatchError(
            block = {
                _loadingState.postValue(true)
                val result = getRecommendationUseCase.getData(requestParam)
                if (result.isNotEmpty()) {
                    val productRecommendation = mapResponseToProductRecommendation(
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
                _loadingState.postValue(false)
            },
            onError = { throwable ->
                _productRecommendation.postValue(Fail(throwable))
                _loadingState.postValue(false)
            }
        )
    }

    fun addItemToCart(
        position: Int,
        shopId: String,
        quantity: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = product.productCardModel.productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            productModels.updateProductCardCarouselItemQuantity(
                productId = product.productCardModel.productId,
                quantity = quantity
            )
            _atcDataTracker.postValue(
                AddToCartDataTrackerModel(
                    position = position,
                    quantity = it.data.quantity,
                    cartId = it.data.cartId,
                    productRecommendation = product
                )
            )
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
        })
    }

    fun addProductToCart(
        position: Int,
        quantity: Int,
        shopId: String
    ) {
        if (productModels.size > position && productModels[position] is TokoNowProductCardCarouselItemUiModel) {
            val product = productModels[position] as TokoNowProductCardCarouselItemUiModel
            val miniCartItem = getMiniCartItem(product.productCardModel.productId)
            when {
                miniCartItem == null && quantity.isZero() -> { /* do nothing */ }
                miniCartItem == null -> addItemToCart(position, shopId, quantity, product)
                quantity.isZero() -> removeItemCart(miniCartItem)
                else -> updateItemCart(miniCartItem, quantity)
            }
        }
    }

    fun updateMiniCartSimplified(miniCartSimplifiedData: MiniCartSimplifiedData?) {
        mMiniCartSimplifiedData = miniCartSimplifiedData

        launch {
            mMiniCartSimplifiedData?.apply {
                if (miniCartItems.values.isEmpty()) {
                    productModels.resetAllProductCardCarouselItemQuantities()
                } else {
                    updateProductQuantityBasedOnMiniCart(this)
                    resetProductQuantityBasedOnMiniCart(this)
                }
                _productModelsUpdate.postValue(productModels)
            }
        }
    }

    fun updateProductRecommendation(requestParam: GetRecommendationRequestParam) {
        if (job != null && requestParam.pageName in productRecommendationPageNames) {
            getRecommendationCarousel(requestParam)
        }
    }
}

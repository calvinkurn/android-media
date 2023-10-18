package com.tokopedia.tokopedianow.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.analytics.model.AddToCartDataTrackerModel
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper.mapResponseToProductRecommendation
import com.tokopedia.tokopedianow.common.domain.mapper.VisitableMapper.NO_ORDER_QUANTITY
import com.tokopedia.tokopedianow.common.domain.mapper.VisitableMapper.resetAllProductCardCarouselItemQuantities
import com.tokopedia.tokopedianow.common.domain.mapper.VisitableMapper.updateProductCardCarouselItemQuantity
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationViewUiModel
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowProductRecommendationViewModel @Inject constructor(
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val addressData: TokoNowLocalAddress,
    userSession: UserSessionInterface,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    dispatchers: CoroutineDispatchers
) : BaseTokoNowViewModel(
    addToCartUseCase,
    updateCartUseCase,
    deleteCartUseCase,
    getMiniCartUseCase,
    affiliateService,
    getTargetedTickerUseCase,
    addressData,
    userSession,
    dispatchers
)  {
    companion object {
        private const val NON_VARIANT_PRODUCT = "0"
    }

    private val _productRecommendation = MutableLiveData<Result<TokoNowProductRecommendationViewUiModel>>()
    private val _productModelsUpdate = MutableLiveData<List<Visitable<*>>>()
    private val _atcDataTracker = MutableLiveData<AddToCartDataTrackerModel>()
    private val _loadingState = MutableLiveData<Boolean>()
    private val _updateToolbarNotification = MutableLiveData<Boolean>()

    private var productModels: MutableList<Visitable<*>> = mutableListOf()
    private var productRecommendationPageNames: MutableList<String> = mutableListOf()
    private var job: Job? = null

    val productRecommendation: LiveData<Result<TokoNowProductRecommendationViewUiModel>>
        get() = _productRecommendation
    val productModelsUpdate: LiveData<List<Visitable<*>>>
        get() = _productModelsUpdate
    val atcDataTracker: LiveData<AddToCartDataTrackerModel>
        get() = _atcDataTracker
    val loadingState: LiveData<Boolean>
        get() = _loadingState
    val updateToolbarNotification: LiveData<Boolean>
        get() = _updateToolbarNotification

    private fun updateProductQuantityBasedOnMiniCart(miniCartSimplifiedData: MiniCartSimplifiedData) {
        miniCartSimplifiedData.miniCartItems.values.forEach { miniCartItem ->
            if (miniCartItem is MiniCartItem.MiniCartItemProduct) {
                val productId = miniCartItem.productId
                val quantity = HomeLayoutMapper.getAddToCartQuantity(productId, miniCartData)

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
        val unavailableProducts = productModels.filter { it is ProductCardCompactCarouselItemUiModel && it.productCardModel.productId !in cartProductIds }
        unavailableProducts.forEach { product ->
            if ((product as ProductCardCompactCarouselItemUiModel).parentId != NON_VARIANT_PRODUCT) {
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

    fun getFirstRecommendationCarousel(requestParam: GetRecommendationRequestParam, tickerPageSource: String) {
        launch {
            val tickerData = getTickerDataAsync(addressData.getWarehouseId().toString(), tickerPageSource).await()
            hasBlockedAddToCart = tickerData?.blockAddToCart.orFalse()
            getRecommendationCarousel(requestParam)
        }
    }

    fun onCartQuantityChanged(
        position: Int,
        quantity: Int,
        shopId: String
    ) {
        if (productModels.size > position && productModels[position] is ProductCardCompactCarouselItemUiModel) {
            val product = productModels[position] as ProductCardCompactCarouselItemUiModel
            val productId = product.getProductId()
            val stock = product.productCardModel.availableStock
            val isVariant = product.productCardModel.isVariant

            onCartQuantityChanged(
                productId = productId,
                quantity = quantity,
                shopId = shopId,
                stock = stock,
                isVariant = isVariant,
                onSuccessAddToCart = {
                    updateProductItemQuantity(productId, quantity)
                    trackProductAddToCart(position, it, product)
                    updateToolbarNotification()
                },
                onSuccessUpdateCart = { _, _ ->
                    updateProductItemQuantity(productId, quantity)
                    updateToolbarNotification()
                },
                onSuccessDeleteCart = { _, _ ->
                    updateProductItemQuantity(productId, quantity)
                    updateToolbarNotification()
                }
            )
        }
    }

    fun updateMiniCartSimplified(miniCartSimplifiedData: MiniCartSimplifiedData?) {
        miniCartSimplifiedData?.let {
            setMiniCartData(miniCartSimplifiedData)
        }

        launch {
            miniCartData?.apply {
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

    private fun getRecommendationCarousel(
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
                        miniCartData = miniCartData,
                        hasBlockedAddToCart = hasBlockedAddToCart
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

    private fun updateProductItemQuantity(productId: String, quantity: Int) {
        productModels.updateProductCardCarouselItemQuantity(
            productId = productId,
            quantity = quantity
        )
    }

    private fun trackProductAddToCart(
        position: Int,
        addToCartModel: AddToCartDataModel,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        _atcDataTracker.postValue(
            AddToCartDataTrackerModel(
                position = position,
                quantity = addToCartModel.data.quantity,
                cartId = addToCartModel.data.cartId,
                productRecommendation = product
            )
        )
    }

    private fun updateToolbarNotification() {
        _updateToolbarNotification.postValue(true)
    }
}

package com.tokopedia.tokopedianow.common.viewmodel

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
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.mappingMiniCartDataToRecommendation
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class TokoNowProductRecommendationViewModel @Inject constructor(
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    private val _recommendationWidget = MutableLiveData<Result<RecommendationWidget>>()
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()
    private val _miniCartRemove = MutableLiveData<Result<Pair<String,String>>>()
    private val _miniCartData = MutableLiveData<MutableMap<MiniCartItemKey, MiniCartItem>>()

    var mMiniCartSimplifiedData: MiniCartSimplifiedData? = null

    val recommendationWidget: LiveData<Result<RecommendationWidget>>
        get() = _recommendationWidget
    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    val miniCartRemove: LiveData<Result<Pair<String,String>>>
        get() = _miniCartRemove
    val miniCartData: LiveData<MutableMap<MiniCartItemKey, MiniCartItem>>
        get() = _miniCartData

    private fun getMiniCartItem(productId: String): MiniCartItem.MiniCartItemProduct? {
        val items = mMiniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
    }

    fun getRecommendationCarousel(
        requestParam: GetRecommendationRequestParam
    ) {
        launchCatchError(
            block = {
                if (_recommendationWidget.value != null) return@launchCatchError
                val result = getRecommendationUseCase.getData(requestParam)
                if (result.isNotEmpty()) {
                    val recommendationResult = result.first()
                    mappingMiniCartDataToRecommendation(recommendationResult, miniCartData.value)
                    _recommendationWidget.postValue(Success(recommendationResult))
                } else {
                    _recommendationWidget.postValue(Fail(Throwable()))
                }
            }, onError = { throwable ->
                _recommendationWidget.postValue(Fail(throwable))
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
            _miniCartUpdate.value = Success(it)
        }, {
            _miniCartUpdate.postValue(Fail(it))
        })
    }

    private fun removeItemCart(miniCartItem: MiniCartItem.MiniCartItemProduct) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            val productId = miniCartItem.productId
            val data = Pair(productId, it.data.message.joinToString(separator = ", "))
            _miniCartRemove.postValue(Success(data))
        }, {
            _miniCartRemove.postValue(Fail(it))
        })
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

    fun updateMiniCartSimplified(miniCartSimplifiedData: MiniCartSimplifiedData?) {
        mMiniCartSimplifiedData = miniCartSimplifiedData
    }
}

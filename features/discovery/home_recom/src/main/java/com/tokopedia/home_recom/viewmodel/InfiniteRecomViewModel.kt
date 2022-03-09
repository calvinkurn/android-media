package com.tokopedia.home_recom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.home_recom.model.datamodel.RecomErrorResponse
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.util.ReccomendationViewModelUtil.asFail
import com.tokopedia.home_recom.util.ReccomendationViewModelUtil.asSuccess
import com.tokopedia.home_recom.util.RecomPageConstant.TEXT_ERROR
import com.tokopedia.home_recom.util.RecomPageConstant.X_SOURCE_RECOM
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by yfsx on 30/08/21.
 */
class InfiniteRecomViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
        private val addToCartUseCase: Lazy<AddToCartUseCase>,
        private val miniCartListSimplifiedUseCase: Lazy<GetMiniCartListSimplifiedUseCase>,
        private val updateCartUseCase: Lazy<UpdateCartUseCase>,
        private val deleteCartUseCase: Lazy<DeleteCartUseCase>,
        private val dispatcher: RecommendationDispatcher
) : BaseViewModel(dispatcher.getMainDispatcher()) {

    val recommendationFirstLiveData: LiveData<List<RecommendationItemDataModel>> get() = _recommendationFirstLiveData
    private val _recommendationFirstLiveData = MutableLiveData<List<RecommendationItemDataModel>>()

    val recommendationNextLiveData: LiveData<List<RecommendationItemDataModel>> get() = _recommendationNextLiveData
    private val _recommendationNextLiveData = SingleLiveEvent<List<RecommendationItemDataModel>>()

    val recommendationWidgetData: LiveData<RecommendationWidget> get() = _recommendationWidgetData
    private val _recommendationWidgetData = MutableLiveData<RecommendationWidget>()

    val miniCartData: LiveData<MutableMap<String, MiniCartItem>> get() = _miniCartData
    private val _miniCartData = MutableLiveData<MutableMap<String, MiniCartItem>>()

    val minicartWidgetUpdater: SingleLiveEvent<MiniCartSimplifiedData> get() = _minicartWidgetUpdater
    private val _minicartWidgetUpdater = SingleLiveEvent<MiniCartSimplifiedData>()

    val errorGetRecomData: LiveData<RecomErrorResponse> get() = _errorGetRecomData
    private val _errorGetRecomData = MutableLiveData<RecomErrorResponse>()

    private val _atcRecomTokonow = MutableLiveData<Result<String>>()
    val atcRecomTokonow: LiveData<Result<String>> get() = _atcRecomTokonow

    private val _atcRecomTokonowSendTracker = SingleLiveEvent<Result<RecommendationItem>>()
    val atcRecomTokonowSendTracker: LiveData<Result<RecommendationItem>> get() = _atcRecomTokonowSendTracker

    private val _deleteCartRecomTokonowSendTracker = SingleLiveEvent<Result<RecommendationItem>>()
    val deleteCartRecomTokonowSendTracker: LiveData<Result<RecommendationItem>> get() = _deleteCartRecomTokonowSendTracker

    private val _atcRecomTokonowResetCard = SingleLiveEvent<RecommendationItem>()
    val atcRecomTokonowResetCard: LiveData<RecommendationItem> get() = _atcRecomTokonowResetCard

    private val _atcRecomTokonowNonLogin = SingleLiveEvent<RecommendationItem>()
    val atcRecomTokonowNonLogin: LiveData<RecommendationItem> get() = _atcRecomTokonowNonLogin

    private val _minicartError = SingleLiveEvent<Throwable>()
    val minicartError: LiveData<Throwable> get() = _minicartError

    private val _refreshMiniCartDataTrigger = SingleLiveEvent<Boolean>()
    val refreshMiniCartDataTrigger: LiveData<Boolean> get() = _refreshMiniCartDataTrigger

    fun getRecommendationFirstPage(pageName: String, productId: String, queryParam: String, forceRefresh: Boolean) {
        launchCatchError(dispatcher.getIODispatcher(), {
            val result = getRecommendationUseCase.get().getData(getBasicRecomParams(pageName = "pdp_2_tokonow", productId = productId, queryParam = queryParam))
            if (result.isEmpty()) {
                _errorGetRecomData.postValue(RecomErrorResponse(isEmptyFirstPage = true, isForceRefreshAndError = forceRefresh))
            } else {
                _recommendationWidgetData.postValue(result[0])
                if (result[0].recommendationItemList.isEmpty()) {
                    _errorGetRecomData.postValue(RecomErrorResponse(isEmptyFirstPage = true, isForceRefreshAndError = forceRefresh))
                } else {
                    _recommendationFirstLiveData.postValue(mappingRecomDataModel(result))
                }
            }
        }) {
            _errorGetRecomData.postValue(RecomErrorResponse(pageNumber = 1, errorThrowable = it, isErrorFirstPage = true, isForceRefreshAndError = forceRefresh))
        }
    }

    fun getRecommendationNextPage(pageName: String, productId: String, pageNumber: Int, queryParam: String) {
        launchCatchError(dispatcher.getIODispatcher(), {
            val result = getRecommendationUseCase.get().getData(getBasicRecomParams(pageName = pageName, pageNumber = pageNumber, productId = productId, queryParam = queryParam))
            if (result.isEmpty()) {
                _errorGetRecomData.postValue(RecomErrorResponse(isEmptyNextPage = true))
            } else {
                _recommendationNextLiveData.postValue(mappingRecomDataModel(result))
            }
        }) {
            _errorGetRecomData.postValue(RecomErrorResponse(pageNumber, it))
        }
    }

    private fun getBasicRecomParams(pageName: String = "", productId: String = "", pageNumber: Int = 1, queryParam: String = ""): GetRecommendationRequestParam {
        return GetRecommendationRequestParam(
                isTokonow = true,
                pageNumber = pageNumber,
                productIds = listOf(productId),
                pageName = pageName,
                xSource = X_SOURCE_RECOM,
                queryParam = queryParam)
    }

    private fun mappingRecomDataModel(recomData: List<RecommendationWidget>): List<RecommendationItemDataModel> {
        val recomItemList = mutableListOf<RecommendationItemDataModel>()
        if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
            val recomWidget = recomData.first().copy()
            if (recomWidget.isTokonow) {
                recomItemList.addAll(mappingMiniCartDataToRecommendation(recomWidget))
            } else {
                recomItemList.addAll(mappingDataRecomToModel(recomWidget))
            }
        }
        return recomItemList
    }

    private fun mappingDataRecomToModel(recomWidget: RecommendationWidget): List<RecommendationItemDataModel> {
        val recomItemList = mutableListOf<RecommendationItemDataModel>()
        recomWidget.recommendationItemList.forEach { item ->
            recomItemList.add(RecommendationItemDataModel(item))
        }
        return recomItemList
    }

    private fun mappingMiniCartDataToRecommendation(recomWidget: RecommendationWidget): List<RecommendationItemDataModel> {
        val recomItemList = mutableListOf<RecommendationItemDataModel>()
        recomWidget.recommendationItemList.forEach { item ->
            miniCartData.value?.let {
                if (item.isProductHasParentID()) {
                    var variantTotalItems = 0
                    it.values.forEach { miniCartItem ->
                        if (miniCartItem.productParentId == item.parentID.toString()) {
                            variantTotalItems += miniCartItem.quantity
                        }
                    }
                    item.updateItemCurrentStock(variantTotalItems)
                } else {
                    item.updateItemCurrentStock(it[item.productId.toString()]?.quantity
                            ?: 0)
                }
            }
            recomItemList.add(RecommendationItemDataModel(item))
        }
        return recomItemList
    }

    fun getMiniCart(shopId: String) {
        launchCatchError(dispatcher.getIODispatcher(), block = {
            miniCartListSimplifiedUseCase.get().setParams(listOf(shopId))
            val result = miniCartListSimplifiedUseCase.get().executeOnBackground()
            val data = result.miniCartItems.associateBy({ it.productId }) {
                it
            }
            _miniCartData.postValue(data.toMutableMap())
            _minicartWidgetUpdater.postValue(result)
        }) {
            _minicartError.postValue(it)
        }
    }
    fun onAtcRecomNonVariantQuantityChanged(recomItem: RecommendationItem, quantity: Int) {
        if (!userSessionInterface.isLoggedIn) {
            _atcRecomTokonowNonLogin.value = recomItem
        } else {
            if (recomItem.quantity == quantity) return
            val miniCartItem = miniCartData.value?.get(recomItem.productId.toString())
            if (quantity == 0) {
                deleteRecomItemFromCart(recomItem, miniCartItem)
            } else if (recomItem.quantity == 0) {
                atcRecomNonVariant(recomItem, quantity)
            } else {
                updateRecomCartNonVariant(recomItem, quantity, miniCartItem)
            }
        }
    }

    fun deleteRecomItemFromCart(recomItem: RecommendationItem, miniCartItem: MiniCartItem?) {
        launchCatchError(block = {
            miniCartItem?.let {
                deleteCartUseCase.get().setParams(listOf(miniCartItem.cartId))
                val result = deleteCartUseCase.get().executeOnBackground()
                val isFailed = result.data.success == 0 || result.status.equals(TEXT_ERROR, true)
                if (isFailed) {
                    val error = result.errorMessage.firstOrNull()
                            ?: result.data.message.firstOrNull()
                    onFailedATCRecomTokonow(MessageErrorException(error ?: ""), recomItem)
                } else {
                    updateMiniCartAfterATCRecomTokonow(
                            message = result.data.message.first(),
                            isDeleteCart = true,
                            recomItem = recomItem)
                }
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }
    }

    fun atcRecomNonVariant(recomItem: RecommendationItem, quantity: Int) {
        launchCatchError(block = {
            val param = AddToCartUseCase.getMinimumParams(
                    recomItem.productId.toString(),
                    recomItem.shopId.toString(),
                    quantity
            )
            val result = withContext(dispatcher.getIODispatcher()) {
                addToCartUseCase.get().createObservable(param).toBlocking().single()
            }
            if (result.isStatusError()) {
                onFailedATCRecomTokonow(MessageErrorException(result.errorMessage.firstOrNull()
                        ?: result.status), recomItem)
            } else {
                recomItem.cartId = result.data.cartId
                updateMiniCartAfterATCRecomTokonow(
                        message = result.data.message.first(),
                        isAtc = true,
                        recomItem = recomItem)
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }
    }

    fun updateRecomCartNonVariant(recomItem: RecommendationItem, quantity: Int, miniCartItem: MiniCartItem?) {
        launchCatchError(block = {
            miniCartItem?.let {
                val copyOfMiniCartItem = UpdateCartRequest(cartId = it.cartId, quantity = quantity, notes = it.notes)
                updateCartUseCase.get().setParams(
                        updateCartRequestList = listOf(copyOfMiniCartItem),
                        source = UpdateCartUseCase.VALUE_SOURCE_PDP_UPDATE_QTY_NOTES
                )
                val result = updateCartUseCase.get().executeOnBackground()

                if (result.error.isNotEmpty()) {
                    onFailedATCRecomTokonow(MessageErrorException(result.error.firstOrNull()
                            ?: ""), recomItem)
                } else {
                    updateMiniCartAfterATCRecomTokonow(
                            message = result.data.message, recomItem = recomItem)
                }
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }

    }

    private fun updateMiniCartAfterATCRecomTokonow(
            message: String,
            isAtc: Boolean = false,
            isDeleteCart: Boolean = false,
            recomItem: RecommendationItem = RecommendationItem()) {
        _atcRecomTokonow.value = message.asSuccess()
        if (isAtc) {
            _atcRecomTokonowSendTracker.value = recomItem.asSuccess()
        } else if (isDeleteCart)
            _deleteCartRecomTokonowSendTracker.value = recomItem.asSuccess()
        _refreshMiniCartDataTrigger.value = true
    }

    private fun onFailedATCRecomTokonow(throwable: Throwable, recomItem: RecommendationItem) {
        _atcRecomTokonow.value = throwable.asFail()
        _atcRecomTokonowResetCard.value = recomItem
    }

}
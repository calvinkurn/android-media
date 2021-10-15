package com.tokopedia.recommendation_widget_common.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.*
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TEXT_ERROR
import com.tokopedia.recommendation_widget_common.viewutil.asSuccess
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import dagger.Lazy
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by yfsx on 02/08/21.
 */
open class RecommendationViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val addToCartUseCase: Lazy<AddToCartUseCase>,
    private val miniCartListSimplifiedUseCase: Lazy<GetMiniCartListSimplifiedUseCase>,
    private val updateCartUseCase: Lazy<UpdateCartUseCase>,
    private val deleteCartUseCase: Lazy<DeleteCartUseCase>,
) : BaseViewModel(dispatcher.main) {

    private var getRecommendationJob: Job? = null

    private val _getRecommendationLiveData = MutableLiveData<Result<RecommendationWidget>>()
    val getRecommendationLiveData: LiveData<Result<RecommendationWidget>>
        get() = _getRecommendationLiveData

    private val _errorGetRecommendation = SingleLiveEvent<RecomErrorModel>()
    val errorGetRecommendation: LiveData<RecomErrorModel>
        get() = _errorGetRecommendation


    val miniCartData: LiveData<MutableMap<String, MiniCartItem>> get() = _miniCartData
    private val _miniCartData = MutableLiveData<MutableMap<String, MiniCartItem>>()

    private val _atcRecomTokonow = MutableLiveData<RecomAtcTokonowResponse>()
    val atcRecomTokonow: LiveData<RecomAtcTokonowResponse> get() = _atcRecomTokonow

    private val _atcRecomTokonowSendTracker = MutableLiveData<Result<RecommendationItem>>()
    val atcRecomTokonowSendTracker: LiveData<Result<RecommendationItem>> get() = _atcRecomTokonowSendTracker

    private val _deleteCartRecomTokonowSendTracker = MutableLiveData<Result<RecommendationItem>>()
    val deleteCartRecomTokonowSendTracker: LiveData<Result<RecommendationItem>> get() = _deleteCartRecomTokonowSendTracker

    private val _atcRecomTokonowResetCard = MutableLiveData<RecommendationItem>()
    val atcRecomTokonowResetCard: LiveData<RecommendationItem> get() = _atcRecomTokonowResetCard

    private val _atcRecomTokonowNonLogin = MutableLiveData<RecommendationItem>()
    val atcRecomTokonowNonLogin: LiveData<RecommendationItem> get() = _atcRecomTokonowNonLogin

    private val _minicartError = MutableLiveData<Throwable>()
    val minicartError: LiveData<Throwable> get() = _minicartError

    private val _refreshMiniCartDataTriggerByPageName = MutableLiveData<String>()
    val refreshMiniCartDataTriggerByPageName: LiveData<String> get() = _refreshMiniCartDataTriggerByPageName

    private val _refreshUIMiniCartData = MutableLiveData<RecomMinicartWrapperData>()
    val refreshUIMiniCartData: LiveData<RecomMinicartWrapperData> get() = _refreshUIMiniCartData

    fun loadRecommendationCarousel(
        pageNumber: Int = 1,
        productIds: List<String> = listOf(),
        queryParam: String = "",
        pageName: String = "",
        categoryIds: List<String> = listOf(),
        xSource: String = "",
        xDevice: String = "",
        isTokonow: Boolean = false,
        keywords: List<String> = listOf()
    ) {
        if (isJobAvailable(getRecommendationJob)) {
            getRecommendationJob = viewModelScope.launchCatchError(dispatcher.io, {
                val result = getRecommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = pageNumber,
                        productIds = productIds,
                        queryParam = queryParam,
                        pageName = pageName,
                        categoryIds = categoryIds,
                        xSource = xSource,
                        xDevice = xDevice,
                        keywords = keywords,
                                isTokonow = isTokonow,
                        ))
                if (result.isNotEmpty()) {
                    val recomWidget = result[0]
                    if (isTokonow) {
                        mappingMiniCartDataToRecommendation(recomWidget)
                    }
                    _getRecommendationLiveData.postValue(recomWidget.asSuccess())
                }
            }) {
                _errorGetRecommendation.postValue(
                    RecomErrorModel(
                        pageName = pageName,
                        throwable = it
                    )
                )
            }
        }
    }

    private fun mappingMiniCartDataToRecommendation(recomWidget: RecommendationWidget) {
        val recomItemList = mutableListOf<RecommendationItem>()
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
                    item.updateItemCurrentStock(
                        it[item.productId.toString()]?.quantity
                            ?: 0
                    )
                }
            }
            recomItemList.add(item)
        }
        recomWidget.recommendationItemList = recomItemList
    }

    fun getMiniCart(shopId: String, pageName: String) {
        launchCatchError(dispatcher.io, block = {
            miniCartListSimplifiedUseCase.get().setParams(listOf(shopId))
            val result = miniCartListSimplifiedUseCase.get().executeOnBackground()
            val data = result.miniCartItems.associateBy({ it.productId }) {
                it
            }
            _miniCartData.postValue(data.toMutableMap())
            _refreshUIMiniCartData.postValue(RecomMinicartWrapperData(pageName, result))
        }) {
            _minicartError.postValue(it)
        }
    }

    fun onAtcRecomNonVariantQuantityChanged(recomItem: RecommendationItem, quantity: Int) {
        if (!userSession.isLoggedIn) {
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
                        recomItem = recomItem
                    )
                }
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }
    }

    fun atcRecomNonVariant(recomItem: RecommendationItem, quantity: Int) {
        launchCatchError(block = {
            val atcParam = AddToCartRequestParams(
                productId = recomItem.productId,
                shopId = recomItem.shopId,
                quantity = quantity
            )
            val result = withContext(dispatcher.io) {
                addToCartUseCase.get().setParams(atcParam)
                addToCartUseCase.get().executeOnBackground()
            }
            if (result.isStatusError()) {
                onFailedATCRecomTokonow(
                    MessageErrorException(
                        result.errorMessage.firstOrNull()
                            ?: result.status
                    ), recomItem
                )
            } else {
                recomItem.cartId = result.data.cartId
                updateMiniCartAfterATCRecomTokonow(
                    message = result.data.message.first(),
                    isAtc = true,
                    recomItem = recomItem
                )
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }
    }

    fun updateRecomCartNonVariant(
        recomItem: RecommendationItem,
        quantity: Int,
        miniCartItem: MiniCartItem?
    ) {
        launchCatchError(block = {
            miniCartItem?.let {
                val copyOfMiniCartItem =
                    UpdateCartRequest(cartId = it.cartId, quantity = quantity, notes = it.notes)
                updateCartUseCase.get().setParams(
                    updateCartRequestList = listOf(copyOfMiniCartItem),
                    source = UpdateCartUseCase.VALUE_SOURCE_PDP_UPDATE_QTY_NOTES
                )
                val result = updateCartUseCase.get().executeOnBackground()

                if (result.error.isNotEmpty()) {
                    onFailedATCRecomTokonow(
                        MessageErrorException(
                            result.error.firstOrNull()
                                ?: ""
                        ), recomItem
                    )
                } else {
                    updateMiniCartAfterATCRecomTokonow(
                        message = result.data.message, recomItem = recomItem
                    )
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
        recomItem: RecommendationItem = RecommendationItem()
    ) {
        if (isAtc) {
            _atcRecomTokonowSendTracker.postValue(recomItem.asSuccess())
            _atcRecomTokonow.postValue(
                RecomAtcTokonowResponse(
                    message = message,
                    recomItem = recomItem
                )
            )
        } else if (isDeleteCart) {
            _deleteCartRecomTokonowSendTracker.postValue(recomItem.asSuccess())
            _atcRecomTokonow.postValue(
                RecomAtcTokonowResponse(
                    message = message,
                    recomItem = recomItem
                )
            )
        }
        _refreshMiniCartDataTriggerByPageName.postValue(recomItem.pageName)
    }

    private fun onFailedATCRecomTokonow(throwable: Throwable, recomItem: RecommendationItem) {
        _atcRecomTokonow.postValue(
            RecomAtcTokonowResponse(
                recomItem = recomItem,
                error = throwable
            )
        )
        _atcRecomTokonowResetCard.postValue(recomItem)
    }

    private fun isJobAvailable(job: Job?): Boolean = job == null || !job?.isActive

}
package com.tokopedia.recommendation_widget_common.presenter

import android.text.TextUtils
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
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.mappingMiniCartDataToRecommendation
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecomAtcTokonowResponse
import com.tokopedia.recommendation_widget_common.presentation.model.RecomErrorModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecomFilterResult
import com.tokopedia.recommendation_widget_common.presentation.model.RecomMinicartWrapperData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TEXT_ERROR
import com.tokopedia.recommendation_widget_common.viewutil.asSuccess
import com.tokopedia.recommendation_widget_common.viewutil.isRecomPageNameEligibleForChips
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetView
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import dagger.Lazy
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import javax.inject.Inject

/**
 * Created by yfsx on 02/08/21.
 */
open class RecomWidgetViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
    private val addToCartUseCase: Lazy<AddToCartUseCase>,
    private val miniCartListSimplifiedUseCase: Lazy<GetMiniCartListSimplifiedUseCase>,
    private val updateCartUseCase: Lazy<UpdateCartUseCase>,
    private val deleteCartUseCase: Lazy<DeleteCartUseCase>,
    private val getRecommendationFilterChips: Lazy<GetRecommendationFilterChips>
) : BaseViewModel(dispatcher.main) {

    private var getRecommendationJob: Job? = null

    private val _getRecommendationLiveData = MutableLiveData<RecommendationWidget>()
    val getRecommendationLiveData: LiveData<RecommendationWidget>
        get() = _getRecommendationLiveData

    private val _errorGetRecommendation = SingleLiveEvent<RecomErrorModel>()
    val errorGetRecommendation: LiveData<RecomErrorModel>
        get() = _errorGetRecommendation


    val miniCartData: LiveData<MutableMap<MiniCartItemKey, MiniCartItem>> get() = _miniCartData
    private val _miniCartData = MutableLiveData<MutableMap<MiniCartItemKey, MiniCartItem>>()

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

    private val _recomFilterResultData = MutableLiveData<RecomFilterResult>()
    val recomFilterResultData: LiveData<RecomFilterResult> get() = _recomFilterResultData

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
        if (isJobAvailable(getRecommendationJob) && isActive) {
            getRecommendationJob = viewModelScope.launchCatchError(block = {
                val recomFilterList = mutableListOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()
                if (pageName.isRecomPageNameEligibleForChips()) {
                    getRecommendationFilterChips.get().setParams(
                        userId = if (userSession.userId.isEmpty()) 0 else userSession.userId.toInt(),
                        pageName = pageName,
                        productIDs = TextUtils.join(",", productIds) ?: "",
                        isTokonow = isTokonow
                    )
                    recomFilterList.addAll(getRecommendationFilterChips.get().executeOnBackground().filterChip)
                }
                val result = getRecommendationUseCase.get().getData(
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
                    val recomWidget = result[0].copy(recommendationFilterChips = recomFilterList)
                    if (isTokonow) {
                        mappingMiniCartDataToRecommendation(recomWidget, miniCartData.value)
                    }
                    _getRecommendationLiveData.postValue(recomWidget)
                } else {
                    _errorGetRecommendation.postValue(RecomErrorModel(pageName = pageName))
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

    fun loadRecomBySelectedChips(recomWidgetMetadata: RecommendationCarouselWidgetView.RecomWidgetMetadata,
                                 oldFilterList: List<AnnotationChip>,
                                 selectedChip: AnnotationChip) {
        if (isJobAvailable(getRecommendationJob) && isActive) {
            val newQueryParam = getQueryParamBasedOnChips(recomWidgetMetadata.queryParam, selectedChip)

            getRecommendationJob = viewModelScope.launchCatchError(block = {
                val recomData = getRecommendationUseCase.get().getData(
                    GetRecommendationRequestParam(
                        pageName = recomWidgetMetadata.pageName,
                        queryParam = newQueryParam,
                        productIds = recomWidgetMetadata.productIds,
                        categoryIds = recomWidgetMetadata.categoryIds,
                        keywords = listOf(recomWidgetMetadata.keyword),
                        isTokonow = recomWidgetMetadata.isTokonow,
                ))
                if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
                    val newRecommendation = recomData.first()
                    _recomFilterResultData.postValue(RecomFilterResult(
                        pageName = recomWidgetMetadata.pageName,
                        recomWidgetData = newRecommendation,
                        filterList = selectOrDeselectAnnotationChip(
                            oldFilterList,
                            selectedChip.recommendationFilterChip.name,
                            selectedChip.recommendationFilterChip.isActivated),
                        isSuccess = true
                    ))
                } else {
                    _recomFilterResultData.postValue(RecomFilterResult(
                        pageName = recomWidgetMetadata.pageName,
                        filterList = selectOrDeselectAnnotationChip(
                            oldFilterList,
                            selectedChip.recommendationFilterChip.name,
                            selectedChip.recommendationFilterChip.isActivated),
                        isSuccess = true
                    ))
                }
            }) {
                _recomFilterResultData.postValue(RecomFilterResult(
                    pageName = recomWidgetMetadata.pageName,
                    filterList = selectOrDeselectAnnotationChip(
                        oldFilterList,
                        selectedChip.recommendationFilterChip.name,
                        selectedChip.recommendationFilterChip.isActivated),
                    isSuccess = false,
                    throwable = it
                ))
            }
        }
    }

    fun updateMiniCartWithPageData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        val data = miniCartSimplifiedData.miniCartItems
        _miniCartData.postValue(data.toMutableMap())
    }

    fun getMiniCart(shopId: String, pageName: String, miniCartSource: MiniCartSource = MiniCartSource.PDPRecommendationWidget) {
        launchCatchError(block = {
            miniCartListSimplifiedUseCase.get().setParams(listOf(shopId), miniCartSource)
            val result = miniCartListSimplifiedUseCase.get().executeOnBackground()
            val data = result.miniCartItems
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
            else {
                when {
                    quantity == 0 -> {
                        deleteRecomItemFromCart(recomItem)
                    }
                    recomItem.quantity == 0 -> {
                        atcRecomNonVariant(recomItem, quantity)
                    }
                    else -> {
                        updateRecomCartNonVariant(recomItem, quantity)
                    }
                }
            }
        }
    }



    private fun getQueryParamBasedOnChips(queryParams: String, annotationChip: AnnotationChip): String {
        var newQueryParams = queryParams
        if (annotationChip.recommendationFilterChip.isActivated) {
            newQueryParams = annotationChip.recommendationFilterChip.value
        }
        return newQueryParams
    }

    private fun selectOrDeselectAnnotationChip(filterData: List<AnnotationChip>?, name: String, isActivated: Boolean): List<AnnotationChip> {
        return filterData?.map {
            it.copy(
                recommendationFilterChip = it.recommendationFilterChip.copy(
                    isActivated =
                    name == it.recommendationFilterChip.name
                            && isActivated
                )
            )
        } ?: listOf()
    }

    private fun deleteRecomItemFromCart(recomItem: RecommendationItem) {
        launchCatchError(block = {
            val miniCartItem = miniCartData.value?.getMiniCartItemProduct(recomItem.productId.toString())
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

    private fun atcRecomNonVariant(recomItem: RecommendationItem, quantity: Int) {
        launchCatchError(block = {
            val atcParam = AddToCartRequestParams(
                productId = recomItem.productId.toString(),
                shopId = recomItem.shopId.toString(),
                quantity = quantity
            )
            addToCartUseCase.get().setParams(atcParam)
            val result = addToCartUseCase.get().executeOnBackground()
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

    private fun updateRecomCartNonVariant(
        recomItem: RecommendationItem,
        quantity: Int
    ) {
        launchCatchError(block = {
            val miniCartItem = miniCartData.value?.getMiniCartItemProduct(recomItem.productId.toString())
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
                            result.error.first()
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
        recomItem: RecommendationItem
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

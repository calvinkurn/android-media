package com.tokopedia.promousage.view.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promousage.data.request.ClearCacheAutoApplyStackParam
import com.tokopedia.promousage.data.request.GetPromoListRecommendationParam
import com.tokopedia.promousage.data.request.ValidateUsePromoUsageParam
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.BoAdditionalData
import com.tokopedia.promousage.domain.entity.PromoAttemptedError
import com.tokopedia.promousage.domain.entity.PromoItemCta
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoAttemptItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.domain.usecase.PromoUsageClearCacheAutoApplyStackUseCase
import com.tokopedia.promousage.domain.usecase.PromoUsageGetPromoListRecommendationUseCase
import com.tokopedia.promousage.domain.usecase.PromoUsageValidateUseUseCase
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.util.logger.PromoErrorException
import com.tokopedia.promousage.util.logger.PromoUsageLogger
import com.tokopedia.promousage.util.test.PromoUsageIdlingResource
import com.tokopedia.promousage.view.mapper.PromoUsageClearCacheAutoApplyStackMapper
import com.tokopedia.promousage.view.mapper.PromoUsageGetPromoListRecommendationMapper
import com.tokopedia.promousage.view.mapper.PromoUsageValidateUseMapper
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class PromoUsageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getPromoListRecommendationUseCase: PromoUsageGetPromoListRecommendationUseCase,
    private val validateUseUseCase: PromoUsageValidateUseUseCase,
    private val clearCacheAutoApplyStackUseCase: PromoUsageClearCacheAutoApplyStackUseCase,
    private val getPromoListRecommendationMapper: PromoUsageGetPromoListRecommendationMapper,
    private val validateUseMapper: PromoUsageValidateUseMapper,
    private val clearCacheAutoApplyStackMapper: PromoUsageClearCacheAutoApplyStackMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val PROMO_STATE_RED = "red"

        private const val VALIDATE_USE_STATUS_SUCCESS = "OK"
        private const val VALIDATE_USE_CODE_SUCCESS = "200"

        private const val CLASH_ARTIFICIAL_DELAY = 1_000L
    }

    private var initialSelectedPromoCodes: List<String>? = null

    @VisibleForTesting
    var clickedGoPayLaterCicilPromoCode: String? = null

    private val _promoPageUiState = MutableLiveData<PromoPageUiState>(PromoPageUiState.Initial)
    val promoPageUiState: LiveData<PromoPageUiState>
        get() = _promoPageUiState

    private val _getPromoRecommendationUiAction = MutableLiveData<GetPromoRecommendationUiAction>()
    val getPromoRecommendationUiAction: LiveData<GetPromoRecommendationUiAction>
        get() = _getPromoRecommendationUiAction

    private val _usePromoRecommendationUiAction = MutableLiveData<UsePromoRecommendationUiAction>()
    val usePromoRecommendationUiAction: LiveData<UsePromoRecommendationUiAction>
        get() = _usePromoRecommendationUiAction

    private val _attemptPromoUiAction = MutableLiveData<AttemptPromoUiAction>()
    val attemptPromoUiAction: LiveData<AttemptPromoUiAction>
        get() = _attemptPromoUiAction

    private val _promoCtaUiAction = MutableLiveData<PromoCtaUiAction>()
    val promoCtaUiAction: LiveData<PromoCtaUiAction>
        get() = _promoCtaUiAction

    private val _clearPromoUiAction = MutableLiveData<ClearPromoUiAction>()
    val clearPromoUiAction: LiveData<ClearPromoUiAction>
        get() = _clearPromoUiAction

    private val _applyPromoUiAction = MutableLiveData<ApplyPromoUiAction>()
    val applyPromoUiAction: LiveData<ApplyPromoUiAction>
        get() = _applyPromoUiAction

    private val _closePromoPageUiAction = MutableLiveData<ClosePromoPageUiAction>()
    val closePromoPageUiAction: LiveData<ClosePromoPageUiAction>
        get() = _closePromoPageUiAction

    private val _clickPromoUiAction = MutableLiveData<ClickPromoUiAction>()
    val clickPromoUiAction: LiveData<ClickPromoUiAction>
        get() = _clickPromoUiAction

    private val _clickTncUiAction = MutableLiveData<ClickTncUiAction>()
    val clickTncUiAction: LiveData<ClickTncUiAction>
        get() = _clickTncUiAction

    private val _autoApplyAction = MutableLiveData<PromoItem>()
    val autoApplyAction: LiveData<PromoItem>
        get() = _autoApplyAction

    fun loadPromoList(
        promoRequest: PromoRequest? = null,
        chosenAddress: ChosenAddress? = null,
        attemptedPromoCode: String = "",
        onSuccess: ((List<DelegateAdapterItem>) -> Unit)? = null
    ) {
        if (attemptedPromoCode.isBlank()) {
            _promoPageUiState.postValue(PromoPageUiState.Initial)
        }

        // Reset pre-selected promo param
        var newPromoRequest = promoRequest?.copy() ?: PromoRequest()
        newPromoRequest = clearPromoRequest(newPromoRequest)
        // Add current attempted code to promo param if exist
        newPromoRequest = updateAttemptedCodeToPromoRequest(attemptedPromoCode, newPromoRequest)
        val pageState = _promoPageUiState.value
        if (pageState is PromoPageUiState.Success) {
            val currentItems = pageState.items.filterIsInstance<PromoItem>()
            // Add or remove code from promo order param
            newPromoRequest = updateCurrentPromoCodeToPromoRequest(currentItems, newPromoRequest)
        }
        // Remove duplicate attempted promo code from promo request
        newPromoRequest =
            removeDuplicateAttemptedCodeFromPromoRequest(attemptedPromoCode, newPromoRequest)

        // Generate CouponListRecommendation param
        val param = GetPromoListRecommendationParam.create(
            promoRequest = newPromoRequest,
            chosenAddress = chosenAddress ?: chosenAddressRequestHelper.getChosenAddress(),
            isPromoRevamp = true
        )
        PromoUsageIdlingResource.increment()
        launchCatchError(
            context = dispatchers.io,
            block = {
                PromoUsageIdlingResource.decrement()
                val response = getPromoListRecommendationUseCase(param)
                if (response.promoListRecommendation.data.resultStatus.message == PromoListRecommendation.STATUS_OK) {
                    handleLoadPromoListSuccess(attemptedPromoCode, response, onSuccess)
                } else {
                    if (response.promoListRecommendation.data.resultStatus.code == ResultStatus.STATUS_COUPON_LIST_EMPTY) {
                        PromoUsageLogger.logOnErrorLoadPromoUsagePage(
                            PromoErrorException("response status ok but data is empty")
                        )
                        handleLoadPromoListSuccess(attemptedPromoCode, response, onSuccess)
                    } else {
                        PromoUsageLogger.logOnErrorLoadPromoUsagePage(
                            PromoErrorException(response.promoListRecommendation.data.resultStatus.message)
                        )
                        val exception = PromoErrorException()
                        handleLoadPromoListFailed(exception)
                    }
                }
            },
            onError = { throwable ->
                PromoUsageLogger.logOnErrorLoadPromoUsagePage(
                    PromoErrorException(message = "response status error")
                )
                PromoUsageIdlingResource.decrement()
                handleLoadPromoListFailed(throwable)
            }
        )
    }

    private fun handleLoadPromoListSuccess(
        attemptedPromoCode: String,
        response: GetPromoListRecommendationResponse,
        onSuccess: ((List<DelegateAdapterItem>) -> Unit)? = null
    ) {
        val tickerInfo = getPromoListRecommendationMapper
            .mapPromoListRecommendationResponseToPageTickerInfo(response)
        var items = getPromoListRecommendationMapper
            .mapPromoListRecommendationResponseToPromoSections(response)
        val attemptedPromoCodeError = getPromoListRecommendationMapper
            .mapPromoListRecommendationResponseToAttemptedPromoCodeError(response)

        var savingInfo = calculatePromoSavingInfo(items)
        savingInfo = savingInfo.copy(
            message = getPromoListRecommendationMapper
                .mapPromoListRecommendationResponseToSavingInfo(response)
                .message
        )
        val promoRecommendation = items.getRecommendationItem()
        if (promoRecommendation != null) {
            _getPromoRecommendationUiAction.postValue(
                GetPromoRecommendationUiAction.NotEmpty(promoRecommendation)
            )
        } else {
            _getPromoRecommendationUiAction.postValue(GetPromoRecommendationUiAction.Empty)
        }
        if (attemptedPromoCodeError.code.isNotBlank() && attemptedPromoCodeError.message.isNotBlank()) {
            _attemptPromoUiAction.postValue(
                AttemptPromoUiAction.Failed(attemptedPromoCodeError.message)
            )
        }
        items = sortPromo(items)
        items = addTncPromo(items)
        items = updateAttemptedPromo(items, attemptedPromoCode, attemptedPromoCodeError)
        if (initialSelectedPromoCodes == null) {
            initialSelectedPromoCodes = items.getSelectedPromoCodes()
        }
        _promoPageUiState.postValue(
            PromoPageUiState.Success(
                hasPromoRecommendationSection = promoRecommendation != null,
                tickerInfo = tickerInfo,
                items = items,
                savingInfo = savingInfo,
                isReload = true,
                isCalculating = false
            )
        )
        onSuccess?.invoke(items)
    }

    private fun handleLoadPromoListFailed(throwable: Throwable) {
        _promoPageUiState.postValue(PromoPageUiState.Error(throwable))
    }

    private fun clearPromoRequest(promoRequest: PromoRequest): PromoRequest {
        return promoRequest.copy(
            attemptedCodes = arrayListOf(),
            codes = arrayListOf(),
            orders = promoRequest.orders.map { it.copy(codes = arrayListOf()) }
        )
    }

    private fun updateAttemptedCodeToPromoRequest(
        attemptedPromoCode: String,
        promoRequest: PromoRequest
    ): PromoRequest {
        return if (attemptedPromoCode.isNotBlank()) {
            promoRequest.copy(
                attemptedCodes = arrayListOf(attemptedPromoCode.uppercase()),
                skipApply = 0
            )
        } else {
            promoRequest.copy(
                skipApply = 1
            )
        }
    }

    private fun updateCurrentPromoCodeToPromoRequest(
        items: List<PromoItem>,
        promoRequest: PromoRequest
    ): PromoRequest {
        val codes = ArrayList(promoRequest.codes)
        items.forEach { item ->
            val promoCode = if (item.useSecondaryPromo) {
                item.secondaryPromo.code
            } else {
                item.code
            }
            if (item.state is PromoItemState.Selected) {
                if (item.shopId.isZero() && !promoRequest.codes.contains(promoCode)) {
                    codes.add(promoCode)
                }
            } else {
                if (item.shopId.isZero() && promoRequest.codes.contains(promoCode)) {
                    codes.remove(promoCode)
                }
            }
        }
        val orders = promoRequest.orders.map { order ->
            var newOrder = order.copy()
            items.forEach { item ->
                val promoCode = if (item.useSecondaryPromo) {
                    item.secondaryPromo.code
                } else {
                    item.code
                }
                if (item.state is PromoItemState.Selected) {
                    // If promo is selected, add promo code to request param
                    // If unique_id == 0, means it's a global promo, else it's promo merchant
                    if (item.uniqueId == order.uniqueId && !order.codes.contains(promoCode)) {
                        val updatedCodes = order.codes
                        updatedCodes.add(promoCode)
                        newOrder = order.copy(codes = updatedCodes)
                    } else if (item.isBebasOngkir) {
                        val additionalBoData = item.boAdditionalData.firstOrNull {
                            order.uniqueId == it.uniqueId
                        }
                        if (additionalBoData != null) {
                            if (!order.codes.contains(additionalBoData.code)) {
                                val updatedCodes = order.codes
                                updatedCodes.add(promoCode)
                                newOrder = order.copy(
                                    shippingId = additionalBoData.shippingId.toInt(),
                                    spId = additionalBoData.spId.toInt(),
                                    codes = updatedCodes
                                )
                            }
                        }
                    }
                } else {
                    // If promo is unselected and exist in current promo request, remove it from promo request
                    // If unique_id == 0, means it's a global promo, else it's promo merchant
                    if (item.uniqueId == order.uniqueId && order.codes.contains(promoCode)) {
                        val updatedCodes = order.codes
                        updatedCodes.remove(promoCode)
                        newOrder = order.copy(codes = updatedCodes)
                    } else if (item.isBebasOngkir) {
                        // if promo is bebas ongkir promo, then remove code only
                        val additionalBoData = item.boAdditionalData.firstOrNull {
                            order.uniqueId == it.uniqueId
                        }
                        if (additionalBoData != null) {
                            if (order.codes.contains(additionalBoData.code)) {
                                val updatedCodes = order.codes
                                updatedCodes.remove(promoCode)
                                newOrder = order.copy(
                                    codes = updatedCodes
                                )
                            }
                        }
                    }
                }
            }
            newOrder
        }
        return promoRequest.copy(
            codes = codes,
            orders = orders
        )
    }

    private fun removeDuplicateAttemptedCodeFromPromoRequest(
        attemptedPromoCode: String,
        promoRequest: PromoRequest
    ): PromoRequest {
        return if (attemptedPromoCode.isNotBlank()) {
            val newPromoRequestCodes =
                promoRequest.codes.filter { it != attemptedPromoCode }
            val newPromoRequestOrders = promoRequest.orders.map { order ->
                order.copy(codes = ArrayList(order.codes.filter { it != attemptedPromoCode }))
            }
            promoRequest.copy(
                codes = ArrayList(newPromoRequestCodes),
                orders = ArrayList(newPromoRequestOrders)
            )
        } else {
            promoRequest
        }
    }

    fun reloadPromoList(
        promoRequest: PromoRequest? = null,
        chosenAddress: ChosenAddress? = null,
        attemptedPromoCode: String = ""
    ) {
        _promoPageUiState.postValue(PromoPageUiState.Initial)
        loadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress,
            attemptedPromoCode = attemptedPromoCode
        )
    }

    fun loadPromoListWithPreSelectedGopayLaterPromo(
        promoRequest: PromoRequest? = null,
        chosenAddress: ChosenAddress? = null,
        attemptedPromoCode: String = ""
    ) {
        _promoPageUiState.postValue(PromoPageUiState.Initial)
        loadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress,
            attemptedPromoCode = attemptedPromoCode
        ) {
            _promoPageUiState.ifSuccess { pageState ->
                val gopayLaterPromo = if (!clickedGoPayLaterCicilPromoCode.isNullOrBlank()) {
                    pageState.items
                        .firstOrNull { item ->
                            item is PromoItem &&
                                item.code == clickedGoPayLaterCicilPromoCode
                        }
                } else {
                    pageState.items
                        .firstOrNull { item ->
                            item is PromoItem &&
                                item.couponType.contains(PromoItem.COUPON_TYPE_GOPAY_LATER_CICIL)
                        } as? PromoItem
                } as? PromoItem
                if (gopayLaterPromo != null) {
                    _autoApplyAction.postValue(gopayLaterPromo)
                    onClickPromo(gopayLaterPromo)
                }
            }
        }
    }

    fun onClickPromo(clickedItem: PromoItem) {
        launchCatchError(
            context = dispatchers.immediate,
            block = {
                val isGoPayLaterCicilPromo =
                    clickedItem.couponType.contains(PromoItem.COUPON_TYPE_GOPAY_LATER_CICIL)
                val isRegisterGoPayLaterCicilPromo =
                    clickedItem.cta.type == PromoItemCta.TYPE_REGISTER_GOPAY_LATER_CICIL
                if (isGoPayLaterCicilPromo && isRegisterGoPayLaterCicilPromo) {
                    clickedGoPayLaterCicilPromoCode = clickedItem.code
                    _promoCtaUiAction.postValue(PromoCtaUiAction.RegisterGoPayLaterCicil(clickedItem.cta))
                } else {
                    _promoPageUiState.ifSuccessSuspend { pageState ->
                        if (pageState.isCalculating) return@ifSuccessSuspend
                        var updatedState = pageState.copy()
                        updatedState = updatedState.copy(
                            isCalculating = true
                        )
                        _promoPageUiState.postValue(updatedState)
                        val useSecondaryPromo = clickedItem.useSecondaryPromo
                        var newClickedItem = clickedItem.copy(
                            state = when (clickedItem.state) {
                                is PromoItemState.Normal -> {
                                    PromoItemState.Selected(useSecondaryPromo = clickedItem.state.useSecondaryPromo)
                                }

                                is PromoItemState.Selected -> {
                                    PromoItemState.Normal(useSecondaryPromo = clickedItem.state.useSecondaryPromo)
                                }

                                else -> {
                                    // Should not process any other state
                                    return@ifSuccessSuspend
                                }
                            }
                        )

                        // Show artificial loading for MVC promo
                        var needToShowLoading = false
                        var updatedItems = updatedState.items
                            .map { item ->
                                if (item is PromoRecommendationItem) {
                                    return@map item.copy(isCalculating = true)
                                } else if (item is PromoItem) {
                                    val isClickedItem = item.code == newClickedItem.code
                                    val isMvcPromoWithSecondary = item.shopId > 0 && item.hasSecondaryPromo
                                    val isDisabledOrIneligibleOrSelected =
                                        item.state is PromoItemState.Disabled ||
                                            item.state is PromoItemState.Ineligible ||
                                            item.state is PromoItemState.Selected
                                    val isVisibleAndExpanded =
                                        item.isVisible && item.isExpanded
                                    if (isMvcPromoWithSecondary && isVisibleAndExpanded &&
                                        !isClickedItem && !isDisabledOrIneligibleOrSelected
                                    ) {
                                        if (!needToShowLoading) {
                                            needToShowLoading = true
                                        }
                                        return@map item.copy(
                                            state = PromoItemState.Loading(useSecondaryPromo = item.state.useSecondaryPromo)
                                        )
                                    } else if (isClickedItem) {
                                        return@map newClickedItem.copy(isCalculating = true)
                                    } else {
                                        return@map item.copy(isCalculating = true)
                                    }
                                } else {
                                    return@map item
                                }
                            }
                        updatedState = updatedState.copy(
                            items = updatedItems,
                            isReload = false
                        )
                        _promoPageUiState.postValue(updatedState)

                        if (needToShowLoading) {
                            PromoUsageIdlingResource.increment()
                            delay(CLASH_ARTIFICIAL_DELAY)
                            PromoUsageIdlingResource.decrement()
                        }

                        // Calculate clash
                        val clashCalculationResult =
                            calculateClickPromo(newClickedItem, updatedItems, useSecondaryPromo)
                        newClickedItem = clashCalculationResult.first
                        updatedItems = clashCalculationResult.second

                        // Update Promo Recommendation section
                        val recommendationItem = updatedItems.getRecommendationItem()
                        if (recommendationItem != null) {
                            val (_, newUpdatedItems) =
                                calculateRecommendedPromo(
                                    clickedItem = newClickedItem,
                                    recommendationItem = recommendationItem,
                                    items = updatedItems
                                )
                            updatedItems = newUpdatedItems
                        }

                        // Sort Promo in each sections
                        updatedItems = sortPromo(updatedItems)

                        // Update TnC section
                        updatedItems = addTncPromo(updatedItems)

                        // Update SavingInfo section
                        val updatedSavingInfo = calculatePromoSavingInfo(
                            items = updatedItems,
                            previousSavingInfo = updatedState.savingInfo
                        )

                        // Remove isCalculating after all process is done
                        updatedItems = updatedItems.map { item ->
                            when (item) {
                                is PromoRecommendationItem -> {
                                    return@map item.copy(isCalculating = false)
                                }

                                is PromoItem -> {
                                    return@map item.copy(isCalculating = false)
                                }

                                else -> {
                                    return@map item
                                }
                            }
                        }

                        // Update items
                        updatedState = pageState.copy(
                            items = updatedItems,
                            savingInfo = updatedSavingInfo,
                            isCalculating = false,
                            isReload = false
                        )
                        _promoPageUiState.postValue(updatedState)

                        // Tell UI about clicked promo (for auto scroll, tracker, etc..)
                        _clickPromoUiAction.postValue(ClickPromoUiAction.Updated(newClickedItem))
                    }
                }
            },
            onError = {
                Timber.e(it)
            }
        )
    }

    private fun calculateRecommendedPromo(
        clickedItem: PromoItem,
        recommendationItem: PromoRecommendationItem,
        items: List<DelegateAdapterItem>
    ): Pair<PromoRecommendationItem, List<DelegateAdapterItem>> {
        val initialSelectedCodes = recommendationItem.selectedCodes
        val isRecommendedCodeSelected = clickedItem.state is PromoItemState.Selected &&
            clickedItem.isRecommended
        val currentSelectedCodes = if (isRecommendedCodeSelected) {
            initialSelectedCodes.plus(clickedItem.code)
        } else {
            initialSelectedCodes.minus(clickedItem.code)
        }
        val updatedRecommendationItem = recommendationItem.copy(
            selectedCodes = currentSelectedCodes
        )
        val updatedSelectedCodes = updatedRecommendationItem.selectedCodes
        val updatedItems = items.map { item ->
            if (item is PromoRecommendationItem) {
                val allRecommendationCodeSelected = updatedSelectedCodes
                    .containsAll(recommendationItem.codes)
                val isSelectedCodesChanged = updatedSelectedCodes.size != initialSelectedCodes.size
                return@map updatedRecommendationItem.copy(
                    showAnimation = isSelectedCodesChanged && allRecommendationCodeSelected
                )
            } else {
                return@map item
            }
        }
        return Pair(updatedRecommendationItem, updatedItems)
    }

    private fun calculateClickPromo(
        clickedItem: PromoItem,
        items: List<DelegateAdapterItem>,
        useSecondaryPromo: Boolean
    ): Pair<PromoItem, List<DelegateAdapterItem>> {
        var newClickedItem = clickedItem.copy()
        var updatedItems = items
            .map { item ->
                if (item is PromoItem && item.id == newClickedItem.id) {
                    return@map newClickedItem
                } else if (item is PromoRecommendationItem) {
                    if (newClickedItem.isRecommended) {
                        if (newClickedItem.state is PromoItemState.Normal &&
                            item.selectedCodes.contains(newClickedItem.code)
                        ) {
                            return@map item.copy(
                                selectedCodes = item.selectedCodes.minus(newClickedItem.code)
                            )
                        } else if (newClickedItem.state is PromoItemState.Selected &&
                            !item.selectedCodes.contains(newClickedItem.code)
                        ) {
                            return@map item.copy(
                                selectedCodes = item.selectedCodes.plus(newClickedItem.code)
                            )
                        }
                    }
                    return@map item
                } else {
                    return@map item
                }
            }
        // Calculate clash
        val (resultItems, isCausingClash) = calculateClash(newClickedItem, updatedItems, useSecondaryPromo)
        updatedItems = resultItems
            .map { item ->
                if (item is PromoItem && item.id == newClickedItem.id) {
                    val updatedState = if (item.state is PromoItemState.Normal &&
                        item.currentClashingPromoCodes.isEmpty() &&
                        item.currentClashingSecondaryPromoCodes.isEmpty()
                    ) {
                        PromoItemState.Normal(useSecondaryPromo = false)
                    } else {
                        item.state
                    }
                    newClickedItem = item.copy(
                        state = updatedState,
                        isCausingOtherPromoClash = isCausingClash
                    )
                    return@map newClickedItem
                } else {
                    return@map item
                }
            }
        return Pair(newClickedItem, updatedItems)
    }

    private fun calculateClash(
        selectedItem: PromoItem,
        updatedItems: List<DelegateAdapterItem>,
        useSecondaryPromo: Boolean
    ): Pair<List<DelegateAdapterItem>, Boolean> {
        var isSelectedPromoCausingClash = false
        var processedItems = updatedItems
        if (selectedItem.state is PromoItemState.Selected) {
            processedItems = updatedItems.map { item ->
                if (item is PromoItem && item.code != selectedItem.code &&
                    item.state !is PromoItemState.Ineligible
                ) {
                    val (resultItem, isCausingClash) =
                        checkAndSetClashOnSelectionEvent(item, selectedItem, useSecondaryPromo)
                    if (!isSelectedPromoCausingClash) {
                        isSelectedPromoCausingClash = isCausingClash
                    }
                    return@map resultItem
                } else {
                    return@map item
                }
            }
        } else if (selectedItem.state is PromoItemState.Normal) {
            processedItems = updatedItems.map { item ->
                if (item is PromoItem && item.code != selectedItem.code &&
                    item.state !is PromoItemState.Ineligible
                ) {
                    val (resultItem, _) =
                        checkAndSetClashOnDeselectionEvent(item, selectedItem, useSecondaryPromo)
                    return@map resultItem
                } else {
                    return@map item
                }
            }

            // Re-calculate clash for adjusted selected promo after deselection event
            val snapshotProcessedItems = processedItems
            snapshotProcessedItems.forEach { adjustedItem ->
                if (adjustedItem is PromoItem && adjustedItem.state is PromoItemState.Selected) {
                    processedItems = processedItems.map { item ->
                        if (item is PromoItem && item.code != adjustedItem.code &&
                            item.state !is PromoItemState.Ineligible
                        ) {
                            val (resultItem, _) =
                                checkAndSetClashOnSelectionEvent(item, adjustedItem, adjustedItem.state.useSecondaryPromo)
                            return@map resultItem
                        } else {
                            return@map item
                        }
                    }
                }
            }
        }
        // Normalize all disabled promo when there's no selected promo
        val hasNoSelectedPromo = processedItems.getSelectedPromoCodes().isEmpty()
        if (hasNoSelectedPromo) {
            processedItems = processedItems.map { item ->
                if (item is PromoItem && item.state is PromoItemState.Disabled) {
                    return@map item.copy(
                        currentClashingPromoCodes = emptyList(),
                        currentClashingSecondaryPromoCodes = emptyList(),
                        state = PromoItemState.Normal(useSecondaryPromo = false)
                    )
                } else {
                    return@map item
                }
            }
        }
        return Pair(processedItems, isSelectedPromoCausingClash)
    }

    private fun checkAndSetClashOnSelectionEvent(
        currentItem: PromoItem,
        selectedItem: PromoItem,
        useSecondaryPromo: Boolean
    ): Pair<PromoItem, Boolean> {
        var resultItem = currentItem.copy()
        var isCausingClash = false
        val selectedPromoCode = if (useSecondaryPromo) {
            selectedItem.secondaryPromo.code
        } else {
            selectedItem.code
        }
        // Check for primary promo code
        val primaryClashingInfo = resultItem.clashingInfos
            .firstOrNull { it.code == selectedPromoCode }
        if (primaryClashingInfo != null &&
            !resultItem.currentClashingPromoCodes.contains(selectedPromoCode)
        ) {
            // Promo is clashing with current selected code
            // add selected promo code to current primary clashing list
            val clashingPrimaryCodes = resultItem.currentClashingPromoCodes
                .plus(selectedPromoCode)
            resultItem = resultItem.copy(
                currentClashingPromoCodes = clashingPrimaryCodes,
                state = PromoItemState.Disabled(
                    useSecondaryPromo = false,
                    message = primaryClashingInfo.message
                )
            )
            isCausingClash = true
        }
        // Check for secondary promo code
        if (resultItem.hasSecondaryPromo) {
            val secondaryClashingInfo = resultItem.secondaryPromo.clashingInfos
                .firstOrNull { it.code == selectedPromoCode }
            if (secondaryClashingInfo != null &&
                !resultItem.currentClashingSecondaryPromoCodes.contains(selectedPromoCode)
            ) {
                // Promo is clashing with current selected code
                // add selected promo code to current secondary clashing list
                val clashingSecondaryCodes = resultItem.currentClashingSecondaryPromoCodes
                    .plus(selectedPromoCode)
                val state = when (resultItem.state) {
                    // Promo is already clashing (with primary code OR other secondary code)
                    // update current clashing info with latest secondary clashing info
                    is PromoItemState.Disabled -> {
                        PromoItemState.Disabled(
                            useSecondaryPromo = true,
                            message = secondaryClashingInfo.message
                        )
                    }

                    // Promo is clashing with secondary while in selected state
                    // adjust selected to show primary promo
                    is PromoItemState.Selected -> {
                        PromoItemState.Selected(useSecondaryPromo = false)
                    }

                    // Promo is clashing with secondary while in normal state
                    // adjust selected to show primary promo
                    else -> {
                        PromoItemState.Normal(useSecondaryPromo = false)
                    }
                }
                resultItem = resultItem.copy(
                    state = state,
                    currentClashingSecondaryPromoCodes = clashingSecondaryCodes
                )
                isCausingClash = true
            } else {
                // Revert item state back to normal with secondary promo
                // if primary is clashing and secondary is empty
                if (resultItem.state is PromoItemState.Disabled) {
                    if (resultItem.currentClashingPromoCodes.contains(selectedPromoCode) &&
                        resultItem.currentClashingSecondaryPromoCodes.isEmpty()
                    ) {
                        resultItem = resultItem.copy(
                            state = PromoItemState.Normal(useSecondaryPromo = true)
                        )
                    }
                }
                isCausingClash = false
            }
        }
        // If item is still in loading state after processing change item back to previous normal state
        if (resultItem.state is PromoItemState.Loading) {
            val notClashingWithAnyPromo = resultItem.currentClashingPromoCodes.isEmpty() &&
                resultItem.currentClashingSecondaryPromoCodes.isEmpty()
            resultItem = if (notClashingWithAnyPromo) {
                resultItem.copy(
                    state = PromoItemState.Normal(useSecondaryPromo = false)
                )
            } else {
                resultItem.copy(
                    state = PromoItemState.Normal(useSecondaryPromo = currentItem.state.useSecondaryPromo)
                )
            }
        }
        return Pair(resultItem, isCausingClash)
    }

    private fun checkAndSetClashOnDeselectionEvent(
        currentItem: PromoItem,
        selectedItem: PromoItem,
        useSecondaryPromo: Boolean
    ): Pair<PromoItem, Boolean> {
        var resultItem = currentItem.copy()
        var isCausingClash = false
        val selectedPromoCode = if (useSecondaryPromo) {
            selectedItem.secondaryPromo.code
        } else {
            selectedItem.code
        }
        if (selectedItem.hasSecondaryPromo) {
            val isClashingWithCurrentPromoSecondary = !useSecondaryPromo &&
                resultItem.currentClashingPromoCodes.contains(selectedItem.secondaryPromo.code)
            if (isClashingWithCurrentPromoSecondary) {
                resultItem = resultItem.copy(
                    currentClashingPromoCodes = resultItem.currentClashingPromoCodes
                        .minus(selectedItem.secondaryPromo.code)
                )
            }
        }
        // Check for primary promo
        val primaryClashingInfo = resultItem.clashingInfos
            .firstOrNull { it.code == selectedPromoCode }
        if (primaryClashingInfo != null &&
            resultItem.currentClashingPromoCodes.contains(selectedPromoCode)
        ) {
            val clashingPrimaryCodes = resultItem.currentClashingPromoCodes
                .minus(selectedPromoCode)
            if (clashingPrimaryCodes.isNotEmpty()) {
                val otherClashingCode = resultItem.currentClashingPromoCodes.first()
                val otherClashingInfo = resultItem.clashingInfos
                    .firstOrNull { it.code == otherClashingCode }
                if (otherClashingInfo != null) {
                    resultItem = resultItem.copy(
                        currentClashingPromoCodes = clashingPrimaryCodes,
                        state = PromoItemState.Disabled(
                            useSecondaryPromo = resultItem.useSecondaryPromo,
                            message = otherClashingInfo.message
                        )
                    )
                    isCausingClash = true
                } else {
                    val state = if (resultItem.state is PromoItemState.Disabled) {
                        val isClashingWithSecondaryPromo = resultItem.hasSecondaryPromo &&
                            resultItem.currentClashingSecondaryPromoCodes.isNotEmpty()
                        if (isClashingWithSecondaryPromo) {
                            val secondaryClashingCode =
                                resultItem.currentClashingSecondaryPromoCodes.first()
                            val secondaryClashingInfo = resultItem.clashingInfos.firstOrNull {
                                it.code == secondaryClashingCode
                            }
                            if (secondaryClashingInfo != null) {
                                PromoItemState.Disabled(
                                    useSecondaryPromo = true,
                                    message = secondaryClashingInfo.message
                                )
                            } else {
                                resultItem.state
                            }
                        } else {
                            PromoItemState.Normal(useSecondaryPromo = false)
                        }
                    } else {
                        resultItem.state
                    }
                    resultItem = resultItem.copy(
                        currentClashingPromoCodes = clashingPrimaryCodes,
                        state = state
                    )
                    isCausingClash = false
                }
            } else {
                val state = if (resultItem.state is PromoItemState.Disabled) {
                    val isClashingWithSecondaryPromo = resultItem.hasSecondaryPromo &&
                        resultItem.currentClashingSecondaryPromoCodes.isNotEmpty()
                    if (isClashingWithSecondaryPromo) {
                        val secondaryClashingCode =
                            resultItem.currentClashingSecondaryPromoCodes.first()
                        val secondaryClashingInfo = resultItem.secondaryPromo.clashingInfos.firstOrNull {
                            it.code == secondaryClashingCode
                        }
                        if (secondaryClashingInfo != null) {
                            PromoItemState.Disabled(
                                useSecondaryPromo = true,
                                message = secondaryClashingInfo.message
                            )
                        } else {
                            resultItem.state
                        }
                    } else {
                        PromoItemState.Normal(useSecondaryPromo = false)
                    }
                } else if (resultItem.state is PromoItemState.Normal) {
                    PromoItemState.Normal(useSecondaryPromo = false)
                } else if (resultItem.state is PromoItemState.Selected) {
                    PromoItemState.Selected(useSecondaryPromo = false)
                } else {
                    resultItem.state
                }
                resultItem = resultItem.copy(
                    currentClashingPromoCodes = clashingPrimaryCodes,
                    state = state
                )
                isCausingClash = false
            }
        }
        // Check for secondary promo
        if (resultItem.hasSecondaryPromo) {
            val secondaryClashingInfo = resultItem.secondaryPromo.clashingInfos
                .firstOrNull { it.code == selectedPromoCode }
            if (secondaryClashingInfo != null &&
                resultItem.currentClashingSecondaryPromoCodes.contains(selectedPromoCode)
            ) {
                val clashingSecondaryCodes = resultItem.currentClashingSecondaryPromoCodes
                    .minus(selectedPromoCode)
                if (clashingSecondaryCodes.isNotEmpty()) {
                    val otherClashingCode = resultItem.currentClashingSecondaryPromoCodes
                        .first()
                    val otherClashingInfo = resultItem.secondaryPromo.clashingInfos
                        .firstOrNull { it.code == otherClashingCode }
                    if (otherClashingInfo != null) {
                        resultItem = resultItem.copy(
                            currentClashingSecondaryPromoCodes = clashingSecondaryCodes,
                            state = PromoItemState.Disabled(
                                useSecondaryPromo = true,
                                message = otherClashingInfo.message
                            )
                        )
                        isCausingClash = true
                    } else {
                        val state = if (resultItem.state is PromoItemState.Disabled) {
                            val isClashingWithPromo =
                                resultItem.currentClashingPromoCodes.isNotEmpty()
                            if (isClashingWithPromo) {
                                val clashingCode =
                                    resultItem.currentClashingPromoCodes.first()
                                val clashingInfo = resultItem.clashingInfos.firstOrNull {
                                    it.code == clashingCode
                                }
                                if (clashingInfo != null) {
                                    PromoItemState.Disabled(
                                        useSecondaryPromo = false,
                                        message = clashingInfo.message
                                    )
                                } else {
                                    resultItem.state
                                }
                            } else {
                                PromoItemState.Normal(useSecondaryPromo = false)
                            }
                        } else if (resultItem.state is PromoItemState.Normal) {
                            PromoItemState.Normal(useSecondaryPromo = false)
                        } else if (resultItem.state is PromoItemState.Selected) {
                            PromoItemState.Selected(useSecondaryPromo = false)
                        } else {
                            resultItem.state
                        }
                        resultItem = resultItem.copy(
                            currentClashingSecondaryPromoCodes = clashingSecondaryCodes,
                            state = state
                        )
                        isCausingClash = false
                    }
                } else {
                    val state = if (resultItem.state is PromoItemState.Disabled) {
                        val isClashingWithPromo = resultItem.currentClashingPromoCodes.isNotEmpty()
                        if (isClashingWithPromo) {
                            val clashingCode =
                                resultItem.currentClashingPromoCodes.first()
                            val clashingInfo = resultItem.clashingInfos.firstOrNull {
                                it.code == clashingCode
                            }
                            if (clashingInfo != null) {
                                PromoItemState.Disabled(
                                    useSecondaryPromo = false,
                                    message = clashingInfo.message
                                )
                            } else {
                                resultItem.state
                            }
                        } else {
                            PromoItemState.Normal(useSecondaryPromo = false)
                        }
                    } else {
                        resultItem.state
                    }
                    resultItem = resultItem.copy(
                        currentClashingSecondaryPromoCodes = clashingSecondaryCodes,
                        state = state
                    )
                    isCausingClash = false
                }
            }
        }
        if (resultItem.state is PromoItemState.Loading) {
            resultItem = if (resultItem.currentClashingPromoCodes.isEmpty() &&
                resultItem.currentClashingSecondaryPromoCodes.isEmpty()
            ) {
                resultItem.copy(
                    state = PromoItemState.Normal(useSecondaryPromo = false)
                )
            } else {
                resultItem.copy(
                    state = PromoItemState.Normal(useSecondaryPromo = currentItem.state.useSecondaryPromo)
                )
            }
        }
        return Pair(resultItem, isCausingClash)
    }

    private fun sortPromo(
        items: List<DelegateAdapterItem>,
        shouldSortRecommendedSection: Boolean = false
    ): List<DelegateAdapterItem> {
        val resultItems = items.toMutableList()
        val sectionHeaders = items.mapIndexed { index, item -> index to item }
            .filter {
                if (shouldSortRecommendedSection) {
                    it.second is PromoRecommendationItem || it.second is PromoAccordionHeaderItem
                } else {
                    it.second is PromoAccordionHeaderItem
                }
            }
            .toMap()
        sectionHeaders.forEach { header ->
            val headerId = header.value.id
            val headerIndex = header.key
            val headerItems = items.filterIsInstance<PromoItem>()
                .filter { it.headerId == headerId }
            var sortedItems = sortPromoInSection(headerItems)
            if (headerId == PromoPageSection.SECTION_RECOMMENDATION) {
                val recommendedItemCount = sortedItems.size
                sortedItems = sortedItems.mapIndexed { index, item ->
                    val isLastRecommended = index == recommendedItemCount - 1
                    return@mapIndexed item.copy(isLastRecommended = isLastRecommended)
                }
            }
            resultItems.removeAll { it is PromoItem && it.headerId == headerId }
            resultItems.addAll(headerIndex + 1, sortedItems)
        }
        return resultItems
    }

    private fun sortPromoInSection(
        items: List<PromoItem>
    ): List<PromoItem> {
        val resultItems = mutableListOf<PromoItem>()
        val selectedItems = items.filter { it.state is PromoItemState.Selected }
            .sortedBy {
                if (it.useSecondaryPromo) {
                    it.secondaryPromo.index
                } else {
                    it.index
                }
            }
        resultItems.addAll(selectedItems)
        val normalItems = items.filter { it.state is PromoItemState.Normal }
            .sortedBy {
                if (it.useSecondaryPromo) {
                    it.secondaryPromo.index
                } else {
                    it.index
                }
            }
        resultItems.addAll(normalItems)
        val disabledItems = items.filter { it.state is PromoItemState.Disabled }
            .sortedBy {
                if (it.useSecondaryPromo) {
                    it.secondaryPromo.index
                } else {
                    it.index
                }
            }
        resultItems.addAll(disabledItems)
        val ineligibleItems = items.filter { it.state is PromoItemState.Ineligible }
            .sortedBy {
                if (it.useSecondaryPromo) {
                    it.secondaryPromo.index
                } else {
                    it.index
                }
            }
        resultItems.addAll(ineligibleItems)
        return resultItems
    }

    private fun addTncPromo(
        items: List<DelegateAdapterItem>
    ): List<DelegateAdapterItem> {
        val selectedPromos = items.getSelectedPromos()
        return if (selectedPromos.isNotEmpty()) {
            val tncItem = items.getTncItem() ?: PromoTncItem()
            val selectedPromoCodes = selectedPromos.map {
                if (it.useSecondaryPromo) {
                    "${it.couponUrl}${it.secondaryPromo.code}"
                } else {
                    "${it.couponUrl}${it.code}"
                }
            }
            val selectedPromoCodesWithTitle = selectedPromos.map {
                if (it.useSecondaryPromo) {
                    "${it.couponUrl}${it.secondaryPromo.code}|${it.secondaryPromo.title}"
                } else {
                    "${it.couponUrl}${it.code}|${it.title}"
                }
            }
            items.filterNot { it is PromoTncItem }
                .plus(
                    tncItem.copy(
                        selectedPromoCodes = selectedPromoCodes,
                        selectedPromoCodesWithTitle = selectedPromoCodesWithTitle
                    )
                )
        } else {
            items.filterNot { it is PromoTncItem }
        }
    }

    private fun updateAttemptedPromo(
        items: List<DelegateAdapterItem>,
        attemptedPromoCode: String,
        attemptedPromoCodeError: PromoAttemptedError
    ): List<DelegateAdapterItem> {
        val attemptItem = items.getAttemptItem()
        if (attemptItem != null) {
            return items.map { item ->
                if (item is PromoAttemptItem) {
                    return@map item.copy(
                        attemptedPromoCode = attemptedPromoCode,
                        errorAttemptedPromoCode = attemptedPromoCodeError.code,
                        errorMessage = attemptedPromoCodeError.message
                    )
                } else {
                    return@map item
                }
            }
        }
        return items
    }

    fun onClickAccordionHeader(clickedItem: PromoAccordionHeaderItem) {
        _promoPageUiState.ifSuccess { pageState ->
            if (pageState.isCalculating) return@ifSuccess
            val currentItems = pageState.items
            val isHeaderExpanded = !clickedItem.isExpanded
            val firstPromoItem = currentItems
                .filterIsInstance<PromoItem>()
                .firstOrNull { it.headerId == clickedItem.id }
            val viewAllItem = currentItems
                .filterIsInstance<PromoAccordionViewAllItem>()
                .firstOrNull { it.headerId == clickedItem.id }
            val updatedItems = currentItems
                .map { item ->
                    if (item is PromoAccordionHeaderItem && item.id == clickedItem.id) {
                        return@map item.copy(isExpanded = isHeaderExpanded)
                    } else if (item is PromoItem && item.headerId == clickedItem.id) {
                        if (viewAllItem != null) {
                            item.copy(
                                isExpanded = firstPromoItem?.id == item.id,
                                isVisible = isHeaderExpanded
                            )
                        } else {
                            item.copy(
                                isExpanded = isHeaderExpanded,
                                isVisible = isHeaderExpanded
                            )
                        }
                    } else if (item is PromoAccordionViewAllItem && item.headerId == clickedItem.id) {
                        return@map item.copy(
                            isExpanded = isHeaderExpanded,
                            isVisible = isHeaderExpanded
                        )
                    } else {
                        return@map item
                    }
                }
            _promoPageUiState.postValue(
                pageState.copy(
                    items = updatedItems,
                    isReload = false
                )
            )
        }
    }

    fun onClickViewAllAccordion(clickedItem: PromoAccordionViewAllItem) {
        _promoPageUiState.ifSuccess { pageState ->
            if (pageState.isCalculating) return@ifSuccess
            val currentItems = pageState.items
            val updatedItems = currentItems
                .map { item ->
                    if (item is PromoItem && item.headerId == clickedItem.headerId) {
                        item.copy(
                            isExpanded = true,
                            isVisible = true
                        )
                    } else {
                        item
                    }
                }
                .filterNot {
                    it is PromoAccordionViewAllItem && it.headerId == clickedItem.headerId
                }
            _promoPageUiState.postValue(
                pageState.copy(
                    items = updatedItems,
                    isReload = false
                )
            )
        }
    }

    fun onClickBuy(
        entryPoint: PromoPageEntryPoint,
        validateUsePromoRequest: ValidateUsePromoRequest,
        boPromoCodes: List<String>,
        isCartCheckoutRevamp: Boolean
    ) {
        _promoPageUiState.ifSuccess { pageState ->
            if (isChangedFromInitialState()) {
                val hasSelectedPromo = pageState.items.getSelectedPromoCodes().isNotEmpty()
                val hasSelectedBoPromo = boPromoCodes.isNotEmpty()
                if (hasSelectedPromo || hasSelectedBoPromo) {
                    onApplyPromo(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes,
                        isCartCheckoutRevamp = isCartCheckoutRevamp,
                        onSuccess = { entryPoint, validateUse, lastValidateUseRequest, appliedPromos ->
                            _applyPromoUiAction.postValue(
                                ApplyPromoUiAction.SuccessWithApplyPromo(
                                    entryPoint = entryPoint,
                                    validateUse = validateUse,
                                    lastValidateUsePromoRequest = lastValidateUseRequest,
                                    appliedPromos = appliedPromos
                                )
                            )
                        },
                        onFailed = { throwable ->
                            _applyPromoUiAction.postValue(
                                ApplyPromoUiAction.Failed(
                                    throwable,
                                    false
                                )
                            )
                        }
                    )
                } else {
                    onClearPromo(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes,
                        onSuccess = { entryPoint, clearPromo, lastValidateUseRequest, clearedPromos ->
                            _clearPromoUiAction.postValue(
                                ClearPromoUiAction.Success(
                                    entryPoint = entryPoint,
                                    clearPromo = clearPromo,
                                    lastValidateUseRequest = lastValidateUseRequest,
                                    clearedPromos = clearedPromos
                                )
                            )
                        },
                        onFailed = { throwable ->
                            _clearPromoUiAction.postValue(ClearPromoUiAction.Failed(throwable))
                        }
                    )
                }
            } else {
                _applyPromoUiAction.postValue(ApplyPromoUiAction.SuccessNoAction)
            }
        }
    }

    fun onApplyPromo(
        entryPoint: PromoPageEntryPoint,
        validateUsePromoRequest: ValidateUsePromoRequest,
        boPromoCodes: List<String>,
        isCartCheckoutRevamp: Boolean,
        onSuccess: ((entryPoint: PromoPageEntryPoint, validateUse: ValidateUsePromoRevampUiModel, lastValidateUseRequest: ValidateUsePromoRequest, appliedPromos: List<PromoItem>) -> Unit)? = null,
        onFailed: ((throwable: Throwable) -> Unit)? = null
    ) {
        _promoPageUiState.ifSuccess { pageState ->
            val currentItems = pageState.items.filterIsInstance<PromoItem>()

            val appliedPromos = currentItems.getSelectedPromos()
            var newValidateUseRequest = updateCurrentPromoCodeToValidateUsePromoRequest(
                items = currentItems,
                validateUsePromoRequest = validateUsePromoRequest
            )
            newValidateUseRequest = removeInvalidPromoCodeFromValidateUsePromoRequest(
                validateUsePromoRequest = newValidateUseRequest,
                selectedPromoCodes = appliedPromos.getSelectedPromoCodes(),
                boPromoCodes = boPromoCodes
            )
            newValidateUseRequest = newValidateUseRequest.copy(
                skipApply = 0,
                isCartCheckoutRevamp = isCartCheckoutRevamp
            )

            val param = ValidateUsePromoUsageParam.create(
                newValidateUseRequest,
                chosenAddressRequestHelper.getChosenAddress()
            )

            PromoUsageIdlingResource.increment()
            launchCatchError(
                context = dispatchers.io,
                block = {
                    PromoUsageIdlingResource.decrement()
                    val response = validateUseUseCase(param)
                    val validateUse = validateUseMapper.mapToValidateUseResponse(response)
                    handleValidateUseSuccess(
                        entryPoint = entryPoint,
                        validateUse = validateUse,
                        lastValidateUsePromoRequest = newValidateUseRequest,
                        appliedPromos = appliedPromos,
                        onSuccess = onSuccess,
                        onFailed = onFailed
                    )
                },
                onError = { throwable ->
                    PromoUsageIdlingResource.decrement()
                    handleValidateUseFailed(
                        throwable = throwable,
                        onFailed = onFailed
                    )
                }
            )
        }
    }

    private fun handleValidateUseSuccess(
        entryPoint: PromoPageEntryPoint,
        validateUse: ValidateUsePromoRevampUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest,
        appliedPromos: List<PromoItem>,
        onSuccess: ((entryPoint: PromoPageEntryPoint, validateUse: ValidateUsePromoRevampUiModel, lastValidateUseRequest: ValidateUsePromoRequest, appliedPromos: List<PromoItem>) -> Unit)? = null,
        onFailed: ((throwable: Throwable) -> Unit)? = null
    ) {
        if (validateUse.status == VALIDATE_USE_STATUS_SUCCESS && validateUse.errorCode == VALIDATE_USE_CODE_SUCCESS) {
            // Response is OK, then need to check whether it's apply promo manual or apply checked promo items
            if (validateUse.promoUiModel.clashingInfoDetailUiModel.isClashedPromos) {
                // Promo is clashing. Need to reload promo page
                _applyPromoUiAction.postValue(
                    ApplyPromoUiAction.Failed(PromoErrorException(), true)
                )
            } else {
                if (validateUse.promoUiModel.globalSuccess) {
                    handleApplyPromoSuccess(
                        entryPoint = entryPoint,
                        validateUse = validateUse,
                        lastValidateUsePromoRequest = lastValidateUsePromoRequest,
                        appliedPromos = appliedPromos,
                        onSuccess = onSuccess
                    )
                } else {
                    handleApplyPromoFailed(
                        validateUse = validateUse,
                        onFailed = onFailed
                    )
                }
            }
        } else {
            // Response is not OK, need to show error message
            val exception = PromoErrorException(validateUse.message.joinToString(". "))
            PromoUsageLogger.logOnErrorApplyPromo(exception)
            onFailed?.invoke(exception)
        }
    }

    private fun handleValidateUseFailed(
        throwable: Throwable,
        onFailed: ((throwable: Throwable) -> Unit)? = null
    ) {
        PromoUsageLogger.logOnErrorApplyPromo(throwable)
        onFailed?.invoke(throwable)
    }

    private fun handleApplyPromoSuccess(
        entryPoint: PromoPageEntryPoint,
        validateUse: ValidateUsePromoRevampUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest,
        appliedPromos: List<PromoItem>,
        onSuccess: ((entryPoint: PromoPageEntryPoint, validateUse: ValidateUsePromoRevampUiModel, lastValidateUseRequest: ValidateUsePromoRequest, appliedPromos: List<PromoItem>) -> Unit)? = null
    ) {
        val isGlobalSuccess = validateUse.promoUiModel.messageUiModel.state != PROMO_STATE_RED
        if (isGlobalSuccess) {
            onSuccess?.invoke(entryPoint, validateUse, lastValidateUsePromoRequest, appliedPromos)
        }
    }

    private fun handleApplyPromoFailed(
        validateUse: ValidateUsePromoRevampUiModel,
        onFailed: ((throwable: Throwable) -> Unit)? = null
    ) {
        val response = validateUse.promoUiModel
        val redStateMap = hashMapOf<String, String>()
        if (!response.success) {
            // Error from global promo
            if (response.codes.isNotEmpty()) {
                response.codes.forEach { code ->
                    if (!redStateMap.containsKey(code)) {
                        redStateMap[code] = response.messageUiModel.text
                    }
                }
            }
        } else {
            // Error from merchant promo
            if (response.voucherOrderUiModels.isNotEmpty()) {
                response.voucherOrderUiModels.forEach { voucherOrder ->
                    if (!voucherOrder.success && voucherOrder.code.isNotBlank() &&
                        !redStateMap.containsKey(voucherOrder.code)
                    ) {
                        redStateMap[voucherOrder.code] = voucherOrder.messageUiModel.text
                    }
                }
            }
        }

        if (redStateMap.isNotEmpty()) {
            _promoPageUiState.ifSuccess { pageState ->
                val currentItems = pageState.items

                val updatedItems = currentItems.map { item ->
                    if (item is PromoItem && redStateMap.keys.containsPromoCode(item)) {
                        val errorMessage =
                            redStateMap[item.getPromoCode(redStateMap.keys.toSet())] ?: ""
                        return@map item.copy(
                            state = PromoItemState.Disabled(
                                useSecondaryPromo = item.useSecondaryPromo,
                                message = errorMessage
                            )
                        )
                    } else {
                        return@map item
                    }
                }
                val updatedSavingInfo = calculatePromoSavingInfo(
                    items = updatedItems,
                    previousSavingInfo = pageState.savingInfo
                )
                _promoPageUiState.postValue(
                    pageState.copy(
                        items = updatedItems,
                        savingInfo = updatedSavingInfo,
                        isReload = false
                    )
                )

                val exception =
                    PromoErrorException(response.additionalInfoUiModel.errorDetailUiModel.message)
                PromoUsageLogger.logOnErrorApplyPromo(exception)

                onFailed?.invoke(exception) ?: run {
                    _applyPromoUiAction.postValue(
                        ApplyPromoUiAction.Failed(exception)
                    )
                }
            }
        } else {
            PromoUsageLogger.logOnErrorApplyPromo(
                PromoErrorException("response is ok but got empty applied promo")
            )
            val exception =
                PromoErrorException(response.additionalInfoUiModel.errorDetailUiModel.message)
            onFailed?.invoke(exception) ?: run {
                _applyPromoUiAction.postValue(ApplyPromoUiAction.Failed(exception, false))
            }
        }
    }

    private fun PromoItem.getPromoCode(codes: Set<String>): String {
        return if (isBebasOngkir) {
            boAdditionalData.map { it.code }.intersect(codes).firstOrNull() ?: ""
        } else {
            if (useSecondaryPromo) {
                code
            } else {
                secondaryPromo.code
            }
        }
    }

    private fun Collection<String>.containsPromoCode(item: PromoItem): Boolean {
        return if (item.isBebasOngkir) {
            this.intersect(item.boAdditionalData.map { it.code }.toSet()).isNotEmpty()
        } else {
            if (item.useSecondaryPromo) {
                this.contains(item.secondaryPromo.code)
            } else {
                this.contains(item.code)
            }
        }
    }

    private fun updateCurrentPromoCodeToValidateUsePromoRequest(
        items: List<PromoItem>,
        validateUsePromoRequest: ValidateUsePromoRequest
    ): ValidateUsePromoRequest {
        val codes = ArrayList(validateUsePromoRequest.codes)
        items.forEach { item ->
            val promoCode: String
            val shopId: Long
            if (item.useSecondaryPromo) {
                promoCode = item.secondaryPromo.code
                shopId = item.secondaryPromo.shopId
            } else {
                promoCode = item.code
                shopId = item.shopId
            }
            if (item.state is PromoItemState.Selected) {
                if (shopId.isZero() && !validateUsePromoRequest.codes.contains(promoCode)) {
                    codes.add(promoCode)
                }
            } else {
                if (shopId.isZero() && validateUsePromoRequest.codes.contains(promoCode)) {
                    codes.remove(promoCode)
                }
            }
        }
        val orders = validateUsePromoRequest.orders.map { order ->
            var newOrder = order.copy()
            items.forEach { item ->
                val newCodes = ArrayList(newOrder.codes)
                val promoCode: String
                val uniqueId: String
                val boAdditionalData: List<BoAdditionalData>
                if (item.useSecondaryPromo) {
                    promoCode = item.secondaryPromo.code
                    uniqueId = item.secondaryPromo.uniqueId
                    boAdditionalData = item.secondaryPromo.boAdditionalData
                } else {
                    promoCode = item.code
                    uniqueId = item.uniqueId
                    boAdditionalData = item.boAdditionalData
                }
                if (item.state is PromoItemState.Selected) {
                    // If promo is selected, add promo code to request param
                    // If unique_id == 0, means it's a global promo, else it's promo merchant
                    if (uniqueId == order.uniqueId && !newCodes.contains(promoCode)) {
                        newCodes.add(promoCode)
                        newOrder = order.copy(codes = newCodes)
                    } else if (item.isBebasOngkir) {
                        // If coupon is bebas ongkir promo, then set shipping id and sp id
                        val boData = boAdditionalData.firstOrNull {
                            order.cartStringGroup == it.cartStringGroup
                        }
                        if (boData != null) {
                            if (!newCodes.contains(boData.code)) {
                                // if code is not already in request param, then add bo additional data
                                newCodes.add(boData.code)
                                newOrder = order.copy(
                                    shippingId = boData.shippingId.toInt(),
                                    spId = boData.spId.toInt(),
                                    codes = newCodes
                                )
                            } else {
                                // if code already in request param, set shipping id and sp id again
                                // in case user changes address from other page and the courier info changes
                                newOrder = order.copy(
                                    shippingId = boData.shippingId.toInt(),
                                    spId = boData.spId.toInt()
                                )
                            }
                            newOrder = newOrder.copy(
                                benefitClass = boData.benefitClass,
                                boCampaignId = boData.boCampaignId,
                                shippingPrice = boData.shippingPrice,
                                shippingSubsidy = boData.shippingSubsidy,
                                etaText = boData.etaText
                            )
                        }
                    }
                } else {
                    // If promo is unselected and exist in current promo request, remove it from promo request
                    // If unique_id == 0, means it's a global promo, else it's promo merchant
                    if (uniqueId == order.uniqueId && newCodes.contains(promoCode)) {
                        newCodes.remove(promoCode)
                        newOrder = order.copy(codes = newCodes)
                    } else if (item.isBebasOngkir) {
                        // if promo is bebas ongkir promo, then remove code only
                        val boData = item.boAdditionalData.firstOrNull {
                            order.uniqueId == it.uniqueId
                        }
                        if (boData != null) {
                            if (newCodes.contains(boData.code)) {
                                newCodes.remove(promoCode)
                                newOrder = order.copy(
                                    codes = newCodes,
                                    benefitClass = "",
                                    boCampaignId = 0,
                                    shippingPrice = 0.0,
                                    shippingSubsidy = 0,
                                    etaText = ""
                                )
                                if (validateUsePromoRequest.state == CartConstant.PARAM_CART) {
                                    newOrder = newOrder.copy(
                                        shippingId = 0,
                                        spId = 0
                                    )
                                }
                            }
                        }
                    }
                }
            }
            return@map newOrder
        }
        return validateUsePromoRequest.copy(
            codes = codes,
            orders = orders
        )
    }

    private fun removeInvalidPromoCodeFromValidateUsePromoRequest(
        validateUsePromoRequest: ValidateUsePromoRequest,
        selectedPromoCodes: List<String>,
        boPromoCodes: List<String>
    ): ValidateUsePromoRequest {
        val invalidPromoCodes = ArrayList<String>()
        validateUsePromoRequest.codes.forEach { code ->
            if (!selectedPromoCodes.contains(code)) {
                invalidPromoCodes.add(code)
            }
        }
        validateUsePromoRequest.orders.forEach { order ->
            order.codes.forEach { code ->
                if (!selectedPromoCodes.contains(code) && !boPromoCodes.contains(code)) {
                    invalidPromoCodes.add(code)
                }
            }
        }

        return validateUsePromoRequest.copy(
            codes = validateUsePromoRequest.codes
                .filter { !invalidPromoCodes.contains(it) }
                .toMutableList(),
            orders = validateUsePromoRequest.orders
                .map { order ->
                    order.copy(
                        codes = order.codes
                            .filter { !invalidPromoCodes.contains(it) }
                            .toMutableList()
                    )
                }
        )
    }

    fun onClearPromo(
        entryPoint: PromoPageEntryPoint,
        validateUsePromoRequest: ValidateUsePromoRequest,
        boPromoCodes: List<String>,
        clearPromoRequest: ClearPromoRequest = ClearPromoRequest(),
        onSuccess: ((entryPoint: PromoPageEntryPoint, clearPromo: ClearPromoUiModel, lastValidateUseRequest: ValidateUsePromoRequest, clearedPromos: List<PromoItem>) -> Unit)? = null,
        onFailed: ((throwable: Throwable) -> Unit)?
    ) {
        _promoPageUiState.ifSuccess { pageState ->
            val tempToBeClearedPromoCodes = arrayListOf<String>()

            val tempGlobalPromoCodes = arrayListOf<String>()
            validateUsePromoRequest.codes.forEach { globalPromoCode ->
                if (!boPromoCodes.contains(globalPromoCode) && !tempGlobalPromoCodes.contains(
                        globalPromoCode
                    )
                ) {
                    tempGlobalPromoCodes.add(globalPromoCode)
                    tempToBeClearedPromoCodes.add(globalPromoCode)
                }
            }

            var tempClearPromoOrders = mutableListOf<ClearPromoOrder>()
            validateUsePromoRequest.orders.forEach { order ->
                var newClearPromoOrder = tempClearPromoOrders
                    .find { it.uniqueId == order.uniqueId }
                    .ifNull {
                        val newClearPromoOrder = ClearPromoOrder(
                            uniqueId = order.uniqueId,
                            boType = order.boType,
                            shopId = order.shopId,
                            isPo = order.isPo,
                            poDuration = order.poDuration.toString(),
                            warehouseId = order.warehouseId,
                            cartStringGroup = order.cartStringGroup
                        )
                        tempClearPromoOrders.add(newClearPromoOrder)
                        newClearPromoOrder
                    }
                order.codes.forEach { orderCode ->
                    if (!boPromoCodes.contains(orderCode) &&
                        !newClearPromoOrder.codes.contains(orderCode)
                    ) {
                        val updatedCodes = newClearPromoOrder.codes.also {
                            it.add(orderCode)
                        }
                        newClearPromoOrder = newClearPromoOrder.copy(
                            codes = updatedCodes
                        )
                    }
                }
            }

            val currentItems = pageState.items
            currentItems.forEach { item ->
                if (item is PromoItem && item.isBebasOngkir) {
                    // Get orders in ClearPromo param that eligible for bo promo
                    val boPromoUniqueIds = item.boAdditionalData.map { it.uniqueId }
                    tempClearPromoOrders = tempClearPromoOrders
                        .map { clearOrder ->
                            if (boPromoUniqueIds.contains(clearOrder.uniqueId)) {
                                // For each eligible order, get bo additional data
                                val boData = item.boAdditionalData
                                    .find { it.uniqueId == clearOrder.uniqueId }
                                if (boData != null) {
                                    // If code is not in clear orders code & is applied in previous page, then add bo code
                                    if (clearOrder.codes.contains(boData.code) &&
                                        boPromoCodes.contains(boData.code)
                                    ) {
                                        tempToBeClearedPromoCodes.add(boData.code)
                                        return@map clearOrder.copy(
                                            codes = clearOrder.codes.also {
                                                it.add(boData.code)
                                            }
                                        )
                                    }
                                }
                            }
                            return@map clearOrder
                        }
                        .toMutableList()
                }
            }

            if (tempToBeClearedPromoCodes.isEmpty()) {
                // If there are no promo to be removed, try removing pre-selected codes
                currentItems.forEach { item ->
                    if (item is PromoItem && item.isPreSelected) {
                        if (item.isBebasOngkir) {
                            item.boAdditionalData.forEach { boData ->
                                tempClearPromoOrders = tempClearPromoOrders
                                    .map { clearOrder ->
                                        if (clearOrder.uniqueId == boData.uniqueId &&
                                            !clearOrder.codes.contains(boData.code)
                                        ) {
                                            tempToBeClearedPromoCodes.add(boData.code)
                                            return@map clearOrder.copy(
                                                codes = clearOrder.codes.also { it.add(boData.code) }
                                            )
                                        }
                                        return@map clearOrder
                                    }
                                    .toMutableList()
                            }
                        } else if (item.shopId > 0) {
                            tempClearPromoOrders = tempClearPromoOrders
                                .map { clearOrder ->
                                    if (!clearOrder.codes.contains(item.code)) {
                                        tempToBeClearedPromoCodes.add(item.code)
                                        return@map clearOrder.copy(
                                            codes = clearOrder.codes.also { it.add(item.code) }
                                        )
                                    }
                                    return@map clearOrder
                                }
                                .toMutableList()
                        } else if (!tempGlobalPromoCodes.contains(item.code)) {
                            tempGlobalPromoCodes.add(item.code)
                            tempToBeClearedPromoCodes.add(item.code)
                        }
                    }
                }
            }

            if (tempToBeClearedPromoCodes.isEmpty()) {
                // if there are no promo to be removed, try removing attempted codes
                currentItems.forEach { item ->
                    if (item is PromoItem && !item.isBebasOngkir && item.isAttempted) {
                        if (item.shopId > 0) {
                            tempClearPromoOrders = tempClearPromoOrders.map { clearOrder ->
                                if (clearOrder.uniqueId == item.uniqueId) {
                                    return@map clearOrder.copy(
                                        codes = clearOrder.codes.also {
                                            it.add(item.code)
                                        }
                                    )
                                }
                                return@map clearOrder
                            }.toMutableList()
                        } else {
                            tempGlobalPromoCodes.add(item.code)
                        }
                    }
                }
            }

            PromoUsageIdlingResource.increment()
            launchCatchError(
                context = dispatchers.io,
                block = {
                    PromoUsageIdlingResource.decrement()
                    val param = ClearCacheAutoApplyStackParam.create(
                        clearPromoRequest.copy(
                            serviceId = PromoUsageClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                            isOcc = validateUsePromoRequest.cartType == CheckoutConstant.PARAM_OCC_MULTI,
                            orderData = ClearPromoOrderData(
                                codes = tempGlobalPromoCodes,
                                orders = tempClearPromoOrders
                            )
                        )
                    )
                    val response = clearCacheAutoApplyStackUseCase(param)
                    val clearPromo = clearCacheAutoApplyStackMapper
                        .mapClearCacheAutoApplyResponse(response)

                    var updatedLastValidateUseRequest = validateUsePromoRequest.copy(
                        codes = mutableListOf(),
                        orders = validateUsePromoRequest.orders.map { it.copy(codes = mutableListOf()) }
                    )
                    if (clearPromo.successDataModel.success) {
                        tempToBeClearedPromoCodes.forEach { code ->
                            if (validateUsePromoRequest.codes.contains(code)) {
                                updatedLastValidateUseRequest = updatedLastValidateUseRequest
                                    .copy(
                                        codes = updatedLastValidateUseRequest.codes.also {
                                            it.remove(code)
                                        }
                                    )
                            }
                            updatedLastValidateUseRequest.orders.map { order ->
                                if (order.codes.contains(code)) {
                                    return@map order
                                        .copy(codes = order.codes.also { it.remove(code) })
                                }
                                return@map order.copy()
                            }
                        }
                    }

                    val clearedPromos = currentItems.filterIsInstance<PromoItem>()
                        .filter {
                            tempToBeClearedPromoCodes.contains(it.code) ||
                                tempToBeClearedPromoCodes.contains(it.secondaryPromo.code)
                        }
                    onSuccess?.invoke(
                        entryPoint,
                        clearPromo,
                        updatedLastValidateUseRequest,
                        clearedPromos
                    )
                },
                onError = { e ->
                    Timber.e(e)
                    PromoUsageIdlingResource.decrement()
                    val throwable = PromoErrorException()
                    onFailed?.invoke(throwable)
                }
            )
        }
    }

    fun onUsePromoRecommendation() {
        launchCatchError(
            context = dispatchers.immediate,
            block = {
                _promoPageUiState.ifSuccess { pageState ->
                    if (pageState.isCalculating) return@ifSuccess
                    val updatedState = pageState.copy(
                        isCalculating = true
                    )
                    _promoPageUiState.postValue(updatedState)

                    var updatedItems = updatedState.items
                    val recommendedPromoCodes =
                        updatedItems.getRecommendationItem()?.codes ?: emptyList()
                    updatedItems = updatedItems.map { item ->
                        when (item) {
                            is PromoItem -> {
                                if (item.state !is PromoItemState.Ineligible) {
                                    return@map item.copy(
                                        state = PromoItemState.Normal(useSecondaryPromo = false),
                                        currentClashingPromoCodes = emptyList(),
                                        currentClashingSecondaryPromoCodes = emptyList()
                                    )
                                } else {
                                    return@map item
                                }
                            }

                            is PromoRecommendationItem -> {
                                return@map item.copy(
                                    selectedCodes = recommendedPromoCodes,
                                    showAnimation = true
                                )
                            }

                            else -> {
                                return@map item
                            }
                        }
                    }
                    recommendedPromoCodes.forEach { code ->
                        var recommendedPromo = updatedItems
                            .firstOrNull { item -> item is PromoItem && item.code == code } as? PromoItem
                        if (recommendedPromo != null) {
                            recommendedPromo =
                                recommendedPromo.copy(state = PromoItemState.Selected(useSecondaryPromo = false))
                            val (_, newUpdatedItems) =
                                calculateClickPromo(recommendedPromo, updatedItems, false)
                            updatedItems = newUpdatedItems
                        }
                    }
                    val updatedSavingInfo = calculatePromoSavingInfo(
                        items = updatedItems,
                        previousSavingInfo = updatedState.savingInfo
                    )
                    updatedItems = sortPromo(updatedItems)
                    updatedItems = addTncPromo(updatedItems)
                    _promoPageUiState.postValue(
                        updatedState.copy(
                            items = updatedItems,
                            savingInfo = updatedSavingInfo,
                            isReload = false,
                            isCalculating = false
                        )
                    )
                    val promoRecommendation = updatedItems.getRecommendationItem()
                    if (promoRecommendation != null) {
                        _usePromoRecommendationUiAction.postValue(
                            UsePromoRecommendationUiAction.Success(
                                promoRecommendation = promoRecommendation,
                                items = updatedItems,
                                isClickUseRecommendation = true
                            )
                        )
                    }
                }
            },
            onError = {
                _usePromoRecommendationUiAction.postValue(UsePromoRecommendationUiAction.Failed)
            }
        )
    }

    fun onRecommendationAnimationEnd() {
        _promoPageUiState.ifSuccess { pageState ->
            val updatedItems = pageState.items.map { item ->
                if (item is PromoRecommendationItem) {
                    return@map item.copy(
                        showAnimation = false
                    )
                } else {
                    return@map item
                }
            }

            _promoPageUiState.postValue(
                pageState.copy(items = updatedItems)
            )
        }
    }

    fun onClickBackToCheckout(
        entryPoint: PromoPageEntryPoint,
        validateUsePromoRequest: ValidateUsePromoRequest,
        boPromoCodes: List<String>,
        isCartCheckoutRevamp: Boolean
    ) {
        _promoPageUiState.ifSuccess { pageState ->
            if (isChangedFromInitialState()) {
                val hasSelectedPromo = pageState.items.getSelectedPromoCodes().isNotEmpty()
                val hasSelectedBoPromo = boPromoCodes.isNotEmpty()
                if (hasSelectedPromo || hasSelectedBoPromo) {
                    onApplyPromo(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes,
                        isCartCheckoutRevamp = isCartCheckoutRevamp,
                        onSuccess = { entryPoint, validateUse, lastValidateUseRequest, appliedPromos ->
                            _applyPromoUiAction.postValue(
                                ApplyPromoUiAction.SuccessWithApplyPromo(
                                    entryPoint = entryPoint,
                                    validateUse = validateUse,
                                    lastValidateUsePromoRequest = lastValidateUseRequest,
                                    appliedPromos = appliedPromos
                                )
                            )
                        },
                        onFailed = { throwable ->
                            _applyPromoUiAction.postValue(
                                ApplyPromoUiAction.Failed(
                                    throwable,
                                    false
                                )
                            )
                        }
                    )
                } else {
                    onClearPromo(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes,
                        onSuccess = { entryPoint, clearPromo, lastValidateUseRequest, clearedPromos ->
                            _clearPromoUiAction.postValue(
                                ClearPromoUiAction.Success(
                                    entryPoint = entryPoint,
                                    clearPromo = clearPromo,
                                    lastValidateUseRequest = lastValidateUseRequest,
                                    clearedPromos = clearedPromos
                                )
                            )
                        },
                        onFailed = { throwable ->
                            _clearPromoUiAction.postValue(ClearPromoUiAction.Failed(throwable))
                        }
                    )
                }
            } else {
                _applyPromoUiAction.postValue(ApplyPromoUiAction.SuccessNoAction)
            }
        }
    }

    private fun calculatePromoSavingInfo(
        items: List<DelegateAdapterItem>,
        previousSavingInfo: PromoSavingInfo? = null
    ): PromoSavingInfo {
        val recommendedPromoCodes = items.getRecommendationItem()?.codes ?: listOf()
        val selectedPromoCodes = items.getSelectedPromoCodes()
        val totalSelectedPromoBenefit = items.sumSelectedPromoBenefit()

        val hasOtherNonRecommendedCodes = selectedPromoCodes
            .subtract(recommendedPromoCodes.toSet())
            .isNotEmpty()
        return PromoSavingInfo(
            selectedPromoCount = selectedPromoCodes.size,
            totalSelectedPromoBenefitAmount = totalSelectedPromoBenefit,
            message = previousSavingInfo?.message ?: "",
            isVisible = totalSelectedPromoBenefit > 0 && hasOtherNonRecommendedCodes
        )
    }

    fun onAttemptPromoCode(
        promoRequest: PromoRequest? = null,
        chosenAddress: ChosenAddress? = null,
        attemptedPromoCode: String,
        onSuccess: ((List<DelegateAdapterItem>) -> Unit)? = null
    ) {
        loadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress,
            attemptedPromoCode = attemptedPromoCode,
            onSuccess = onSuccess
        )
    }

    fun onClosePromoPage(
        entryPoint: PromoPageEntryPoint,
        validateUsePromoRequest: ValidateUsePromoRequest,
        boPromoCodes: List<String>,
        isCartCheckoutRevamp: Boolean
    ) {
        _promoPageUiState.ifSuccess { pageState ->
            if (isChangedFromInitialState()) {
                val appliedPromos = pageState.items.getSelectedPromos()
                val hasSelectedPromo = appliedPromos.isNotEmpty()
                val hasSelectedBoPromo = boPromoCodes.isNotEmpty()
                if (hasSelectedPromo || hasSelectedBoPromo) {
                    onApplyPromo(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes,
                        isCartCheckoutRevamp = isCartCheckoutRevamp,
                        onSuccess = { entryPoint, validateUse, lastValidateUseRequest, appliedPromos ->
                            _closePromoPageUiAction.postValue(
                                ClosePromoPageUiAction.SuccessWithApplyPromo(
                                    entryPoint = entryPoint,
                                    validateUse = validateUse,
                                    lastValidateUsePromoRequest = lastValidateUseRequest,
                                    appliedPromos = appliedPromos
                                )
                            )
                        },
                        onFailed = { throwable ->
                            _closePromoPageUiAction.postValue(
                                ClosePromoPageUiAction.Failed(throwable)
                            )
                        }
                    )
                } else {
                    onClearPromo(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes,
                        onSuccess = { entryPoint, clearPromo, lastValidateUseRequest, clearedPromos ->
                            _closePromoPageUiAction.postValue(
                                ClosePromoPageUiAction.SuccessWithClearPromo(
                                    entryPoint = entryPoint,
                                    clearPromo = clearPromo,
                                    lastValidateUsePromoRequest = lastValidateUseRequest,
                                    clearedPromos = clearedPromos
                                )
                            )
                        },
                        onFailed = { throwable ->
                            _closePromoPageUiAction.postValue(
                                ClosePromoPageUiAction.Failed(throwable)
                            )
                        }
                    )
                }
            } else {
                _closePromoPageUiAction.postValue(ClosePromoPageUiAction.SuccessNoAction)
            }
        }
    }

    fun onClickTnc() {
        _promoPageUiState.ifSuccess { pageState ->
            _clickTncUiAction.postValue(
                ClickTncUiAction.Success(
                    selectedPromos = pageState.items.getSelectedPromos()
                )
            )
        }
    }

    private fun isChangedFromInitialState(): Boolean {
        val latestSelectedCodes = _promoPageUiState.asSuccessOrNull()
            ?.items?.getSelectedPromoCodes() ?: emptyList()
        val initialSelectedCodes = initialSelectedPromoCodes ?: emptyList()

        if (initialSelectedCodes.count() != latestSelectedCodes.count()) {
            return true
        }
        if (!initialSelectedCodes.containsAll(latestSelectedCodes)) {
            return true
        }
        return false
    }
}

internal fun LiveData<PromoPageUiState>.ifSuccess(invoke: (PromoPageUiState.Success) -> Unit) {
    if (this.value is PromoPageUiState.Success) {
        invoke(this.value as PromoPageUiState.Success)
    }
}

internal suspend fun LiveData<PromoPageUiState>.ifSuccessSuspend(invoke: suspend (PromoPageUiState.Success) -> Unit) {
    if (this.value is PromoPageUiState.Success) {
        invoke(this.value as PromoPageUiState.Success)
    }
}

internal fun LiveData<PromoPageUiState>?.asSuccessOrNull(): PromoPageUiState.Success? {
    return this?.value as? PromoPageUiState.Success
}

internal fun List<DelegateAdapterItem>.getRecommendationItem(): PromoRecommendationItem? {
    return filterIsInstance<PromoRecommendationItem>().firstOrNull()
}

internal fun List<DelegateAdapterItem>.getTncItem(): PromoTncItem? {
    return filterIsInstance<PromoTncItem>().firstOrNull()
}

internal fun List<DelegateAdapterItem>.getAttemptItem(): PromoAttemptItem? {
    return filterIsInstance<PromoAttemptItem>().firstOrNull()
}

internal fun List<DelegateAdapterItem>.getAttemptedPromos(): List<PromoItem> {
    return filterIsInstance<PromoItem>().filter { it.isAttempted }
}

internal fun List<DelegateAdapterItem>.getSelectedPromos(): List<PromoItem> {
    return filterIsInstance<PromoItem>()
        .filter { it.state is PromoItemState.Selected }
}

internal fun List<DelegateAdapterItem>.getSelectedPromoCodes(): List<String> {
    return filterIsInstance<PromoItem>()
        .filter { it.state is PromoItemState.Selected }
        .map {
            if (it.useSecondaryPromo) {
                it.secondaryPromo.code
            } else {
                it.code
            }
        }
}

internal fun List<DelegateAdapterItem>.sumSelectedPromoBenefit(): Double {
    return filterIsInstance<PromoItem>()
        .filter { it.state is PromoItemState.Selected }
        .sumOf {
            if (it.useSecondaryPromo) {
                it.secondaryPromo.benefitAmount
            } else {
                it.benefitAmount
            }
        }
}

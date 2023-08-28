package com.tokopedia.promousage.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promousage.data.request.GetPromoListRecommendationParam
import com.tokopedia.promousage.data.request.ValidateUsePromoUsageParam
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.domain.entity.BoAdditionalData
import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.domain.usecase.PromoUsageClearCacheAutoApplyStackUseCase
import com.tokopedia.promousage.domain.usecase.PromoUsageGetPromoListRecommendationUseCase
import com.tokopedia.promousage.domain.usecase.PromoUsageValidateUseUseCase
import com.tokopedia.promousage.util.analytics.PromoUsageAnalytics
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
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class PromoUsageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getPromoListRecommendationUseCase: PromoUsageGetPromoListRecommendationUseCase,
    private val validateUseUseCase: PromoUsageValidateUseUseCase,
    private val clearCacheAutoApplyStackUseCase: PromoUsageClearCacheAutoApplyStackUseCase,
    private val getPromoListRecommendationMapper: PromoUsageGetPromoListRecommendationMapper,
    private val validateUseMapper: PromoUsageValidateUseMapper,
    private val clearCacheAutoApplyStackMapper: PromoUsageClearCacheAutoApplyStackMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    private val promoUsageAnalytics: PromoUsageAnalytics
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val PROMO_STATE_RED = "red"

        private const val VALIDATE_USE_STATUS_SUCCESS = "OK"
        private const val VALIDATE_USE_CODE_SUCCESS = "200"
    }

    private var entryPoint: PromoPageEntryPoint = PromoPageEntryPoint.CART_PAGE
    private var defaultErrorMessage: String = ""

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

    fun setEntryPoint(entryPoint: PromoPageEntryPoint) {
        this.entryPoint = entryPoint
    }

    fun setDefaultErrorMessage(errorMessage: String) {
        this.defaultErrorMessage = errorMessage
    }

    fun loadPromoList(
        promoRequest: PromoRequest? = null,
        chosenAddress: ChosenAddress? = null,
        attemptedPromoCode: String = "",
        onSuccess: (() -> Unit)? = null
    ) {
        _promoPageUiState.postValue(PromoPageUiState.Initial)

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
//                delay(1_000)
//                handleLoadPromoListSuccess(GetPromoListRecommendationResponse())
                PromoUsageIdlingResource.decrement()
                val response = getPromoListRecommendationUseCase(param)
                if (response.promoListRecommendation.data.resultStatus.message == PromoListRecommendation.STATUS_OK) {
                    handleLoadPromoListSuccess(response)
                    onSuccess?.invoke()
                } else {
                    PromoUsageLogger.logOnErrorLoadPromoUsagePage(
                        PromoErrorException(message = "response status error")
                    )
                    val exception = PromoErrorException()
                    handleLoadPromoListFailed(exception)
                }
            },
            onError = { throwable ->
                PromoUsageIdlingResource.decrement()
                handleLoadPromoListFailed(throwable)

                val hasAttemptedPromo = attemptedPromoCode.isNotBlank()
                if (hasAttemptedPromo) {
                    promoUsageAnalytics.onErrorAttemptPromo(
                        attemptedPromoCode = attemptedPromoCode,
                        errorMessage = throwable.message ?: ""
                    )
                }
            }
        )
    }

    private fun handleLoadPromoListSuccess(response: GetPromoListRecommendationResponse) {
        val tickerInfo = getPromoListRecommendationMapper
            .mapPromoListRecommendationResponseToPageTickerInfo(response)
        val items = getPromoListRecommendationMapper
            .mapPromoListRecommendationResponseToPromoSections(response)
        var savingInfo = calculatePromoSavingInfo(items)
        savingInfo = savingInfo.copy(
            message = getPromoListRecommendationMapper
                .mapPromoListRecommendationResponseToSavingInfo(response)
                .message
        )
        val attemptedPromoCodeError = getPromoListRecommendationMapper
            .mapPromoListRecommendationResponseToAttemptedPromoCodeError(response)

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
        _promoPageUiState.postValue(
            PromoPageUiState.Success(
                tickerInfo = tickerInfo,
                items = items,
                savingInfo = savingInfo
            )
        )
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
        val codes = ArrayList<String>()
        items.forEach { item ->
            val promoCode = if (item.useSecondaryPromo) {
                item.secondaryPromo.code
            } else {
                item.code
            }
            if (item.state is PromoItemState.Selected) {
                if (item.shopId.isZero() && promoRequest.codes.contains(promoCode)) {
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
            attemptedPromoCode = attemptedPromoCode,
        )
    }

    fun loadPromoListWithPreSelectedPromo(
        promoRequest: PromoRequest? = null,
        chosenAddress: ChosenAddress? = null,
        attemptedPromoCode: String = "",
        preSelectPromoCode: String = ""
    ) {
        _promoPageUiState.postValue(PromoPageUiState.Initial)
        loadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress,
            attemptedPromoCode = attemptedPromoCode
        ) {
            if (preSelectPromoCode.isNotBlank()) {
                val pageState = _promoPageUiState.value
                if (pageState is PromoPageUiState.Success) {
                    val preSelectedPromo = pageState.items.getPromoByCode(preSelectPromoCode)
                    if (preSelectedPromo != null) {
                        onClickPromo(preSelectedPromo)
                    }
                }
            }
        }
    }

    fun onClickPromo(clickedItem: PromoItem) {
        val isGoPayLaterCicilPromo =
            clickedItem.couponType.contains(PromoItem.COUPON_TYPE_GOPAY_LATER_CICIL)
        val isRegisterGoPayLaterCicilPromo =
            clickedItem.cta.type == PromoCta.TYPE_REGISTER_GOPAY_LATER_CICIL
        if (isGoPayLaterCicilPromo && isRegisterGoPayLaterCicilPromo) {
            _promoCtaUiAction.postValue(PromoCtaUiAction.RegisterGoPayLaterCicil(clickedItem.cta))
        } else {
            _promoPageUiState.ifSuccess { pageState ->
                val currentItems = pageState.items
                val newClickedItemState = if (clickedItem.state is PromoItemState.Normal) {
                    PromoItemState.Selected
                } else {
                    PromoItemState.Normal
                }
                var newClickedItem = clickedItem.copy(
                    state = newClickedItemState
                )
                var updatedItems = currentItems
                    .map { item ->
                        if (item is PromoItem && item.id == clickedItem.id) {
                            return@map newClickedItem
                        } else if (item is PromoRecommendationItem) {
                            if (newClickedItem.state is PromoItemState.Normal
                                && newClickedItem.isRecommended
                            ) {
                                return@map item.copy(
                                    selectedCodes = item.selectedCodes
                                        .filterNot { it == newClickedItem.code }
                                )
                            }
                            return@map item
                        } else {
                            return@map item
                        }
                    }
                    .toMutableList()
                // Calculate clash
                val result = calculateClash(newClickedItem, updatedItems, false)
                updatedItems = result.first.toMutableList()
                newClickedItem = newClickedItem.copy(
                    isCausingOtherPromoClash = result.second
                )
                processAndSendEventClickPromo(newClickedItem)
                // Update TnC section
                val tncItem = updatedItems.getTncItem() ?: PromoTncItem()
                val selectedPromoCodes = updatedItems.getSelectedPromoCodes()
                updatedItems.removeFirst { it is PromoTncItem }
                if (selectedPromoCodes.isNotEmpty()) {
                    updatedItems.add(tncItem.copy(selectedPromoCodes = selectedPromoCodes))
                }
                // Update SavingInfo section
                val updatedSavingInfo = calculatePromoSavingInfo(
                    items = updatedItems,
                    previousSavingInfo = pageState.savingInfo
                )
                _promoPageUiState.postValue(
                    pageState.copy(
                        items = updatedItems,
                        savingInfo = updatedSavingInfo
                    )
                )
            }


            // Show loading for MVC section
//            launchCatchError(
//                context = dispatchers.immediate,
//                block = {
//                    _promoPageUiState.ifSuccess { pageState ->
//                        val currentItems = pageState.items
//                        val updatedItems = currentItems.map { item ->
//                            if (item is PromoItem && item.code != clickedItem.code
//                                && item.shopId > 0 && item.state !is PromoItemState.Disabled
//                            ) {
//                                return@map item.copy(state = PromoItemState.Loading)
//                            } else {
//                                return@map item
//                            }
//                        }
//                        _promoPageUiState.postValue(pageState.copy(items = updatedItems))
//                    }
//                },
//                onError = {
//                    // no-op
//                }
//            )
        }
    }

    private fun calculateClash(
        selectedItem: PromoItem,
        updatedItems: List<DelegateAdapterItem>,
        isUseRecommendedPromo: Boolean
    ) : Pair<List<DelegateAdapterItem>, Boolean> {
        var isSelectedPromoCausingClash = false
        var processedItems = updatedItems
        if (selectedItem.state is PromoItemState.Selected) {
            processedItems = updatedItems.map { item ->
                if (item is PromoItem && item.code != selectedItem.code) {
                    val result = checkAndSetClashOnSelectionEvent(item, selectedItem)
                    if (!isSelectedPromoCausingClash) {
                        isSelectedPromoCausingClash = result.second
                    }
                    return@map result.first
                } else {
                    return@map item
                }
            }
        } else if (selectedItem.state is PromoItemState.Normal) {
            processedItems = updatedItems.map { item ->
                if (item is PromoItem && item.code != selectedItem.code) {
                    val result = checkAndSetClashOnDeselectionEvent(
                        item, selectedItem,
                        isUseRecommendedPromo
                    )
                    return@map result.first
                } else {
                    return@map item
                }
            }
        }
        return Pair(processedItems, isSelectedPromoCausingClash)
    }

    private fun checkAndSetClashOnSelectionEvent(
        currentItem: PromoItem,
        selectedItem: PromoItem
    ): Pair<PromoItem, Boolean> {
        var resultItem = currentItem.copy(state = PromoItemState.Normal)
        var isCausingClash = false
        val selectedPromoCode = if (selectedItem.useSecondaryPromo) {
            selectedItem.secondaryPromo.code
        } else {
            selectedItem.code
        }
        val primaryClashingInfo = currentItem.clashingInfos
            .firstOrNull { it.code == selectedPromoCode }
        if (primaryClashingInfo != null
            && !currentItem.currentClashingPromoCodes.contains(selectedPromoCode)
        ) {
            val currentClashingCodes = resultItem.currentClashingPromoCodes
                .toMutableList()
            currentClashingCodes.add(selectedPromoCode)
            resultItem = resultItem.copy(
                currentClashingPromoCodes = currentClashingCodes,
                state = PromoItemState.Ineligible(primaryClashingInfo.message)
            )
            isCausingClash = true
        }
        if (currentItem.secondaryPromo.code.isNotBlank()) {
            val secondaryClashingInfo = currentItem.secondaryPromo.clashingInfos
                .firstOrNull { it.code == selectedPromoCode }
            if (secondaryClashingInfo != null
                && !currentItem.currentClashingSecondaryPromoCodes.contains(selectedPromoCode)) {
                val currentClashingCodes = resultItem.currentClashingSecondaryPromoCodes
                    .toMutableList()
                currentClashingCodes.add(selectedPromoCode)
                resultItem = resultItem.copy(
                    currentClashingSecondaryPromoCodes = currentClashingCodes,
                    state = PromoItemState.Ineligible(secondaryClashingInfo.message)
                )
                isCausingClash = true
            }
        }
        return Pair(resultItem, isCausingClash)
    }

    private fun checkAndSetClashOnDeselectionEvent(
        currentItem: PromoItem,
        selectedItem: PromoItem,
        isApplyRecommendedPromo: Boolean
    ): Pair<PromoItem, Boolean> {
        val selectedPromoCode = if (selectedItem.useSecondaryPromo) {
            selectedItem.secondaryPromo.code
        } else {
            selectedItem.code
        }
        val primaryClashingInfo = currentItem.clashingInfos
            .firstOrNull { it.code == selectedPromoCode }
        return Pair(currentItem, false)
    }

    fun onClickAccordionHeader(clickedItem: PromoAccordionHeaderItem) {
        _promoPageUiState.ifSuccess { pageState ->
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
                pageState.copy(items = updatedItems)
            )
        }
    }

    fun onClickViewAllAccordion(clickedItem: PromoAccordionViewAllItem) {
        _promoPageUiState.ifSuccess { pageState ->
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
                pageState.copy(items = updatedItems)
            )
        }
    }

    fun onClickBuy(
        entryPoint: PromoPageEntryPoint,
        validateUsePromoRequest: ValidateUsePromoRequest,
        boPromoCodes: List<String>
    ) {
        _promoPageUiState.ifSuccess { pageState ->
            val hasSelectedPromo = pageState.items.getSelectedPromoCodes().isNotEmpty()
            if (hasSelectedPromo) {
                onApplyPromo(entryPoint, validateUsePromoRequest, boPromoCodes)
            } else {
                onClearPromo(entryPoint, validateUsePromoRequest, boPromoCodes)
            }
        }
    }

    fun onApplyPromo(
        entryPoint: PromoPageEntryPoint,
        validateUsePromoRequest: ValidateUsePromoRequest,
        boPromoCodes: List<String>
    ) {
        _promoPageUiState.ifSuccess { pageState ->
            val currentItems = pageState.items.filterIsInstance<PromoItem>()

            val selectedPromoCodes = currentItems.getSelectedPromoCodes()
            var newValidateUseRequest = updateCurrentPromoCodeToValidateUsePromoRequest(
                items = currentItems,
                validateUsePromoRequest = validateUsePromoRequest
            )
            newValidateUseRequest = removeInvalidPromoCodeFromValidateUsePromoRequest(
                validateUsePromoRequest = newValidateUseRequest,
                selectedPromoCodes = selectedPromoCodes,
                boPromoCodes = boPromoCodes
            )
            newValidateUseRequest = newValidateUseRequest.copy(skipApply = 0)

            val param = ValidateUsePromoUsageParam.create(newValidateUseRequest)

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
                        selectedPromoCodes = selectedPromoCodes
                    )
                },
                onError = { throwable ->
                    PromoUsageIdlingResource.decrement()
                    handleValidateUseFailed(throwable, selectedPromoCodes)
                }
            )
        }
    }

    private fun handleValidateUseSuccess(
        entryPoint: PromoPageEntryPoint,
        validateUse: ValidateUsePromoRevampUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest,
        selectedPromoCodes: List<String>
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
                        entryPoint,
                        selectedPromoCodes,
                        validateUse,
                        lastValidateUsePromoRequest
                    )
                } else {
                    handleApplyPromoFailed(selectedPromoCodes, validateUse)
                }
            }
        } else {
            // Response is not OK, need to show error message
            val exception = PromoErrorException(validateUse.message.joinToString(". "))
            PromoUsageLogger.logOnErrorApplyPromo(exception)
            _applyPromoUiAction.postValue(ApplyPromoUiAction.Failed(exception, false))
            processAndSendEventViewErrorAfterClickPakaiPromo(exception, selectedPromoCodes)
        }
    }

    private fun handleValidateUseFailed(
        throwable: Throwable,
        selectedPromoCodes: List<String>
    ) {
        PromoUsageLogger.logOnErrorApplyPromo(throwable)
        _applyPromoUiAction.postValue(
            ApplyPromoUiAction.Failed(throwable, false)
        )
        processAndSendEventViewErrorAfterClickPakaiPromo(throwable, selectedPromoCodes)
    }

    private fun handleApplyPromoSuccess(
        entryPoint: PromoPageEntryPoint,
        selectedPromoCodes: List<String>,
        validateUse: ValidateUsePromoRevampUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest
    ) {
        val isGlobalSuccess = validateUse.promoUiModel.messageUiModel.state != PROMO_STATE_RED
        if (isGlobalSuccess) {
            processAndSendEventCLickPakaiPromoSuccessAnalytics(selectedPromoCodes)
            _applyPromoUiAction.postValue(
                ApplyPromoUiAction.Success(
                    entryPoint = entryPoint,
                    validateUse = validateUse,
                    lastValidateUsePromoRequest = lastValidateUsePromoRequest
                )
            )
        }
    }

    private fun handleApplyPromoFailed(
        selectedPromoCodes: List<String>,
        validateUse: ValidateUsePromoRevampUiModel
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
                    if (!voucherOrder.success && voucherOrder.code.isNotBlank()
                        && !redStateMap.containsKey(voucherOrder.code)
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
                            state = PromoItemState.Disabled(errorMessage)
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
                        savingInfo = updatedSavingInfo
                    )
                )

                val exception =
                    PromoErrorException(response.additionalInfoUiModel.errorDetailUiModel.message)
                PromoUsageLogger.logOnErrorApplyPromo(exception)

                _applyPromoUiAction.postValue(
                    ApplyPromoUiAction.Failed(
                        throwable = exception,
                        shouldReload = false
                    )
                )
                processAndSendEventViewErrorAfterClickPakaiPromo(exception, selectedPromoCodes)
            }
        } else {
            PromoUsageLogger.logOnErrorApplyPromo(
                PromoErrorException("response is ok but got empty applied promo")
            )
            val exception =
                PromoErrorException(response.additionalInfoUiModel.errorDetailUiModel.message)
            _applyPromoUiAction.postValue(ApplyPromoUiAction.Failed(exception, false))
            processAndSendEventViewErrorAfterClickPakaiPromo(exception, selectedPromoCodes)
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
        val codes = ArrayList<String>()
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
                if (shopId.isZero() && validateUsePromoRequest.codes.contains(promoCode)) {
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
                if (item.state is PromoItemState.Selected && !item.hasClashingPromo) {
                    // If promo is selected, add promo code to request param
                    // If unique_id == 0, means it's a global promo, else it's promo merchant
                    if (uniqueId == order.uniqueId && !order.codes.contains(promoCode)) {
                        val updatedCodes = order.codes
                        updatedCodes.add(promoCode)
                        newOrder = order.copy(codes = updatedCodes)
                    } else if (item.isBebasOngkir) {
                        // If coupon is bebas ongkir promo, then set shipping id and sp id
                        val boData = boAdditionalData.firstOrNull {
                            order.cartStringGroup == it.cartStringGroup
                        }
                        if (boData != null) {
                            if (!order.codes.contains(boData.code)) {
                                // if code is not already in request param, then add bo additional data
                                val updatedCodes = order.codes
                                updatedCodes.add(boData.code)
                                newOrder = order.copy(
                                    shippingId = boData.shippingId.toInt(),
                                    spId = boData.spId.toInt(),
                                    codes = updatedCodes
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
                    if (uniqueId == order.uniqueId && order.codes.contains(promoCode)) {
                        val updatedCodes = order.codes
                        updatedCodes.remove(promoCode)
                        newOrder = order.copy(codes = updatedCodes)
                    } else if (item.isBebasOngkir) {
                        // if promo is bebas ongkir promo, then remove code only
                        val boData = item.boAdditionalData.firstOrNull {
                            order.uniqueId == it.uniqueId
                        }
                        if (boData != null) {
                            if (order.codes.contains(boData.code)) {
                                val updatedCodes = order.codes
                                updatedCodes.remove(promoCode)
                                newOrder = order.copy(
                                    codes = updatedCodes,
                                    benefitClass = "",
                                    boCampaignId = 0,
                                    shippingPrice = 0.0,
                                    shippingSubsidy = 0,
                                    etaText = "",
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
            newOrder
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
                .filter { invalidPromoCodes.contains(it) }
                .toMutableList(),
            orders = validateUsePromoRequest.orders
                .map { order ->
                    order.copy(
                        codes = order.codes
                            .filter { invalidPromoCodes.contains(it) }
                            .toMutableList()
                    )
                }
        )
    }

    fun onClearPromo(
        entryPoint: PromoPageEntryPoint,
        validateUsePromoRequest: ValidateUsePromoRequest,
        boPromoCodes: List<String>,
        clearPromoRequest: ClearPromoRequest = ClearPromoRequest()
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
                    if (!boPromoCodes.contains(orderCode) && !newClearPromoOrder.codes.contains(
                            orderCode
                        )
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
                                    if (clearOrder.codes.contains(boData.code)
                                        && boPromoCodes.contains(boData.code)
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
                                        if (clearOrder.uniqueId == boData.uniqueId
                                            && !clearOrder.codes.contains(boData.code)
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
                    val param = clearPromoRequest.copy(
                        serviceId = PromoUsageClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                        isOcc = validateUsePromoRequest.cartType == CheckoutConstant.PARAM_OCC_MULTI,
                        orderData = ClearPromoOrderData(
                            codes = tempGlobalPromoCodes,
                            orders = tempClearPromoOrders
                        )
                    )
                    val response = clearCacheAutoApplyStackUseCase(param)
                    val clearPromo = clearCacheAutoApplyStackMapper
                        .mapClearCacheAutoApplyResponse(response)

                    var updatedLastValidateUseRequest = validateUsePromoRequest.copy()
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

                    _clearPromoUiAction.postValue(
                        ClearPromoUiAction.Success(
                            entryPoint = entryPoint,
                            clearPromo = clearPromo,
                            lastValidateUseRequest = updatedLastValidateUseRequest
                        )
                    )
                },
                onError = {
                    PromoUsageIdlingResource.decrement()
                    val throwable = PromoErrorException()
                    _clearPromoUiAction.postValue(ClearPromoUiAction.Failed(throwable))
                }
            )
        }
    }

    fun onUsePromoRecommendation() {
        launchCatchError(
            context = dispatchers.io,
            block = {
                delay(1_000L)
                _promoPageUiState.ifSuccess { pageState ->
                    val currentItems = pageState.items
                    val recommendedPromoCodes = currentItems
                        .filterIsInstance<PromoItem>()
                        .filter { it.isRecommended }
                        .map { it.code }
                    var updatedItems = currentItems.map { item ->
                        if (item is PromoItem) {
                            if (item.isRecommended) {
                                item.copy(state = PromoItemState.Selected)
                            } else if (item.state == PromoItemState.Selected) {
                                item.copy(state = PromoItemState.Normal)
                            } else {
                                item
                            }
                        } else if (item is PromoRecommendationItem) {
                            item.copy(selectedCodes = recommendedPromoCodes)
                        } else {
                            item
                        }
                    }
                    val updatedSavingInfo = calculatePromoSavingInfo(
                        items = updatedItems,
                        previousSavingInfo = pageState.savingInfo
                    )
                    _promoPageUiState.postValue(
                        pageState.copy(
                            items = updatedItems,
                            savingInfo = updatedSavingInfo
                        )
                    )
                    val promoRecommendation = updatedItems.getRecommendationItem()
                    if (promoRecommendation != null) {
                        _usePromoRecommendationUiAction.postValue(
                            UsePromoRecommendationUiAction.Success(promoRecommendation)
                        )
                    }
                }
            },
            onError = {
                _usePromoRecommendationUiAction.postValue(UsePromoRecommendationUiAction.Failed)
            }
        )
    }

    fun onBackToCheckout(
        entryPoint: PromoPageEntryPoint,
        validateUsePromoRequest: ValidateUsePromoRequest,
        boPromoCodes: List<String>
    ) {
        onApplyPromo(entryPoint, validateUsePromoRequest, boPromoCodes)
    }

    private fun calculatePromoSavingInfo(
        items: List<DelegateAdapterItem>,
        previousSavingInfo: PromoSavingInfo? = null
    ): PromoSavingInfo {
        val selectedPromoCount = items.getSelectedPromoCodes().size
        val totalSelectedPromoBenefit = items.sumSelectedPromoBenefit()
        return PromoSavingInfo(
            selectedPromoCount = selectedPromoCount,
            totalSelectedPromoBenefitAmount = totalSelectedPromoBenefit,
            message = previousSavingInfo?.message ?: ""
        )
    }

    fun onAttemptPromoCode(
        promoRequest: PromoRequest? = null,
        chosenAddress: ChosenAddress? = null,
        attemptedPromoCode: String,
        onSuccess: (() -> Unit)? = null
    ) {
        loadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress,
            attemptedPromoCode = attemptedPromoCode,
            onSuccess = onSuccess
        )
    }

    fun isChangedFromInitialState(): Boolean {
        // Check if
        // Case 1: has any promo item unchecked, but exist as pre applied promo item
        // Case 2: has any promo item checked but have not been applied
        val currentItems = _promoPageUiState.asSuccessOrNull()
            ?.items?.filterIsInstance<PromoItem>()
        if (currentItems != null) {
            val initialPreSelectedPromoCodes =
                currentItems.filter { it.isPreSelected }.map { it.code }
            if (initialPreSelectedPromoCodes.isNotEmpty()) {
                currentItems.forEach { item ->
                    val code = if (item.useSecondaryPromo) {
                        item.secondaryPromo.code
                    } else {
                        item.code
                    }
                    // Case 1
                    if (initialPreSelectedPromoCodes.contains(code)
                        && item.state !is PromoItemState.Selected
                    ) {
                        return true
                    }
                    // Case 2
                    if (!initialPreSelectedPromoCodes.contains(code)
                        && item.state is PromoItemState.Selected
                    ) {
                        return true
                    }
                }
            }
        }
        return false
    }

    // region Analytics

    private fun getPageSourceForAnalytics(entryPoint: PromoPageEntryPoint): Int {
        return when (entryPoint) {
            PromoPageEntryPoint.CART_PAGE -> PromoUsageAnalytics.SOURCE_CART_PAGE
            PromoPageEntryPoint.CHECKOUT_PAGE -> PromoUsageAnalytics.SOURCE_CHECKOUT_PAGE
            PromoPageEntryPoint.OCC_PAGE -> PromoUsageAnalytics.SOURCE_OCC_PAGE
        }
    }

    private fun processAndSendEventCLickPakaiPromoSuccessAnalytics(
        selectedPromoCodes: List<String>
    ) {
        val promoRecommendation =
            _promoPageUiState.asSuccessOrNull()?.items?.getRecommendationItem()
        val promoRecommendationCount = promoRecommendation?.codes?.size ?: 0
        val selectedRecommendationCount = promoRecommendation
            ?.codes?.count { selectedPromoCodes.contains(it) } ?: 0
        val status = if (selectedPromoCodes.size == selectedRecommendationCount
            && selectedRecommendationCount == promoRecommendationCount
        ) {
            "1"
        } else {
            "0"
        }
        promoUsageAnalytics.eventClickPakaiPromoSuccess(
            pageSource = getPageSourceForAnalytics(entryPoint),
            status = status,
            selectedPromoCodes = selectedPromoCodes
        )
    }

    private fun processAndSendEventViewErrorAfterClickPakaiPromo(
        throwable: Throwable,
        selectedPromoCodes: List<String>
    ) {
        val errorMessage = throwable.message.ifNullOrBlank { defaultErrorMessage }
        _promoPageUiState.ifSuccess { pageState ->
            pageState.items.forEach { item ->
                if (item is PromoItem && selectedPromoCodes.containsPromoCode(item)) {
                    promoUsageAnalytics.eventViewErrorAfterClickPakaiPromo(
                        pageSource = getPageSourceForAnalytics(entryPoint),
                        promoId = item.id,
                        errorMessage = errorMessage
                    )
                }
            }
        }
    }

    private fun processAndSendEventClickPromo(clickedItem: PromoItem) {
        _promoPageUiState.ifSuccess { _ ->
            val clickedPromoCode = if (clickedItem.useSecondaryPromo) {
                clickedItem.secondaryPromo.code
            } else {
                clickedItem.code
            }
            if (clickedItem.state is PromoItemState.Selected) {
                promoUsageAnalytics.eventClickSelectKupon(
                    getPageSourceForAnalytics(entryPoint),
                    clickedPromoCode,
                    clickedItem.isCausingOtherPromoClash
                )
                if (clickedItem.isAttempted) {
                    promoUsageAnalytics.eventClickSelectPromo(
                        getPageSourceForAnalytics(entryPoint),
                        clickedItem.code
                    )
                }
            } else {
                promoUsageAnalytics.eventClickDeselectKupon(
                    getPageSourceForAnalytics(entryPoint),
                    clickedPromoCode,
                    clickedItem.isCausingOtherPromoClash
                )
                if (clickedItem.isAttempted) {
                    promoUsageAnalytics.eventClickDeselectPromo(
                        getPageSourceForAnalytics(entryPoint),
                        clickedItem.code
                    )
                }
            }
        }
    }
}

internal fun LiveData<PromoPageUiState>.ifSuccess(invoke: (PromoPageUiState.Success) -> Unit) {
    if (this.value is PromoPageUiState.Success) {
        invoke(this.value as PromoPageUiState.Success)
    }
}

internal fun LiveData<PromoPageUiState>?.asSuccessOrNull(): PromoPageUiState.Success? {
    return this?.value as? PromoPageUiState.Success
}

internal fun List<DelegateAdapterItem>.getPromoByCode(promoCode: String): PromoItem? {
    return asSequence()
        .filterIsInstance<PromoItem>()
        .firstOrNull { it.code == promoCode || it.secondaryPromo.code == promoCode }
}

internal fun List<DelegateAdapterItem>.getRecommendationItem(): PromoRecommendationItem? {
    return filterIsInstance<PromoRecommendationItem>().firstOrNull()
}

internal fun List<DelegateAdapterItem>.getTncItem(): PromoTncItem? {
    return filterIsInstance<PromoTncItem>().firstOrNull()
}

internal fun List<DelegateAdapterItem>.getSelectedPromoCodes(): List<String> {
    return filterIsInstance<PromoItem>()
        .filter { it.state is PromoItemState.Selected }
        .map { it.code }
}

internal fun List<DelegateAdapterItem>.sumSelectedPromoBenefit(): Double {
    return filterIsInstance<PromoItem>()
        .filter { it.state is PromoItemState.Selected }
        .sumOf { it.benefitDetail.amountIdr }
}

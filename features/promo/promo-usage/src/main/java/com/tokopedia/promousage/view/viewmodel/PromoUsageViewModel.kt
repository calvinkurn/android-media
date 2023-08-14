package com.tokopedia.promousage.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promousage.data.request.GetCouponListRecommendationParam
import com.tokopedia.promousage.data.request.ValidateUsePromoUsageParam
import com.tokopedia.promousage.data.response.GetCouponListRecommendationResponse
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
import com.tokopedia.promousage.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promousage.domain.usecase.GetCouponListRecommendationUseCase
import com.tokopedia.promousage.domain.usecase.ValidateUsePromoUsageUseCase
import com.tokopedia.promousage.util.analytics.PromoUsageAnalytics
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.util.logger.PromoErrorException
import com.tokopedia.promousage.util.logger.PromoUsageLogger
import com.tokopedia.promousage.util.test.PromoUsageIdlingResource
import com.tokopedia.promousage.view.mapper.PromoUsageClearCacheAutoApplyStackMapper
import com.tokopedia.promousage.view.mapper.PromoUsageGetCouponListRecommendationMapper
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
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    private val getCouponListRecommendationUseCase: GetCouponListRecommendationUseCase,
    private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
    private val validateUsePromoUsageUseCase: ValidateUsePromoUsageUseCase,
    private val getCouponListRecommendationMapper: PromoUsageGetCouponListRecommendationMapper,
    private val validateUseMapper: PromoUsageValidateUseMapper,
    private val clearCacheAutoApplyStackMapper: PromoUsageClearCacheAutoApplyStackMapper,
    private val promoUsageAnalytics: PromoUsageAnalytics
) : BaseViewModel(dispatchers.main) {

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
        val param = GetCouponListRecommendationParam.create(
            promoRequest = newPromoRequest,
            chosenAddress = chosenAddress ?: chosenAddressRequestHelper.getChosenAddress()
        )
        PromoUsageIdlingResource.increment()
        launchCatchError(
            context = dispatchers.io,
            block = {
                PromoUsageIdlingResource.decrement()
//                val response = getCouponListRecommendationUseCase(param)
//                if (response.couponListRecommendation.status == CouponListRecommendation.STATUS_OK) {
                // TODO: Remove artificial delay
                delay(1_000)
                handleLoadPromoListSuccess(GetCouponListRecommendationResponse())
//                    handleLoadPromoListSuccess(response)
                onSuccess?.invoke()
//                } else {
//                    PromoUsageLogger.logOnErrorLoadPromoUsagePage(
//                        PromoErrorException(message = "response status error")
//                    )
//                    val exception = PromoErrorException()
//                    handleLoadPromoListFailed(exception)
//                }
//                val items = if (attemptedPromoCode.isNotBlank()) {
//                    getCouponListRecommendationMapper
//                        .mapCouponListRecommendationResponseToPromoSectionsWithAttemptedCode()
//                } else {
//                    getCouponListRecommendationMapper
//                        .mapCouponListRecommendationResponseToPromoSections(response)
//                }
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

    private fun handleLoadPromoListSuccess(response: GetCouponListRecommendationResponse) {
        val tickerInfo = getCouponListRecommendationMapper
            .mapCouponListRecommendationResponseToPageTickerInfo(response)
        val items = getCouponListRecommendationMapper
            .mapCouponListRecommendationResponseToPromoSections(response)
        val savingInfo = calculatePromoSavingInfo(items)
        val attemptedPromoCodeError = getCouponListRecommendationMapper
            .mapCouponListRecommendationResponseToAttemptedPromoCodeError(response)

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
            val pageState = _promoPageUiState.value
            if (pageState is PromoPageUiState.Success) {
                val currentItems = pageState.items
                val updatedItems = currentItems
                    .map { item ->
                        if (item is PromoItem && item.id == clickedItem.id) {
                            val newState = if (clickedItem.state is PromoItemState.Normal) {
                                PromoItemState.Selected
                            } else {
                                PromoItemState.Normal
                            }
                            item.copy(state = newState)
                        } else {
                            item
                        }
                    }
                    .toMutableList()
                // Update TnC section
                val tncItem = updatedItems.getTncItem() ?: PromoTncItem()
                val selectedPromoCodes = updatedItems.getSelectedPromoCodes()
                updatedItems.removeFirst { it is PromoTncItem }
                if (selectedPromoCodes.isNotEmpty()) {
                    updatedItems.add(tncItem.copy(selectedPromoCodes = selectedPromoCodes))
                }
                // Update SavingInfo section
                val updatedSavingInfo = calculatePromoSavingInfo(updatedItems)
                _promoPageUiState.postValue(
                    pageState.copy(
                        items = updatedItems,
                        savingInfo = updatedSavingInfo
                    )
                )
            }
        }
    }

    fun onClickAccordionHeader(clickedItem: PromoAccordionHeaderItem) {
        _promoPageUiState.ifSuccess { pageState ->
            val currentItems = pageState.items
            val updatedItems = currentItems
                .map { item ->
                    val isExpanded = !clickedItem.isExpanded
                    if (item is PromoAccordionHeaderItem && item.id == clickedItem.id) {
                        item.copy(isExpanded = isExpanded)
                    } else if (item is PromoItem && item.headerId == clickedItem.id) {
                        item.copy(isExpanded = isExpanded)
                    } else if (item is PromoAccordionViewAllItem && item.headerId == clickedItem.id) {
                        item.copy(isExpanded = isExpanded)
                    } else {
                        item
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
                        item.copy(isVisible = true)
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
        if (entryPoint == PromoPageEntryPoint.CART_PAGE) {
            onApplyPromo(validateUsePromoRequest, boPromoCodes)
        } else if (entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE) {
            onApplyPromo(validateUsePromoRequest, boPromoCodes)
        }
    }

    fun onApplyPromo(
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
                    val response = validateUsePromoUsageUseCase(param)
                    val validateUse = validateUseMapper.mapToValidateUseResponse(response)

                    handleValidateUseSuccess(validateUse)
                },
                onError = { throwable ->
                    PromoUsageIdlingResource.decrement()
                    handleValidateUseFailed(throwable)
                }
            )
        }
    }

    private fun handleValidateUseSuccess(
        validateUse: ValidateUsePromoRevampUiModel
    ) {
        _applyPromoUiAction.postValue(
            ApplyPromoUiAction.Success(validateUse)
        )
    }

    private fun handleValidateUseFailed(throwable: Throwable) {
        PromoUsageLogger.logOnErrorApplyPromo(throwable)
        _applyPromoUiAction.postValue(
            ApplyPromoUiAction.Failed(
                throwable = throwable,
                shouldReload = false
            )
        )
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
                        serviceId = ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
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
                    val updatedItems = currentItems.map { item ->
                        if (item is PromoItem) {
                            if (item.isRecommended) {
                                item.copy(state = PromoItemState.Selected)
                            } else {
                                item.copy(state = PromoItemState.Normal)
                            }
                        } else {
                            item
                        }
                    }
                    val updatedSavingInfo = calculatePromoSavingInfo(updatedItems)
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
        onSuccess: (() -> Unit)?
    ) {
        onSuccess?.invoke()
    }

    private fun calculatePromoSavingInfo(items: List<DelegateAdapterItem>): PromoSavingInfo {
        val selectedPromoCount = items.getSelectedPromoCodes().size
        val totalSelectedPromoBenefit = items.sumSelectedPromoBenefit()
        return PromoSavingInfo(
            selectedPromoCount = selectedPromoCount,
            totalSelectedPromoBenefitAmount = totalSelectedPromoBenefit
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
}

internal fun LiveData<PromoPageUiState>.ifSuccess(invoke: (PromoPageUiState.Success) -> Unit) {
    if (this.value is PromoPageUiState.Success) {
        invoke(this.value as PromoPageUiState.Success)
    }
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

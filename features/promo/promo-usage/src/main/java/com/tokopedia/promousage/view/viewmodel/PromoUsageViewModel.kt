package com.tokopedia.promousage.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promousage.data.request.GetCouponListRecommendationParam
import com.tokopedia.promousage.data.response.GetCouponListRecommendationResponse
import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.PromoPageState
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.domain.usecase.GetCouponListRecommendationUseCase
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.view.mapper.PromoUsageMapper
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class PromoUsageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    private val getCouponListRecommendationUseCase: GetCouponListRecommendationUseCase,
    private val promoUsageMapper: PromoUsageMapper
) : BaseViewModel(dispatchers.main) {

    private val _promoPageState = MutableLiveData<PromoPageState>(PromoPageState.Initial)
    val promoPageState: LiveData<PromoPageState>
        get() = _promoPageState

    private val _promoRecommendation = MutableLiveData<PromoRecommendationItem?>()
    val promoRecommendation: LiveData<PromoRecommendationItem?>
        get() = _promoRecommendation

    private val _promoCtaAction = MutableLiveData<PromoCta>()
    val promoCtaAction: LiveData<PromoCta>
        get() = _promoCtaAction

    fun loadPromoList(
        promoRequest: PromoRequest? = null,
        chosenAddress: ChosenAddress? = null,
        attemptedPromoCode: String = "",
        onSuccess: (() -> Unit)? = null
    ) {
        val currentState = _promoPageState.value
        if (currentState is PromoPageState.Success) {
            _promoPageState.postValue(PromoPageState.Loading)
        } else {
            _promoPageState.postValue(PromoPageState.Initial)
        }

        // Reset pre-selected promo param
        var newPromoRequest = promoRequest?.copy() ?: PromoRequest()
        newPromoRequest = clearPromoRequest(newPromoRequest)
        // Add current attempted code to promo param if exist
        newPromoRequest = updateAttemptedCodeToPromoRequest(attemptedPromoCode, newPromoRequest)
        val pageState = _promoPageState.value
        if (pageState is PromoPageState.Success) {
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
        launchCatchError(
            context = dispatchers.io,
            block = {
                // TODO: Remove artificial delay
                delay(1_000)

                // TODO: Uncomment after BE ready
                //val response = getCouponListRecommendationUseCase(param)
                val response = GetCouponListRecommendationResponse()
                val tickerInfo =
                    promoUsageMapper.mapCouponListRecommendationResponseToPageTickerInfo(response)
                // TODO: Update after BE ready
                val items = if (attemptedPromoCode.isNotBlank()) {
                    promoUsageMapper.mapCouponListRecommendationResponseToPromoSectionsWithAttemptedCode()
                } else {
                    promoUsageMapper.mapCouponListRecommendationResponseToPromoSections(response)
                }
                val updatedSavingInfo = calculatePromoSavingInfo(items)

                _promoRecommendation.postValue(items.getRecommendationItem())
                _promoPageState.postValue(
                    PromoPageState.Success(
                        tickerInfo = tickerInfo,
                        items = items,
                        savingInfo = updatedSavingInfo
                    )
                )
                onSuccess?.invoke()
            },
            onError = { throwable ->
                Timber.e(throwable)
                _promoPageState.postValue(PromoPageState.Error)
            }
        )
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

    fun loadPromoListWithPreSelectedPromo(
        promoRequest: PromoRequest? = null,
        chosenAddress: ChosenAddress? = null,
        attemptedPromoCode: String = "",
        preSelectPromoCode: String = ""
    ) {
        _promoPageState.postValue(PromoPageState.Initial)
        loadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress,
            attemptedPromoCode = attemptedPromoCode,
            onSuccess = {
                if (preSelectPromoCode.isNotBlank()) {
                    val pageState = _promoPageState.value
                    if (pageState is PromoPageState.Success) {
                        val preSelectedPromo = pageState.items.getPromoByCode(preSelectPromoCode)
                        if (preSelectedPromo != null) {
                            onClickPromo(preSelectedPromo)
                        }
                    }
                }
            }
        )
    }

    fun onClickPromo(clickedItem: PromoItem) {
        val isGoPayLaterCicilPromo =
            clickedItem.couponType.contains(PromoItem.COUPON_TYPE_GOPAY_LATER_CICIL)
        val isRegisterGoPayLaterCicilPromo =
            clickedItem.cta.type == PromoCta.TYPE_REGISTER_GOPAY_LATER_CICIL
        if (isGoPayLaterCicilPromo && isRegisterGoPayLaterCicilPromo) {
            _promoCtaAction.postValue(clickedItem.cta)
        } else {
            val pageState = _promoPageState.value
            if (pageState is PromoPageState.Success) {
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
                _promoPageState.postValue(
                    pageState.copy(
                        items = updatedItems,
                        savingInfo = updatedSavingInfo
                    )
                )
            }
        }
    }

    fun onClickAccordionHeader(clickedItem: PromoAccordionHeaderItem) {
        val pageState = _promoPageState.value
        if (pageState is PromoPageState.Success) {
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
            _promoPageState.postValue(
                pageState.copy(items = updatedItems)
            )
        }
    }

    fun onClickViewAllAccordion(clickedItem: PromoAccordionViewAllItem) {
        val pageState = _promoPageState.value
        if (pageState is PromoPageState.Success) {
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
            _promoPageState.postValue(
                pageState.copy(items = updatedItems)
            )
        }
    }

    fun onClickBuy(
        entryPoint: PromoPageEntryPoint,
        onSuccess: (() -> Unit)?
    ) {
        _promoPageState.postValue(PromoPageState.Loading)
        if (entryPoint == PromoPageEntryPoint.CART_PAGE) {
            // TODO: Handle callback to Cart page
            onSuccess?.invoke()
        } else if (entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE) {
            // TODO: Handle callback to Checkout page
            onSuccess?.invoke()
        }
    }

    fun onClickApplyPromoRecommendation(
        onSuccess: (() -> Unit)?
    ) {
        _promoPageState.postValue(PromoPageState.Loading)
        launchCatchError(
            context = dispatchers.io,
            block = {
                delay(1_000L)
                unselectAllSelectedPromos()

                onSuccess?.invoke()
            },
            onError = {

            }
        )
    }

    fun onClickBackToCheckout(
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

    private fun unselectAllSelectedPromos() {
        val pageState = _promoPageState.value
        if (pageState is PromoPageState.Success) {
            val currentItems = pageState.items
            val updatedItems = currentItems.map { item ->
                if (item is PromoItem) {
                    item.copy(state = PromoItemState.Normal)
                } else {
                    item
                }
            }
            val updatedSavingInfo = calculatePromoSavingInfo(updatedItems)
            _promoPageState.postValue(
                pageState.copy(
                    items = updatedItems,
                    savingInfo = updatedSavingInfo
                )
            )
        }
    }

    private fun unselectAllSelectedPromoInSection(headerId: String) {
        val pageState = _promoPageState.value
        if (pageState is PromoPageState.Success) {
            val currentItems = pageState.items
            val updatedItems = currentItems.map { item ->
                if (item is PromoItem && item.headerId == headerId) {
                    item.copy(state = PromoItemState.Normal)
                } else {
                    item
                }
            }
            val updatedSavingInfo = calculatePromoSavingInfo(updatedItems)
            _promoPageState.postValue(
                pageState.copy(
                    items = updatedItems,
                    savingInfo = updatedSavingInfo
                )
            )
        }
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

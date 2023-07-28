package com.tokopedia.promousage.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promousage.data.request.GetCouponListRecommendationParam
import com.tokopedia.promousage.data.response.GetCouponListRecommendationResponse
import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoItemCardDetail
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.PromoPageState
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
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

    private val _promoTncAction = MutableLiveData<PromoTncItem>()
    val promoTncAction: LiveData<PromoTncItem>
        get() = _promoTncAction

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
        var newPromoRequest = promoRequest ?: PromoRequest()
        newPromoRequest = newPromoRequest.copy(
            attemptedCodes = arrayListOf(),
            codes = arrayListOf(),
            orders = newPromoRequest.orders.map { it.copy(codes = arrayListOf()) }
        )
        // Add current attempted code to promo param
        if (attemptedPromoCode.isNotBlank()) {
            newPromoRequest = newPromoRequest.copy(
                attemptedCodes = arrayListOf(attemptedPromoCode),
                skipApply = 0
            )
        } else {
            newPromoRequest = newPromoRequest.copy(
                skipApply = 1
            )
        }
        // Add current selected promo code to promo param
        // TODO: Add current selected promo code
        // Remove duplicate attempted promo code from
        if (attemptedPromoCode.isNotBlank()) {
            val filteredPromoRequestCodes =
                newPromoRequest.codes.filter { it != attemptedPromoCode }
            val filteredPromoRequestOrders = newPromoRequest.orders.map { order ->
                order.copy(codes = ArrayList(order.codes.filter { it != attemptedPromoCode }))
            }
            newPromoRequest = newPromoRequest.copy(
                codes = ArrayList(filteredPromoRequestCodes),
                orders = ArrayList(filteredPromoRequestOrders)
            )
        }

        // Generate CouponListRecommendation param
        val param = GetCouponListRecommendationParam.create(
            promoRequest = newPromoRequest,
            chosenAddress = chosenAddress ?: chosenAddressRequestHelper.getChosenAddress()
        )
        launchCatchError(
            context = dispatchers.io,
            block = {
                _promoPageState.postValue(PromoPageState.Loading)
                // TODO: Remove artificial delay
                delay(2_000)

                //val response = getCouponListRecommendationUseCase(param)
                val response = GetCouponListRecommendationResponse()
                val tickerInfo =
                    promoUsageMapper.mapCouponListRecommendationResponseToPageTickerInfo(response)
                val items =
                    promoUsageMapper.mapCouponListRecommendationResponseToPromoSections(response)
                val updatedSavingInfo = calculatePromoSavingInfo(items)

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
        val pageState = _promoPageState.value
        if (pageState is PromoPageState.Success) {
            val currentItems = pageState.items
            val updatedItems = currentItems
                .map { item ->
                    if (item is PromoItem && item.id == clickedItem.id) {
                        val newState = if (clickedItem.state is PromoItemState.Normal) {
                            val cardDetail =
                                clickedItem.cardDetails.first { it.state == PromoItemCardDetail.TYPE_SELECTED }
                            PromoItemState.Selected(cardDetail)
                        } else {
                            val cardDetail =
                                clickedItem.cardDetails.first { it.state == PromoItemCardDetail.TYPE_INITIAL }
                            PromoItemState.Normal(cardDetail)
                        }
                        item.copy(state = newState)
                    } else {
                        item
                    }
                }
                .toMutableList()
            val tncItem = updatedItems.getTncItem() ?: PromoTncItem()
            val selectedPromoCodes = updatedItems.getSelectedPromoCodes()
            updatedItems.removeFirst { it is PromoTncItem }
            if (selectedPromoCodes.isNotEmpty()) {
                updatedItems.add(tncItem.copy(selectedPromoCodes = selectedPromoCodes))
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

    fun onClickTnc() {
        val pageState = _promoPageState.value
        if (pageState is PromoPageState.Success) {
            val currentItems = pageState.items
            val tncItem = currentItems.getTncItem()
            tncItem?.let {
                val selectedPromoCodes = currentItems.getSelectedPromoCodes()
                _promoTncAction.postValue(it.copy(selectedPromoCodes = selectedPromoCodes))
            }
        }
    }

    fun onClickBuyButton(entryPoint: PromoPageEntryPoint) {
        if (entryPoint == PromoPageEntryPoint.CART_PAGE) {
            // TODO: Handle callback to Cart page
        } else if (entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE) {
            // TODO: Handle callback to Checkout page
        }
    }

    fun onClickBackToCheckoutButton() {

    }

    private fun calculatePromoSavingInfo(items: List<DelegateAdapterItem>): PromoSavingInfo {
        val selectedPromoCount = items.getSelectedPromoCodes().size
        val totalSelectedPromoBenefit = items.sumSelectedPromoBenefit()
        return PromoSavingInfo(
            selectedPromoCount = selectedPromoCount,
            totalSelectedPromoBenefitAmount = totalSelectedPromoBenefit
        )
    }

    private fun List<DelegateAdapterItem>.getPromoByCode(promoCode: String): PromoItem? {
        return asSequence()
            .filterIsInstance<PromoItem>()
            .firstOrNull { it.code == promoCode || it.secondaryPromo.code == promoCode }
    }

    private fun List<DelegateAdapterItem>.getTncItem(): PromoTncItem? {
        return filterIsInstance<PromoTncItem>().firstOrNull()
    }

    private fun List<DelegateAdapterItem>.getSelectedPromoCodes(): List<String> {
        return filterIsInstance<PromoItem>()
            .filter { it.state is PromoItemState.Selected }
            .map { it.code }
    }

    private fun List<DelegateAdapterItem>.sumSelectedPromoBenefit(): Double {
        return filterIsInstance<PromoItem>()
            .filter { it.state is PromoItemState.Selected }
            .sumOf { it.benefitDetail.amountIdr }
    }
}

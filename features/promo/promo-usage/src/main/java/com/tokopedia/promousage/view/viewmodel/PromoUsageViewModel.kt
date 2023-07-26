package com.tokopedia.promousage.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promousage.data.request.GetCouponListRecommendationParam
import com.tokopedia.promousage.data.response.GetCouponListRecommendationResponse
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.PromoItem
import com.tokopedia.promousage.domain.entity.PromoPageState
import com.tokopedia.promousage.domain.entity.list.PromoAccordionItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.usecase.GetCouponListRecommendationUseCase
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.view.mapper.PromoUsageMapper
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import kotlinx.coroutines.delay
import javax.inject.Inject

class PromoUsageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    private val getCouponListRecommendationUseCase: GetCouponListRecommendationUseCase,
    private val promoUsageMapper: PromoUsageMapper
) : BaseViewModel(dispatchers.main) {

    private val _promoPageState = MutableLiveData<PromoPageState>()
    val promoPageState: LiveData<PromoPageState>
        get() = _promoPageState

    fun getPromoList(
        promoRequest: PromoRequest? = null,
        chosenAddress: ChosenAddress? = null,
        attemptedPromoCode: String = ""
    ) {
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
                // TODO: Remove artificial delay
                delay(2_000)

                //val response = getCouponListRecommendationUseCase(param)
                val response = GetCouponListRecommendationResponse()
                val tickerInfo =
                    promoUsageMapper.mapCouponListRecommendationResponseToPagetickerInfo(response)
                val sections =
                    promoUsageMapper.mapCouponListRecommendationResponseToPromoSections(response)
                _promoPageState.postValue(
                    PromoPageState.Success(
                        tickerInfo = tickerInfo,
                        sections = sections
                    )
                )
            },
            onError = { throwable ->
                _promoPageState.postValue(PromoPageState.Error)
            }
        )
    }

    fun onClickVoucherAccordion(selectedPromoAccordionSection: PromoAccordionItem) {
        val pageState = _promoPageState.value
        if (pageState is PromoPageState.Success) {
            val currentSections = pageState.sections
            val updatedSections = currentSections.map { item ->
                if (item is PromoAccordionItem && item.id == selectedPromoAccordionSection.id) {
                    val isExpanded = selectedPromoAccordionSection.isExpanded
                    selectedPromoAccordionSection.copy(isExpanded = !isExpanded)
                } else {
                    item
                }
            }
            _promoPageState.postValue(
                pageState.copy(sections = updatedSections)
            )
        }
    }

    fun onClickViewAllVoucher(selectedPromoAccordionSection: PromoAccordionItem) {
        val pageState = _promoPageState.value
        if (pageState is PromoPageState.Success) {
            val currentSections = pageState.sections
            val updatedSections = currentSections.map { item ->
                if (item is PromoAccordionItem && item.id == selectedPromoAccordionSection.id) {
                    val expandedVouchers = item.sections.viewAll()
                    item.copy(sections = expandedVouchers)
                } else {
                    item
                }
            }
            _promoPageState.postValue(
                pageState.copy(sections = updatedSections)
            )
        }
    }

    fun onButtonBuyClick(entryPoint: PromoPageEntryPoint) {
        if (entryPoint == PromoPageEntryPoint.CART_PAGE) {
            // TODO: Handle callback to Cart page
        } else if (entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE) {
            // TODO: Handle callback to Checkout page
        }
    }

    fun onButtonBackToShipmentClick() {

    }

    fun onVoucherSelected(selectedPromo: PromoItem) {

    }

    private fun List<DelegateAdapterItem>.viewAll(): List<DelegateAdapterItem> {
        //Inside VoucherAccordion there are 2 items [Voucher, ViewAllVoucher] model
        val updatedVoucherAccordionItems = this.toMutableList()

        //Remove ViewAllVoucher to make view all CTA gone
        updatedVoucherAccordionItems.removeAll { it is PromoAccordionViewAllItem }

        //Change Voucher visible state to true to make all voucher visible
        val expandedVouchers = updatedVoucherAccordionItems.map { item ->
            val promo = item as PromoItem
            promo.copy(isExpanded = true)
        }

        return expandedVouchers
    }
}

fun List<PromoItem>.toCollapsibleList(): List<DelegateAdapterItem> {
    if (isEmpty()) return emptyList()

    val formattedVouchers = mutableListOf<DelegateAdapterItem>()

    val numberOfExpandedVoucher = count { it.isExpanded }

    val lastIndexOfExpandedVoucher = indexOfLast { it.isExpanded }

    val collapsedVoucherCount = size - numberOfExpandedVoucher
    val viewAllWidgetItem = PromoAccordionViewAllItem(promoCount = collapsedVoucherCount)

    formattedVouchers.addAll(this)

    //Modify the list ordering. Placing view all widget item after last index of expanded voucher
    formattedVouchers.add(lastIndexOfExpandedVoucher + 1, viewAllWidgetItem)

    return formattedVouchers
}

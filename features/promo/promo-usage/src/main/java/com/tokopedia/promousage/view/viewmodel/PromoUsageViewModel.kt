package com.tokopedia.promousage.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promousage.data.DummyData
import com.tokopedia.promousage.data.request.GetCouponListRecommendationParam
import com.tokopedia.promousage.domain.entity.EntryPoint
import com.tokopedia.promousage.domain.entity.Promo
import com.tokopedia.promousage.domain.entity.list.PromoAccordionItem
import com.tokopedia.promousage.domain.usecase.GetCouponListRecommendationUseCase
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.view.mapper.PromoUsageMapper
import com.tokopedia.promousage.view.mapper.viewAll
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import javax.inject.Inject

class PromoUsageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    private val getCouponListRecommendationUseCase: GetCouponListRecommendationUseCase,
    private val promoUsageMapper: PromoUsageMapper
) : BaseViewModel(dispatchers.main) {

    private val _items = MutableLiveData<Result<List<DelegateAdapterItem>>>()
    val items: LiveData<Result<List<DelegateAdapterItem>>>
        get() = _items

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
            val filteredPromoRequestCodes = newPromoRequest.codes.filter { it != attemptedPromoCode }
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
                //val promoSections = promoUsageMapper.mapCouponListRecommendationResponseToPromoSections(response)
                val promoSections = DummyData.sections
                _items.postValue(Success(promoSections))
            },
            onError = { throwable ->
                _items.postValue(Fail(throwable))
            }
        )
    }

    fun onClickVoucherAccordion(selectedPromoAccordionSection: PromoAccordionItem) {
        val currentItems = items.value?.currentItemsOrEmpty() ?: return
        val updatedItems = currentItems.map { item ->
            if (item is PromoAccordionItem && item.id == selectedPromoAccordionSection.id) {
                val isExpanded = selectedPromoAccordionSection.isExpanded
                selectedPromoAccordionSection.copy(isExpanded = !isExpanded)
            } else {
                item
            }
        }
        _items.postValue(Success(updatedItems))
    }

    fun onClickViewAllVoucher(selectedPromoAccordionSection: PromoAccordionItem) {
        val currentItems = items.value?.currentItemsOrEmpty() ?: return

        val updatedItems = currentItems.map { item ->
            if (item is PromoAccordionItem && item.id == selectedPromoAccordionSection.id) {
                val expandedVouchers = item.sections.viewAll()
                item.copy(sections = expandedVouchers)
            } else {
                item
            }
        }

        _items.postValue(Success(updatedItems))
    }

    fun onButtonBuyClick(entryPoint: EntryPoint) {
        if (entryPoint == EntryPoint.CART_PAGE) {
            // TODO: Handle callback to Cart page
        } else if (entryPoint == EntryPoint.ONE_CLICK_CHECKOUT_PAGE) {
            // TODO: Handle callback to Checkout page
        }
    }

    fun onButtonBackToShipmentClick() {

    }

    fun onVoucherSelected(selectedPromo: Promo) {

    }

    private fun Result<List<DelegateAdapterItem>>.currentItemsOrEmpty(): List<DelegateAdapterItem> {
        return if (this is Success) {
            this.data
        } else {
            emptyList()
        }
    }
}

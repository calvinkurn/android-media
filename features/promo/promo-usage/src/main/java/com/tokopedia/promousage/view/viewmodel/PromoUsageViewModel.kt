package com.tokopedia.promousage.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.promousage.data.DummyData
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.domain.entity.EntryPoint
import com.tokopedia.promousage.domain.entity.list.ViewAllVoucher
import com.tokopedia.promousage.domain.entity.Promo
import com.tokopedia.promousage.domain.entity.list.VoucherAccordion
import com.tokopedia.promousage.domain.entity.list.TermAndCondition
import com.tokopedia.promousage.domain.entity.list.VoucherCode
import com.tokopedia.promousage.domain.entity.list.VoucherRecommendation
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import javax.inject.Inject

class PromoUsageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    // TODO: Remove dummy data when BE available
    private val cashbackPromos = DummyData.cashbackPromos
    private val freeShippingPromos = DummyData.freeShippingPromos
    private val discountPromos = DummyData.discountPromos
    private val recommendationPromos = DummyData.recommendedPromos

    private val _items = MutableLiveData<Result<List<DelegateAdapterItem>>>()
    val items: LiveData<Result<List<DelegateAdapterItem>>>
        get() = _items

    fun getVouchers() {
        launchCatchError(
            dispatchers.io,
            block = {
                delay(2_000)

                val items = listOf<DelegateAdapterItem>(
                    VoucherRecommendation("Kamu bisa hemat Rp30.000 dari 2 promo!", recommendationPromos),
                    VoucherAccordion(
                        "${cashbackPromos.size} promo buat cashback",
                        true,
                        cashbackPromos.toCollapsibleList()
                    ),
                    VoucherAccordion(
                        "${freeShippingPromos.size} promo buat pengiriman kamu",
                        true,
                        freeShippingPromos.toCollapsibleList()
                    ),
                    VoucherAccordion(
                        "${discountPromos.size} promo buat diskon kamu",
                        true,
                        discountPromos.toCollapsibleList()
                    ),
                    VoucherCode,
                    TermAndCondition
                )
                _items.postValue(Success(items))
            },
            onError = { throwable ->
                _items.postValue(Fail(throwable))
            }
        )
    }

    fun onClickVoucherAccordion(selectedVoucherAccordion: VoucherAccordion) {
        val currentItems = items.value?.currentItemsOrEmpty() ?: return

        val updatedItems = currentItems.map { item ->
            if (item is VoucherAccordion && item.title == selectedVoucherAccordion.title) {
                val isExpanded = selectedVoucherAccordion.isExpanded
                selectedVoucherAccordion.copy(isExpanded = !isExpanded)
            } else {
                item
            }
        }


        _items.postValue(Success(updatedItems))
    }

    fun onClickViewAllVoucher(selectedVoucherAccordion: VoucherAccordion) {
        val currentItems = items.value?.currentItemsOrEmpty() ?: return

        val updatedItems = currentItems.map { item ->
            if (item is VoucherAccordion && item.title == selectedVoucherAccordion.title) {
                val expandedVouchers = item.vouchers.viewAll()
                item.copy(vouchers = expandedVouchers)
            } else {
                item
            }
        }

        _items.postValue(Success(updatedItems))
    }

    fun onButtonBuyClick(entryPoint: EntryPoint) {
        if (entryPoint == EntryPoint.CART_PAGE) {

        } else if (entryPoint == EntryPoint.ONE_CLICK_CHECKOUT_PAGE) {

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

    private fun List<Promo>.toCollapsibleList(): List<DelegateAdapterItem> {
        if (isEmpty()) return emptyList()

        val formattedVouchers = mutableListOf<DelegateAdapterItem>()

        val numberOfExpandedVoucher = count { it.isVisible }

        val lastIndexOfExpandedVoucher = indexOfLast { it.isVisible }

        val collapsedVoucherCount = size - numberOfExpandedVoucher
        val viewAllWidgetItem = ViewAllVoucher(collapsedVoucherCount)

        formattedVouchers.addAll(this)

        //Modify the list ordering. Placing view all widget item after last index of expanded voucher
        formattedVouchers.add(lastIndexOfExpandedVoucher + 1, viewAllWidgetItem)

        return formattedVouchers
    }

    private fun List<DelegateAdapterItem>.viewAll(): List<DelegateAdapterItem> {
        return filterIsInstance<Promo>().map { it.copy(isVisible = true) }
    }
}

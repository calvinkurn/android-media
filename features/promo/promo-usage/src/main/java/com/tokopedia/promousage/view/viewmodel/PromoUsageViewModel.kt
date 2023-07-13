package com.tokopedia.promousage.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.promocheckoutmarketplace.presentation.bottomsheet.DelegateAdapterItem
import com.tokopedia.promocheckoutmarketplace.presentation.bottomsheet.EntryPoint
import com.tokopedia.promocheckoutmarketplace.presentation.bottomsheet.ViewAllVoucher
import com.tokopedia.promocheckoutmarketplace.presentation.bottomsheet.Voucher
import com.tokopedia.promocheckoutmarketplace.presentation.bottomsheet.VoucherSection
import com.tokopedia.promocheckoutmarketplace.presentation.bottomsheet.VoucherSource
import com.tokopedia.promocheckoutmarketplace.presentation.bottomsheet.VoucherState
import com.tokopedia.promocheckoutmarketplace.presentation.bottomsheet.VoucherType
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import javax.inject.Inject

class PromoVoucherListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {



    private val cashbackVouchers = listOf(
        Voucher(
            1,
            100_000,
            "Cashback - Loading",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.CASHBACK,
            VoucherState.Loading,
            VoucherSource.Promo,
            true
        ),

        Voucher(
            2,
            100_000,
            "Cashback - Normal",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.CASHBACK,
            VoucherState.Normal,
            VoucherSource.Promo,
            false
        ),
        Voucher(
            3,
            100_000,
            "Cashback - Selected",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.CASHBACK,
            VoucherState.Selected,
            VoucherSource.Promo,
            false
        ),
        Voucher(
            4,
            100_000,
            "Cashback - Disabled",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.CASHBACK,
            VoucherState.Disabled,
            VoucherSource.Promo,
            false
        ),
        Voucher(
            5,
            100_000,
            "Cashback - Ineligible",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.CASHBACK,
            VoucherState.Ineligible("Belum bisa dipakai barengan promo yang dipilih."),
            VoucherSource.Promo,
            false
        ),
    )

    private val freeShippingVouchers = listOf(
        Voucher(
            6,
            100_000,
            "Free shipping - Loading",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.FREE_SHIPPING,
            VoucherState.Loading,
            VoucherSource.Promo,
            true
        ),
        Voucher(
            7,
            100_000,
            "Free shipping - Normal",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.FREE_SHIPPING,
            VoucherState.Normal,
            VoucherSource.Promo,
            true
        ),
        Voucher(
            8,
            100_000,
            "Free shipping - Selected",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.FREE_SHIPPING,
            VoucherState.Selected,
            VoucherSource.Promo,
            true
        ),
        Voucher(
            9,
            100_000,
            "Free shipping - Disabled",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.FREE_SHIPPING,
            VoucherState.Disabled,
            VoucherSource.Promo,
            true
        ),
        Voucher(
            10,
            100_000,
            "Free shipping - Ineligible",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.FREE_SHIPPING,
            VoucherState.Ineligible("Belum bisa dipakai barengan promo yang dipilih."),
            VoucherSource.Promo,
            true
        ),
    )


    private val discountVouchers = listOf(
        Voucher(
            11,
            100_000,
            "Discount - Loading",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.DISCOUNT,
            VoucherState.Loading,
            VoucherSource.Promo,
            true
        ),
        Voucher(
            12,
            100_000,
            "Discount - Normal",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.DISCOUNT,
            VoucherState.Normal,
            VoucherSource.Promo,
            true
        ),
        Voucher(
            13,
            100_000,
            "Discount - Selected",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.DISCOUNT,
            VoucherState.Selected,
            VoucherSource.Promo,
            true
        ),
        Voucher(
            14,
            100_000,
            "Discount - Disabled",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.DISCOUNT,
            VoucherState.Disabled,
            VoucherSource.Promo,
            true
        ),
        Voucher(
            15,
            100_000,
            "Discount - Ineligible",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.DISCOUNT,
            VoucherState.Ineligible("Belum bisa dipakai barengan promo yang dipilih."),
            VoucherSource.Promo,
            true
        ),
    )

    private val _sections = MutableLiveData<com.tokopedia.usecase.coroutines.Result<List<VoucherSection>>>()
    val sections: LiveData<com.tokopedia.usecase.coroutines.Result<List<VoucherSection>>>
        get() = _sections

    private val _recommendationVouchers = MutableLiveData<com.tokopedia.usecase.coroutines.Result<List<Voucher>>>()
    val recommendationVouchers: LiveData<com.tokopedia.usecase.coroutines.Result<List<Voucher>>>
        get() = _recommendationVouchers

    fun getVouchers() {
        launchCatchError(
            dispatchers.io,
            block = {
                delay(2_000)

                val mockedVouchers = listOf(
                    VoucherSection(
                        "${cashbackVouchers.size} promo buat cashback",
                        true,
                        cashbackVouchers.toCollapsibleList()
                    ),
                    VoucherSection(
                        "${freeShippingVouchers.size} promo buat pengiriman kamu",
                        true,
                        freeShippingVouchers
                    ),
                    VoucherSection(
                        "${discountVouchers.size} promo buat diskon kamu",
                        true,
                        discountVouchers
                    )
                )
                _sections.postValue(Success(mockedVouchers))
                /* val recommendationVouchers = listOf(
                     Voucher(
                         300,
                         100_000,
                         "Cashback - Loading",
                         "2 hari",
                         "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                         "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
                         VoucherType.CASHBACK,
                         VoucherState.Normal,
                         VoucherSource.Promo,
                         true
                     ),
                     Voucher(
                         400,
                         100_000,
                         "Cashback - Normal",
                         "2 hari",
                         "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                         "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
                         VoucherType.CASHBACK,
                         VoucherState.Normal,
                         VoucherSource.Promo,
                         true
                     ),
                 )
                 _recommendationVouchers.value = Success(recommendationVouchers)*/
            },
            onError = { throwable ->
                _sections.postValue(Fail(throwable))
            }
        )
    }

    private fun List<Voucher>.toCollapsibleList(): List<DelegateAdapterItem> {
        if (isEmpty()) return emptyList()

        val formattedVouchers = mutableListOf<DelegateAdapterItem>()

        val numberOfExpandedVoucher = count { it.shouldVisible }

        val lastIndexOfExpandedVoucher = indexOfLast { it.shouldVisible }

        val collapsedVoucherCount = size - numberOfExpandedVoucher
        val viewAllWidgetItem = ViewAllVoucher(collapsedVoucherCount)

        formattedVouchers.addAll(this)

        //Modify the list ordering. Placing view all widget item after last index of expanded voucher
        formattedVouchers.add(lastIndexOfExpandedVoucher + 1, viewAllWidgetItem)

        return formattedVouchers
    }

    fun onClickViewAllVoucher(
        allVoucherSections: List<VoucherSection>,
        selectedVoucherSection: VoucherSection
    ) {
        val sections = mutableListOf<VoucherSection>()

        allVoucherSections.forEach { voucherSection ->
            val updatedVoucherSection = if (voucherSection.title == selectedVoucherSection.title) {
                val vouchersOnly = voucherSection.vouchers.toMutableList()

                //Remove view all voucher widget from the list
                vouchersOnly.removeAll { it is ViewAllVoucher }

                //Make all the voucher within the same section to be visible
                val updatedVouchers = vouchersOnly.map {
                    val voucher = it as Voucher
                    voucher.copy(shouldVisible = true)
                }

                voucherSection.copy(vouchers = updatedVouchers)
            } else {
                voucherSection
            }

            sections.add(updatedVoucherSection)
        }

        _sections.postValue(Success(sections))
    }

    fun onButtonBuyClick(entryPoint: EntryPoint) {
        if (entryPoint == EntryPoint.CART_PAGE) {

        } else if (entryPoint == EntryPoint.ONE_CLICK_CHECKOUT_PAGE) {

        }
    }

    fun onButtonBackToShipmentClick() {

    }

    fun onVoucherSelected(selectedVoucher: Voucher) {

    }

    fun onClickChevron(
        currentVoucherSections: List<VoucherSection>,
        selectedVoucherSection: VoucherSection
    ) {
        val isExpanded = selectedVoucherSection.isExpanded
        if (isExpanded) {
            collapseSection(currentVoucherSections, selectedVoucherSection)
        } else {
            expandSection(currentVoucherSections, selectedVoucherSection)
        }
    }

    private fun expandSection(currentVoucherSections: List<VoucherSection>, voucherSection: VoucherSection) {
        val updatedVoucherSections = currentVoucherSections.map { currentVoucherSection ->
            if (currentVoucherSection.title == voucherSection.title) {
                currentVoucherSection.copy(isExpanded = true)
            } else {
                currentVoucherSection
            }
        }
        _sections.postValue(Success(updatedVoucherSections))
    }

    private fun collapseSection(currentVoucherSections: List<VoucherSection>, voucherSection: VoucherSection) {
        val updatedVoucherSections = currentVoucherSections.map { currentVoucherSection ->
            if (currentVoucherSection.title == voucherSection.title) {
                currentVoucherSection.copy(isExpanded = false)
            } else {
                currentVoucherSection
            }
        }
        _sections.postValue(Success(updatedVoucherSections))
    }

}

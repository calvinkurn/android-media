package com.tokopedia.promousage.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.domain.entity.EntryPoint
import com.tokopedia.promousage.domain.entity.list.ViewAllVoucher
import com.tokopedia.promousage.domain.entity.list.Voucher
import com.tokopedia.promousage.domain.entity.list.VoucherAccordion
import com.tokopedia.promousage.domain.entity.VoucherSource
import com.tokopedia.promousage.domain.entity.VoucherState
import com.tokopedia.promousage.domain.entity.VoucherType
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

    private var selectedVouchersSet = mutableSetOf<Voucher>()
    private val currentItems : List<DelegateAdapterItem>
        get() = _vouchers.value?.currentItemsOrEmpty() ?: emptyList()

    private val _vouchers = MutableLiveData<Result<List<DelegateAdapterItem>>>()
    val vouchers: LiveData<Result<List<DelegateAdapterItem>>>
        get() = _vouchers

    private val _selectedVouchers = MutableLiveData<Set<Voucher>>()
    val selectedVouchers: LiveData<Set<Voucher>>
        get() = _selectedVouchers

    private val _validatedVoucher = MutableLiveData<Result<Voucher>>()
    val validatedVoucher: LiveData<Result<Voucher>>
        get() = _validatedVoucher

    fun getVouchers() {
        launchCatchError(
            dispatchers.io,
            block = {
                delay(500)

                val items = listOf<DelegateAdapterItem>(
                    VoucherRecommendation("Kamu bisa hemat Rp30.000 dari 2 promo!", recommendationVouchers),
                    VoucherAccordion(
                        "${cashbackVouchers.size} promo buat pembayaran tertentu",
                        false,
                        cashbackVouchers.toCollapsibleList()
                    ),
                    VoucherAccordion(
                        "${freeShippingVouchers.size} promo buat pengiriman kamu",
                        false,
                        freeShippingVouchers.toCollapsibleList()
                    ),
                    VoucherAccordion(
                        "${discountVouchers.size} promo lainnya buat kamu",
                        false,
                        discountVouchers.toCollapsibleList()
                    ),
                    VoucherCode(userInputVoucherCode = "", errorMessage = "", voucher = null),
                    TermAndCondition
                )
                _vouchers.postValue(Success(items))
            },
            onError = { throwable ->
                _vouchers.postValue(Fail(throwable))
            }
        )
    }

    fun onClickVoucherAccordion(selectedVoucherAccordion: VoucherAccordion) {
        val updatedItems = currentItems.map { item ->
            if (item is VoucherAccordion && item.title == selectedVoucherAccordion.title) {
                val isExpanded = selectedVoucherAccordion.isExpanded
                selectedVoucherAccordion.copy(isExpanded = !isExpanded)
            } else {
                item
            }
        }


        _vouchers.value = Success(updatedItems)
    }

    fun onClickViewAllVoucher(selectedVoucherAccordion: VoucherAccordion) {
        val updatedItems = currentItems.map { item ->
            if (item is VoucherAccordion && item.title == selectedVoucherAccordion.title) {
                val expandedVouchers = item.vouchers.expandAll()
                item.copy(vouchers = expandedVouchers)
            } else {
                item
            }
        }

        _vouchers.value = Success(updatedItems)
    }

    fun onButtonBuyClick(entryPoint: EntryPoint) {
        if (entryPoint == EntryPoint.CART_PAGE) {

        } else if (entryPoint == EntryPoint.ONE_CLICK_CHECKOUT_PAGE) {

        }
    }

    fun onButtonBackToShipmentClick() {

    }

    fun onVoucherClick(selectedVoucher: Voucher) {
        val isAlreadyInSelection =
            selectedVouchersSet.any { voucher -> voucher.id == selectedVoucher.id }

        if (isAlreadyInSelection) {
            removeFromSelection(selectedVoucher)
        } else {
            addToSelection(selectedVoucher)
        }
    }

    private fun addToSelection(selectedVoucher: Voucher) {
        val isSelectable = selectedVoucher.voucherState is VoucherState.Normal || selectedVoucher.voucherState is VoucherState.Selected

        if (isSelectable) {
            val updatedVouchers = currentItems.map { widget ->
                when (widget) {
                    is VoucherAccordion -> {
                        val selectedVouchers = widget.vouchers.map { item ->
                            if (item is ViewAllVoucher) {
                                item
                            } else {
                                val voucher = item as Voucher
                                if (selectedVoucher.id == voucher.id) {
                                    voucher.copy(voucherState = VoucherState.Selected)
                                } else {
                                    voucher
                                }
                            }
                        }

                        widget.copy(vouchers = selectedVouchers)
                    }

                    is VoucherRecommendation -> {
                        val selectedVouchers = widget.vouchers.map { item ->
                            val voucher = item as Voucher
                            if (selectedVoucher.id == voucher.id) {
                                voucher.copy(voucherState = VoucherState.Selected)
                            } else {
                                voucher
                            }
                        }

                        widget.copy(vouchers = selectedVouchers)
                    }

                    else -> {
                        widget
                    }
                }
            }

            _vouchers.value = Success(updatedVouchers)

            selectedVouchersSet.add(selectedVoucher)
            _selectedVouchers.value = selectedVouchersSet
        }
    }

    private fun removeFromSelection(selectedVoucher: Voucher) {
        val updatedVouchers = currentItems.map { widget ->
            when (widget) {
                is VoucherAccordion -> {
                    val selectedVouchers = widget.vouchers.map { item ->
                        if (item is ViewAllVoucher) {
                            item
                        } else {
                            val voucher = item as Voucher
                            if (selectedVoucher.id == voucher.id) {
                                voucher.copy(voucherState = VoucherState.Selected)
                            } else {
                                voucher
                            }
                        }
                    }

                    widget.copy(vouchers = selectedVouchers)
                }

                is VoucherRecommendation -> {
                    val selectedVouchers = widget.vouchers.map { item ->
                        val voucher = item as Voucher
                        if (selectedVoucher.id == voucher.id) {
                            voucher.copy(voucherState = VoucherState.Normal)
                        } else {
                            voucher
                        }
                    }

                    widget.copy(vouchers = selectedVouchers)
                }

                else -> {
                    widget
                }
            }
        }

        _vouchers.value = Success(updatedVouchers)

        selectedVouchersSet.removeAll { voucher -> voucher.id == selectedVoucher.id }
        _selectedVouchers.value = selectedVouchersSet
    }

    fun onCtaUseVoucherCodeClick(voucherCode: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                //TODO implement gql call to validate voucher code
                val validatedVoucher = Voucher(
                    181,
                    200_000,
                    "From user input voucher code",
                    "2 hari",
                    "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                    "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
                    VoucherType.CASHBACK,
                    VoucherState.Selected,
                    VoucherSource.UserInput(voucherCode),
                    true
                )

                val updatedItems = currentItems.map { item ->
                    if (item is VoucherCode) {
                        item.copy(
                            userInputVoucherCode = voucherCode,
                            errorMessage = "",
                            voucher = validatedVoucher
                        )
                    } else {
                        item
                    }
                }

                val isAlreadyInSelection = selectedVouchersSet.any { voucher -> voucher.id == validatedVoucher.id }
                if (!isAlreadyInSelection) {
                    selectedVouchersSet.add(validatedVoucher)
                }

                _selectedVouchers.postValue(selectedVouchersSet)
                _vouchers.postValue(Success(updatedItems))

                _validatedVoucher.postValue(Success(validatedVoucher))
            },
            onError = { throwable ->
                _validatedVoucher.postValue(Fail(throwable))
            }
        )
    }

    fun onVoucherCodeClearIconClick() {
        val updatedItems = currentItems.map { item ->
            if (item is VoucherCode) {
                item.copy(
                    userInputVoucherCode = "",
                    errorMessage = "",
                    voucher = null
                )
            } else {
                item
            }
        }

        _vouchers.value = Success(updatedItems)
    }

    private fun Result<List<DelegateAdapterItem>>.currentItemsOrEmpty(): List<DelegateAdapterItem> {
        return if (this is Success) {
            this.data
        } else {
            emptyList()
        }
    }

    private fun List<Voucher>.toCollapsibleList(): List<DelegateAdapterItem> {
        if (isEmpty()) return emptyList()

        val formattedVouchers = mutableListOf<DelegateAdapterItem>()

        val numberOfExpandedVoucher = count { it.visible }

        val lastIndexOfExpandedVoucher = indexOfLast { it.visible }

        val collapsedVoucherCount = size - numberOfExpandedVoucher
        val viewAllWidgetItem = ViewAllVoucher(collapsedVoucherCount)

        formattedVouchers.addAll(this)

        //Modify the list ordering. Placing view all widget item after last index of expanded voucher
        formattedVouchers.add(lastIndexOfExpandedVoucher + 1, viewAllWidgetItem)

        return formattedVouchers
    }

    private fun List<DelegateAdapterItem>.expandAll(): List<DelegateAdapterItem> {
        //Inside VoucherAccordion there are 2 items [Voucher, ViewAllVoucher] model
        val updatedVoucherAccordionItems = this.toMutableList()

        //Remove ViewAllVoucher to make view all CTA gone
        updatedVoucherAccordionItems.removeAll { it is ViewAllVoucher }

        //Change Voucher visible state to true to make all voucher visible
        val expandedVouchers = updatedVoucherAccordionItems.map { item ->
            val voucher = item as Voucher
            voucher.copy(visible = true)
        }

        return expandedVouchers
    }


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
        Voucher(
            60,
            100_000,
            "Cashback - Actionable - Gopay Later Cicil",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.CASHBACK,
            VoucherState.Actionable("Aktifkan GoPay Later Cicil untuk pakai promo", "Aktifkan GoPay Later Cicil", "tokopedia://cart"),
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
            false
        ),
        Voucher(
            8,
            100_000,
            "Free shipping - Normal",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.FREE_SHIPPING,
            VoucherState.Normal,
            VoucherSource.Promo,
            false
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
            false
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
            false
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
            "Discount - Normal",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.DISCOUNT,
            VoucherState.Normal,
            VoucherSource.Promo,
            false
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
            false
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
            false
        ),
    )
    private val recommendationVouchers = listOf(
        Voucher(
            30,
            100_000,
            "Recom - Cashback - Normal",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.CASHBACK,
            VoucherState.Normal,
            VoucherSource.Promo,
            true
        ),
        Voucher(
            31,
            100_000,
            "Recom - Discount - Normal",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.DISCOUNT,
            VoucherState.Normal,
            VoucherSource.Promo,
            true
        )
    )
}

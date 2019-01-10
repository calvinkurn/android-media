package com.tokopedia.expresscheckout.view.variant

import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 03/12/18.
 */

interface CheckoutVariantActionListener {

    fun onNeedToNotifySingleItem(position: Int)

    fun onNeedToNotifyAllItem()

    fun onClickEditProfile()

    fun onClickEditDuration()

    fun onClickEditCourier()

    fun onClickInsuranceInfo(insuranceInfo: String)

    fun onBindProductUpdateQuantityViewModel(stockWording: String)

    fun onBindVariantGetProductViewModel(): ProductViewModel?

    fun onBindVariantUpdateProductViewModel()

    fun onChangeVariant(selectedOptionViewModel: OptionVariantViewModel)

    fun onChangeQuantity(quantityViewModel: QuantityViewModel)

    fun onSummaryChanged(summaryViewModel: SummaryViewModel?)

    fun onInsuranceCheckChanged(insuranceViewModel: InsuranceViewModel)
}
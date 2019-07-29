package com.tokopedia.purchase_platform.express_checkout.view.variant

import com.tokopedia.purchase_platform.express_checkout.view.variant.viewmodel.*
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 03/12/18.
 */

interface CheckoutVariantActionListener {

    fun onNeedToNotifySingleItem(position: Int)

    fun onNeedToRemoveSingleItem(position: Int)

    fun onNeedToNotifyAllItem()

    fun onClickEditProfile()

    fun onClickEditDuration()

    fun onClickEditCourier()

    fun onClickInsuranceInfo(insuranceInfo: String)

    fun onBindProductUpdateQuantityViewModel(productViewModel: ProductViewModel, stockWording: String)

    fun onBindVariantGetProductViewModel(): ProductViewModel?

    fun onBindVariantUpdateProductViewModel()

    fun onChangeVariant(selectedOptionViewModel: OptionVariantViewModel)

    fun onChangeQuantity(quantityViewModel: QuantityViewModel)

    fun onChangeNote(noteViewModel: NoteViewModel)

    fun onSummaryChanged(summaryViewModel: SummaryViewModel?)

    fun onInsuranceCheckChanged(insuranceViewModel: InsuranceViewModel)

    fun onNeedToValidateButtonBuyVisibility()

    fun onNeedToRecalculateRatesAfterChangeTemplate()

    fun onNeedToUpdateOnboardingStatus()

    fun onVariantGuidelineClick(variantGuideline: String)

    fun onGetCompositeSubscriber(): CompositeSubscription?
}
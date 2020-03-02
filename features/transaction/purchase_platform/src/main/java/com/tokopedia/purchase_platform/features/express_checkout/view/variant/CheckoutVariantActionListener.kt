package com.tokopedia.purchase_platform.features.express_checkout.view.variant

import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.*
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

    fun onBindProductUpdateQuantityViewModel(productUiModel: ProductUiModel, stockWording: String)

    fun onBindVariantGetProductViewModel(): ProductUiModel?

    fun onBindVariantUpdateProductViewModel()

    fun onChangeVariant(selectedOptionUiModel: OptionVariantUiModel)

    fun onChangeQuantity(quantityUiModel: QuantityUiModel)

    fun onChangeNote(noteUiModel: NoteUiModel)

    fun onSummaryChanged(summaryUiModel: SummaryUiModel?)

    fun onInsuranceCheckChanged(insuranceUiModel: InsuranceUiModel)

    fun onNeedToValidateButtonBuyVisibility()

    fun onNeedToRecalculateRatesAfterChangeTemplate()

    fun onNeedToUpdateOnboardingStatus()

    fun onVariantGuidelineClick(variantGuideline: String)

    fun onInsuranceSelectedStateChanged(element: InsuranceRecommendationUiModel?, isSelected: Boolean)

    fun sendEventInsuranceSelectedStateChanged(isChecked: Boolean, title: String)

    fun onGetCompositeSubscriber(): CompositeSubscription?

    fun sendEventInsuranceInfoClicked()
}
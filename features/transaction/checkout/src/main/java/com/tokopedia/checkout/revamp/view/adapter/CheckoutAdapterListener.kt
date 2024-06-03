package com.tokopedia.checkout.revamp.view.adapter

import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductBenefitModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.checkoutpayment.view.OrderPaymentFee
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.unifycomponents.ImageUnify

interface CheckoutAdapterListener {

    fun onChangeAddress()

    fun onViewNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)

    fun onClickApplyNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)

    fun onClickCancelNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)

    fun getOrderByCartStringGroup(cartStringGroup: String): CheckoutOrderModel?

    fun onClickLihatOnTickerOrderError(
        shopId: String,
        errorMessage: String,
        order: CheckoutOrderModel,
        position: Int
    )

    fun onViewFreeShippingPlusBadge()

    fun onCheckboxAddonProductListener(
        isChecked: Boolean,
        addOnProductDataItemModel: AddOnProductDataItemModel,
        product: CheckoutProductModel,
        bindingAdapterPosition: Int
    )

    fun onClickAddonProductInfoIcon(addOn: AddOnProductDataItemModel)

    fun onClickSeeAllAddOnProductService(product: CheckoutProductModel)

    fun onImpressionAddOnProductService(addonType: Int, productId: String)

    fun onClickAddOnGiftingProductLevel(product: CheckoutProductModel)

    fun onImpressionAddOnGiftingProductLevel(productId: String)

    fun onNoteClicked(product: CheckoutProductModel, bindingAdapterPosition: Int)

    fun onShowLottieNotes(
        buttonChangeNote: ImageUnify,
        buttonChangeNoteLottie: LottieAnimationView,
        bindingAdapterPosition: Int
    )

    fun openAddOnGiftingOrderLevelBottomSheet(order: CheckoutOrderModel)

    fun addOnGiftingOrderLevelImpression(order: CheckoutOrderModel)

    fun onLoadShippingState(order: CheckoutOrderModel, position: Int)

    fun onChangeShippingDuration(order: CheckoutOrderModel, position: Int)

    fun onChangeShippingCourier(order: CheckoutOrderModel, position: Int)

    fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel, order: CheckoutOrderModel, position: Int)

    fun onViewErrorInCourierSection(errorMessage: String)

    fun onOntimeDeliveryClicked(url: String)

    fun onClickSetPinpoint(position: Int)

    fun onClickRefreshErrorLoadCourier()

    fun onCancelVoucherLogisticClicked(promoCode: String, position: Int, order: CheckoutOrderModel)

    fun getHostFragmentManager(): FragmentManager

    fun onInsuranceCheckedForTrackingAnalytics()

    fun onInsuranceChecked(isChecked: Boolean, order: CheckoutOrderModel, position: Int)

    fun onInsuranceInfoTooltipClickedTrackingAnalytics()

    fun uploadPrescriptionAction(
        uploadPrescriptionUiModel: UploadPrescriptionUiModel,
        buttonText: String,
        buttonNotes: String
    )

    fun onClickPromoCheckout(lastApplyUiModel: LastApplyUiModel)

    fun onClickReloadPromoWidget()

    fun onSendAnalyticsClickPromoCheckout(isApplied: Boolean, listAllPromoCodes: List<String>)

    fun onSendAnalyticsViewPromoCheckoutApplied()

    fun sendImpressionUserSavingTotalSubsidyEvent(
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel
    )

    fun sendClickUserSavingAndPromoEntryPointEvent(
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel
    )

    fun sendImpressionUserSavingDetailTotalSubsidyEvent(
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel
    )

    fun sendClickUserSavingDetailTotalSubsidyEvent(
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel
    )

    fun sendImpressionPromoEntryPointErrorEvent(
        errorMessage: String,
        lastApply: LastApplyUiModel
    )

    fun showPlatformFeeTooltipInfoBottomSheet(platformFeeModel: ShipmentPaymentFeeModel)

    fun getParentWidth(): Int

    fun onCrossSellItemChecked(checked: Boolean, crossSellModel: CheckoutCrossSellModel)

    fun onEgoldChecked(checked: Boolean, egoldModel: CheckoutEgoldModel)

    fun onDonationChecked(checked: Boolean, checkoutDonationModel: CheckoutDonationModel)

    fun checkPlatformFee()

    fun onInsuranceTncClicked()

    fun onProcessToPayment()

    fun onClickBmgmInfoIcon(
        offerId: String,
        shopId: String
    )

    fun onPaymentLevelAddOnsImpressed(
        categoryName: String,
        crossSellProductId: String
    )

    fun showDropshipInfoBottomSheet()

    fun showDropshipToasterErrorProtectionUsage()

    fun checkLatestProtectionOptIn(cartStringGroup: String): Boolean

    fun onCheckChangedDropship(isChecked: Boolean, position: Int)

    fun setValidationDropshipName(name: String, isValid: Boolean, position: Int)

    fun setValidationDropshipPhone(phone: String, isValid: Boolean, position: Int)

    fun onSendImpressionDropshipWidgetAnalytics()

    fun onViewGwpBenefit(benefit: CheckoutProductBenefitModel)

    fun onRetryGetPayment()

    fun onChangePayment(payment: CheckoutPaymentModel)

    fun onChangeInstallment(payment: CheckoutPaymentModel)

    fun onPaymentAction(payment: CheckoutPaymentModel)

    fun showPaymentFeeTooltipInfoBottomSheet(paymentFee: OrderPaymentFee)

    fun onCheckoutItemQuantityChanged(product: CheckoutProductModel, value: Int)

    fun clearAllFocus()

    fun onQtyMinusButtonClicked()

    fun onQtyPlusButtonClicked()

    fun onClickInputQty()
}

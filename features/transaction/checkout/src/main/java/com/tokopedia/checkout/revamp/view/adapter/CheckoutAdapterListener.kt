package com.tokopedia.checkout.revamp.view.adapter

import androidx.fragment.app.FragmentManager
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel

interface CheckoutAdapterListener {

    fun onChangeAddress()

    fun onViewNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)

    fun onClickApplyNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)

    fun onClickCancelNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)

    fun onViewFreeShippingPlusBadge()

    fun onCheckboxAddonProductListener(
        isChecked: Boolean,
        addOnProductDataItemModel: AddOnProductDataItemModel,
        product: CheckoutProductModel,
        bindingAdapterPosition: Int
    )

    fun onClickAddonProductInfoIcon(addOnDataInfoLink: String)

    fun onClickSeeAllAddOnProductService(product: CheckoutProductModel)

    fun onImpressionAddOnProductService(addonType: Int, productId: String)

    fun onClickAddOnGiftingProductLevel(product: CheckoutProductModel)

    fun onImpressionAddOnGiftingProductLevel(productId: String)

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

    fun getHostFragmentManager(): FragmentManager

    fun onInsuranceCheckedForTrackingAnalytics()

    fun onInsuranceChecked(isChecked: Boolean, order: CheckoutOrderModel, position: Int)

    fun onInsuranceInfoTooltipClickedTrackingAnalytics()

    fun onClickPromoCheckout(lastApplyUiModel: LastApplyUiModel)

    fun onSendAnalyticsClickPromoCheckout(isApplied: Boolean, listAllPromoCodes: List<String>)

    fun onSendAnalyticsViewPromoCheckoutApplied()

    fun showPlatformFeeTooltipInfoBottomSheet(platformFeeModel: ShipmentPaymentFeeModel)

    fun getParentWidth(): Int

    fun onEgoldChecked(checked: Boolean)

    fun checkPlatformFee()

    fun onInsuranceTncClicked()

    fun onProcessToPayment()
}

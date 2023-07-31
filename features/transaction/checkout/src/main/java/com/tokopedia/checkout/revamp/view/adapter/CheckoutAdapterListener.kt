package com.tokopedia.checkout.revamp.view.adapter

import androidx.fragment.app.FragmentManager
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
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

    fun onClickAddOnsProductWidget(addonType: Int, productId: String, isChecked: Boolean)

    fun onClickLihatSemuaAddOnProductWidget()

    fun onClickAddOnGiftingProductLevel(product: CheckoutProductModel)

    fun onImpressionAddOnGiftingProductLevel(productId: String)

    fun openAddOnGiftingOrderLevelBottomSheet(order: CheckoutOrderModel)

    fun addOnGiftingOrderLevelImpression(products: List<CheckoutProductModel>)

    fun onLoadShippingState(order: CheckoutOrderModel, position: Int)

    fun onChangeShippingDuration(order: CheckoutOrderModel, position: Int)

    fun onChangeShippingCourier(order: CheckoutOrderModel, position: Int)

    fun getHostFragmentManager(): FragmentManager

    fun onClickPromoCheckout(lastApplyUiModel: LastApplyUiModel)

    fun onSendAnalyticsClickPromoCheckout(isApplied: Boolean, listAllPromoCodes: List<String>)

    fun onSendAnalyticsViewPromoCheckoutApplied()

    fun showPlatformFeeTooltipInfoBottomSheet(platformFeeModel: ShipmentPaymentFeeModel)

    fun getParentWidth(): Int

    fun onEgoldChecked(checked: Boolean)

    fun onInsuranceTncClicked()

    fun onProcessToPayment()
}

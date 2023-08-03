package com.tokopedia.checkout.view

import androidx.fragment.app.FragmentManager
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import rx.subjects.PublishSubject

interface ShipmentAdapterActionListener {
    fun onCancelVoucherLogisticClicked(
        pslCode: String,
        uniqueId: String,
        position: Int,
        shipmentCartItemModel: ShipmentCartItemModel
    )

    fun onDataEnableToCheckout()
    fun onNeedToSaveState(shipmentCartItemModel: ShipmentCartItemModel)
    fun onDataDisableToCheckout(message: String?)
    fun onCheckoutValidationResult(
        result: Boolean,
        shipmentData: Any?,
        position: Int,
        epharmacyError: Boolean
    )

    fun onChangeAddress()
    fun onFinishChoosingShipment(
        lastSelectedCourierOrder: Int,
        lastSelectedCourierOrderCartString: String?,
        forceHitValidateUse: Boolean,
        skipValidateUse: Boolean
    )

    fun onInsuranceChecked(position: Int)
    fun onNeedUpdateViewItem(position: Int)
    fun onInsuranceTncClicked()
    fun onOntimeDeliveryClicked(url: String)
    fun onNeedUpdateRequestData()
    fun onDropshipCheckedForTrackingAnalytics()
    fun onInsuranceCheckedForTrackingAnalytics()
    fun onDonationChecked(checked: Boolean)
    fun onCrossSellItemChecked(checked: Boolean, crossSellModel: CrossSellModel, index: Int)
    fun onEgoldChecked(checked: Boolean)
    fun onChangeShippingDuration(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel,
        position: Int
    )

    fun onChangeShippingCourier(
        recipientAddressModel: RecipientAddressModel?,
        shipmentCartItemModel: ShipmentCartItemModel?,
        position: Int,
        selectedShippingCourierUiModels: List<ShippingCourierUiModel>?
    )

    fun hideSoftKeyboard()
    fun onLoadShippingState(
        shipperId: Int,
        spId: Int,
        itemPosition: Int,
        shipmentDetailData: ShipmentDetailData,
        shipmentCartItemModel: ShipmentCartItemModel,
        shopShipmentList: List<ShopShipment>,
        isTradeInDropOff: Boolean
    )

    fun onPurchaseProtectionLogicError()
    fun onPurchaseProtectionChangeListener(position: Int)
    fun navigateToProtectionMore(cartItemModel: CartItemModel)
    fun onProcessToPayment()
    fun onChangeTradeInDropOffClicked(latitude: String?, longitude: String?)
    val isTradeInByDropOff: Boolean
    fun hasSelectTradeInLocation(): Boolean
    fun onTradeInAddressTabChanged(addressPosition: Int)
    fun onClickPromoCheckout(lastApplyUiModel: LastApplyUiModel?)
    fun onSendAnalyticsClickPromoCheckout(isApplied: Boolean, listAllPromoCodes: List<String>)
    fun onSendAnalyticsViewPromoCheckoutApplied()
    fun onCheckShippingCompletionClicked()
    fun onShowTickerShippingCompletion()
    fun onClickTradeInInfo()
    fun onClickSwapInIndomaret()
    fun onSwapInUserAddress()
    val currentFragmentManager: FragmentManager
    fun scrollToPositionWithOffset(position: Int)
    fun onClickLihatOnTickerOrderError(
        shopId: String,
        errorMessage: String,
        shipmentCartItemTopModel: ShipmentCartItemTopModel
    )
    fun onClickRefreshErrorLoadCourier()
    fun onViewErrorInCourierSection(errorMessage: String)
    fun onClickSetPinpoint(position: Int)
    fun openAddOnProductLevelBottomSheet(
        cartItemModel: CartItemModel,
        addOnWordingModel: AddOnWordingModel?
    )

    fun openAddOnOrderLevelBottomSheet(
        cartItemModel: ShipmentCartItemModel,
        addOnWordingModel: AddOnWordingModel?
    )

    fun addOnProductLevelImpression(productId: String)
    fun addOnOrderLevelImpression(cartItemModelList: List<CartItemModel>)
    fun onViewUpsellCard(shipmentUpsellModel: ShipmentUpsellModel)
    fun onClickUpsellCard(shipmentUpsellModel: ShipmentUpsellModel)
    fun onViewNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)
    fun onClickApplyNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)
    fun onClickCancelNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)
    fun onViewFreeShippingPlusBadge()
    fun onInsuranceInfoTooltipClickedTrackingAnalytics()
    fun onChangeScheduleDelivery(
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel,
        position: Int,
        donePublisher: PublishSubject<Boolean>
    )

    fun checkPlatformFee()

    fun showPlatformFeeTooltipInfoBottomSheet(platformFeeModel: ShipmentPaymentFeeModel)

    fun updateShipmentCostModel()
}

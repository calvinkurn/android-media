package com.tokopedia.checkout.revamp.view.viewholder

import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutOrderBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingCheckoutRevampWidget
import com.tokopedia.logisticcart.shipping.model.InsuranceWidgetUiModel
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetUiModel
import com.tokopedia.purchase_platform.common.feature.bottomsheet.InsuranceBottomSheet
import com.tokopedia.purchase_platform.common.prefs.PlusCoachmarkPrefs

class CheckoutOrderViewHolder(
    private val binding: ItemCheckoutOrderBinding,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root),
    ShippingCheckoutRevampWidget.ShippingWidgetListener {

    private lateinit var order: CheckoutOrderModel

    private val plusCoachmarkPrefs: PlusCoachmarkPrefs by lazy {
        PlusCoachmarkPrefs(itemView.context)
    }

    fun bind(order: CheckoutOrderModel, addressModel: CheckoutAddressModel?) {
        this.order = order
        renderAddOnOrderLevel(order)
        renderShippingWidget(order, addressModel)
        renderVibration(order)
    }

    private fun renderAddOnOrderLevel(order: CheckoutOrderModel) {
        val addOnsDataModel = order.addOnsOrderLevelModel
        val addOnsButton = addOnsDataModel.addOnsButtonModel
        val statusAddOn = addOnsDataModel.status
        if (statusAddOn == 0) {
            binding.buttonGiftingAddonOrderLevel.visibility = View.GONE
        } else {
            if (statusAddOn == 1) {
                if (addOnsDataModel.addOnsDataItemModelList.isNotEmpty()) {
                    binding.buttonGiftingAddonOrderLevel.showActive(
                        addOnsButton.title,
                        addOnsButton.description
                    )
                } else {
                    binding.buttonGiftingAddonOrderLevel.showEmptyState(
                        addOnsButton.title,
                        addOnsButton.description.ifEmpty { "(opsional)" }
                    )
                }
            } else if (statusAddOn == 2) {
                binding.buttonGiftingAddonOrderLevel.showInactive(addOnsButton.title, addOnsButton.description)
            }
            binding.buttonGiftingAddonOrderLevel.visibility = View.VISIBLE
            binding.buttonGiftingAddonOrderLevel.setOnClickListener {
                listener.openAddOnGiftingOrderLevelBottomSheet(
                    order
                )
            }
            listener.addOnGiftingOrderLevelImpression(order)
        }
    }

    private fun renderShippingWidget(
        order: CheckoutOrderModel,
        addressModel: CheckoutAddressModel?
    ) {
        binding.shippingWidget.setupListener(this)
        binding.shippingWidget.hideTradeInShippingInfo()

        if (order.isError) {
            // todo custom error wording
            binding.shippingWidget.renderErrorCourierState(
                ShippingWidgetUiModel(
                    currentAddress = RecipientAddressModel()
                )
            )
        } else if (order.shipment.isLoading) {
            binding.shippingWidget.prepareLoadCourierState()
            binding.shippingWidget.renderLoadingCourierState()
        } else {
            val courierItemData = order.shipment.courierItemData
            val insurance = order.shipment.insurance
            if (courierItemData == null) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                if (order.isDisableChangeCourier && order.hasGeolocation) {
                    binding.shippingWidget.showLayoutStateFailedShipping(
                        ShippingWidgetUiModel(
                            currentAddress = RecipientAddressModel()
                        )
                    )
                } else {
                    binding.shippingWidget.showLayoutNoSelectedShipping(
                        ShippingWidgetUiModel(
                            currentAddress = RecipientAddressModel()
                        )
                    )
                    showMultiplePlusOrderCoachmark(
                        order,
                        binding.shippingWidget.layoutStateNoSelectedShipping
                    )
                }
                loadCourierState(order, addressModel?.recipientAddressModel)
            } else if (courierItemData.scheduleDeliveryUiModel != null) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showContainerShippingExperience()
                // todo selly
                binding.shippingWidget.renderScheduleDeliveryWidget(
                    ShippingWidgetUiModel(
                        // CourierItemData.etaErrorCode
                        etaErrorCode = courierItemData.selectedShipper.etaErrorCode,
                        // CourierItemData.etaText
                        estimatedTimeArrival = courierItemData.selectedShipper.etaText ?: "",

                        // Bebas ongkir & NOW Shipment
                        hideShipperName = courierItemData.selectedShipper.isHideShipperName,
                        freeShippingTitle = courierItemData.selectedShipper.freeShippingChosenCourierTitle,
                        // Now Shipment
                        // label
                        logPromoDesc = courierItemData.logPromoDesc ?: "",
                        voucherLogisticExists = !courierItemData.logPromoCode.isNullOrEmpty(),
                        isHasShownCourierError = false,

                        // CourierItemData.name
                        courierName = courierItemData.name ?: "",
                        // CourierItemData.shipperPrice
                        courierShipperPrice = courierItemData.selectedShipper.shipperPrice,

                        currentAddress = RecipientAddressModel(),

                        scheduleDeliveryUiModel = courierItemData.scheduleDeliveryUiModel,
                        insuranceData = InsuranceWidgetUiModel(
                            useInsurance = insurance.isCheckInsurance,
                            insuranceType = courierItemData.selectedShipper.insuranceType,
                            insuranceUsedDefault = courierItemData.selectedShipper.insuranceUsedDefault,
                            insuranceUsedInfo = courierItemData.selectedShipper.insuranceUsedInfo,
                            insurancePrice = courierItemData.selectedShipper.insurancePrice.toDouble(),
                            isInsurance = order.isInsurance
                        )
                    )
                )
            } else if (order.isDisableChangeCourier) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showContainerShippingExperience()
                // todo now
                binding.shippingWidget.renderSingleShippingCourier(
                    ShippingWidgetUiModel(
                        // CourierItemData.etaErrorCode
                        etaErrorCode = courierItemData.etaErrorCode,
                        // CourierItemData.etaText
                        estimatedTimeArrival = courierItemData.etaText ?: "",

                        // Bebas ongkir & NOW Shipment
                        hideShipperName = courierItemData.isHideShipperName,
                        freeShippingTitle = courierItemData.freeShippingChosenCourierTitle,
                        // Now Shipment
                        // label
                        logPromoDesc = courierItemData.logPromoDesc ?: "",
                        voucherLogisticExists = !courierItemData.logPromoCode.isNullOrEmpty(),
                        isHasShownCourierError = false,

                        // CourierItemData.name
                        courierName = courierItemData.name ?: "",
                        // CourierItemData.shipperPrice
                        courierShipperPrice = courierItemData.shipperPrice,

                        currentAddress = RecipientAddressModel(),
                        insuranceData = InsuranceWidgetUiModel(
                            useInsurance = insurance.isCheckInsurance,
                            insuranceType = courierItemData.selectedShipper.insuranceType,
                            insuranceUsedDefault = courierItemData.selectedShipper.insuranceUsedDefault,
                            insuranceUsedInfo = courierItemData.selectedShipper.insuranceUsedInfo,
                            insurancePrice = courierItemData.selectedShipper.insurancePrice.toDouble(),
                            isInsurance = order.isInsurance
                        )
                    )
                )
            } else if (courierItemData.logPromoCode?.isNotEmpty() == true) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showContainerShippingExperience()
                binding.shippingWidget.showLayoutFreeShippingCourier(
                    ShippingWidgetUiModel(
                        currentAddress = RecipientAddressModel()
                    )
                )
                binding.shippingWidget.renderFreeShippingCourier(
                    ShippingWidgetUiModel(
                        courierErrorTitle = order.courierSelectionErrorTitle,
                        // renderErrorCourierState - shipmentCartItemModel.courierSelectionErrorDescription
                        courierErrorDescription = order.courierSelectionErrorDescription,

                        // renderShippingVibrationAnimation
                        isShippingBorderRed = false,
                        // renderShippingVibrationAnimation
                        isTriggerShippingVibrationAnimation = false,

                        // CourierItemData.etaErrorCode
                        etaErrorCode = courierItemData.etaErrorCode,
                        // CourierItemData.etaText
                        estimatedTimeArrival = courierItemData.etaText ?: "",

                        // Bebas ongkir & NOW Shipment
                        hideShipperName = courierItemData.isHideShipperName,
                        freeShippingTitle = courierItemData.freeShippingChosenCourierTitle,
                        // Now Shipment
                        // label
                        logPromoDesc = "",
                        voucherLogisticExists = false,
                        isHasShownCourierError = false,

                        // showNormalShippingCourier
                        currentAddress = RecipientAddressModel(),
                        // CourierItemData.estimatedTimeDelivery
                        estimatedTimeDelivery = courierItemData.estimatedTimeDelivery ?: "",

                        // CourierItemData.name
                        courierName = courierItemData.name ?: "",
                        // CourierItemData.shipperPrice
                        courierShipperPrice = courierItemData.shipperPrice,

                        merchantVoucher = courierItemData.merchantVoucherProductModel,
                        ontimeDelivery = courierItemData.ontimeDelivery,
                        cashOnDelivery = courierItemData.codProductData,

                        // CourierItemData.durationCardDescription
                        whitelabelEtaText = courierItemData.durationCardDescription,

                        scheduleDeliveryUiModel = null,
                        insuranceData = InsuranceWidgetUiModel(
                            useInsurance = insurance.isCheckInsurance,
                            insuranceType = courierItemData.selectedShipper.insuranceType,
                            insuranceUsedDefault = courierItemData.selectedShipper.insuranceUsedDefault,
                            insuranceUsedInfo = courierItemData.selectedShipper.insuranceUsedInfo,
                            insurancePrice = courierItemData.selectedShipper.insurancePrice.toDouble(),
                            isInsurance = order.isInsurance
                        )
                    )
                )
            } else if (courierItemData.isHideChangeCourierCard) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showContainerShippingExperience()
                binding.shippingWidget.renderNormalShippingWithoutChooseCourierCard(
                    ShippingWidgetUiModel(
                        courierErrorTitle = order.courierSelectionErrorTitle,
                        // renderErrorCourierState - shipmentCartItemModel.courierSelectionErrorDescription
                        courierErrorDescription = order.courierSelectionErrorDescription,

                        // renderShippingVibrationAnimation
                        isShippingBorderRed = false,
                        // renderShippingVibrationAnimation
                        isTriggerShippingVibrationAnimation = false,

                        // CourierItemData.etaErrorCode
                        etaErrorCode = courierItemData.etaErrorCode,
                        // CourierItemData.etaText
                        estimatedTimeArrival = courierItemData.etaText ?: "",

                        // Bebas ongkir & NOW Shipment
                        hideShipperName = false,
                        freeShippingTitle = "",
                        // Now Shipment
                        // label
                        logPromoDesc = "",
                        voucherLogisticExists = false,
                        isHasShownCourierError = false,

                        // showNormalShippingCourier
                        currentAddress = RecipientAddressModel(),
                        // CourierItemData.estimatedTimeDelivery
                        estimatedTimeDelivery = courierItemData.estimatedTimeDelivery ?: "",

                        // CourierItemData.name
                        courierName = courierItemData.name ?: "",
                        // CourierItemData.shipperPrice
                        courierShipperPrice = courierItemData.shipperPrice,

                        merchantVoucher = courierItemData.merchantVoucherProductModel,
                        ontimeDelivery = courierItemData.ontimeDelivery,
                        cashOnDelivery = courierItemData.codProductData,

                        // CourierItemData.durationCardDescription
                        whitelabelEtaText = courierItemData.durationCardDescription,

                        scheduleDeliveryUiModel = null,
                        insuranceData = InsuranceWidgetUiModel(
                            useInsurance = insurance.isCheckInsurance,
                            insuranceType = courierItemData.selectedShipper.insuranceType,
                            insuranceUsedDefault = courierItemData.selectedShipper.insuranceUsedDefault,
                            insuranceUsedInfo = courierItemData.selectedShipper.insuranceUsedInfo,
                            insurancePrice = courierItemData.selectedShipper.insurancePrice.toDouble(),
                            isInsurance = order.isInsurance
                        )
                    )
                )
            } else {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showContainerShippingExperience()
                binding.shippingWidget.renderNormalShippingCourier(
                    ShippingWidgetUiModel(
                        courierErrorTitle = order.courierSelectionErrorTitle,
                        // renderErrorCourierState - shipmentCartItemModel.courierSelectionErrorDescription
                        courierErrorDescription = order.courierSelectionErrorDescription,

                        // renderShippingVibrationAnimation
                        isShippingBorderRed = false,
                        // renderShippingVibrationAnimation
                        isTriggerShippingVibrationAnimation = false,

                        // CourierItemData.etaErrorCode
                        etaErrorCode = courierItemData.etaErrorCode,
                        // CourierItemData.etaText
                        estimatedTimeArrival = courierItemData.etaText ?: "",

                        // Bebas ongkir & NOW Shipment
                        hideShipperName = false,
                        freeShippingTitle = "",
                        // Now Shipment
                        // label
                        logPromoDesc = "",
                        voucherLogisticExists = false,
                        isHasShownCourierError = false,

                        // showNormalShippingCourier
                        currentAddress = RecipientAddressModel(),
                        // CourierItemData.estimatedTimeDelivery
                        estimatedTimeDelivery = courierItemData.estimatedTimeDelivery ?: "",

                        // CourierItemData.name
                        courierName = courierItemData.name ?: "",
                        // CourierItemData.shipperPrice
                        courierShipperPrice = courierItemData.shipperPrice,

                        merchantVoucher = courierItemData.merchantVoucherProductModel,
                        ontimeDelivery = courierItemData.ontimeDelivery,
                        cashOnDelivery = courierItemData.codProductData,

                        // CourierItemData.durationCardDescription
                        whitelabelEtaText = courierItemData.durationCardDescription,

                        scheduleDeliveryUiModel = null,
                        insuranceData = InsuranceWidgetUiModel(
                            useInsurance = insurance.isCheckInsurance,
                            insuranceType = courierItemData.selectedShipper.insuranceType,
                            insuranceUsedDefault = courierItemData.selectedShipper.insuranceUsedDefault,
                            insuranceUsedInfo = courierItemData.selectedShipper.insuranceUsedInfo,
                            insurancePrice = courierItemData.selectedShipper.insurancePrice.toDouble(),
                            isInsurance = order.isInsurance
                        )
                    )
                )
            }
        }
    }

    private fun loadCourierState(
        shipmentCartItemModel: CheckoutOrderModel,
        recipientAddressModel: RecipientAddressModel?
//        ratesDataConverter: RatesDataConverter,
//        saveStateType: Int
    ) {
//        with(binding) {
//            val shipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
//            if (shipmentCartItemModel.isStateLoadingCourierState) {
//                renderLoadingCourierState()
//            } else {
//                var hasLoadCourier = false
//                shippingWidget.hideShippingStateLoading()
//                when (saveStateType) {
//                    ShipmentCartItemBottomViewHolder.SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF ->
//                        hasLoadCourier =
//                            shipmentDetailData?.selectedCourierTradeInDropOff != null
//
//                    ShipmentCartItemBottomViewHolder.SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE ->
//                        hasLoadCourier =
//                            shipmentDetailData?.selectedCourier != null
//                }
        if (shipmentCartItemModel.isCustomPinpointError) {
            renderErrorPinpointCourier()
        } else if (shouldAutoLoadCourier(shipmentCartItemModel, recipientAddressModel)) {
//                if (!hasLoadCourier) {
//                    val tmpShipmentDetailData = ratesDataConverter.getShipmentDetailData(
//                        shipmentCartItemModel,
//                        recipientAddressModel
//                    )
//                    val hasLoadCourierState =
//                        if (saveStateType == ShipmentCartItemBottomViewHolder.SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF) {
//                            shipmentCartItemModel.isStateHasLoadCourierTradeInDropOffState
//                        } else {
//                            shipmentCartItemModel.isStateHasLoadCourierState
//                        }
            if (!order.isStateHasLoadCourierState) {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    order.isStateHasLoadCourierState = true
                    listener.onLoadShippingState(order, position)
//                            loadCourierStateData(
//                                shipmentCartItemModel,
//                                saveStateType,
//                                tmpShipmentDetailData,
//                                position
//                            )
                }
            } else {
//                        renderNoSelectedCourier(
//                            shipmentCartItemModel,
//                            recipientAddressModel,
//                            saveStateType
//                        )
            }
        } else {
            Log.i("qwertyuiop", "no auto")
        }
//            } else {
//                renderNoSelectedCourier(
//                    shipmentCartItemModel,
//                    recipientAddressModel,
//                    saveStateType
//                )
//                showMultiplePlusOrderCoachmark(
//                    shipmentCartItemModel,
//                    shippingWidget.layoutStateNoSelectedShipping
//                )
//            }
//        }
    }
//    }

    private fun shouldAutoLoadCourier(
        shipmentCartItemModel: CheckoutOrderModel,
        recipientAddressModel: RecipientAddressModel?
    ): Boolean {
        return recipientAddressModel != null && (
            (recipientAddressModel.isTradeIn && recipientAddressModel.selectedTabIndex != 0 && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !recipientAddressModel.dropOffAddressName.isNullOrEmpty()) ||
                (recipientAddressModel.isTradeIn && recipientAddressModel.selectedTabIndex == 0 && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !recipientAddressModel.provinceName.isNullOrEmpty()) ||
                (!recipientAddressModel.isTradeIn && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !recipientAddressModel.provinceName.isNullOrEmpty()) ||
                (!recipientAddressModel.isTradeIn && shipmentCartItemModel.boCode.isNotEmpty() && !recipientAddressModel.provinceName.isNullOrEmpty()) || // normal address auto apply BO
                shipmentCartItemModel.isAutoCourierSelection // tokopedia now
            )
    }

    private fun renderErrorPinpointCourier() {
//        binding.containerShippingOptions.root.visibility = View.VISIBLE
        binding.shippingWidget.renderErrorPinpointCourier()
    }

    private fun renderVibration(order: CheckoutOrderModel) {
        binding.shippingWidget.renderShippingVibrationAnimation(
            ShippingWidgetUiModel(
                isShippingBorderRed = order.isShippingBorderRed,
                isTriggerShippingVibrationAnimation = order.isTriggerShippingVibrationAnimation,
                currentAddress = RecipientAddressModel()
            )
        )
        order.isTriggerShippingVibrationAnimation = false
    }

    private fun showMultiplePlusOrderCoachmark(
        order: CheckoutOrderModel,
        anchorView: View?
    ) {
        if (order.coachmarkPlus.isShown && !plusCoachmarkPrefs.getPlusCoachMarkHasShown() && anchorView != null) {
            val coachMarkItem = ArrayList<CoachMark2Item>()
            val coachMark = CoachMark2(itemView.context)
            coachMarkItem.add(
                CoachMark2Item(
                    anchorView,
                    order.coachmarkPlus.title,
                    order.coachmarkPlus.content,
                    CoachMark2.POSITION_BOTTOM
                )
            )
            coachMark.showCoachMark(coachMarkItem, null, 0)
            plusCoachmarkPrefs.setPlusCoachmarkHasShown(true)
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_order
    }

    override fun onChangeDurationClickListener(currentAddress: RecipientAddressModel) {
        listener.onChangeShippingDuration(order, bindingAdapterPosition)
    }

    override fun onChangeCourierClickListener(currentAddress: RecipientAddressModel) {
        listener.onChangeShippingCourier(order, bindingAdapterPosition)
    }

    override fun onOnTimeDeliveryClicked(url: String) {
        listener.onOntimeDeliveryClicked(url)
    }

    override fun onClickSetPinpoint() {
        listener.onClickSetPinpoint(bindingAdapterPosition)
    }

    override fun onClickLayoutFailedShipping(recipientAddressModel: RecipientAddressModel) {
        listener.onLoadShippingState(order, bindingAdapterPosition)
        listener.onClickRefreshErrorLoadCourier()
    }

    override fun onViewErrorInCourierSection(logPromoDesc: String) {
        listener.onViewErrorInCourierSection(logPromoDesc)
    }

    override fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel) {
        listener.onChangeScheduleDelivery(scheduleDeliveryUiModel, order, bindingAdapterPosition)
    }

    override fun getHostFragmentManager(): FragmentManager {
        return listener.getHostFragmentManager()
    }

    override fun onInsuranceCheckedForTrackingAnalytics() {
        listener.onInsuranceCheckedForTrackingAnalytics()
    }

    override fun onInsuranceChecked(shippingWidgetUiModel: ShippingWidgetUiModel) {
        listener.onInsuranceChecked(shippingWidgetUiModel.insuranceData!!.useInsurance!!, order, bindingAdapterPosition)
    }

    override fun onInsuranceInfoTooltipClickedTrackingAnalytics() {
        listener.onInsuranceInfoTooltipClickedTrackingAnalytics()
    }

    override fun showInsuranceBottomSheet(description: String) {
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            val insuranceBottomSheet = InsuranceBottomSheet()
            insuranceBottomSheet.setDesc(description)
            insuranceBottomSheet.show(
                binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.title_bottomsheet_insurance),
                binding.root.context,
                listener.getHostFragmentManager()
            )
        }
    }
}

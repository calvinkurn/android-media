package com.tokopedia.checkout.revamp.view.viewholder

import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutOrderBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingCheckoutRevampWidget
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetUiModel

class CheckoutOrderViewHolder(
    private val binding: ItemCheckoutOrderBinding,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root),
    ShippingCheckoutRevampWidget.ShippingWidgetListener {

    private lateinit var order: CheckoutOrderModel

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
                binding.buttonGiftingAddonOrderLevel.state =
                    com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView.State.ACTIVE
            } else if (statusAddOn == 2) {
                binding.buttonGiftingAddonOrderLevel.state =
                    com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView.State.INACTIVE
            }
            binding.buttonGiftingAddonOrderLevel.visibility = View.VISIBLE
            binding.buttonGiftingAddonOrderLevel.title = addOnsButton.title
            binding.buttonGiftingAddonOrderLevel.desc = addOnsButton.description
            binding.buttonGiftingAddonOrderLevel.urlLeftIcon = addOnsButton.leftIconUrl
            binding.buttonGiftingAddonOrderLevel.urlRightIcon = addOnsButton.rightIconUrl
            binding.buttonGiftingAddonOrderLevel.setOnClickListener {
                listener.openAddOnGiftingOrderLevelBottomSheet(
                    order
                )
            }
            listener.addOnGiftingOrderLevelImpression(order.products)
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
            if (courierItemData == null) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showLayoutNoSelectedShipping(RecipientAddressModel())
                // todo coachmark

                //                showMultiplePlusOrderCoachmark(
//                    shipmentCartItemModel,
//                    shippingWidget.layoutStateNoSelectedShipping
//                )
                loadCourierState(order, addressModel?.recipientAddressModel)
            } else if (courierItemData.scheduleDeliveryUiModel != null) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showContainerShippingExperience()
                // todo selly
                binding.shippingWidget.renderScheduleDeliveryWidget(
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

                        scheduleDeliveryUiModel = courierItemData.scheduleDeliveryUiModel
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

                        currentAddress = RecipientAddressModel()
                    )
                )
            } else if (courierItemData.logPromoCode?.isNotEmpty() == true) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showContainerShippingExperience()
                binding.shippingWidget.showLayoutFreeShippingCourier(RecipientAddressModel())
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
                        insuranceData = InsuranceData()
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
                        insuranceData = InsuranceData()
                    ),
                    RecipientAddressModel()
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
                        insuranceData = InsuranceData()
                    ),
                    RecipientAddressModel()
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
            recipientAddressModel.isTradeIn && recipientAddressModel.selectedTabIndex != 0 && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !TextUtils.isEmpty(
                recipientAddressModel.dropOffAddressName
            ) || recipientAddressModel.isTradeIn && recipientAddressModel.selectedTabIndex == 0 && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !TextUtils.isEmpty(
                recipientAddressModel.provinceName
            ) || !recipientAddressModel.isTradeIn && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !TextUtils.isEmpty(
                recipientAddressModel.provinceName
            ) || !recipientAddressModel.isTradeIn && shipmentCartItemModel.boCode.isNotEmpty() && !TextUtils.isEmpty(
                recipientAddressModel.provinceName
            ) || // normal address auto apply BO
                shipmentCartItemModel.isAutoCourierSelection
            ) // tokopedia now
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
        TODO("Not yet implemented")
    }

    override fun onClickSetPinpoint() {
        TODO("Not yet implemented")
    }

    override fun onClickLayoutFailedShipping(recipientAddressModel: RecipientAddressModel) {
        TODO("Not yet implemented")
    }

    override fun onViewErrorInCourierSection(logPromoDesc: String) {
        // todo
    }

    override fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel) {
        TODO("Not yet implemented")
    }

    override fun getHostFragmentManager(): FragmentManager {
        return listener.getHostFragmentManager()
    }
}

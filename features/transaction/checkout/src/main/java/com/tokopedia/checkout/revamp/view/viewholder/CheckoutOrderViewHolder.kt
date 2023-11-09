package com.tokopedia.checkout.revamp.view.viewholder

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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingCheckoutRevampWidget
import com.tokopedia.logisticcart.shipping.model.InsuranceWidgetUiModel
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetUiModel
import com.tokopedia.purchase_platform.common.feature.bottomsheet.InsuranceBottomSheet
import com.tokopedia.purchase_platform.common.prefs.PlusCoachmarkPrefs
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR

class CheckoutOrderViewHolder(
    private val binding: ItemCheckoutOrderBinding,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root),
    ShippingCheckoutRevampWidget.ShippingWidgetListener {

    private var order: CheckoutOrderModel? = null

    private val plusCoachmarkPrefs: PlusCoachmarkPrefs by lazy {
        PlusCoachmarkPrefs(itemView.context)
    }

    fun bind(order: CheckoutOrderModel, addressModel: CheckoutAddressModel?) {
        this.order = order
        renderAddOnOrderLevel(order)
        renderShippingWidget(order, addressModel)
        renderVibration(order)
        renderDropshipWidget(order)
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
                        addOnsButton.description,
                        addOnsButton.rightIconUrl
                    )
                } else {
                    binding.buttonGiftingAddonOrderLevel.showEmptyState(
                        addOnsButton.title,
                        addOnsButton.description.ifEmpty { "(opsional)" },
                        addOnsButton.rightIconUrl
                    )
                }
            } else if (statusAddOn == 2) {
                binding.buttonGiftingAddonOrderLevel.showInactive(addOnsButton.title, addOnsButton.description, addOnsButton.rightIconUrl)
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
                    currentAddress = RecipientAddressModel(),
                    courierErrorTitle = order.courierSelectionErrorTitle
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
                if (order.isCustomPinpointError) {
                    renderErrorPinpointCourier()
                } else if (order.isDisableChangeCourier && order.hasGeolocation) {
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
            } else if (courierItemData.scheduleDeliveryUiModel != null) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showContainerShippingExperience()
                // todo selly
                binding.shippingWidget.renderScheduleDeliveryWidget(
                    ShippingWidgetUiModel(
                        // Bebas ongkir & NOW Shipment
                        freeShippingTitle = courierItemData.freeShippingChosenCourierTitle,
                        // Now Shipment
                        // label
                        logPromoDesc = courierItemData.logPromoDesc ?: "",
                        voucherLogisticExists = !courierItemData.selectedShipper.logPromoCode.isNullOrEmpty(),
                        isHasShownCourierError = false, // todo: analytics

                        // CourierItemData.name
                        courierName = courierItemData.name ?: "",
                        // CourierItemData.shipperPrice
                        courierShipperPrice = courierItemData.shipperPrice,

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
                        // Bebas ongkir & NOW Shipment
                        freeShippingTitle = courierItemData.freeShippingChosenCourierTitle,
                        // Now Shipment
                        // label
                        logPromoDesc = courierItemData.logPromoDesc ?: "",
                        voucherLogisticExists = !courierItemData.selectedShipper.logPromoCode.isNullOrEmpty(),
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
            } else if (courierItemData.selectedShipper.logPromoCode?.isNotEmpty() == true) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showContainerShippingExperience()
                binding.shippingWidget.showLayoutFreeShippingCourier(
                    ShippingWidgetUiModel(
                        currentAddress = RecipientAddressModel()
                    )
                )
                if (order.isError) {
                    if (bindingAdapterPosition > RecyclerView.NO_POSITION) {
                        listener.onCancelVoucherLogisticClicked(
                            courierItemData.selectedShipper.logPromoCode!!,
                            bindingAdapterPosition,
                            order
                        )
                    }
                }
                binding.shippingWidget.renderFreeShippingCourier(
                    ShippingWidgetUiModel(
                        // CourierItemData.etaErrorCode
                        etaErrorCode = courierItemData.etaErrorCode,
                        // CourierItemData.etaText
                        estimatedTimeArrival = courierItemData.etaText ?: "",

                        // Bebas ongkir & NOW Shipment
                        hideShipperName = courierItemData.isHideShipperName,
                        freeShippingTitle = courierItemData.freeShippingChosenCourierTitle,
                        freeShippingLogo = courierItemData.freeShippingChosenImage,

                        // showNormalShippingCourier
                        currentAddress = RecipientAddressModel(),
                        cashOnDelivery = courierItemData.codProductData,

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
                binding.shippingWidget.renderWhitelabelKurirRekomendasiService(
                    ShippingWidgetUiModel(
                        // showNormalShippingCourier
                        currentAddress = RecipientAddressModel(),
                        // CourierItemData.estimatedTimeDelivery
                        estimatedTimeDelivery = courierItemData.estimatedTimeDelivery ?: "",

                        // CourierItemData.shipperPrice
                        courierShipperPrice = courierItemData.shipperPrice,

                        ontimeDelivery = courierItemData.ontimeDelivery,

                        // CourierItemData.durationCardDescription
                        whitelabelEtaText = courierItemData.durationCardDescription,

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
                        // CourierItemData.etaErrorCode
                        etaErrorCode = courierItemData.etaErrorCode,
                        // CourierItemData.etaText
                        estimatedTimeArrival = courierItemData.etaText ?: "",

                        // showNormalShippingCourier
                        currentAddress = RecipientAddressModel(),
                        // CourierItemData.estimatedTimeDelivery
                        estimatedTimeDelivery = courierItemData.estimatedTimeDelivery ?: "",

                        // CourierItemData.name
                        courierName = courierItemData.name ?: "",
                        // CourierItemData.shipperPrice
                        courierShipperPrice = courierItemData.shipperPrice,

                        merchantVoucher = courierItemData.merchantVoucherProductModel,
                        cashOnDelivery = courierItemData.codProductData,

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

    private fun renderErrorPinpointCourier() {
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

    private fun renderDropshipWidget(order: CheckoutOrderModel) {
        if (order.shipment.courierItemData?.isSelected == true && order.shipment.courierItemData.isAllowDropshiper) {
            binding.dropshipWidget.visible()
        } else {
            binding.dropshipWidget.apply {
                gone()
                dropshipName?.editText?.setText("")
                dropshipPhone?.editText?.setText("")
            }
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_order
    }

    override fun onChangeDurationClickListener(currentAddress: RecipientAddressModel) {
        order?.let {
            listener.onChangeShippingDuration(it, bindingAdapterPosition)
        }
    }

    override fun onChangeCourierClickListener(currentAddress: RecipientAddressModel) {
        order?.let {
            listener.onChangeShippingCourier(it, bindingAdapterPosition)
        }
    }

    override fun onOnTimeDeliveryClicked(url: String) {
        listener.onOntimeDeliveryClicked(url)
    }

    override fun onClickSetPinpoint() {
        listener.onClickSetPinpoint(bindingAdapterPosition)
    }

    override fun onClickLayoutFailedShipping(recipientAddressModel: RecipientAddressModel) {
        order?.let {
            listener.onLoadShippingState(it, bindingAdapterPosition)
            listener.onClickRefreshErrorLoadCourier()
        }
    }

    override fun onViewErrorInCourierSection(logPromoDesc: String) {
        listener.onViewErrorInCourierSection(logPromoDesc)
    }

    override fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel) {
        order?.let {
            listener.onChangeScheduleDelivery(scheduleDeliveryUiModel, it, bindingAdapterPosition)
        }
    }

    override fun getHostFragmentManager(): FragmentManager {
        return listener.getHostFragmentManager()
    }

    override fun onInsuranceCheckedForTrackingAnalytics() {
        listener.onInsuranceCheckedForTrackingAnalytics()
    }

    override fun onInsuranceChecked(shippingWidgetUiModel: ShippingWidgetUiModel) {
        order?.let {
            listener.onInsuranceChecked(shippingWidgetUiModel.insuranceData!!.useInsurance!!, it, bindingAdapterPosition)
        }
    }

    override fun onInsuranceInfoTooltipClickedTrackingAnalytics() {
        listener.onInsuranceInfoTooltipClickedTrackingAnalytics()
    }

    override fun showInsuranceBottomSheet(description: String) {
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            val insuranceBottomSheet = InsuranceBottomSheet()
            insuranceBottomSheet.setDesc(description)
            insuranceBottomSheet.show(
                binding.root.context.getString(purchase_platformcommonR.string.title_bottomsheet_insurance),
                binding.root.context,
                listener.getHostFragmentManager()
            )
        }
    }
}

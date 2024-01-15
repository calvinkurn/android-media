package com.tokopedia.checkout.revamp.view.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutOrderBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.widget.CheckoutDropshipWidget
import com.tokopedia.checkout.revamp.view.widget.CheckoutDropshipWidget.Companion.DROPSHIPPER_MAX_NAME_LENGTH
import com.tokopedia.checkout.revamp.view.widget.CheckoutDropshipWidget.Companion.DROPSHIPPER_MAX_PHONE_LENGTH
import com.tokopedia.checkout.revamp.view.widget.CheckoutDropshipWidget.Companion.DROPSHIPPER_MIN_NAME_LENGTH
import com.tokopedia.checkout.revamp.view.widget.CheckoutDropshipWidget.Companion.DROPSHIPPER_MIN_PHONE_LENGTH
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingCheckoutRevampWidget
import com.tokopedia.logisticcart.shipping.model.InsuranceWidgetUiModel
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetCourierError
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetUiModel
import com.tokopedia.purchase_platform.common.feature.bottomsheet.InsuranceBottomSheet
import com.tokopedia.purchase_platform.common.prefs.PlusCoachmarkPrefs
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR

class CheckoutOrderViewHolder(
    private val binding: ItemCheckoutOrderBinding,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root),
    ShippingCheckoutRevampWidget.ShippingWidgetListener,
    CheckoutDropshipWidget.DropshipWidgetListener {

    private var order: CheckoutOrderModel? = null

    private val plusCoachmarkPrefs: PlusCoachmarkPrefs by lazy {
        PlusCoachmarkPrefs(itemView.context)
    }

    fun bind(order: CheckoutOrderModel, addressModel: CheckoutAddressModel?) {
        this.order = order
        renderAddOnOrderLevel(order)
        renderShippingWidget(order, addressModel)
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
                binding.buttonGiftingAddonOrderLevel.showInactive(
                    addOnsButton.title,
                    addOnsButton.description,
                    addOnsButton.rightIconUrl
                )
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
        val uiModel = order.toShipmentWidgetModel()
        binding.shippingWidget.render(uiModel)
    }

    private fun CheckoutOrderModel.toErrorState(): ShippingWidgetCourierError? {
        if (shipment.courierItemData != null) {
            return null
        }
        return if (isCustomPinpointError) {
            ShippingWidgetCourierError.NEED_PINPOINT
        } else if (isDisableChangeCourier && hasGeolocation) {
            ShippingWidgetCourierError.COURIER_UNAVAILABLE
        } else {
            ShippingWidgetCourierError.COURIER_EMPTY
        }
    }

    private fun CheckoutOrderModel.toShipmentWidgetModel(): ShippingWidgetUiModel {
        val model = ShippingWidgetUiModel(
            cartError = isError,
            loading = shipment.isLoading,
            courierError = toErrorState(),
            isDisableChangeCourier = isDisableChangeCourier,
            courierErrorTitle = courierSelectionErrorTitle,
            isShippingBorderRed = isShippingBorderRed,
            isTriggerShippingVibrationAnimation = isTriggerShippingVibrationAnimation
        )
        val courierItemData = shipment.courierItemData
        if (courierItemData == null) {
            return model
        } else {
            return model.copy(
                etaErrorCode = courierItemData.etaErrorCode,
                estimatedTimeArrival = courierItemData.etaText ?: "",
                hideShipperName = courierItemData.isHideShipperName,
                freeShippingTitle = courierItemData.freeShippingChosenCourierTitle,
                freeShippingLogo = courierItemData.freeShippingChosenImage,
                logPromoDesc = courierItemData.logPromoDesc ?: "",
                voucherLogisticExists = !courierItemData.selectedShipper.logPromoCode.isNullOrEmpty(),
                logisticPromoCode = courierItemData.selectedShipper.logPromoCode,
                isHasShownCourierError = false,
                boOrderMessage = courierItemData.boOrderMessage,
                estimatedTimeDelivery = courierItemData.estimatedTimeDelivery ?: "",
                courierName = courierItemData.name ?: "",
                courierShipperPrice = courierItemData.shipperPrice,
                courierOrderMessage = courierItemData.courierOrderMessage,
                merchantVoucher = courierItemData.merchantVoucherProductModel,
                ontimeDelivery = courierItemData.ontimeDelivery,
                cashOnDelivery = courierItemData.codProductData,
                whitelabelEtaText = courierItemData.durationCardDescription,
                scheduleDeliveryUiModel = courierItemData.scheduleDeliveryUiModel,
                insuranceData = InsuranceWidgetUiModel(
                    useInsurance = shipment.insurance.isCheckInsurance,
                    insuranceType = courierItemData.selectedShipper.insuranceType,
                    insuranceUsedDefault = courierItemData.selectedShipper.insuranceUsedDefault,
                    insuranceUsedInfo = courierItemData.selectedShipper.insuranceUsedInfo,
                    insurancePrice = courierItemData.selectedShipper.insurancePrice.toDouble(),
                    isInsurance = isInsurance
                )
            )
        }
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
        binding.dropshipWidget.setupListener(this)
        if (order.isEnableDropship) {
            if (order.useDropship) {
                if (order.stateDropship == CheckoutDropshipWidget.State.ERROR) {
                    binding.dropshipWidget.state = order.stateDropship
                } else {
                    binding.dropshipWidget.state = CheckoutDropshipWidget.State.SELECTED
                }
                binding.dropshipWidget.dropshipName?.editText?.setText(order.dropshipName)
                binding.dropshipWidget.dropshipPhone?.editText?.setText(order.dropshipPhone)
            } else {
                if (order.stateDropship != CheckoutDropshipWidget.State.GONE) {
                    binding.dropshipWidget.state = order.stateDropship
                } else {
                    binding.dropshipWidget.state = CheckoutDropshipWidget.State.INIT
                }
            }
            listener.onSendImpressionDropshipWidgetAnalytics()
        } else {
            binding.dropshipWidget.state = CheckoutDropshipWidget.State.GONE
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_order
    }

    override fun onChangeDurationClickListener() {
        order?.let {
            listener.onChangeShippingDuration(it, bindingAdapterPosition)
        }
    }

    override fun onChangeCourierClickListener() {
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

    override fun onClickLayoutFailedShipping() {
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
            listener.onInsuranceChecked(
                shippingWidgetUiModel.insuranceData!!.useInsurance!!,
                it,
                bindingAdapterPosition
            )
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

    override fun onViewCartErrorState(shippingWidgetUiModel: ShippingWidgetUiModel) {
        order?.run {
            if (shippingWidgetUiModel.logisticPromoCode?.isNotEmpty() == true) {
                listener?.onCancelVoucherLogisticClicked(
                    shippingWidgetUiModel.logisticPromoCode!!,
                    bindingAdapterPosition,
                    this
                )
            }
        }
    }

    override fun onRenderVibrationAnimation(shippingWidgetUiModel: ShippingWidgetUiModel) {
        this?.order?.isTriggerShippingVibrationAnimation = false
    }

    override fun onClickDropshipLabel() {
        listener.showDropshipInfoBottomSheet()
    }

    override fun isAddOnProtectionOptIn(): Boolean {
        return listener.checkLatestProtectionOptIn(order?.cartStringGroup ?: "")
    }

    override fun showToasterErrorProtectionUsage() {
        listener.showDropshipToasterErrorProtectionUsage()
    }

    override fun onClickDropshipSwitch(isChecked: Boolean) {
        listener.onCheckChangedDropship(isChecked, bindingAdapterPosition)
    }

    override fun setDropshipName(name: String) {
        val isNameValid = name.isNotEmpty() && name.length > DROPSHIPPER_MIN_NAME_LENGTH &&
            name.length < DROPSHIPPER_MAX_NAME_LENGTH
        order?.isDropshipNameValid = isNameValid
        order?.dropshipName = name
        listener.setValidationDropshipName(name, isNameValid, bindingAdapterPosition)
    }

    override fun setDropshipPhone(phone: String) {
        val isPhoneValid = phone.isNotEmpty() && phone.length > DROPSHIPPER_MIN_PHONE_LENGTH &&
            phone.length < DROPSHIPPER_MAX_PHONE_LENGTH
        order?.isDropshipPhoneValid = isPhoneValid
        order?.dropshipPhone = phone
        listener.setValidationDropshipPhone(phone, isPhoneValid, bindingAdapterPosition)
    }
}

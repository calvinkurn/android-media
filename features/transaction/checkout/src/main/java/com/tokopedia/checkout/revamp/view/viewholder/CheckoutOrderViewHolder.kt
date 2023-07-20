package com.tokopedia.checkout.revamp.view.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutOrderBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
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

    fun bind(order: CheckoutOrderModel) {
        this.order = order
        renderAddOnOrderLevel(order)
        renderShippingWidget(order)
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

    private fun renderShippingWidget(order: CheckoutOrderModel) {
        binding.shippingWidget.setupListener(this)
        binding.shippingWidget.hideTradeInShippingInfo()

        // prepare load
        if (order.shipment.isLoading) {
            binding.shippingWidget.prepareLoadCourierState()
            binding.shippingWidget.renderLoadingCourierState()
        } else {
            val courierItemData = order.shipment.courierItemData
            if (courierItemData == null) {
                binding.shippingWidget.prepareLoadCourierState()
                binding.shippingWidget.hideShippingStateLoading()
                binding.shippingWidget.showLayoutNoSelectedShipping(RecipientAddressModel())
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
        TODO("Not yet implemented")
    }

    override fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel) {
        TODO("Not yet implemented")
    }

    override fun getHostFragmentManager(): FragmentManager {
        TODO("Not yet implemented")
    }
}

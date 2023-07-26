package com.tokopedia.checkout.revamp.view.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutOrderBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingWidget
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel

class CheckoutOrderViewHolder(private val binding: ItemCheckoutOrderBinding, private val listener: CheckoutAdapterListener) : RecyclerView.ViewHolder(binding.root),
    ShippingWidget.ShippingWidgetListener {

    fun bind(order: CheckoutOrderModel) {
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
                    binding.buttonGiftingAddonOrderLevel.state = com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView.State.ACTIVE
                } else if (statusAddOn == 2) {
                    binding.buttonGiftingAddonOrderLevel.state = com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView.State.INACTIVE
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
        binding.shippingWidget.prepareLoadCourierState()
        binding.shippingWidget.showLayoutNoSelectedShipping(ShipmentCartItemModel(cartStringGroup = ""), RecipientAddressModel())
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_order
    }

    override fun onChangeDurationClickListener(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel
    ) {
        listener.onChangeShippingDuration()
    }

    override fun onChangeCourierClickListener(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel
    ) {
        TODO("Not yet implemented")
    }

    override fun onOnTimeDeliveryClicked(url: String) {
        TODO("Not yet implemented")
    }

    override fun onClickSetPinpoint() {
        TODO("Not yet implemented")
    }

    override fun onClickLayoutFailedShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel
    ) {
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

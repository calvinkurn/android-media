package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ShipmentDonationViewHolder(itemView: View, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(itemView) {
    private val cbDonation: CheckboxUnify = itemView.findViewById(R.id.cb_donation)
    private val tvDonationTitle: TextView = itemView.findViewById(R.id.tv_donation_title)
    private val imgDonationInfo: IconUnify = itemView.findViewById(R.id.img_donation_info)
    private val llContainer: ViewGroup = itemView.findViewById(R.id.ll_container)

    @SuppressLint("ClickableViewAccessibility", "NewApi")
    fun bindViewHolder(shipmentDonationModel: ShipmentDonationModel) {
        llContainer.setOnClickListener {
            if (shipmentDonationModel.isEnabled) {
                cbDonation.isChecked = !cbDonation.isChecked
            }
        }
        cbDonation.isChecked = shipmentDonationModel.isChecked
        cbDonation.skipAnimation()
        tvDonationTitle.text = shipmentDonationModel.donation.title
        imgDonationInfo.setOnClickListener {
            if (shipmentDonationModel.isEnabled) {
                showBottomSheet(shipmentDonationModel)
            }
        }
        cbDonation.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (shipmentDonationModel.isEnabled) {
                shipmentAdapterActionListener.onDonationChecked(isChecked)
            }
        }
        if (shipmentDonationModel.isEnabled) {
            cbDonation.isEnabled = true
            llContainer.foreground = ContextCompat.getDrawable(llContainer.context, com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item)
        } else {
            cbDonation.isEnabled = false
            llContainer.foreground = ContextCompat.getDrawable(llContainer.context, com.tokopedia.purchase_platform.common.R.drawable.fg_disabled_item)
        }
    }

    private fun showBottomSheet(shipmentDonationModel: ShipmentDonationModel) {
        GeneralBottomSheet().apply {
            setTitle(shipmentDonationModel.donation.title)
            setDesc(shipmentDonationModel.donation.description)
            setButtonText(llContainer.context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
            setIcon(R.drawable.checkout_module_ic_donation)
            setButtonOnClickListener { it.dismiss() }
        }.show(llContainer.context, shipmentAdapterActionListener.currentFragmentManager)
    }

    companion object {
        @JvmField
        val ITEM_VIEW_DONATION = R.layout.item_donation
    }

}
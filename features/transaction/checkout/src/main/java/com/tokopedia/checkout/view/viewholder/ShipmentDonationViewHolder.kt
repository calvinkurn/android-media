package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemDonationBinding
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet

class ShipmentDonationViewHolder(private val binding: ItemDonationBinding, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ClickableViewAccessibility", "NewApi")
    fun bindViewHolder(shipmentDonationModel: ShipmentDonationModel) {
        binding.llContainer.setOnClickListener {
            if (shipmentDonationModel.isEnabled) {
                binding.cbDonation.isChecked = !binding.cbDonation.isChecked
            }
        }
        binding.cbDonation.isChecked = shipmentDonationModel.isChecked
        binding.cbDonation.skipAnimation()
        binding.tvDonationTitle.text = shipmentDonationModel.donation.title
        binding.imgDonationInfo.setOnClickListener {
            if (shipmentDonationModel.isEnabled) {
                showBottomSheet(shipmentDonationModel)
            }
        }
        binding.cbDonation.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (shipmentDonationModel.isEnabled) {
                shipmentAdapterActionListener.onDonationChecked(isChecked)
            }
        }
        if (shipmentDonationModel.isEnabled) {
            binding.cbDonation.isEnabled = true
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                binding.llContainer.foreground = ContextCompat.getDrawable(binding.root.context, com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item)
            }
        } else {
            binding.cbDonation.isEnabled = false
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                binding.llContainer.foreground = ContextCompat.getDrawable(binding.root.context, com.tokopedia.purchase_platform.common.R.drawable.fg_disabled_item)
            }
        }
    }

    private fun showBottomSheet(shipmentDonationModel: ShipmentDonationModel) {
        GeneralBottomSheet().apply {
            setTitle(shipmentDonationModel.donation.title)
            setDesc(shipmentDonationModel.donation.description)
            setButtonText(binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
            setIcon(R.drawable.checkout_module_ic_donation)
            setButtonOnClickListener { it.dismiss() }
        }.show(binding.root.context, shipmentAdapterActionListener.currentFragmentManager)
    }

    companion object {
        @JvmField
        val ITEM_VIEW_DONATION = R.layout.item_donation
    }
}

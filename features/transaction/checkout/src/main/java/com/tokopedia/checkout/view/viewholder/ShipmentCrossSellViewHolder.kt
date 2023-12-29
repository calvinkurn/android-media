package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCrossSellBinding
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet

class ShipmentCrossSellViewHolder(private val binding: ItemCrossSellBinding, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ClickableViewAccessibility", "NewApi")
    fun bindViewHolder(shipmentCrossSellModel: ShipmentCrossSellModel) {
        binding.llContainer.setOnClickListener {
            if (shipmentCrossSellModel.isEnabled) {
                binding.cbCrossSell.isChecked = !binding.cbCrossSell.isChecked
            }
        }
        binding.cbCrossSell.isChecked = shipmentCrossSellModel.isChecked
        binding.cbCrossSell.skipAnimation()
        binding.tvCrossSellTitle.text = MethodChecker.fromHtml(shipmentCrossSellModel.crossSellModel.info.title)
        binding.tvCrossSellDesc.text = MethodChecker.fromHtml(shipmentCrossSellModel.crossSellModel.info.subtitle)
        binding.imgCrossSellInfo.setOnClickListener {
            if (shipmentCrossSellModel.isEnabled) {
                showBottomSheet(shipmentCrossSellModel)
            }
        }

        if (shipmentCrossSellModel.isEnabled) {
            binding.cbCrossSell.isEnabled = true
            binding.llContainer.alpha = 1.0f
            binding.cbCrossSell.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                shipmentAdapterActionListener.onCrossSellItemChecked(isChecked, shipmentCrossSellModel.crossSellModel, shipmentCrossSellModel.index)
            }
        } else {
            binding.cbCrossSell.gone()
            binding.llContainer.alpha = 0.5f
        }
    }

    private fun showBottomSheet(shipmentCrossSellModel: ShipmentCrossSellModel) {
        GeneralBottomSheet().apply {
            setTitle(MethodChecker.fromHtml(shipmentCrossSellModel.crossSellModel.bottomSheet.title).toString())
            setDesc(MethodChecker.fromHtml(shipmentCrossSellModel.crossSellModel.bottomSheet.subtitle).toString())
            setButtonText(binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
            setButtonOnClickListener { it.dismiss() }
        }.show(binding.root.context, shipmentAdapterActionListener.currentFragmentManager)
    }

    companion object {
        @JvmField
        val ITEM_VIEW_CROSS_SELL = R.layout.item_cross_sell
    }
}

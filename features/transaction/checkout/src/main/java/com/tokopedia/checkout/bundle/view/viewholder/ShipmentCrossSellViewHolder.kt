package com.tokopedia.checkout.bundle.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.R
import com.tokopedia.checkout.bundle.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.bundle.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShipmentCrossSellViewHolder(itemView: View, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(itemView) {
    private val cbCrossSell: CheckboxUnify = itemView.findViewById(R.id.cb_cross_sell)
    private val tvCrossSellTitle: Typography = itemView.findViewById(R.id.tv_cross_sell_title)
    private val tvCrossSellDesc: Typography = itemView.findViewById(R.id.tv_cross_sell_desc)
    private val imgCrossSellInfo: IconUnify = itemView.findViewById(R.id.img_cross_sell_info)
    private val llContainer: ViewGroup = itemView.findViewById(R.id.ll_container)

    @SuppressLint("ClickableViewAccessibility", "NewApi")
    fun bindViewHolder(shipmentCrossSellModel: ShipmentCrossSellModel, index: Int) {
        llContainer.setOnClickListener {
            if (shipmentCrossSellModel.isEnabled) {
                cbCrossSell.isChecked = !cbCrossSell.isChecked
            }
        }
        cbCrossSell.isChecked = shipmentCrossSellModel.isChecked
        cbCrossSell.skipAnimation()
        tvCrossSellTitle.text = MethodChecker.fromHtml(shipmentCrossSellModel.crossSellModel.info.title)
        tvCrossSellDesc.text = MethodChecker.fromHtml(shipmentCrossSellModel.crossSellModel.info.subtitle)
        imgCrossSellInfo.setOnClickListener {
            if (shipmentCrossSellModel.isEnabled) {
                showBottomSheet(shipmentCrossSellModel)
            }
        }

        if (shipmentCrossSellModel.isEnabled) {
            cbCrossSell.isEnabled = true
            llContainer.alpha = 1.0f
            cbCrossSell.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                shipmentAdapterActionListener.onCrossSellItemChecked(isChecked, shipmentCrossSellModel.crossSellModel, index)
            }
        } else {
            cbCrossSell.gone()
            llContainer.alpha = 0.5f
        }
    }

    private fun showBottomSheet(shipmentCrossSellModel: ShipmentCrossSellModel) {
        GeneralBottomSheet().apply {
            setTitle(MethodChecker.fromHtml(shipmentCrossSellModel.crossSellModel.bottomSheet.title).toString())
            setDesc(MethodChecker.fromHtml(shipmentCrossSellModel.crossSellModel.bottomSheet.subtitle).toString())
            setButtonText(llContainer.context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
            setButtonOnClickListener { it.dismiss() }
        }.show(llContainer.context, shipmentAdapterActionListener.currentFragmentManager)
    }

    companion object {
        @JvmField
        val ITEM_VIEW_CROSS_SELL = R.layout.item_cross_sell
    }

}
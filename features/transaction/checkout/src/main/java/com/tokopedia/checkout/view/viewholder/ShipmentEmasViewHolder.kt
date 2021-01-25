package com.tokopedia.checkout.view.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.design.component.Tooltip
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.purchase_platform.common.utils.Utils.removeDecimalSuffix
import com.tokopedia.unifyprinciples.Typography

class ShipmentEmasViewHolder(itemView: View, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(itemView) {
    private val buyEmas: CheckBox = itemView.findViewById(R.id.cb_emas)
    private val tvEmasTitle: Typography = itemView.findViewById(R.id.tv_emas_title)
    private val tvEmasDesc: Typography = itemView.findViewById(R.id.tv_emas_sub_title)
    private val imgEmasInfo: IconUnify = itemView.findViewById(R.id.img_emas_info)
    private val llContainer: LinearLayout = itemView.findViewById(R.id.ll_container)

    fun bindViewHolder(egoldAttributeModel: EgoldAttributeModel) {
        llContainer.setOnClickListener { buyEmas.isChecked = !buyEmas.isChecked }
        buyEmas.isChecked = egoldAttributeModel.isChecked
        tvEmasTitle.text = egoldAttributeModel.titleText
        imgEmasInfo.setOnClickListener { showBottomSheet(egoldAttributeModel) }
        tvEmasDesc.text = MethodChecker.fromHtml(
                String.format(llContainer.context.getString(R.string.emas_checkout_desc),
                        egoldAttributeModel.subText,
                        removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(egoldAttributeModel.buyEgoldValue, false))
                )
        )
        buyEmas.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean -> shipmentAdapterActionListener.onEgoldChecked(isChecked) }
    }

    private fun showBottomSheet(egoldAttributeModel: EgoldAttributeModel) {
        val tooltip = Tooltip(imgEmasInfo.context)
        tooltip.setTitle(egoldAttributeModel.titleText)
        tooltip.setDesc(egoldAttributeModel.tooltipText)
        tooltip.setTextButton(imgEmasInfo.context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
        tooltip.setWithIcon(false)
        tooltip.btnAction.setOnClickListener { v: View? -> tooltip.dismiss() }
        tooltip.show()
    }

    companion object {
        @JvmField
        val ITEM_VIEW_EMAS = R.layout.checkout_holder_item_emas
    }

}
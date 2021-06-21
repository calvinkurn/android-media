package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet
import com.tokopedia.purchase_platform.common.utils.Utils.removeDecimalSuffix
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class ShipmentEmasViewHolder(itemView: View, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(itemView) {
    private val buyEmas: CheckBox = itemView.findViewById(R.id.cb_emas)
    private val tvEmasTitle: Typography = itemView.findViewById(R.id.tv_emas_title)
    private val tvEmasDesc: Typography = itemView.findViewById(R.id.tv_emas_sub_title)
    private val imgEmasInfo: IconUnify = itemView.findViewById(R.id.img_emas_info)
    private val llContainer: LinearLayout = itemView.findViewById(R.id.ll_container)

    @SuppressLint("NewApi")
    fun bindViewHolder(egoldAttributeModel: EgoldAttributeModel) {
        llContainer.setOnClickListener {
            if (egoldAttributeModel.isEnabled) {
                buyEmas.isChecked = !buyEmas.isChecked
            }
        }
        buyEmas.isChecked = egoldAttributeModel.isChecked
        tvEmasTitle.text = egoldAttributeModel.titleText
        imgEmasInfo.setOnClickListener {
            if (egoldAttributeModel.isEnabled) {
                showBottomSheet(egoldAttributeModel)
            }
        }
        tvEmasDesc.text = MethodChecker.fromHtml(
                String.format(llContainer.context.getString(R.string.emas_checkout_desc),
                        egoldAttributeModel.subText,
                        removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(egoldAttributeModel.buyEgoldValue, false))
                )
        )
        buyEmas.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (egoldAttributeModel.isEnabled) {
                shipmentAdapterActionListener.onEgoldChecked(isChecked)
            }
        }
        if (egoldAttributeModel.isEnabled) {
            buyEmas.isEnabled = true
            llContainer.foreground = ContextCompat.getDrawable(llContainer.context, com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item)
        } else {
            buyEmas.isEnabled = false
            llContainer.foreground = ContextCompat.getDrawable(llContainer.context, com.tokopedia.purchase_platform.common.R.drawable.fg_disabled_item)
        }
    }

    private fun showBottomSheet(egoldAttributeModel: EgoldAttributeModel) {
        GeneralBottomSheet().apply {
            setTitle(egoldAttributeModel.titleText ?: "")
            setDesc(egoldAttributeModel.tooltipText ?: "")
            setButtonText(imgEmasInfo.context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
            setButtonOnClickListener { it.dismiss() }
        }.show(llContainer.context, shipmentAdapterActionListener.currentFragmentManager)
    }

    companion object {
        @JvmField
        val ITEM_VIEW_EMAS = R.layout.checkout_holder_item_emas
    }

}
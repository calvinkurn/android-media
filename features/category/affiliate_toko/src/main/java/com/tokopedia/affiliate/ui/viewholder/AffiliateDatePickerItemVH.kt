package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateRangePickerModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateDatePickerItemVH(itemView: View,private val dateClickedInterface: AffiliateDatePickerInterface?)
    : AbstractViewHolder<AffiliateDateRangePickerModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_date_picker_item
    }

    override fun bind(element: AffiliateDateRangePickerModel?) {
        itemView.findViewById<Typography>(R.id.text).text = element?.dateRange?.text
        val radioBtn = itemView.findViewById<RadioButtonUnify>(R.id.textRadioButton)
        radioBtn.isChecked= element?.dateRange?.isSelected == true
        itemView.setOnClickListener {
            radioBtn.isChecked = true
        }
        radioBtn.setOnCheckedChangeListener { _,isChecked ->
            if(isChecked) dateClickedInterface?.onDateRangeClicked(adapterPosition)
        }


    }
}

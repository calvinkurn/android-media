package com.tokopedia.filter.newdynamicfilter.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.CheckBox

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView

open class DynamicFilterDetailViewHolder(itemView: View, private val filterDetailView: DynamicFilterDetailView) : RecyclerView.ViewHolder(itemView) {

    protected var checkBox: CheckBox = itemView.findViewById(R.id.filter_detail_item_checkbox)

    open fun bind(option: Option) {
        itemView.setOnClickListener { checkBox.isChecked = !checkBox.isChecked }
        OptionHelper.bindOptionWithCheckbox(option, checkBox, filterDetailView)
    }
}

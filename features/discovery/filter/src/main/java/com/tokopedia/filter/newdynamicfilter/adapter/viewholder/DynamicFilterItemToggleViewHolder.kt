package com.tokopedia.filter.newdynamicfilter.adapter.viewholder

import androidx.appcompat.widget.SwitchCompat
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterView

class DynamicFilterItemToggleViewHolder(itemView: View, private val dynamicFilterView: DynamicFilterView) : DynamicFilterViewHolder(itemView) {

    private val title: TextView? = itemView.findViewById(R.id.title)
    private val toggle: SwitchCompat? = itemView.findViewById(R.id.toggle)

    override fun bind(filter: Filter) {
        val option = filter.getOptions()[0]
        title?.text = option.name

        itemView.setOnClickListener { toggle?.isChecked = toggle?.isChecked != true }

        bindSwitchForOption(option)
    }

    private fun bindSwitchForOption(option: Option) {
        val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked -> dynamicFilterView.saveCheckedState(option, isChecked) }

        toggle?.let {
            bindSwitch(it,
                dynamicFilterView.loadLastCheckedState(option),
                onCheckedChangeListener)
        }
    }
}

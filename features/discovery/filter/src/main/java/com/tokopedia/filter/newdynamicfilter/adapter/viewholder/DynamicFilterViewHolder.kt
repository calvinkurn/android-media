package com.tokopedia.filter.newdynamicfilter.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SwitchCompat
import android.view.View
import android.widget.CompoundButton

import com.tokopedia.filter.common.data.Filter

abstract class DynamicFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal fun bindSwitch(switchView: SwitchCompat,
                            isChecked: Boolean,
                            onCheckedChangeListener: CompoundButton.OnCheckedChangeListener) {

        switchView.setOnCheckedChangeListener(null)

        switchView.isChecked = true.equals(isChecked)

        switchView.setOnCheckedChangeListener(onCheckedChangeListener)
    }

    abstract fun bind(filter: Filter)
}

package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.view.interfaces.SelectedFilterListener
import com.tokopedia.filter.common.data.Option
import com.tokopedia.unifyprinciples.Typography

class SelectedFilterItemViewHolder(itemView: View, private val clickListener: SelectedFilterListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(option: Option) {
        val filterText = itemView.findViewById<Typography>(R.id.filter_text)
        filterText.text = option.name
        filterText.setOnClickListener { clickListener.onSelectedFilterRemoved(option.uniqueId) }
    }
}
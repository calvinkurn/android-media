package com.tokopedia.category_levels.adapters.viewholder.catalogShimmer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.category_levels.R
import com.tokopedia.category_levels.view.interfaces.SelectedFilterListener
import com.tokopedia.filter.common.data.Option
import com.tokopedia.unifyprinciples.Typography

class SelectedFilterItemViewHolder(itemView: View, private val clickListener: SelectedFilterListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(option: Option) {
        val filterText = itemView.findViewById<Typography>(R.id.filter_text)
        filterText.text = option.name
        filterText.setOnClickListener { clickListener.onSelectedFilterRemoved(option.uniqueId) }
    }
}
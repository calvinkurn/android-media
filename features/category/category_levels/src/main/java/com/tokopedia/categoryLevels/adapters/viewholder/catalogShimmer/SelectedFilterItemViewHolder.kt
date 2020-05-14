package com.tokopedia.categoryLevels.adapters.viewholder.catalogShimmer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.categoryLevels.R
import com.tokopedia.categoryLevels.view.interfaces.SelectedFilterListener
import com.tokopedia.filter.common.data.Option
import com.tokopedia.unifyprinciples.Typography

class SelectedFilterItemViewHolder(itemView: View, private val clickListener: SelectedFilterListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(option: Option) {
        val filterText = itemView.findViewById<Typography>(R.id.filter_text)
        filterText.text = option.name
        filterText.setOnClickListener { clickListener.onSelectedFilterRemoved(option.uniqueId) }
    }
}
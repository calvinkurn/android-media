package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.ChipsUnify

class TopAdsTabInsightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val chip: ChipsUnify = itemView.findViewById(R.id.tabInsightId)

    fun bind(title: String) {
        chip.chipText = title
    }

    fun toggleActivate(isActive: Boolean) {
        if (isActive) {
            chip.chipType = ChipsUnify.TYPE_SELECTED

        } else {
            chip.chipType = ChipsUnify.TYPE_NORMAL

        }
    }
}

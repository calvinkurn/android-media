package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.topads_dash_item_insight_tab_layout.view.*

class TopAdsTabInsightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val chip: ChipsUnify = itemView.tabInsightId

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

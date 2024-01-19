package com.tokopedia.deals.ui.search.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.ui.search.listener.DealsSearchListener
import com.tokopedia.deals.ui.search.model.response.Item
import com.tokopedia.unifycomponents.ChipsUnify

class LastSeenViewHolder(itemView: View, private val dealsSearchListener: DealsSearchListener) : RecyclerView.ViewHolder(itemView) {

    private val chip: ChipsUnify = itemView.findViewById(R.id.chip_green_item)

    fun bindData(item: Item) {
        chip.chip_text.text = item.title
        setOnClickListener(item)
    }

    private fun setOnClickListener(item: Item) {
        dealsSearchListener.onLastSeenClicked(itemView, item)
    }
}

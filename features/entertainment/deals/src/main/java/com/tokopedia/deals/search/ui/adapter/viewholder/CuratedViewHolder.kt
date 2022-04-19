package com.tokopedia.deals.search.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.deals.R
import com.tokopedia.deals.search.listener.DealsSearchListener
import com.tokopedia.deals.search.model.response.Category
import com.tokopedia.unifycomponents.ChipsUnify

class CuratedViewHolder(itemView: View, private val dealsSearchListener: DealsSearchListener): RecyclerView.ViewHolder(itemView) {

    private val chip: ChipsUnify = itemView.findViewById(R.id.chip_green_item)

    fun bindData(item: Category){
        chip.chip_text.text = item.title
        setOnClickListener(item)
    }

    private fun setOnClickListener(item: Category) {
        dealsSearchListener.onCuratedChipClicked(itemView, item)
    }
}
package com.tokopedia.filter.newdynamicfilter.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.adapter.BottomSheetExpandableItemSelectedListAdapter
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetDynamicFilterView

class BottomSheetExpandableItemViewHolder(itemView: View, private val filterView: BottomSheetDynamicFilterView) : DynamicFilterViewHolder(itemView) {

    private val titleContainer: LinearLayout = itemView.findViewById(R.id.title_container)
    private val title: TextView = itemView.findViewById(R.id.expandable_item_title)
    private val seeAllButton: View = itemView.findViewById(R.id.see_all_button)
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.expandable_item_selected_list)
    private var adapter: BottomSheetExpandableItemSelectedListAdapter? = null

    override fun bind(filter: Filter) {
        adapter = BottomSheetExpandableItemSelectedListAdapter(filterView, filter.title)
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        title.text = filter.title

        if (hasCustomOptions(filter)) {
            titleContainer.setOnClickListener { filterView.onExpandableItemClicked(filter) }
            seeAllButton.visibility = View.VISIBLE
        } else {
            titleContainer.setOnClickListener(null)
            seeAllButton.visibility = View.GONE
        }

        adapter?.setSelectedOptionsList(filterView.getSelectedOptions(filter))
    }

    private fun hasCustomOptions(filter: Filter): Boolean {
        for (option in filter.getOptions()) {
            if (!option.isPopular) {
                return true
            }
        }
        return false
    }
}

package com.tokopedia.filter.newdynamicfilter.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.adapter.ExpandableItemSelectedListAdapter
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterView

/**
 * Created by henrypriyono on 8/11/17.
 */

class DynamicFilterExpandableItemViewHolder(itemView: View, private var filterView: DynamicFilterView) : DynamicFilterViewHolder(itemView) {

    private var titleContainer: LinearLayout = itemView.findViewById(R.id.title_container)
    private var title: TextView = itemView.findViewById(R.id.expandable_item_title)
    private var recyclerView: RecyclerView = itemView.findViewById(R.id.expandable_item_selected_list)
    private lateinit var adapter: ExpandableItemSelectedListAdapter

    override fun bind(filter: Filter) {
        adapter = ExpandableItemSelectedListAdapter(filterView)
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        title.text = filter.title
        titleContainer.setOnClickListener { filterView.onExpandableItemClicked(filter) }

        adapter.setSelectedOptionsList(filterView.getSelectedOptions(filter))
    }
}

package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailInsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.InsightListAdapter

class GroupDetailInsightListViewHolder(private val view1: View) :
    AbstractViewHolder<GroupDetailInsightListUiModel>(view1) {
    private val insightListAdapter = InsightListAdapter { _, _ -> }
    private var groupCardRecyclerView: RecyclerView = view1.findViewById(R.id.groupCardRecyclerView)
    private val layoutManager by lazy {
        LinearLayoutManager(
            view1.context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    override fun bind(element: GroupDetailInsightListUiModel?) {

        groupCardRecyclerView.layoutManager = layoutManager
        groupCardRecyclerView.adapter = insightListAdapter
        insightListAdapter.submitList(element?.adGroups)
    }

    companion object {
        val LAYOUT = R.layout.topads_group_detail_insight_list_layout
    }
}

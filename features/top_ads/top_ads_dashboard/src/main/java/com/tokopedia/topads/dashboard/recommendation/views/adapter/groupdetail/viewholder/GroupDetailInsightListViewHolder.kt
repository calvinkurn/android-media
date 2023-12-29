package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.decoration.RecommendationInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailInsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.InsightListAdapter
class GroupDetailInsightListViewHolder(
    private val view: View,
    private val onInsightItemClick: (list: ArrayList<AdGroupUiModel>, item: AdGroupUiModel) -> Unit
) :
    AbstractViewHolder<GroupDetailInsightListUiModel>(view) {
    private val insightListAdapter = InsightListAdapter(onInsightItemClick)
    private var groupCardRecyclerView: RecyclerView = view.findViewById(R.id.groupCardRecyclerView)
    private val layoutManager by lazy {
        LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    override fun bind(element: GroupDetailInsightListUiModel?) {
        val itemDecoration = RecommendationInsightItemDecoration(
            view.context,
            LinearLayoutManager.VERTICAL
        )
        groupCardRecyclerView.addItemDecoration(itemDecoration)
        groupCardRecyclerView.layoutManager = layoutManager
        groupCardRecyclerView.adapter = insightListAdapter
        insightListAdapter.submitList(element?.adGroups)
    }

    companion object {
        val LAYOUT = R.layout.topads_group_detail_insight_list_layout
    }
}

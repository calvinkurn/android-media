package com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.EMPTY_GROUP_LIST_IMG_URL
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.FAILED_LIST_STATE_IMG_URL
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.INVALID_GROUP_TYPE
import com.tokopedia.topads.dashboard.recommendation.data.model.local.*
import com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders.EmptyStateViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders.FailedStateViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders.GroupListItemViewHolder

class GroupListAdapter(private val onItemCheckedChangeListener: (String) -> Unit, private val reloadPage: (() -> Unit)?) :
    ListAdapter<GroupListUiModel, RecyclerView.ViewHolder>(GroupListDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.topads_insight_centre_group_list_item_layout -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_insight_centre_group_list_item_layout, parent, false)
                GroupListItemViewHolder(view)
            }
            R.layout.topads_empty_state_layout -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_empty_state_layout, parent, false)
                EmptyStateViewHolder(view)
            }
            R.layout.topads_insight_centre_group_item_loading_layout -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_insight_centre_group_item_loading_layout, parent, false)
                object : RecyclerView.ViewHolder(view) {}
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_failed_state_layout, parent, false)
                FailedStateViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is GroupItemUiModel -> {
                (holder as? GroupListItemViewHolder)?.bind(item, onItemCheckedChangeListener)
            }
            is EmptyGroupListUiModel -> {
                (holder as? EmptyStateViewHolder)?.bind(EMPTY_GROUP_LIST_IMG_URL)
            }
            is FailedGroupListUiModel -> {
                (holder as? FailedStateViewHolder)?.bind(FAILED_LIST_STATE_IMG_URL,reloadPage)
            }
            is LoadingGroupsUiModel -> { /*empty shimmer view holder, thus no need for callback to bind() */}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GroupItemUiModel -> R.layout.topads_insight_centre_group_list_item_layout
            is EmptyGroupListUiModel -> R.layout.topads_empty_state_layout
            is FailedGroupListUiModel -> R.layout.topads_failed_state_layout
            is LoadingGroupsUiModel -> R.layout.topads_insight_centre_group_item_loading_layout
            else -> throw IllegalArgumentException(INVALID_GROUP_TYPE)
        }
    }
}

class GroupListDiffUtilCallBack : DiffUtil.ItemCallback<GroupListUiModel>() {
    override fun areItemsTheSame(
        oldItem: GroupListUiModel,
        newItem: GroupListUiModel
    ): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(
        oldItem: GroupListUiModel,
        newItem: GroupListUiModel
    ): Boolean {
        return oldItem.equalsWith(newItem)
    }

}

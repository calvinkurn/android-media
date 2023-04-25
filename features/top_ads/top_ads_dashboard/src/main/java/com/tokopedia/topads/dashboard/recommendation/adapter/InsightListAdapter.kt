package com.tokopedia.topads.dashboard.recommendation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.LoadingUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.LoaderUnify

class InsightListAdapter :
    ListAdapter<InsightListUiModel, RecyclerView.ViewHolder>(InsightListDiffUtilCallBack()) {

    inner class InsightListItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chipsUnify: ChipsUnify = view.findViewById(R.id.topAdsLayoutChips)
        fun bind(item: AdGroupUiModel) {
            chipsUnify.chipText = item.adGroupID
        }
    }

    inner class LoadingItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val loader: LoaderUnify = view.findViewById(R.id.bottomLoader)
        fun bind(item: LoadingUiModel) {
            loader.showWithCondition(item.isLoading)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.topads_layout) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.topads_layout, parent, false)
            InsightListItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.insight_bottom_loading_layout, parent, false)
            LoadingItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item is AdGroupUiModel) {
            (holder as? InsightListItemViewHolder)?.bind(item)
        } else if (item is LoadingUiModel) {
            (holder as? LoadingItemViewHolder)?.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AdGroupUiModel -> R.layout.topads_layout
            is LoadingUiModel -> R.layout.insight_bottom_loading_layout
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

}

class InsightListDiffUtilCallBack : DiffUtil.ItemCallback<InsightListUiModel>() {
    override fun areItemsTheSame(
        oldItem: InsightListUiModel,
        newItem: InsightListUiModel
    ): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(
        oldItem: InsightListUiModel,
        newItem: InsightListUiModel
    ): Boolean {
        return oldItem.equalsWith(newItem)
    }
}

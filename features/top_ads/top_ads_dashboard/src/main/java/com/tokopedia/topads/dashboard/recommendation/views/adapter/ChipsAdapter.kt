package com.tokopedia.topads.dashboard.recommendation.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.SaranTopAdsChipsUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class ChipsAdapter(private val onChipClick: (List<SaranTopAdsChipsUiModel>, Int) -> Unit) :
    ListAdapter<SaranTopAdsChipsUiModel, ChipsAdapter.TopAdsViewHolder>(MyDiffCallback()) {

    inner class TopAdsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chipsUnify: ChipsUnify = view.findViewById(R.id.topAdsLayoutChips)
        fun bind(
            item: SaranTopAdsChipsUiModel,
            currentList: MutableList<SaranTopAdsChipsUiModel>,
            position: Int
        ) {
            chipsUnify.chipText = item.name
            chipsUnify.chipType =
                if (item.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
            chipsUnify.setOnClickListener {
                onChipClick.invoke(currentList, position)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.topads_insight_chips_item_layout, parent, false)
        return TopAdsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopAdsViewHolder, position: Int) {
        holder.bind(getItem(position), currentList, position)
    }

    fun getSelectedChips():Int {
        return currentList.indexOfFirst { it.isSelected }
    }

}

class MyDiffCallback : DiffUtil.ItemCallback<SaranTopAdsChipsUiModel>() {
    override fun areItemsTheSame(
        oldItem: SaranTopAdsChipsUiModel,
        newItem: SaranTopAdsChipsUiModel
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: SaranTopAdsChipsUiModel,
        newItem: SaranTopAdsChipsUiModel
    ): Boolean {
        return oldItem.isSelected == newItem.isSelected
    }
}

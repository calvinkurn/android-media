package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.ChipsData.chipsList
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsItemUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class GroupDetailsChipsAdapter(
    private val onChipsClick: (Int) -> Unit,
) :
    RecyclerView.Adapter<GroupDetailsChipsAdapter.ChipsViewHolder>() {

    inner class ChipsViewHolder(
        val itemView: View,
        private val onChipsClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val chips: ChipsUnify = itemView.findViewById(R.id.topAdsLayoutChips)
        fun bind(groupDetailChipsItemUiModel: GroupDetailChipsItemUiModel, position: Int) {
            chips.chipText = groupDetailChipsItemUiModel.chipsTitle
            chips.chipType =
                if (groupDetailChipsItemUiModel.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
            itemView.setOnClickListener {
                doOperation(groupDetailChipsItemUiModel)
                onChipsClick(position)
            }
        }

        private fun doOperation(groupDetailChipsItemUiModel: GroupDetailChipsItemUiModel) {
            chipsList.forEach {
                it.isSelected = it.chipsTitle == groupDetailChipsItemUiModel.chipsTitle
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipsViewHolder {
        return ChipsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.topads_insight_chips_item_layout,
                parent,
                false
            ), onChipsClick
        )
    }

    override fun onBindViewHolder(holder: ChipsViewHolder, position: Int) {
        holder.bind(chipsList[position], position)
    }

    override fun getItemCount(): Int {
        return chipsList.count()
    }

}

package com.tokopedia.home_component.viewholders.mission.v3

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.home_component.visitable.Mission4SquareUiModel

class MissionWidgetAdapter(
    private val listener: Mission4SquareWidgetListener? = null
) : ListAdapter<Mission4SquareUiModel, MissionWidgetCardViewHolder>(
    MissionWidgetDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionWidgetCardViewHolder {
        return MissionWidgetCardViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: MissionWidgetCardViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size
}

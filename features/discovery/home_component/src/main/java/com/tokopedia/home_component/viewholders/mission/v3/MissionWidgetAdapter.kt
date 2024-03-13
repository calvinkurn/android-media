package com.tokopedia.home_component.viewholders.mission.v3

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.home_component.visitable.MissionWidgetDataModel

class MissionWidgetAdapter : ListAdapter<MissionWidgetDataModel, MissionWidgetCardViewHolder>(
    MissionWidgetDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionWidgetCardViewHolder {
        return MissionWidgetCardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MissionWidgetCardViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size
}

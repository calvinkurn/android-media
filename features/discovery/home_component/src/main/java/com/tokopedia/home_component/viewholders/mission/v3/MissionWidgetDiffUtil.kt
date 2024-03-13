package com.tokopedia.home_component.viewholders.mission.v3

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.home_component.visitable.MissionWidgetDataModel

class MissionWidgetDiffUtil : DiffUtil.ItemCallback<MissionWidgetDataModel>() {

    override fun areItemsTheSame(
        oldItem: MissionWidgetDataModel,
        newItem: MissionWidgetDataModel
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: MissionWidgetDataModel,
        newItem: MissionWidgetDataModel
    ) = oldItem == newItem
}


package com.tokopedia.home_component.viewholders.mission.v3

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.home_component.visitable.Mission4SquareUiModel

class MissionWidgetDiffUtil : DiffUtil.ItemCallback<Mission4SquareUiModel>() {

    override fun areItemsTheSame(
        oldItem: Mission4SquareUiModel,
        newItem: Mission4SquareUiModel
    ) = oldItem.data.id == newItem.data.id

    override fun areContentsTheSame(
        oldItem: Mission4SquareUiModel,
        newItem: Mission4SquareUiModel
    ) = oldItem == newItem
}


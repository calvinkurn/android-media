package com.tokopedia.home_component.widget.mission

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by frenzel
 */
internal open class MissionWidgetDiffUtil: DiffUtil.ItemCallback<MissionWidgetVisitable>() {

    override fun areItemsTheSame(
        oldItem: MissionWidgetVisitable,
        newItem: MissionWidgetVisitable
    ): Boolean = oldItem.getId() == newItem.getId()

    override fun areContentsTheSame(
        oldItem: MissionWidgetVisitable,
        newItem: MissionWidgetVisitable
    ): Boolean = oldItem.equalsWith(newItem)

    override fun getChangePayload(
        oldItem: MissionWidgetVisitable,
        newItem: MissionWidgetVisitable
    ): Any? {
        return oldItem.getChangePayloadFrom(newItem)
    }
}

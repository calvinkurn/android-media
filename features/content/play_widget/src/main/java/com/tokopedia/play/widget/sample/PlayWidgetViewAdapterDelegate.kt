package com.tokopedia.play.widget.sample

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 10/10/20
 */
class PlayWidgetViewAdapterDelegate(
        coordinatorMap: Map<PlayWidgetCoordinator, PlayWidgetViewHolder?>
) : TypedAdapterDelegate<PlayWidgetUiModel, PlayWidgetUiModel, PlayWidgetViewHolder>(PlayWidgetViewHolder.layout) {

    private val mCoordinatorMap = coordinatorMap.toMutableMap()

    override fun onBindViewHolder(item: PlayWidgetUiModel, holder: PlayWidgetViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetViewHolder {
        val coordinator = mCoordinatorMap.entries.firstOrNull { it.value == null }?.key ?: throw IllegalStateException("No valid coordinator")
        val vh = PlayWidgetViewHolder(basicView, coordinator)
        mCoordinatorMap[coordinator] = vh
        return vh
    }
}
package com.tokopedia.play.ui.engagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.ui.engagement.viewholder.EngagementWidgetViewHolder
import com.tokopedia.play_common.databinding.ViewGameInteractiveBinding
import com.tokopedia.play_common.view.game.GameSmallWidgetView

/**
 * @author by astidhiyaa on 24/08/22
 */
class EngagementWidgetAdapter(listener: EngagementWidgetViewHolder.Listener) :
    BaseDiffUtilAdapter<EngagementUiModel>() {

    init {
        delegatesManager.addDelegate(EngagementWidgetAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: EngagementUiModel, newItem: EngagementUiModel): Boolean {
        return if (oldItem is EngagementUiModel.Game && newItem is EngagementUiModel.Game) oldItem.interactive.id == newItem.interactive.id
        else if (oldItem is EngagementUiModel.Promo && newItem is EngagementUiModel.Promo) oldItem.info.id == newItem.info.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: EngagementUiModel,
        newItem: EngagementUiModel
    ): Boolean {
        return oldItem == newItem
    }

    internal class EngagementWidgetAdapterDelegate(private val listener: EngagementWidgetViewHolder.Listener) :
        TypedAdapterDelegate<EngagementUiModel, EngagementUiModel, EngagementWidgetViewHolder>(
            commonR.layout.view_play_empty
        ) {

        override fun onBindViewHolder(
            item: EngagementUiModel,
            holder: EngagementWidgetViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): EngagementWidgetViewHolder {
            val view = GameSmallWidgetView(parent.context)
            return EngagementWidgetViewHolder(view, listener)
        }
    }
}
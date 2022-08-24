package com.tokopedia.play.ui.engagement.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.ui.engagement.viewholder.EngagementWidgetViewHolder
import com.tokopedia.play_common.view.game.GameSmallWidgetView
import com.tokopedia.play_common.R as commonR

/**
 * @author by astidhiyaa on 24/08/22
 */
class EngagementWidgetAdapter(listener: EngagementWidgetViewHolder.Listener) :
    BaseDiffUtilAdapter<EngagementUiModel>() {

    init {
        delegatesManager.addDelegate(GameDelegate(listener))
        delegatesManager.addDelegate(PromoDelegate(listener))
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

    internal class GameDelegate(private val listener: EngagementWidgetViewHolder.Listener) :
        TypedAdapterDelegate<EngagementUiModel.Game, EngagementUiModel, EngagementWidgetViewHolder>(
            commonR.layout.view_play_empty
        ) {

        override fun onBindViewHolder(
            item: EngagementUiModel.Game,
            holder: EngagementWidgetViewHolder
        ) {
            holder.bindGame(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): EngagementWidgetViewHolder {
            val view = GameSmallWidgetView(parent.context)
            return EngagementWidgetViewHolder(view, listener)
        }
    }

    internal class PromoDelegate(private val listener: EngagementWidgetViewHolder.Listener) :
        TypedAdapterDelegate<EngagementUiModel.Promo, EngagementUiModel, EngagementWidgetViewHolder>(
            commonR.layout.view_play_empty
        ) {

        override fun onBindViewHolder(
            item: EngagementUiModel.Promo,
            holder: EngagementWidgetViewHolder
        ) {
            holder.bindPromo(item)
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
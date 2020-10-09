package com.tokopedia.play.widget.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumOverlayAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumAdapter(
        private val listener: CardMediumListener
) : BaseDiffUtilAdapter<PlayWidgetMediumItemUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayWidgetCardMediumOverlayAdapterDelegate(listener))
                .addDelegate(PlayWidgetCardMediumChannelAdapterDelegate(listener))
                .addDelegate(PlayWidgetCardMediumBannerAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: PlayWidgetMediumItemUiModel, newItem: PlayWidgetMediumItemUiModel): Boolean {
        return if (oldItem is PlayWidgetMediumChannelUiModel && newItem is PlayWidgetMediumChannelUiModel) oldItem.channelId == newItem.channelId
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayWidgetMediumItemUiModel, newItem: PlayWidgetMediumItemUiModel): Boolean {
        return oldItem == newItem
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is PlayWidgetCardMediumChannelViewHolder
                && (holder.getChannelType() == PlayWidgetChannelType.Live
                        || holder.getChannelType() == PlayWidgetChannelType.Vod)) {
            listener.onCardAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is PlayWidgetCardMediumChannelViewHolder
                && (holder.getChannelType() == PlayWidgetChannelType.Live
                        || holder.getChannelType() == PlayWidgetChannelType.Vod)) {
            listener.onCardDetachedFromWindow(holder)
        }
    }

    interface CardMediumListener {
        fun onCardClicked(item: PlayWidgetMediumItemUiModel, position: Int)
        fun onCardVisible(item: PlayWidgetMediumItemUiModel, position: Int)
        fun onCardAttachedToWindow(card: PlayWidgetCardMediumChannelViewHolder)
        fun onCardDetachedFromWindow(card: PlayWidgetCardMediumChannelViewHolder)
    }
}
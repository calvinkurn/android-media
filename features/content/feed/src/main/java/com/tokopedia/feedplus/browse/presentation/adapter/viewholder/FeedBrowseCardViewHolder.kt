package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardBinding
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.widget.PlayWidgetCardView

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseCardViewHolder(
    binding: ItemFeedBrowseCardBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    private val playWidget = binding.root

    private val playWidgetListener = object : PlayWidgetCardView.Listener {
        override fun onCardClicked(view: PlayWidgetCardView, item: PlayWidgetChannelUiModel) {
            listener.onCardClicked(item, bindingAdapterPosition)
        }
    }

    init {
        playWidget.setListener(playWidgetListener)
    }

    fun bind(item: PlayWidgetChannelUiModel) {
        playWidget.setData(item)
        playWidget.addOnImpressionListener(item.impressHolder) {
            listener.onCardImpressed(item, bindingAdapterPosition)
        }
    }

    interface Listener {
        fun onCardImpressed(
            item: PlayWidgetChannelUiModel,
            position: Int
        )
        fun onCardClicked(
            item: PlayWidgetChannelUiModel,
            position: Int
        )
    }
}

package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.widget.PlayWidgetCardView

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseCardViewHolder(
    binding: ItemFeedBrowseCardBinding,
    listener: PlayWidgetCardView.Listener
): RecyclerView.ViewHolder(binding.root) {

    private val playWidget = binding.root

    init {
        playWidget.setListener(listener)
    }

    fun bind(item: PlayWidgetChannelUiModel) {
        playWidget.setData(item)
    }
}

package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseCardViewHolder(
    binding: ItemFeedBrowseCardBinding
): RecyclerView.ViewHolder(binding.root) {

    private val playWidget = binding.root

    fun bind(item: PlayWidgetChannelUiModel) {
        playWidget.setData(item)
    }
}

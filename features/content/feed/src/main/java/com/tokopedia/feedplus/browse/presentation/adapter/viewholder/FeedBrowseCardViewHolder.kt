package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowsePayloads
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChannelLoadingBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.widget.PlayWidgetCardView
import com.tokopedia.play_common.util.ImpressionListener
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.play_common.util.removeImpressionListener

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class FeedBrowseChannelViewHolder private constructor() {

    internal class Channel private constructor(
        binding: ItemFeedBrowseCardBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val playWidget = binding.root

        private val playWidgetListener = object : PlayWidgetCardView.Listener {
            override fun onCardClicked(view: PlayWidgetCardView, item: PlayWidgetChannelUiModel) {
                listener.onCardClicked(item, absoluteAdapterPosition)
            }
        }

        private var mImpressionListener: ImpressionListener? = null

        init {
            playWidget.setListener(playWidgetListener)
        }

        fun bind(item: PlayWidgetChannelUiModel) {
            mImpressionListener?.let { playWidget.removeImpressionListener(it) }
            mImpressionListener = playWidget.addImpressionListener {
                listener.onCardImpressed(item, absoluteAdapterPosition)
            }
            playWidget.setData(item)
        }

        fun bindPayloads(item: PlayWidgetChannelUiModel, payloads: FeedBrowsePayloads) {
            playWidget.setData(item)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener
            ): Channel {
                return Channel(
                    ItemFeedBrowseCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    listener
                )
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

    internal class Loading private constructor(
        binding: ItemFeedBrowseChannelLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): Loading {
                return Loading(
                    ItemFeedBrowseChannelLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}

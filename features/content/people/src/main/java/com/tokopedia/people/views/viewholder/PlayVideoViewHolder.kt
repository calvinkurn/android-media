package com.tokopedia.people.views.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.people.databinding.UpItemUserPostChannelBinding
import com.tokopedia.people.databinding.UpItemUserPostLoadingBinding
import com.tokopedia.people.databinding.UpItemUserPostTranscodeBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.widget.large.PlayWidgetCardLargeChannelView
import com.tokopedia.play.widget.ui.widget.large.PlayWidgetCardLargeTranscodeView

/**
 * Created By : Jonathan Darwin on February 10, 2023
 */
class PlayVideoViewHolder private constructor() {

    class Channel(
        private val binding: UpItemUserPostChannelBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlayWidgetChannelUiModel) {
            listener.onImpressPlayWidgetData(
                item,
                item.video.isLive,
                item.channelId,
                layoutPosition + 1,
            )

            binding.playWidgetLargeView.setModel(item)
            binding.playWidgetLargeView.setListener(object : PlayWidgetCardLargeChannelView.Listener {
                override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel) {
                    listener.onPlayWidgetLargeClick(
                        item.appLink,
                        item.channelId,
                        item.video.isLive && item.channelType == PlayWidgetChannelType.Live,
                        item.video.coverUrl,
                        layoutPosition + 1,
                    )
                }

                override fun onToggleReminderChannelClicked(
                    item: PlayWidgetChannelUiModel,
                    reminderType: PlayWidgetReminderType
                ) {
                    listener.onPlayReminderClick(item)
                }
            })
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Channel(
                UpItemUserPostChannelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener,
            )
        }

        interface Listener {
            fun onPlayReminderClick(channel: PlayWidgetChannelUiModel)
            fun onPlayWidgetLargeClick(appLink: String, channelID: String, isLive: Boolean, imageUrl: String, pos: Int)
            fun onImpressPlayWidgetData(channel: PlayWidgetChannelUiModel, isLive: Boolean, channelId: String, pos: Int)
        }
    }

    class Transcode(
        private val binding: UpItemUserPostTranscodeBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlayWidgetChannelUiModel) {
            binding.playWidgetLargeTranscodeView.setData(item)
            binding.playWidgetLargeTranscodeView.setListener(object : PlayWidgetCardLargeTranscodeView.Listener {
                override fun onFailedTranscodingChannelDeleteButtonClicked(
                    view: View,
                    item: PlayWidgetChannelUiModel
                ) {
                    listener.onDeletePlayChannel(item)
                }
            })
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Transcode(
                UpItemUserPostTranscodeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener,
            )
        }

        interface Listener {
            fun onDeletePlayChannel(channel: PlayWidgetChannelUiModel)
        }
    }

    class Loading(
        binding: UpItemUserPostLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            val layoutParams = itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
            ) = Loading(
                UpItemUserPostLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}

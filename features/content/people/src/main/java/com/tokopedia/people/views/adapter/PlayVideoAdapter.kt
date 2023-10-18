package com.tokopedia.people.views.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.people.views.viewholder.PlayVideoViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created By : Jonathan Darwin on February 10, 2023
 */
class PlayVideoAdapter(
    channelWidgetListener: PlayVideoViewHolder.Channel.Listener,
    transcodeWidgetListener: PlayVideoViewHolder.Transcode.Listener,
    private val onLoading: () -> Unit
) : BaseDiffUtilAdapter<PlayVideoAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(PlayVideoAdapterDelegate.Channel(channelWidgetListener))
            .addDelegate(PlayVideoAdapterDelegate.Transcode(transcodeWidgetListener))
            .addDelegate(PlayVideoAdapterDelegate.Loading())
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if(position == (itemCount - 1)) onLoading()
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return when {
            oldItem is Model.Loading && newItem is Model.Loading -> false
            oldItem is Model.Channel && newItem is Model.Channel -> oldItem.data.channelId == newItem.data.channelId
            oldItem is Model.Transcode && newItem is Model.Transcode -> oldItem.data.channelId == newItem.data.channelId
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed interface Model {
       data class Channel(val data: PlayWidgetChannelUiModel) : Model
       data class Transcode(val data: PlayWidgetChannelUiModel) : Model
       object Loading : Model
   }
}

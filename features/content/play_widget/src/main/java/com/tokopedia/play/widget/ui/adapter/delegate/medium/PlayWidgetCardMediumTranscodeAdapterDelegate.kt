package com.tokopedia.play.widget.ui.adapter.delegate.medium

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumTranscodeViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.util.blur.ImageBlurUtil

/**
 * Created by jegul on 04/11/20
 */
class PlayWidgetCardMediumTranscodeAdapterDelegate(
        private val imageBlurUtil: ImageBlurUtil,
        private val listener: PlayWidgetCardMediumTranscodeViewHolder.Listener
) : BaseAdapterDelegate<PlayWidgetMediumChannelUiModel, PlayWidgetMediumItemUiModel, PlayWidgetCardMediumTranscodeViewHolder>(
        PlayWidgetCardMediumTranscodeViewHolder.layoutRes
) {
    private val allowedTypes = listOf(PlayWidgetChannelType.Transcoding, PlayWidgetChannelType.FailedTranscoding)

    override fun onBindViewHolder(item: PlayWidgetMediumChannelUiModel, holder: PlayWidgetCardMediumTranscodeViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardMediumTranscodeViewHolder {
        return PlayWidgetCardMediumTranscodeViewHolder(basicView, imageBlurUtil, listener)
    }

    override fun isForViewType(itemList: List<PlayWidgetMediumItemUiModel>, position: Int, isFlexibleType: Boolean): Boolean {
        val item = itemList[position]
        return if (item is PlayWidgetMediumChannelUiModel) item.channelType in allowedTypes
        else false
    }
}
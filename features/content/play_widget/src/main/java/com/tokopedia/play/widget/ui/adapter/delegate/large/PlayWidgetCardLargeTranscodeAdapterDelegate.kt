package com.tokopedia.play.widget.ui.adapter.delegate.large

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeTranscodeViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.util.blur.ImageBlurUtil

/**
 * @author by astidhiyaa on 17/01/22
 */
class PlayWidgetCardLargeTranscodeAdapterDelegate(
    private val imageBlurUtil: ImageBlurUtil,
    private val listener: PlayWidgetCardLargeTranscodeViewHolder.Listener
) : BaseAdapterDelegate<PlayWidgetLargeChannelUiModel, PlayWidgetLargeItemUiModel, PlayWidgetCardLargeTranscodeViewHolder>(
    PlayWidgetCardLargeTranscodeViewHolder.layoutRes
) {
    private val allowedTypes =
        listOf(PlayWidgetChannelType.Transcoding, PlayWidgetChannelType.FailedTranscoding)

    override fun onBindViewHolder(
        item: PlayWidgetLargeChannelUiModel,
        holder: PlayWidgetCardLargeTranscodeViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayWidgetCardLargeTranscodeViewHolder {
        return PlayWidgetCardLargeTranscodeViewHolder(basicView, imageBlurUtil, listener)
    }

    override fun isForViewType(
        itemList: List<PlayWidgetLargeItemUiModel>,
        position: Int,
        isFlexibleType: Boolean
    ): Boolean {
        val item = itemList[position]
        return if (item is PlayWidgetLargeChannelUiModel) item.channelType in allowedTypes
        else false
    }
}
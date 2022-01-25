package com.tokopedia.play.widget.ui.adapter.delegate.jumbo

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboTranscodeViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.util.blur.ImageBlurUtil

/**
 * @author by astidhiyaa on 17/01/22
 */
class PlayWidgetCardJumboTranscodeAdapterDelegate(
    private val imageBlurUtil: ImageBlurUtil,
    private val listener: PlayWidgetCardJumboTranscodeViewHolder.Listener
) : BaseAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, PlayWidgetCardJumboTranscodeViewHolder>(
    PlayWidgetCardJumboTranscodeViewHolder.layoutRes
) {
    private val allowedTypes =
        listOf(PlayWidgetChannelType.Transcoding, PlayWidgetChannelType.FailedTranscoding)

    override fun onBindViewHolder(
        item: PlayWidgetChannelUiModel,
        holder: PlayWidgetCardJumboTranscodeViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayWidgetCardJumboTranscodeViewHolder {
        return PlayWidgetCardJumboTranscodeViewHolder(basicView, imageBlurUtil, listener)
    }

    override fun isForViewType(
        itemList: List<PlayWidgetItemUiModel>,
        position: Int,
        isFlexibleType: Boolean
    ): Boolean {
        val item = itemList[position]
        return if (item is PlayWidgetChannelUiModel) item.widgetType in allowedTypes
        else false
    }
}
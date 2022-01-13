package com.tokopedia.play.widget.ui.adapter.delegate.jumbo

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeBannerViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboItemUiModel

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardJumboBannerAdapterDelegate(
    private val jumboCardBannerListener: PlayWidgetCardJumboBannerViewHolder.Listener
) : TypedAdapterDelegate<PlayWidgetJumboBannerUiModel, PlayWidgetJumboItemUiModel, PlayWidgetCardJumboBannerViewHolder>(
    PlayWidgetCardLargeBannerViewHolder.layoutRes
) {

    override fun onBindViewHolder(
        item: PlayWidgetJumboBannerUiModel,
        holder: PlayWidgetCardJumboBannerViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayWidgetCardJumboBannerViewHolder {
        return PlayWidgetCardJumboBannerViewHolder(basicView, jumboCardBannerListener)
    }
}
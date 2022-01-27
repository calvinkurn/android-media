package com.tokopedia.play.widget.ui.adapter.delegate.jumbo

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboBannerViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardJumboBannerAdapterDelegate(
    private val jumboCardBannerListener: PlayWidgetCardJumboBannerViewHolder.Listener
) : TypedAdapterDelegate<PlayWidgetBannerUiModel, PlayWidgetItemUiModel, PlayWidgetCardJumboBannerViewHolder>(
    PlayWidgetCardJumboBannerViewHolder.layoutRes
) {

    override fun onBindViewHolder(
        item: PlayWidgetBannerUiModel,
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
package com.tokopedia.play.widget.ui.adapter.delegate.large

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeBannerViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetCardLargeBannerAdapterDelegate(
    private val largeCardBannerListener: PlayWidgetCardLargeBannerViewHolder.Listener
) : TypedAdapterDelegate<PlayWidgetBannerUiModel, PlayWidgetItemUiModel, PlayWidgetCardLargeBannerViewHolder>(
    PlayWidgetCardLargeBannerViewHolder.layoutRes
) {

    override fun onBindViewHolder(
        item: PlayWidgetBannerUiModel,
        holder: PlayWidgetCardLargeBannerViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayWidgetCardLargeBannerViewHolder {
        return PlayWidgetCardLargeBannerViewHolder(basicView, largeCardBannerListener)
    }
}
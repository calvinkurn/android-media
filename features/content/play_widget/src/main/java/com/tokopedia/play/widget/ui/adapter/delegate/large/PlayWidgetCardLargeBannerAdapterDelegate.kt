package com.tokopedia.play.widget.ui.adapter.delegate.large

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeBannerViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeItemUiModel

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetCardLargeBannerAdapterDelegate(
    private val largeCardBannerListener: PlayWidgetCardLargeBannerViewHolder.Listener
) : TypedAdapterDelegate<PlayWidgetLargeBannerUiModel, PlayWidgetLargeItemUiModel, PlayWidgetCardLargeBannerViewHolder>(
    PlayWidgetCardLargeBannerViewHolder.layoutRes
) {

    override fun onBindViewHolder(
        item: PlayWidgetLargeBannerUiModel,
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
package com.tokopedia.play.widget.ui.adapter.delegate.small

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.small.PlayWidgetCardSmallBannerViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetCardSmallBannerAdapterDelegate(
        private val smallCardBannerListener: PlayWidgetCardSmallBannerViewHolder.Listener
) : TypedAdapterDelegate<PlayWidgetBannerUiModel, PlayWidgetItemUiModel, PlayWidgetCardSmallBannerViewHolder>(
        PlayWidgetCardSmallBannerViewHolder.layout
) {

    override fun onBindViewHolder(item: PlayWidgetBannerUiModel, holder: PlayWidgetCardSmallBannerViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardSmallBannerViewHolder {
        return PlayWidgetCardSmallBannerViewHolder(basicView, smallCardBannerListener)
    }
}
package com.tokopedia.play.widget.ui.adapter.delegate.medium

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumBannerViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumBannerAdapterDelegate(
        private val mediumCardBannerListener: PlayWidgetCardMediumBannerViewHolder.Listener
) : TypedAdapterDelegate<PlayWidgetBannerUiModel, PlayWidgetItemUiModel, PlayWidgetCardMediumBannerViewHolder>(
        PlayWidgetCardMediumBannerViewHolder.layoutRes
) {

    override fun onBindViewHolder(item: PlayWidgetBannerUiModel, holder: PlayWidgetCardMediumBannerViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardMediumBannerViewHolder {
        return PlayWidgetCardMediumBannerViewHolder(basicView, mediumCardBannerListener)
    }
}
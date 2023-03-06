package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayBroadcastPreparationBannerViewHolder

internal class PlayBroadcastPreparationBannerAdapterDelegate {

    internal class BannerView : TypedAdapterDelegate<
        PlayBroadcastPreparationBannerModel,
        PlayBroadcastPreparationBannerModel,
        PlayBroadcastPreparationBannerViewHolder>(R.layout.view_play_bro_banner) {
        override fun onBindViewHolder(
            item: PlayBroadcastPreparationBannerModel,
            holder: PlayBroadcastPreparationBannerViewHolder
        ) {
            holder.bindData(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayBroadcastPreparationBannerViewHolder {
            return PlayBroadcastPreparationBannerViewHolder.create(parent)
        }
    }

}

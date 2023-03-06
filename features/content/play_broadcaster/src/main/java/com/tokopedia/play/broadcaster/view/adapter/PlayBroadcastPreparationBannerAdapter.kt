package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayBroadcastPreparationBannerAdapterDelegate

class PlayBroadcastPreparationBannerAdapter : BaseDiffUtilAdapter<PlayBroadcastPreparationBannerModel>() {

    init {
        delegatesManager
            .addDelegate(PlayBroadcastPreparationBannerAdapterDelegate.BannerView())
    }

    override fun areItemsTheSame(
        oldItem: PlayBroadcastPreparationBannerModel,
        newItem: PlayBroadcastPreparationBannerModel
    ): Boolean {
        return oldItem.icon == newItem.icon
    }

    override fun areContentsTheSame(
        oldItem: PlayBroadcastPreparationBannerModel,
        newItem: PlayBroadcastPreparationBannerModel
    ): Boolean {
        return oldItem == newItem
    }

}

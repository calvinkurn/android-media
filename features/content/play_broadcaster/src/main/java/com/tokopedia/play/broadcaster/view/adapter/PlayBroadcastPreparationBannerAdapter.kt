package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayBroadcastPreparationBannerAdapterDelegate

class PlayBroadcastPreparationBannerAdapter(
    listener: BannerListener,
) : BaseDiffUtilAdapter<PlayBroadcastPreparationBannerModel>() {

    init {
        delegatesManager
            .addDelegate(PlayBroadcastPreparationBannerAdapterDelegate.BannerView(listener))
    }

    override fun areItemsTheSame(
        oldItem: PlayBroadcastPreparationBannerModel,
        newItem: PlayBroadcastPreparationBannerModel
    ): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(
        oldItem: PlayBroadcastPreparationBannerModel,
        newItem: PlayBroadcastPreparationBannerModel
    ): Boolean {
        return oldItem == newItem
    }

    interface BannerListener {
        fun onBannerClick(data: PlayBroadcastPreparationBannerModel)
    }

    fun getPerformanceDashboardPosition(): Int {
        itemList.forEachIndexed { index, playBroadcastPreparationBannerModel ->
            if (playBroadcastPreparationBannerModel.type == PlayBroadcastPreparationBannerModel.TYPE_DASHBOARD)
                return index
        }
        return -1
    }

}

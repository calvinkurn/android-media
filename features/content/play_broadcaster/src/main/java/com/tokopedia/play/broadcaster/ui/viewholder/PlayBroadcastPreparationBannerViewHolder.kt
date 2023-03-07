package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.view.adapter.PlayBroadcastPreparationBannerAdapter
import com.tokopedia.play.broadcaster.view.custom.banner.PlayBroBannerView

class PlayBroadcastPreparationBannerViewHolder(
    private val view: PlayBroBannerView,
    private val listener: PlayBroadcastPreparationBannerAdapter.BannerListener,
) : RecyclerView.ViewHolder(view) {

    fun bindData(item: PlayBroadcastPreparationBannerModel) {
        view.apply {
            title = item.title
            description = item.description
            bannerIcon = item.icon
            setOnClickListener { listener.onBannerClick(item) }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: PlayBroadcastPreparationBannerAdapter.BannerListener
        ) = PlayBroadcastPreparationBannerViewHolder(
            PlayBroBannerView(parent.context),
            listener,
        )
    }
}


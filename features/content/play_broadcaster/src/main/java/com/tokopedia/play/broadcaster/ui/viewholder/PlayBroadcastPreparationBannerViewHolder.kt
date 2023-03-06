package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.view.custom.banner.PlayBroBannerView

class PlayBroadcastPreparationBannerViewHolder(
    private val view: PlayBroBannerView,
) : RecyclerView.ViewHolder(view) {

    fun bindData(item: PlayBroadcastPreparationBannerModel) {
        view.title = item.title
        view.description = item.description
        view.bannerIcon = item.icon
    }

    companion object {
        fun create(parent: ViewGroup) = PlayBroadcastPreparationBannerViewHolder(
            PlayBroBannerView(parent.context)
        )
    }
}


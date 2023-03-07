package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroBannerBinding
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.view.adapter.PlayBroadcastPreparationBannerAdapter

class PlayBroadcastPreparationBannerViewHolder(
    private val view: ViewPlayBroBannerBinding,
    private val listener: PlayBroadcastPreparationBannerAdapter.BannerListener,
) : RecyclerView.ViewHolder(view.root) {

    fun bindData(item: PlayBroadcastPreparationBannerModel) {
        view.apply {
            tvTitle.text = item.title
            tvDescription.text = item.description
            icBanner.setImage(item.icon)
            root.setOnClickListener { listener.onBannerClick(item) }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: PlayBroadcastPreparationBannerAdapter.BannerListener
        ) = PlayBroadcastPreparationBannerViewHolder(
            ViewPlayBroBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener,
        )
    }
}


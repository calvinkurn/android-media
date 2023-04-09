package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroBannerBinding
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.view.adapter.PlayBroadcastPreparationBannerAdapter

class PlayBroadcastPreparationBannerViewHolder(
    private val view: ViewPlayBroBannerBinding,
    private val listener: PlayBroadcastPreparationBannerAdapter.BannerListener,
) : RecyclerView.ViewHolder(view.root) {

    private val mContext = view.root.context

    fun bindData(item: PlayBroadcastPreparationBannerModel) {
        val title: String
        val description: String
        val icon: Int
        when (item.type) {
            PlayBroadcastPreparationBannerModel.TYPE_SHORTS -> {
                title = mContext.getString(R.string.play_bro_banner_shorts_title)
                description = mContext.getString(R.string.play_bro_banner_shorts_description)
                icon = IconUnify.SHORT_VIDEO
            }
            PlayBroadcastPreparationBannerModel.TYPE_DASHBOARD -> {
                title = mContext.getString(R.string.play_bro_banner_performance_title)
                description = mContext.getString(R.string.play_bro_banner_performance_dashboard_coachmark_subtitle)
                icon = IconUnify.GRAPH_REPORT
            }
            else -> {
                title = ""
                description = ""
                icon = 0
            }
        }
        view.apply {
            tvTitle.text = title
            tvDescription.text = description
            icBanner.setImage(icon)
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


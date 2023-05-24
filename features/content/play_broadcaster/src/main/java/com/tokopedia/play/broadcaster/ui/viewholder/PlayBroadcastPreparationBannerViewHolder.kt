package com.tokopedia.play.broadcaster.ui.viewholder

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroBannerBinding
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.view.adapter.PlayBroadcastPreparationBannerAdapter
import com.tokopedia.unifyprinciples.R as unifyR

class PlayBroadcastPreparationBannerViewHolder(
    private val view: ViewPlayBroBannerBinding,
    private val listener: PlayBroadcastPreparationBannerAdapter.BannerListener,
) : RecyclerView.ViewHolder(view.root) {

    private val mContext = view.root.context

    fun bindData(item: PlayBroadcastPreparationBannerModel) {
        calculateCustomWidth()

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
                description = mContext.getString(R.string.play_bro_banner_performance_subtitle)
                icon = IconUnify.GRAPH_REPORT
            }
            PlayBroadcastPreparationBannerModel.TYPE_SHORTS_AFFILIATE -> {
                title = mContext.getString(R.string.play_bro_banner_shorts_join_affiliate_title)
                description = mContext.getString(R.string.play_bro_banner_shorts_join_affiliate_description)
                icon = IconUnify.SALDO
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

    private fun calculateCustomWidth() {
        val itemOffset = view.root.context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4) * TIMES_TWO
        val displayMetrics = DisplayMetrics()
        (view.cuContainer.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)

        view.cuContainer.layoutParams.width = (displayMetrics.widthPixels - itemOffset)
    }

    companion object {
        private const val TIMES_TWO = 2
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


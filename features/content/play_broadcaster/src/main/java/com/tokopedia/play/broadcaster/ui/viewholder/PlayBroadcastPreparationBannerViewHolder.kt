package com.tokopedia.play.broadcaster.ui.viewholder

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroBannerBinding
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.view.adapter.PlayBroadcastPreparationBannerAdapter
import timber.log.Timber
import com.tokopedia.unifyprinciples.R as unifyR

class PlayBroadcastPreparationBannerViewHolder(
    private val view: ViewPlayBroBannerBinding,
    private val listener: PlayBroadcastPreparationBannerAdapter.BannerListener,
) : RecyclerView.ViewHolder(view.root) {

    fun bindData(item: PlayBroadcastPreparationBannerModel) {
        calculateCustomWidth()
        view.apply {
            tvTitle.text = item.title
            tvDescription.text = item.description
            icBanner.setImage(item.icon)
            root.setOnClickListener { listener.onBannerClick(item) }
        }
    }

    private fun calculateCustomWidth() {
        val itemOffset = view.root.context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4) * TIMES_TWO
        val displayMetrics = DisplayMetrics()
        (view.cuContainer.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)

        Timber.d(displayMetrics.widthPixels.toString())
        Timber.d((displayMetrics.widthPixels - itemOffset).toString())
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


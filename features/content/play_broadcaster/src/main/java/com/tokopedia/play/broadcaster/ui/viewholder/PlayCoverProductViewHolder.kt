package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage
import kotlinx.android.synthetic.main.item_play_cover_from_product.view.*

/**
 * @author by furqan on 07/06/2020
 */
class PlayCoverProductViewHolder(itemView: View,
                                 private val listener: Listener)
    : RecyclerView.ViewHolder(itemView) {

    fun bind(item: String) {
        with(itemView) {
            ivPlayCoverThumbnail.loadImage(item)
            ivPlayCoverThumbnail.setOnClickListener {
                listener.onCoverSelectedFromProduct(adapterPosition)
            }
        }
    }

    interface Listener {
        fun onCoverSelectedFromProduct(position: Int)
    }

    companion object {
        val LAYOUT = R.layout.item_play_cover_from_product
    }
}
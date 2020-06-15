package com.tokopedia.play.broadcaster.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverProductViewHolder

/**
 * @author by furqan on 07/06/2020
 */
class PlayCoverProductAdapter(
        private val imageUrls: List<String>,
        private val listener: PlayCoverProductViewHolder.Listener)
    : RecyclerView.Adapter<PlayCoverProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayCoverProductViewHolder =
            PlayCoverProductViewHolder(LayoutInflater.from(parent.context)
                    .inflate(PlayCoverProductViewHolder.LAYOUT, parent, false),
                    listener)

    override fun getItemCount(): Int = imageUrls.size

    override fun onBindViewHolder(holder: PlayCoverProductViewHolder, position: Int) {
        holder.bind(imageUrls[position])
    }

}
package com.tokopedia.catalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.VideoComponentData
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import kotlinx.android.synthetic.main.item_catalog_video.view.*

class VideosViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(model: VideoComponentData, catalogDetailListener: CatalogDetailListener) {
        itemView.video_title_tv.text = model.title
        itemView.channel_name.text = model.author
        itemView.video_thumbnail_iv.setOnClickListener {
            catalogDetailListener.playVideo(model,adapterPosition)
        }
        model.thumbnail?.let {
            itemView.video_thumbnail_iv.loadImageWithoutPlaceholder(model.thumbnail)
        }
    }
}
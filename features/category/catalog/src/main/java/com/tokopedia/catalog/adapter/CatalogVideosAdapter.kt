package com.tokopedia.catalog.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.VideoComponentData
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import kotlinx.android.synthetic.main.item_catalog_video.view.*

class CatalogVideosAdapter (val list : ArrayList<VideoComponentData>, private val catalogDetailListener: CatalogDetailListener)
    : RecyclerView.Adapter<CatalogVideosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_catalog_video, null))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position],catalogDetailListener)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
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
}
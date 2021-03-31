package com.tokopedia.catalog.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.raw.TopSpecificationsComponentData
import com.tokopedia.catalog.model.raw.VideoComponentData
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_catalog_specification.view.*
import kotlinx.android.synthetic.main.item_catalog_video.view.*

class CatalogVideosAdapter (val list : ArrayList<VideoComponentData>)
    : RecyclerView.Adapter<CatalogVideosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_catalog_video, null))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(model: VideoComponentData) {
            itemView.video_title_tv.text = "Review iPhone 11 - Ga bikin sirik lagi."
            itemView.channel_name.text = "GadgetIn"
        }
    }
}
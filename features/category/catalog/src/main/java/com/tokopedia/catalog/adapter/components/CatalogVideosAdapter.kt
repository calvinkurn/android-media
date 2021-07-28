package com.tokopedia.catalog.adapter.components

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.VideoComponentData
import com.tokopedia.catalog.viewholder.components.VideosViewHolder

class CatalogVideosAdapter (val list : ArrayList<VideoComponentData>, private val catalogDetailListener: CatalogDetailListener)
    : RecyclerView.Adapter<VideosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        return VideosViewHolder(View.inflate(parent.context, R.layout.item_catalog_video, null))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        holder.bind(list[position],catalogDetailListener)
    }
}
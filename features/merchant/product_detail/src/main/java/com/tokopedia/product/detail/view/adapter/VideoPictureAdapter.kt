package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.view.viewholder.VideoPictureViewHolder
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator

/**
 * Created by Yehezkiel on 23/11/20
 */
class VideoPictureAdapter(val productExoPlayer: ProductVideoCoordinator?) : RecyclerView.Adapter<VideoPictureViewHolder>() {

    private var mediaData: List<MediaDataModel> = listOf()

    fun updateData(mediaData: List<MediaDataModel>) {
        this.mediaData = mediaData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPictureViewHolder {
        return VideoPictureViewHolder(LayoutInflater.from(parent.context)
                .inflate(VideoPictureViewHolder.LAYOUT, parent, false), productExoPlayer)
    }

    override fun onBindViewHolder(holder: VideoPictureViewHolder, position: Int) {
        holder.bind(mediaData[position])
    }

    override fun getItemCount(): Int = mediaData.size
}
package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.view.viewholder.ProductPictureViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductVideoViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductVideoViewHolder.Companion.VIDEO_TYPE
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder

/**
 * Created by Yehezkiel on 23/11/20
 */
class VideoPictureAdapter(private val productVideoCoordinator: ProductVideoCoordinator?,
                          private val onVideoFullScreenClicked: ((String) -> Unit)? = null) : RecyclerView.Adapter<AbstractViewHolder<MediaDataModel>>() {

    var mediaData: List<MediaDataModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<MediaDataModel> {
        return if (viewType == MEDIA_VIDEO_VIEW_TYPE) {
            ProductVideoViewHolder(LayoutInflater.from(parent.context)
                    .inflate(ProductVideoViewHolder.LAYOUT, parent, false), productVideoCoordinator, onVideoFullScreenClicked)
        } else {
            ProductPictureViewHolder(LayoutInflater.from(parent.context)
                    .inflate(ProductPictureViewHolder.LAYOUT, parent, false))
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<MediaDataModel>, position: Int) {
        holder.bind(mediaData[position])
    }

    override fun getItemCount(): Int = mediaData.size

    override fun getItemViewType(position: Int): Int {
        return if (mediaData[position].type == VIDEO_TYPE) {
            MEDIA_VIDEO_VIEW_TYPE
        } else {
            MEDIA_PICTURE_VIEW_TYPE
        }
    }

    companion object {
        const val MEDIA_PICTURE_VIEW_TYPE = 1
        const val MEDIA_VIDEO_VIEW_TYPE = 2
    }
}
package com.tokopedia.product.detail.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.Video
import com.tokopedia.product.detail.data.util.thumbnailUrl
import kotlinx.android.synthetic.main.item_youtube_thumbnail.view.*

class YoutubeThumbnailAdapter(private val videos: MutableList<Video>,
                              private val clickItemListener: ((Video, Int) -> Unit)? = null)
    : RecyclerView.Adapter<YoutubeThumbnailAdapter.YoutubeThumbnailViewHolder>() {

    lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeThumbnailViewHolder {
        return YoutubeThumbnailViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_youtube_thumbnail, parent, false))
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: YoutubeThumbnailViewHolder, position: Int) {
        with(holder.itemView) {
            if (videos.isEmpty()) {
                recyclerView.visibility = View.GONE
            } else {
                ImageHandler.LoadImage(video_thumbnail, videos[position].thumbnailUrl)
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    inner class YoutubeThumbnailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener { clickItemListener?.invoke(videos[adapterPosition], adapterPosition) }
        }
    }
}
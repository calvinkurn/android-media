package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.media.loader.loadImage
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.data.util.thumbnailUrl
import kotlinx.android.synthetic.main.item_youtube_thumbnail.view.*

class YoutubeThumbnailAdapter(private val youtubeVideos: MutableList<YoutubeVideo>,
                              private val clickItemListener: ((YoutubeVideo, Int) -> Unit)? = null)
    : RecyclerView.Adapter<YoutubeThumbnailAdapter.YoutubeThumbnailViewHolder>() {

    lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeThumbnailViewHolder {
        return YoutubeThumbnailViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_youtube_thumbnail, parent, false))
    }

    override fun getItemCount(): Int = youtubeVideos.size

    override fun onBindViewHolder(holder: YoutubeThumbnailViewHolder, position: Int) {
        with(holder.itemView) {
            if (youtubeVideos.isEmpty()) {
                recyclerView.visibility = View.GONE
            } else {
                video_thumbnail.loadImage(youtubeVideos[position].thumbnailUrl)
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
            itemView.setOnClickListener { clickItemListener?.invoke(youtubeVideos[adapterPosition], adapterPosition) }
        }
    }
}
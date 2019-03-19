package com.tokopedia.product.detail.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.util.thumbnailUrl
import kotlinx.android.synthetic.main.item_youtube_thumbnail.view.*
import java.lang.Exception

class YoutubeThumbnailAdapter(private val videos: MutableList<Video>,
                              private val clickItemListener: ((Video, Int)->Unit)? = null)
    : RecyclerView.Adapter<YoutubeThumbnailAdapter.YoutubeThumbnailViewHolder>() {

    lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeThumbnailViewHolder {
        return YoutubeThumbnailViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_youtube_thumbnail, parent, false))
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: YoutubeThumbnailViewHolder, position: Int) {
        with(holder.view){
            Glide.with(context)
                    .load(videos[position].thumbnailUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                            if (videos.isEmpty())
                                recyclerView.visibility = View.GONE
                            holder.itemView.gone()
                            youtube_thumbnail_loading_bar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                            target?.onResourceReady(resource, null)
                            youtube_thumbnail_loading_bar.visibility = View.GONE
                            return true
                        }
                    })
                    .into(video_thumbnail)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    inner class YoutubeThumbnailViewHolder(val view: View): RecyclerView.ViewHolder(view){
        init {
            itemView.setOnClickListener { clickItemListener?.invoke(videos[adapterPosition], adapterPosition) }
        }
    }
}
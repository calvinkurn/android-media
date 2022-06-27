package com.tokopedia.media.editor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.editor.R
import com.tokopedia.media.loader.loadImage

class ThumbnailDrawerAdapter constructor(
    private val thumbnails: List<String> = mutableListOf()
) : RecyclerView.Adapter<ThumbnailDrawerAdapter.ThumbnailDrawerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailDrawerViewHolder {
        return ThumbnailDrawerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ThumbnailDrawerViewHolder, position: Int) {
        holder.bind(thumbnails[position])
    }

    override fun getItemCount() = thumbnails.size

    class ThumbnailDrawerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgThumbnail: ImageView = view.findViewById(R.id.img_thumbnail)

        fun bind(thumbnail: String) {
            imgThumbnail.loadImage(thumbnail) {
                centerCrop()
                setRoundedRadius(8f)
            }
        }

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.partial_editor_thumbnail_drawer

            fun create(viewGroup: ViewGroup): ThumbnailDrawerViewHolder {
                val layout = LayoutInflater
                    .from(viewGroup.context)
                    .inflate(LAYOUT, viewGroup, false)
                return ThumbnailDrawerViewHolder(layout)
            }
        }

    }

}
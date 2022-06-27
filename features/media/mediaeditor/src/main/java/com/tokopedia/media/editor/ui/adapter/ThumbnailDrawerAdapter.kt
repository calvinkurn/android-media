package com.tokopedia.media.editor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.editor.R
import com.tokopedia.media.loader.loadImage

class ThumbnailDrawerAdapter constructor(
    private val thumbnails: List<String> = mutableListOf(),
    private val listener: Listener
) : RecyclerView.Adapter<ThumbnailDrawerViewHolder>() {

    private var itemSelectedIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailDrawerViewHolder {
        return ThumbnailDrawerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ThumbnailDrawerViewHolder, position: Int) {
        holder.bind(thumbnails[position], position)

        holder.setThumbnailSelected(itemSelectedIndex == position)
        listener.onItemClicked(thumbnails[itemSelectedIndex])

        holder.itemView.setOnClickListener {
            notifyItemChanged(itemSelectedIndex)
            itemSelectedIndex = holder.adapterPosition
            notifyItemChanged(itemSelectedIndex)
        }
    }

    override fun getItemCount() = thumbnails.size

    interface Listener {
        fun onItemClicked(url: String)
    }

}

class ThumbnailDrawerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val imgThumbnail: ImageView = view.findViewById(R.id.img_thumbnail)
    private val context by lazy { itemView.context }

    private var lastItemSelected = 0

    fun bind(thumbnail: String, position: Int) {
        setThumbnailSelected(position == lastItemSelected)
        imgThumbnail.loadImage(thumbnail) {
            centerCrop()
            setRoundedRadius(10f)
        }
    }

    fun setThumbnailSelected(isSelected: Boolean) {
        if (isSelected) {
            val paddingSize = context.resources.getDimension(R.dimen.editor_thumbnail_selected_padding).toInt()
            val backgroundAsset = MethodChecker.getDrawable(
                context,
                R.drawable.editor_rect_green_selected_thumbnail
            )

            imgThumbnail.setPadding(paddingSize, paddingSize, paddingSize, paddingSize)
            imgThumbnail.background = backgroundAsset
        } else {
            imgThumbnail.setPadding(0, 0, 0, 0)
            imgThumbnail.background = null
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
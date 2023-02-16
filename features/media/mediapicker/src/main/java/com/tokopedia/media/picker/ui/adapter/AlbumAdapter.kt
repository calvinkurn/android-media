package com.tokopedia.media.picker.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ViewItemAlbumPickerBinding
import com.tokopedia.media.picker.utils.loadPickerImage
import com.tokopedia.picker.common.uimodel.AlbumUiModel
import com.tokopedia.utils.view.binding.viewBinding

internal typealias OnAlbumClickListener = (AlbumUiModel) -> Unit

class AlbumAdapter constructor(
    private val albums: MutableList<AlbumUiModel> = mutableListOf(),
    private val listener: OnAlbumClickListener? = null
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position], listener)
    }

    override fun getItemCount() = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(albums: List<AlbumUiModel>) {
        this.albums.clear()
        this.albums.addAll(albums)
        notifyDataSetChanged()
    }

    class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ViewItemAlbumPickerBinding? by viewBinding()
        private val context by lazy { itemView.context }

        fun bind(album: AlbumUiModel, listener: OnAlbumClickListener?) {
            binding?.txtName?.text = album.name
            binding?.txtCount?.text = context.getString(
                R.string.picker_album_subtitle,
                album.count
            )
            binding?.imgPreview?.loadPickerImage(album.uri.toString())

            itemView.setOnClickListener {
                listener?.invoke(album)
            }
        }

        companion object {
            @LayoutRes val LAYOUT = R.layout.view_item_album_picker

            fun create(parent: ViewGroup): AlbumViewHolder {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(LAYOUT, parent, false)

                return AlbumViewHolder(view)
            }
        }

    }

}

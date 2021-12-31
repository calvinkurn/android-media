package com.tokopedia.picker.ui.activity.album.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.picker.R
import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.databinding.ViewItemAlbumPickerBinding
import com.tokopedia.picker.ui.fragment.OnAlbumClickListener
import com.tokopedia.utils.view.binding.viewBinding

class AlbumAdapter constructor(
    private val albums: MutableList<Album> = mutableListOf(),
    private val listener: OnAlbumClickListener? = null
) : RecyclerView.Adapter<AlbumAdapter.FileDirectoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileDirectoryViewHolder {
        return FileDirectoryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FileDirectoryViewHolder, position: Int) {
        holder.bind(albums[position], listener)
    }

    override fun getItemCount() = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(albums: List<Album>) {
        this.albums.clear()
        this.albums.addAll(albums)
        notifyDataSetChanged()
    }

    class FileDirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ViewItemAlbumPickerBinding? by viewBinding()
        private val context by lazy { itemView.context }

        fun bind(album: Album, listener: OnAlbumClickListener?) {
            binding?.txtName?.text = album.name

            binding?.txtCount?.text = context.getString(
                R.string.picker_album_subtitle,
                album.count
            )

            // TODO, didn't rounded yet
            album.preview?.let {
                binding?.imgPreview?.loadImageRounded(it, 10f)
            }

            itemView.setOnClickListener {
                listener?.invoke(album)
            }
        }

        companion object {
            @LayoutRes val LAYOUT = R.layout.view_item_album_picker

            fun create(parent: ViewGroup): FileDirectoryViewHolder {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(LAYOUT, parent, false)

                return FileDirectoryViewHolder(view)
            }
        }

    }

}
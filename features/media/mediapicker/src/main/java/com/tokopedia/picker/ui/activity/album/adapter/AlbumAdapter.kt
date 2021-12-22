package com.tokopedia.picker.ui.activity.album.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.picker.R
import com.tokopedia.picker.data.entity.Directory
import com.tokopedia.picker.databinding.ViewItemAlbumPickerBinding
import com.tokopedia.picker.ui.fragment.OnDirectoryClickListener
import com.tokopedia.utils.view.binding.viewBinding

class AlbumAdapter constructor(
    private val albums: MutableList<Directory> = mutableListOf(),
    private val listener: OnDirectoryClickListener? = null
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position], listener)
    }

    override fun getItemCount() = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(albums: List<Directory>) {
        this.albums.clear()
        this.albums.addAll(albums)
        notifyDataSetChanged()
    }

    class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ViewItemAlbumPickerBinding? by viewBinding()
        private val context by lazy { itemView.context }

        fun bind(directory: Directory, listener: OnDirectoryClickListener?) {
            binding?.txtName?.text = directory.name

            binding?.txtCount?.text = context.getString(
                com.tokopedia.picker.R.string.picker_album_subtitle,
                directory.medias.size
            )

            binding?.imgPreview?.loadImageRounded(
                directory.medias.first().uri,
                10f
            )

            itemView.setOnClickListener {
                listener?.invoke(directory)
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
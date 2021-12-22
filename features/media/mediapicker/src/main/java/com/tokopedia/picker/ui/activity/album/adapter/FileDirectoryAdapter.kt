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

class FileDirectoryAdapter constructor(
    private val directories: MutableList<Directory> = mutableListOf(),
    private val listener: OnDirectoryClickListener? = null
) : RecyclerView.Adapter<FileDirectoryAdapter.FileDirectoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileDirectoryViewHolder {
        return FileDirectoryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FileDirectoryViewHolder, position: Int) {
        holder.bind(directories[position], listener)
    }

    override fun getItemCount() = directories.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(albums: List<Directory>) {
        this.directories.clear()
        this.directories.addAll(albums)
        notifyDataSetChanged()
    }

    class FileDirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ViewItemAlbumPickerBinding? by viewBinding()
        private val context by lazy { itemView.context }

        fun bind(directory: Directory, listener: OnDirectoryClickListener?) {
            binding?.txtName?.text = directory.name

            binding?.txtCount?.text = context.getString(
                R.string.picker_album_subtitle,
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

            fun create(parent: ViewGroup): FileDirectoryViewHolder {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(LAYOUT, parent, false)

                return FileDirectoryViewHolder(view)
            }
        }

    }

}
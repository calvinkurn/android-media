package com.tokopedia.stories.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tokopedia.stories.widget.databinding.ItemStoriesAvatarSampleBinding

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesAvatarSampleAdapter(
    private val storiesAvatarManager: StoriesAvatarManager
) : ListAdapter<String, StoriesAvatarSampleAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent, storiesAvatarManager)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemStoriesAvatarSampleBinding,
        private val storiesAvatarManager: StoriesAvatarManager
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shopId: String) {
            binding.imgAvatar.load("https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg")
            storiesAvatarManager.manage(binding.root, shopId)
        }

        companion object {
            fun create(parent: ViewGroup, storiesAvatarManager: StoriesAvatarManager): ViewHolder {
                return ViewHolder(
                    ItemStoriesAvatarSampleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    storiesAvatarManager
                )
            }
        }
    }
}

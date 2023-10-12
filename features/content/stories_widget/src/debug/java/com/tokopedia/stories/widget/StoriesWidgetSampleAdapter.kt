package com.tokopedia.stories.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tokopedia.stories.widget.databinding.ItemStoriesWidgetSampleBinding

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesWidgetSampleAdapter(
    private val storiesWidgetManager: StoriesWidgetManager
) : ListAdapter<String, StoriesWidgetSampleAdapter.ViewHolder>(
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
        return ViewHolder.create(parent, storiesWidgetManager)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemStoriesWidgetSampleBinding,
        private val storiesWidgetManager: StoriesWidgetManager
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shopId: String) {
            binding.imgAvatar.load("https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg")
            storiesWidgetManager.manage(binding.root, shopId)
        }

        companion object {
            fun create(parent: ViewGroup, storiesWidgetManager: StoriesWidgetManager): ViewHolder {
                return ViewHolder(
                    ItemStoriesWidgetSampleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    storiesWidgetManager
                )
            }
        }
    }
}

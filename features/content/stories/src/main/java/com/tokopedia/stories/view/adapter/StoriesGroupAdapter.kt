package com.tokopedia.stories.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.stories.R
import com.tokopedia.stories.databinding.ItemStoriesGroupBinding
import com.tokopedia.stories.view.model.StoriesGroupHeader

class StoriesGroupAdapter(
    listener: Listener
) : BaseDiffUtilAdapter<StoriesGroupHeader>() {

    init {
        delegatesManager.addDelegate(StoriesGroupAdapterDelegate.StoriesGroup(listener))
    }

    override fun areItemsTheSame(
        oldItem: StoriesGroupHeader,
        newItem: StoriesGroupHeader
    ): Boolean {
        return oldItem.groupId == newItem.groupId
    }

    override fun areContentsTheSame(
        oldItem: StoriesGroupHeader,
        newItem: StoriesGroupHeader
    ): Boolean {
        return oldItem == newItem
    }

    interface Listener {
        fun onClickGroup(position: Int, data: StoriesGroupHeader)
        fun onGroupImpressed(data: StoriesGroupHeader)
    }

    internal class StoriesGroupAdapterDelegate {

        internal class StoriesGroup(
            private val listener: Listener
        ) : TypedAdapterDelegate<StoriesGroupHeader, StoriesGroupHeader,
            StoriesGroupViewHolder>(R.layout.layout_empty) {

            override fun onBindViewHolder(
                item: StoriesGroupHeader,
                holder: StoriesGroupViewHolder
            ) {
                holder.bind(item)
                listener.onGroupImpressed(item)
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                basicView: View
            ): StoriesGroupViewHolder {
                return StoriesGroupViewHolder.create(listener, parent)
            }
        }
    }

    internal class StoriesGroupViewHolder(
        private val listener: Listener,
        private val binding: ItemStoriesGroupBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: StoriesGroupHeader) {
            binding.imgGroupImage.setImageUrl(data.image)
            binding.txtGroupTitle.text = data.groupName

            if (data.isSelected) selectedView()
            else unSelectedView()

            binding.root.setOnClickListener {
                listener.onClickGroup(bindingAdapterPosition, data)
            }
        }

        private fun selectedView() {
            binding.imgGroupImage.alpha = 1F
            binding.txtGroupTitle.alpha = 1F
        }

        private fun unSelectedView() {
            binding.imgGroupImage.alpha = 0.5F
            binding.txtGroupTitle.alpha = 0.5F
        }

        companion object {
            fun create(
                listener: Listener,
                parent: ViewGroup,
            ) = StoriesGroupViewHolder(
                listener,
                ItemStoriesGroupBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }

    }

}


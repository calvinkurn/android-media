package com.tokopedia.stories.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.stories.R
import com.tokopedia.stories.databinding.ItemStoryGroupBinding
import com.tokopedia.stories.view.model.StoryGroupHeader

class StoryGroupAdapter(
    listener: Listener
) : BaseDiffUtilAdapter<StoryGroupHeader>() {

    init {
        delegatesManager.addDelegate(StoryGroupAdapterDelegate.StoryGroup(listener))
    }

    override fun areItemsTheSame(
        oldItem: StoryGroupHeader,
        newItem: StoryGroupHeader
    ): Boolean {
        return oldItem.groupId == newItem.groupId
    }

    override fun areContentsTheSame(
        oldItem: StoryGroupHeader,
        newItem: StoryGroupHeader
    ): Boolean {
        return oldItem == newItem
    }

    interface Listener {
        fun onClickGroup(position: Int)
    }

}

internal class StoryGroupAdapterDelegate {

    internal class StoryGroup(
        private val listener: StoryGroupAdapter.Listener
    ) : TypedAdapterDelegate<StoryGroupHeader, StoryGroupHeader,
        StoryGroupViewHolder>(R.layout.layout_empty) {

        override fun onBindViewHolder(
            item: StoryGroupHeader,
            holder: StoryGroupViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): StoryGroupViewHolder {
            return StoryGroupViewHolder.create(listener, parent)
        }
    }
}

class StoryGroupViewHolder(
    private val listener: StoryGroupAdapter.Listener,
    private val binding: ItemStoryGroupBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: StoryGroupHeader) {
        binding.imgGroupImage.setImageUrl(data.image)
        binding.txtGroupTitle.text = data.title

        if (data.isSelected) selectedView()
        else unSelectedView()

        binding.root.setOnClickListener {
            listener.onClickGroup(bindingAdapterPosition)
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
            listener: StoryGroupAdapter.Listener,
            parent: ViewGroup,
        ) = StoryGroupViewHolder(
            listener,
            ItemStoryGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

}


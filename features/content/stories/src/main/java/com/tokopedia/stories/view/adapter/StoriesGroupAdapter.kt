package com.tokopedia.stories.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.stories.R
import com.tokopedia.stories.databinding.ItemStoriesGroupBinding
import com.tokopedia.stories.view.model.StoriesGroupUiModel

class StoriesGroupAdapter(
    listener: Listener
) : BaseDiffUtilAdapter<StoriesGroupUiModel>() {

    init {
        delegatesManager
            .addDelegate(StoriesGroupAdapterDelegate.StoriesGroup(listener))
    }

    override fun areItemsTheSame(
        oldItem: StoriesGroupUiModel,
        newItem: StoriesGroupUiModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: StoriesGroupUiModel,
        newItem: StoriesGroupUiModel
    ): Boolean {
        return oldItem == newItem
    }

    interface Listener {
        fun onClickGroup(position: Int)
    }

}

internal class StoriesGroupAdapterDelegate {

    internal class StoriesGroup(
        private val listener: StoriesGroupAdapter.Listener
    ) : TypedAdapterDelegate<StoriesGroupUiModel, StoriesGroupUiModel,
        StoriesGroupViewHolder>(R.layout.layout_empty) {

        override fun onBindViewHolder(
            item: StoriesGroupUiModel,
            holder: StoriesGroupViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): StoriesGroupViewHolder {
            return StoriesGroupViewHolder.create(listener, parent)
        }
    }
}

class StoriesGroupViewHolder(
    private val listener: StoriesGroupAdapter.Listener,
    private val binding: ItemStoriesGroupBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: StoriesGroupUiModel) {
        binding.imgGroupImage.setImageUrl(data.image)
        binding.txtGroupTitle.text = data.title

        if (data.selected) selectedView()
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
            listener: StoriesGroupAdapter.Listener,
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


package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.feedplus.databinding.ItemFeedLoadingBinding

/**
 * Created by Jonathan Darwin on 26 February 2024
 */
class FeedLoadingViewHolder(
    binding: ItemFeedLoadingBinding,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        val layoutParams = itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }

    companion object {

        fun create(parent: ViewGroup) = FeedLoadingViewHolder(
            ItemFeedLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }
}

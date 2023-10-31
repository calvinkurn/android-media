package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipPlaceholderBinding

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class ChipPlaceholderViewHolder private constructor(
    binding: ItemFeedBrowseChipPlaceholderBinding,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): ChipPlaceholderViewHolder {
            return ChipPlaceholderViewHolder(
                ItemFeedBrowseChipPlaceholderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
            )
        }
    }
}

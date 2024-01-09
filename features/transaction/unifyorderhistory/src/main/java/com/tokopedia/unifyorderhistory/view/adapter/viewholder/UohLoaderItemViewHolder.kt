package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohLoaderItemBinding

class UohLoaderItemViewHolder(private val binding: UohLoaderItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UohTypeData) {
        binding.clLoader.visible()
    }
}

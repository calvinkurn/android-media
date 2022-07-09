package com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowTagBinding

class TagViewHolder(
    private val binding: ItemTokopedianowTagBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(tag: String) {
        binding.lblTag.text = tag
    }
}
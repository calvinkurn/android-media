package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemProductContentLoadingBinding

class ProductContentLoadingViewHolder(
    binding: ItemProductContentLoadingBinding
) : ViewHolder(binding.root) {

    fun bind() {}

    companion object {
        fun create(parent: ViewGroup) =
            ProductContentLoadingViewHolder(
                binding = ItemProductContentLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
    }
}

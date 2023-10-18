package com.tokopedia.product.detail.postatc.base

import androidx.recyclerview.widget.DiffUtil

object PostAtcDiffItemCallback : DiffUtil.ItemCallback<PostAtcUiModel>() {
    override fun areItemsTheSame(oldItem: PostAtcUiModel, newItem: PostAtcUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostAtcUiModel, newItem: PostAtcUiModel): Boolean {
        return oldItem.equalsWith(newItem)
    }
}

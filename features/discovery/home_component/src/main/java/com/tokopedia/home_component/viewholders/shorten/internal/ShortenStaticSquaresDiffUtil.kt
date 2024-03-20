package com.tokopedia.home_component.viewholders.shorten.internal

import androidx.recyclerview.widget.DiffUtil

class ShortenStaticSquaresDiffUtil : DiffUtil.ItemCallback<ShortenVisitable>() {

    override fun areItemsTheSame(oldItem: ShortenVisitable, newItem: ShortenVisitable): Boolean {
        return oldItem.visitableId() == newItem.visitableId()
    }

    override fun areContentsTheSame(oldItem: ShortenVisitable, newItem: ShortenVisitable): Boolean {
        return oldItem.equalsWith(newItem)
    }
}

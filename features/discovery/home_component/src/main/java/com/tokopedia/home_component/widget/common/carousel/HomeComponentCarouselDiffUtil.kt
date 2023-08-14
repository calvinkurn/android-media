package com.tokopedia.home_component.widget.common.carousel

import androidx.recyclerview.widget.DiffUtil

class HomeComponentCarouselDiffUtil: DiffUtil.ItemCallback<HomeComponentCarouselVisitable>() {
    override fun areItemsTheSame(
        oldItem: HomeComponentCarouselVisitable,
        newItem: HomeComponentCarouselVisitable
    ): Boolean {
        return oldItem.getId() == newItem.getId()
    }

    override fun areContentsTheSame(
        oldItem: HomeComponentCarouselVisitable,
        newItem: HomeComponentCarouselVisitable
    ): Boolean {
        return oldItem.equalsWith(newItem)
    }
}

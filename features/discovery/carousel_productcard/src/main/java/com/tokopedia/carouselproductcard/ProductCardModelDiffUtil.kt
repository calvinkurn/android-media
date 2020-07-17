package com.tokopedia.carouselproductcard

import androidx.recyclerview.widget.DiffUtil

internal class ProductCardModelDiffUtil: DiffUtil.ItemCallback<BaseCarouselCardModel>() {

    override fun areItemsTheSame(oldItem: BaseCarouselCardModel, newItem: BaseCarouselCardModel): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: BaseCarouselCardModel, newItem: BaseCarouselCardModel): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}
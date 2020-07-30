package com.tokopedia.carouselproductcard

import androidx.recyclerview.widget.DiffUtil

internal class ProductCardModelDiffUtil: DiffUtil.ItemCallback<CarouselProductCardModel>() {
    override fun areItemsTheSame(oldItem: CarouselProductCardModel, newItem: CarouselProductCardModel): Boolean {
        return oldItem.productCardModel.productName == newItem.productCardModel.productName
    }

    override fun areContentsTheSame(oldItem: CarouselProductCardModel, newItem: CarouselProductCardModel): Boolean {
        return oldItem.productCardModel == newItem.productCardModel
    }
}
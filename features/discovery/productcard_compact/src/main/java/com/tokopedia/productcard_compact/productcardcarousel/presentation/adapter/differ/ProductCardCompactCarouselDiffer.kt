package com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel

class ProductCardCompactCarouselDiffer : DiffUtil.ItemCallback<Visitable<*>>() {
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return if (oldItem is ProductCardCompactCarouselItemUiModel && newItem is ProductCardCompactCarouselItemUiModel) {
            oldItem.productCardModel.productId == newItem.productCardModel.productId
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return oldItem.equals(newItem)
    }

    override fun getChangePayload(oldItem: Visitable<*>, newItem: Visitable<*>): Any? {
        return if (oldItem is ProductCardCompactCarouselItemUiModel && newItem is ProductCardCompactCarouselItemUiModel) {
            oldItem.productCardModel != newItem.productCardModel
        } else {
            super.getChangePayload(oldItem, newItem)
        }
    }
}

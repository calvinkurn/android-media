package com.tokopedia.tokopedianow.common.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel

class TokoNowProductCardCarouselDiffer: DiffUtil.ItemCallback<Visitable<*>>() {
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return if (oldItem is TokoNowProductCardCarouselItemUiModel && newItem is TokoNowProductCardCarouselItemUiModel) {
            oldItem.productCardModel.productId == newItem.productCardModel.productId && oldItem.productCardModel.orderQuantity == newItem.productCardModel.orderQuantity
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return oldItem.equals(newItem)
    }
}

package com.tokopedia.tokopedianow.home.presentation.adapter.leftcarousel

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel

class HomeLeftCarouselAtcProductCardDiffer: DiffUtil.ItemCallback<Visitable<*>>() {

    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return if (oldItem is HomeLeftCarouselAtcProductCardUiModel && newItem is HomeLeftCarouselAtcProductCardUiModel) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return oldItem.equals(newItem)
    }
}

package com.tokopedia.home_component.widget.shop_flash_sale

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselDiffUtil

class ShopFlashSaleItemDiffUtilCallback: DiffUtil.ItemCallback<Visitable<CommonCarouselProductCardTypeFactory>>() {
    override fun areItemsTheSame(
        oldItem: Visitable<CommonCarouselProductCardTypeFactory>,
        newItem: Visitable<CommonCarouselProductCardTypeFactory>
    ): Boolean {
        return if(oldItem is HomeComponentCarouselDiffUtil && newItem is HomeComponentCarouselDiffUtil) {
            oldItem.getId() == newItem.getId()
        } else oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: Visitable<CommonCarouselProductCardTypeFactory>,
        newItem: Visitable<CommonCarouselProductCardTypeFactory>
    ): Boolean {
        return if(oldItem is HomeComponentCarouselDiffUtil && newItem is HomeComponentCarouselDiffUtil) {
            oldItem.equalsWith(newItem)
        } else false
    }
}

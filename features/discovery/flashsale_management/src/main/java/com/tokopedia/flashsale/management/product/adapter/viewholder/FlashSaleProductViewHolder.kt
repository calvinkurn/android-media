package com.tokopedia.flashsale.management.product.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.product.model.FlashSaleProductViewModel

class FlashSaleProductViewHolder(view: View): AbstractViewHolder<FlashSaleProductViewModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_flash_sale_product
    }

    override fun bind(element: FlashSaleProductViewModel) {
    }

}
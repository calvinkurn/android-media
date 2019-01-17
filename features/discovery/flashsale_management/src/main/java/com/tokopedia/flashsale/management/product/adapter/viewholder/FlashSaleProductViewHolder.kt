package com.tokopedia.flashsale.management.product.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.product.data.FlashSaleProductItem
import com.tokopedia.flashsale.management.product.data.FlashSaleSubmissionProductItem
import kotlinx.android.synthetic.main.item_flash_sale_product.view.*

class FlashSaleProductViewHolder(view: View): AbstractViewHolder<FlashSaleProductItem>(view) {
    companion object {
        val LAYOUT = R.layout.item_flash_sale_product
    }

    override fun bind(flashSaleProductItem: FlashSaleProductItem) {
        itemView.run {
            flashSaleProductWidget.setData(flashSaleProductItem)
        }
    }

}
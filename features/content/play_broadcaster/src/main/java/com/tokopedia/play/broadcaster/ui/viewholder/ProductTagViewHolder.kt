package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.type.StockAvailable
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on November 23, 2021
 */
class ProductTagViewHolder(
    itemView: View
): BaseViewHolder(itemView) {

    private val ivProductTag: ImageUnify = itemView.findViewById(R.id.iv_bro_product_tag)
    private val tvProductTagStock: Typography = itemView.findViewById(R.id.tv_bro_product_tag_stock)
    private val tvProductTagPrice: Typography = itemView.findViewById(R.id.tv_bro_product_tag_price)

    fun bind(item: ProductContentUiModel) {
        ivProductTag.setImageUrl(item.imageUrl)
        tvProductTagStock.text = getString(R.string.play_product_stock_amount, if (item.stock is StockAvailable) item.stock.stock else "-")
        tvProductTagPrice.text = "RP 999.999.999"
    }

    companion object {
        val LAYOUT = R.layout.item_play_product_tag
    }
}
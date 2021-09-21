package com.tokopedia.shop.search.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.search.util.SpanTextHelper
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDynamicResultDataModel
import com.tokopedia.unifyprinciples.Typography

class ShopSearchProductDynamicResultViewHolder(
        private val view: View
) : AbstractViewHolder<ShopSearchProductDynamicResultDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_search_product_dynamic_result_layout
    }

    private val ivProduct: ImageView? = itemView.findViewById(R.id.iv_product)
    private val tvLabelProductName: Typography? = itemView.findViewById(R.id.tv_label_product_name)
    private val tvLabelPrice: Typography? = itemView.findViewById(R.id.tv_label_price)

    override fun bind(element: ShopSearchProductDynamicResultDataModel) {
        ivProduct?.loadImage(element.imageUri)
        val sourceString = element.name
        val targetString = element.searchQuery
        val isTargetStringExists = sourceString.indexOf(targetString, ignoreCase = true) != -1
        tvLabelProductName?.text = if (isTargetStringExists) {
            SpanTextHelper(sourceString).bold(targetString, true, true)
        } else {
            SpanTextHelper(sourceString).bold(sourceString, false, true)
        }
        tvLabelPrice?.text = element.price
    }
}
package com.tokopedia.shop.search.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.search.util.SpanTextHelper
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDynamicResultDataModel
import kotlinx.android.synthetic.main.shop_search_product_dynamic_result_layout.view.*

class ShopSearchProductDynamicResultViewHolder(
        private val view: View
) : AbstractViewHolder<ShopSearchProductDynamicResultDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_search_product_dynamic_result_layout
    }

    override fun bind(element: ShopSearchProductDynamicResultDataModel) {
        with(view) {
            iv_product.loadImage(element.imageUri)
            val sourceString = element.name
            val targetString = element.searchQuery
            val isTargetStringExists = sourceString.indexOf(targetString, ignoreCase = true) != -1
            tv_label_product_name.text = if (isTargetStringExists) {
                SpanTextHelper(sourceString).bold(targetString, true, true)
            } else {
                SpanTextHelper(sourceString).bold(sourceString, false, true)
            }
            tv_label_price.text = element.price
        }
    }
}
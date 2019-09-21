package com.tokopedia.shop.search.view.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDynamicResultDataModel
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.R
import kotlinx.android.synthetic.main.shop_search_product_dynamic_result_layout.view.*

class ShopSearchProductDynamicResultViewHolder(
        private val view: View
) : AbstractViewHolder<ShopSearchProductDynamicResultDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_search_product_dynamic_result_layout
    }

    override fun bind(element: ShopSearchProductDynamicResultDataModel) {
        with(view) {
            ImageHandler.loadImageCircle2(view.context, iv_product, element.imageUri)
            val prodName = element.name
            val startIndex = prodName.indexOf(element.searchQuery, ignoreCase = true)
            val endIndex = startIndex + element.searchQuery.length
            val spannableString = SpannableString(prodName)
            if(startIndex >=0) {
                spannableString.setSpan(
                        StyleSpan(Typeface.BOLD),
                        0,
                        startIndex,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                        StyleSpan(Typeface.BOLD),
                        endIndex,
                        element.name.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
            tv_label_product_name.text = spannableString
            tv_label_price.text = element.price
        }
    }
}
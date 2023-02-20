package com.tokopedia.shop.search.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ShopSearchProductDynamicResultLayoutBinding
import com.tokopedia.shop.search.util.SpanTextHelper
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDynamicResultDataModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopSearchProductDynamicResultViewHolder(
    private val view: View
) : AbstractViewHolder<ShopSearchProductDynamicResultDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_search_product_dynamic_result_layout
    }

    private val viewBinding: ShopSearchProductDynamicResultLayoutBinding? by viewBinding()
    private val ivProduct: ImageView? = viewBinding?.ivProduct
    private val tvLabelProductName: Typography? = viewBinding?.tvLabelProductName
    private val tvLabelPrice: Typography? = viewBinding?.tvLabelPrice

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

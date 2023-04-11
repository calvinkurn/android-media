package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.BsItemProductDetailHeaderBinding
import com.tokopedia.product.info.view.models.ProductDetailInfoHeaderDataModel

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoHeaderViewHolder(
    view: View,
) : AbstractViewHolder<ProductDetailInfoHeaderDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_header
    }

    private val binding = BsItemProductDetailHeaderBinding.bind(view)

    override fun bind(element: ProductDetailInfoHeaderDataModel) = with(binding) {
        pdpHeaderProductTitle.text = element.title
        pdpHeaderImg.loadImage(element.image)
    }
}

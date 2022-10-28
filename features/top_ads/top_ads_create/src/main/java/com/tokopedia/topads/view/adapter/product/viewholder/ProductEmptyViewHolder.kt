package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Author errysuprayogi on 11,November,2019
 */
class ProductEmptyViewHolder(val view: View): ProductViewHolder<ProductEmptyViewModel>(view) {

    private val imageView8 : ImageUnify? = view.findViewById(R.id.imageView8)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_product_list_item_no_product
    }

    override fun bind(item: ProductEmptyViewModel) {
        imageView8?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.no_products))
    }

}
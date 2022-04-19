package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductShimmerViewModel

/**
 * Author errysuprayogi on 11,November,2019
 */
class ProductShimmerViewHolder(val view: View): ProductViewHolder<ProductShimmerViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_product_list_item_shimmering
    }

    override fun bind(item: ProductShimmerViewModel) {
    }

}
package com.tokopedia.topads.edit.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductShimmerViewModel



class ProductShimmerViewHolder(val view: View): ProductViewHolder<ProductShimmerViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_layout_product_list_item_shimmering
    }
}
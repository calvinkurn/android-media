package com.tokopedia.topads.edit.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductEmptyViewModel
import kotlinx.android.synthetic.main.topads_edit_select_layout_product_list_item_no_product.view.*


class ProductEmptyViewHolder(val view: View): ProductViewHolder<ProductEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_layout_product_list_item_no_product
    }

    override fun bind(item: ProductEmptyViewModel) {
        view.imageView8.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.no_products))
    }
}
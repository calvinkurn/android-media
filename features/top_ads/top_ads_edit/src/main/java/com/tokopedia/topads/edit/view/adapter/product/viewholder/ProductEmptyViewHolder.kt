package com.tokopedia.topads.edit.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.unifycomponents.ImageUnify


class ProductEmptyViewHolder(val view: View): ProductViewHolder<ProductEmptyViewModel>(view) {

    private val imageView : ImageUnify = view.findViewById(R.id.imageView8)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_layout_product_list_item_no_product
    }

    override fun bind(item: ProductEmptyViewModel) {
        imageView.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.no_products))
    }
}
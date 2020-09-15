package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.ResponseProductList
import com.tokopedia.topads.view.adapter.etalase.viewmodel.EtalaseItemViewModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemViewModel
import kotlinx.android.synthetic.main.topads_create_layout_product_filter_list_item.view.*
import kotlinx.android.synthetic.main.topads_create_layout_product_list_item_no_product.view.*
import kotlinx.android.synthetic.main.topads_create_layout_product_list_item_product.view.*

/**
 * Author errysuprayogi on 11,November,2019
 */
class ProductEmptyViewHolder(val view: View): ProductViewHolder<ProductEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_product_list_item_no_product
    }

    override fun bind(item: ProductEmptyViewModel) {
        item.let {
            view.imageView8.setImageDrawable(view.context.getResDrawable(R.drawable.ill_no_product))
        }
    }

}